package com.hastashilpa.app;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hastashilpa.app.databinding.ActivityMarketplaceListBinding;
import com.hastashilpa.app.models.ProductListing;
import com.hastashilpa.app.utils.PrefsHelper;

import java.util.Locale;

public class MarketplaceListActivity extends AppCompatActivity {

    private ActivityMarketplaceListBinding binding;
    private PrefsHelper prefsHelper;
    private String selectedCategory = "Furniture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMarketplaceListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefsHelper = new PrefsHelper(this);

        setupToolbar();
        setupCategoryDropdown();
        setupSubmit();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("List Your Product");
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupCategoryDropdown() {
        String[] categories = {"Furniture", "Home Décor", "Kitchen", "Office", "Garden", "Baskets & Storage"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        binding.actvCategory.setAdapter(adapter);
        binding.actvCategory.setText(categories[0], false);
        selectedCategory = categories[0];
        binding.actvCategory.setOnItemClickListener((parent, v, pos, id) -> {
            selectedCategory = categories[pos];
        });
        binding.actvCategory.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String typed = binding.actvCategory.getText().toString().trim();
                if (!typed.isEmpty()) selectedCategory = typed;
            }
        });
    }

    private void setupSubmit() {
        binding.btnSubmitListing.setOnClickListener(v -> {
            String name = binding.etProductName.getText().toString().trim();
            String desc = binding.etDescription.getText().toString().trim();
            String priceStr = binding.etAskingPrice.getText().toString().trim();
            String qtyStr = binding.etQuantity.getText().toString().trim();
            String artisan = binding.etArtisanName.getText().toString().trim();

            if (name.isEmpty()) {
                binding.tilProductName.setError("Product name required");
                return;
            }
            if (priceStr.isEmpty()) {
                binding.tilAskingPrice.setError("Price required");
                return;
            }

            double price = Double.parseDouble(priceStr);
            int qty = qtyStr.isEmpty() ? 1 : Integer.parseInt(qtyStr);
            if (artisan.isEmpty()) artisan = prefsHelper.getArtisanName();

            ProductListing listing = new ProductListing(
                    name, desc, selectedCategory, price, price * 1.1, qty, artisan);
            prefsHelper.saveListing(listing);

            Toast.makeText(this, "✅ Product listed successfully!", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }
}
