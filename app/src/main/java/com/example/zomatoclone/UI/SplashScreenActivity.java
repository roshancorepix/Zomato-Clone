package com.example.zomatoclone.UI;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import androidx.core.content.ContextCompat;
import com.example.zomatoclone.R;
import com.example.zomatoclone.SharedPrefrence.PreferenceManager;

public class SplashScreenActivity extends AppCompatActivity {

    private static long SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        setContentView(R.layout.activity_splash_screen);

        PreferenceManager.init(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceManager.getUserLogin()){
                    startActivity(new Intent(SplashScreenActivity.this,PermissionActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreenActivity.this, LoginMenuActivity.class));
                    finish();
                }
            }
        },SPLASH_TIME_OUT);
    }
}