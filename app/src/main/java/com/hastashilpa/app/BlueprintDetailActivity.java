package com.hastashilpa.app;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.hastashilpa.app.databinding.ActivityBlueprintDetailBinding;
import com.hastashilpa.app.models.Design;

import java.util.List;
import java.util.Locale;

public class BlueprintDetailActivity extends AppCompatActivity {

    private ActivityBlueprintDetailBinding binding;
    private Design design;

    // Zoom state
    private Matrix matrix = new Matrix();
    private float[] matrixValues = new float[9];
    private float scale = 1f;
    private static final float MIN_ZOOM = 1f;
    private static final float MAX_ZOOM = 5f;
    private ScaleGestureDetector scaleGestureDetector;
    private PointF last = new PointF();
    private PointF start = new PointF();
    private int mode = 0; // NONE=0, DRAG=1, ZOOM=2
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBlueprintDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            design = getIntent().getSerializableExtra("design", Design.class);
        } else {
            design = (Design) getIntent().getSerializableExtra("design");
        }
        if (design == null) { finish(); return; }

        setupToolbar();
        populateDesignInfo();
        setupBlueprintZoom();
        setupButtons();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(design.getTitle());
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void populateDesignInfo() {
        binding.tvDesignTitle.setText(design.getTitle());
        binding.tvDesignCategory.setText(design.getCategory());
        binding.tvDescription.setText(design.getDescription());
        binding.tvDifficulty.setText(design.getDifficulty());
        binding.tvEstimatedTime.setText(design.getEstimatedHours() + " hours");
        binding.tvMarketValue.setText("₹" + String.format(Locale.getDefault(), "%.0f", design.getMarketValue()));
        binding.tvBambooPoles.setText(design.getBambooPoles());
        binding.tvCaneMeters.setText(design.getCaneMeters());

        // Tag
        String tag = design.getTag();
        if ("TRENDING".equals(tag)) {
            binding.tvTag.setVisibility(View.VISIBLE);
            binding.tvTag.setText("🔥 Trending");
            binding.tvTag.setBackgroundResource(R.drawable.bg_tag_trending);
        } else if ("NEW".equals(tag)) {
            binding.tvTag.setVisibility(View.VISIBLE);
            binding.tvTag.setText("✨ New");
            binding.tvTag.setBackgroundResource(R.drawable.bg_tag_new);
        } else {
            binding.tvTag.setVisibility(View.GONE);
        }

        // Materials chips
        if (design.getMaterials() != null) {
            for (String mat : design.getMaterials()) {
                Chip chip = new Chip(this);
                chip.setText(mat);
                chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.chip_bg, null)));
                chip.setTextColor(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.chip_text, null)));
                chip.setClickable(false);
                binding.materialsChipGroup.addView(chip);
            }
        }

        // Dimensions
        if (design.getDimensions() != null) {
            for (Design.Dimension dim : design.getDimensions()) {
                View dimView = getLayoutInflater().inflate(R.layout.item_dimension_row, binding.dimensionsContainer, false);
                ((TextView) dimView.findViewById(R.id.tvDimLabel)).setText(dim.getLabel());
                ((TextView) dimView.findViewById(R.id.tvDimValue)).setText(dim.getValue());
                binding.dimensionsContainer.addView(dimView);
            }
        }

        // Assembly steps
        if (design.getAssemblySteps() != null) {
            int stepNum = 1;
            for (String step : design.getAssemblySteps()) {
                View stepView = getLayoutInflater().inflate(R.layout.item_assembly_step, binding.stepsContainer, false);
                ((TextView) stepView.findViewById(R.id.tvStepNumber)).setText(String.valueOf(stepNum++));
                ((TextView) stepView.findViewById(R.id.tvStepText)).setText(step);
                binding.stepsContainer.addView(stepView);
            }
        }

        // Set blueprint placeholder image color based on category
        setBlueprintImage();
    }

    private void setBlueprintImage() {
        binding.ivBlueprint.setBackgroundResource(R.drawable.bg_blueprint_canvas);
        int resId;
        switch (design.getId()) {
            case 1: resId = R.drawable.bp_laptop_stand;  break;
            case 2: resId = R.drawable.bp_lamp_shade;    break;
            case 3: resId = R.drawable.bp_serving_tray;  break;
            case 4: resId = R.drawable.bp_plant_stand;   break;
            case 5: resId = R.drawable.bp_shelf;         break;
            case 6: resId = R.drawable.bp_magazine_rack; break;
            default: resId = R.drawable.ic_blueprint_placeholder; break;
        }
        binding.ivBlueprint.setImageResource(resId);
    }

    private void setupBlueprintZoom() {
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        binding.ivBlueprint.setScaleType(ImageView.ScaleType.MATRIX);

        binding.ivBlueprint.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    matrix.set(binding.ivBlueprint.getImageMatrix());
                    start.set(event.getX(), event.getY());
                    last.set(event.getX(), event.getY());
                    mode = DRAG;
                    // Prevent parent from stealing touch events
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = ZOOM;
                    // Definitely prevent parent scroll during pinch-zoom
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_MOVE:
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if (mode == DRAG && scale > 1f) {
                        float dx = event.getX() - last.x;
                        float dy = event.getY() - last.y;
                        matrix.postTranslate(dx, dy);
                        binding.ivBlueprint.setImageMatrix(matrix);
                        last.set(event.getX(), event.getY());
                    } else if (mode == ZOOM) {
                        binding.ivBlueprint.setImageMatrix(matrix);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    mode = 0;
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;
                    break;
            }
            return true;
        });

        // Double tap to reset zoom
        binding.ivBlueprint.setOnClickListener(null);
        binding.btnResetZoom.setOnClickListener(v -> {
            matrix = new Matrix();
            scale = 1f;
            binding.ivBlueprint.setImageMatrix(matrix);
            binding.ivBlueprint.setScaleType(ImageView.ScaleType.FIT_CENTER);
        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float newScale = scale * scaleFactor;
            if (newScale < MIN_ZOOM) scaleFactor = MIN_ZOOM / scale;
            if (newScale > MAX_ZOOM) scaleFactor = MAX_ZOOM / scale;
            scale *= scaleFactor;
            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            binding.ivBlueprint.setScaleType(ImageView.ScaleType.MATRIX);
            binding.ivBlueprint.setImageMatrix(matrix);
            return true;
        }
    }

    private void setupButtons() {
        binding.btnDownloadBlueprint.setOnClickListener(v -> downloadBlueprint());

        binding.btnShareBlueprint.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, design.getTitle() + " — Hasta-Shilpa Blueprint");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Check out this bamboo design: " + design.getTitle() +
                    "\nCategory: " + design.getCategory() +
                    "\nMarket Value: ₹" + (int) design.getMarketValue() +
                    "\nShared via Hasta-Shilpa App");
            startActivity(Intent.createChooser(shareIntent, "Share Blueprint via"));
        });

        binding.btnPriceSuggester.setOnClickListener(v -> {
            Intent intent = new Intent(this, PriceSuggesterActivity.class);
            intent.putExtra("hours", design.getEstimatedHours());
            startActivity(intent);
        });
    }

    private void downloadBlueprint() {
        try {
            binding.ivBlueprint.setDrawingCacheEnabled(true);
            Bitmap src = Bitmap.createBitmap(binding.ivBlueprint.getDrawingCache());
            binding.ivBlueprint.setDrawingCacheEnabled(false);
            if (src == null) {
                src = Bitmap.createBitmap(
                        binding.ivBlueprint.getWidth(),
                        binding.ivBlueprint.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(src);
                binding.ivBlueprint.draw(canvas);
            }
            String fileName = design.getTitle().replaceAll("[^a-zA-Z0-9]", "_") + "_Blueprint.png";
            OutputStream fos;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/HastaShilpa");
                Uri uri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri == null) throw new Exception("MediaStore insert failed");
                fos = getContentResolver().openOutputStream(uri);
            } else {
                java.io.File dir = new java.io.File(
                        Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "HastaShilpa");
                if (!dir.exists()) dir.mkdirs();
                fos = new java.io.FileOutputStream(new java.io.File(dir, fileName));
            }
            if (fos != null) {
                src.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            }
            Toast.makeText(this, "✅ Blueprint saved to Pictures/HastaShilpa", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }
}
