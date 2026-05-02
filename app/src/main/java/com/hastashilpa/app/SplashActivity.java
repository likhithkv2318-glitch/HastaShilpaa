package com.hastashilpa.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import com.hastashilpa.app.utils.DataRepository;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Preload fonts + data on background thread to avoid ANR
        new Thread(() -> {
            ResourcesCompat.getFont(this, R.font.poppins_regular);
            ResourcesCompat.getFont(this, R.font.poppins_medium);
            ResourcesCompat.getFont(this, R.font.poppins_semibold);
            ResourcesCompat.getFont(this, R.font.poppins_bold);
            DataRepository.warmCache();
        }).start();

        View logoContainer = findViewById(R.id.logoContainer);
        TextView appName = findViewById(R.id.tvAppName);
        TextView tagline = findViewById(R.id.tvTagline);
        TextView subtitle = findViewById(R.id.tvSubtitle);

        // Logo animation — scale + fade in
        AnimationSet logoAnim = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(700);
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(700);
        logoAnim.addAnimation(scale);
        logoAnim.addAnimation(fadeIn);
        logoAnim.setFillAfter(true);
        logoContainer.startAnimation(logoAnim);

        // App name slide up + fade
        AlphaAnimation nameAlpha = new AlphaAnimation(0f, 1f);
        nameAlpha.setDuration(600);
        nameAlpha.setStartOffset(500);
        nameAlpha.setFillAfter(true);
        TranslateAnimation nameTranslate = new TranslateAnimation(0, 0, 40, 0);
        nameTranslate.setDuration(600);
        nameTranslate.setStartOffset(500);
        nameTranslate.setFillAfter(true);
        AnimationSet nameAnim = new AnimationSet(true);
        nameAnim.addAnimation(nameAlpha);
        nameAnim.addAnimation(nameTranslate);
        nameAnim.setFillAfter(true);
        appName.setVisibility(View.VISIBLE);
        appName.startAnimation(nameAnim);

        // Tagline delayed
        AlphaAnimation tagAnim = new AlphaAnimation(0f, 1f);
        tagAnim.setDuration(600);
        tagAnim.setStartOffset(900);
        tagAnim.setFillAfter(true);
        tagline.setVisibility(View.VISIBLE);
        tagline.startAnimation(tagAnim);

        // Subtitle
        AlphaAnimation subAnim = new AlphaAnimation(0f, 1f);
        subAnim.setDuration(600);
        subAnim.setStartOffset(1200);
        subAnim.setFillAfter(true);
        subtitle.setVisibility(View.VISIBLE);
        subtitle.startAnimation(subAnim);

        // Navigate to MainActivity after 1.5 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 1500);
    }
}
