package com.hastashilpa.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hastashilpa.app.databinding.ActivityMainBinding;
import com.hastashilpa.app.fragments.BlueprintsFragment;
import com.hastashilpa.app.fragments.MarketplaceFragment;
import com.hastashilpa.app.fragments.TrackerFragment;
import com.hastashilpa.app.fragments.TrendsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        setupBottomNavigation();
        setupBackPress();

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new TrendsFragment());
            binding.bottomNav.setSelectedItemId(R.id.nav_trends);
        }
    }

    private void setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_trends) {
                loadFragment(new TrendsFragment());
                return true;
            } else if (id == R.id.nav_blueprints) {
                loadFragment(new BlueprintsFragment());
                return true;
            } else if (id == R.id.nav_tracker) {
                loadFragment(new TrackerFragment());
                return true;
            } else if (id == R.id.nav_market) {
                loadFragment(new MarketplaceFragment());
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        currentFragment = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void setupBackPress() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!(currentFragment instanceof TrendsFragment)) {
                    binding.bottomNav.setSelectedItemId(R.id.nav_trends);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
