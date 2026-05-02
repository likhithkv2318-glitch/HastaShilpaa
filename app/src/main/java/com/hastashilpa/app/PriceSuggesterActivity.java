package com.hastashilpa.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hastashilpa.app.databinding.ActivityPriceSuggesterBinding;
import com.hastashilpa.app.utils.PrefsHelper;

import java.util.Locale;

public class PriceSuggesterActivity extends AppCompatActivity {

    private ActivityPriceSuggesterBinding binding;
    private PrefsHelper prefsHelper;

    // Price calculation variables
    private double materialCost = 0;
    private double hourlyRate = 80;
    private double hoursWorked = 0;
    private double finishingCost = 0;
    private int overheadPercent = 15;
    private int profitPercent = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPriceSuggesterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefsHelper = new PrefsHelper(this);

        setupToolbar();
        loadSavedPrefs();
        setupListeners();
        setupSeekBars();

        // Pre-fill hours if coming from blueprint
        int hours = getIntent().getIntExtra("hours", 0);
        if (hours > 0) {
            binding.etHoursWorked.setText(String.valueOf(hours));
        }
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Price Suggester");
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadSavedPrefs() {
        hourlyRate = prefsHelper.getHourlyRate();
        binding.etHourlyRate.setText(String.format(Locale.getDefault(), "%.0f", hourlyRate));
        binding.etBambooCostPerPole.setText(String.format(Locale.getDefault(), "%.0f", prefsHelper.getBambooPrice()));
        binding.etCaneCostPerMeter.setText(String.format(Locale.getDefault(), "%.0f", prefsHelper.getCanePrice()));
    }

    private void setupListeners() {
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int i, int b, int c) { recalculate(); }
            @Override public void afterTextChanged(Editable s) {}
        };

        binding.etMaterialCost.addTextChangedListener(watcher);
        binding.etHourlyRate.addTextChangedListener(watcher);
        binding.etHoursWorked.addTextChangedListener(watcher);
        binding.etFinishingCost.addTextChangedListener(watcher);
        binding.etBambooCostPerPole.addTextChangedListener(watcher);
        binding.etCaneCostPerMeter.addTextChangedListener(watcher);
        binding.etBambooPoles.addTextChangedListener(watcher);
        binding.etCaneMeters.addTextChangedListener(watcher);

        binding.btnCalculate.setOnClickListener(v -> {
            savePrefs();
            recalculate();
            binding.resultCard.setVisibility(View.VISIBLE);
            binding.resultCard.animate().alpha(1f).setDuration(400).start();
        });
    }

    private void setupSeekBars() {
        binding.seekOverhead.setProgress(overheadPercent);
        binding.tvOverheadValue.setText(overheadPercent + "%");
        binding.seekOverhead.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int progress, boolean b) {
                overheadPercent = progress;
                binding.tvOverheadValue.setText(progress + "%");
                recalculate();
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });

        binding.seekProfit.setProgress(profitPercent);
        binding.tvProfitValue.setText(profitPercent + "%");
        binding.seekProfit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int progress, boolean b) {
                profitPercent = progress;
                binding.tvProfitValue.setText(progress + "%");
                recalculate();
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });
    }

    private void recalculate() {
        try {
            // Material cost from text field or computed from poles + cane
            double directMaterial = parseDouble(binding.etMaterialCost.getText().toString());
            double bambooPrice = parseDouble(binding.etBambooCostPerPole.getText().toString());
            double canePrice = parseDouble(binding.etCaneCostPerMeter.getText().toString());
            int poles = parseInt(binding.etBambooPoles.getText().toString());
            double caneM = parseDouble(binding.etCaneMeters.getText().toString());

            double computedMaterial = (bambooPrice * poles) + (canePrice * caneM);
            materialCost = directMaterial > 0 ? directMaterial : computedMaterial;

            hourlyRate = parseDouble(binding.etHourlyRate.getText().toString());
            hoursWorked = parseDouble(binding.etHoursWorked.getText().toString());
            finishingCost = parseDouble(binding.etFinishingCost.getText().toString());

            double labourCost = hourlyRate * hoursWorked;
            double totalBase = materialCost + labourCost + finishingCost;
            double overhead = totalBase * overheadPercent / 100.0;
            double totalCost = totalBase + overhead;
            double profit = totalCost * profitPercent / 100.0;
            double suggestedPrice = totalCost + profit;

            // Update breakdown
            binding.tvBreakMaterial.setText("₹" + fmt(materialCost));
            binding.tvBreakLabour.setText("₹" + fmt(labourCost));
            binding.tvBreakFinishing.setText("₹" + fmt(finishingCost));
            binding.tvBreakOverhead.setText("₹" + fmt(overhead));
            binding.tvBreakProfit.setText("₹" + fmt(profit));
            binding.tvTotalCost.setText("₹" + fmt(totalCost));
            binding.tvSuggestedPrice.setText("₹" + fmt(suggestedPrice));

            // Price tier advice
            if (suggestedPrice < 500) {
                binding.tvPriceAdvice.setText("💡 Consider bundling multiple units to increase per-order value.");
            } else if (suggestedPrice < 2000) {
                binding.tvPriceAdvice.setText("✅ Great price point for online and local market!");
            } else {
                binding.tvPriceAdvice.setText("🏆 Premium range — target urban lifestyle stores and export.");
            }

        } catch (NumberFormatException e) {
            // Ignore partial input
        }
    }

    private void savePrefs() {
        try {
            prefsHelper.setHourlyRate((float) parseDouble(binding.etHourlyRate.getText().toString()));
            prefsHelper.setBambooPrice((float) parseDouble(binding.etBambooCostPerPole.getText().toString()));
            prefsHelper.setCanePrice((float) parseDouble(binding.etCaneCostPerMeter.getText().toString()));
        } catch (Exception ignored) {}
    }

    private double parseDouble(String s) {
        if (s == null || s.isEmpty()) return 0;
        return Double.parseDouble(s.trim());
    }

    private int parseInt(String s) {
        if (s == null || s.isEmpty()) return 0;
        return Integer.parseInt(s.trim());
    }

    private String fmt(double val) {
        return String.format(Locale.getDefault(), "%.0f", val);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }
}
