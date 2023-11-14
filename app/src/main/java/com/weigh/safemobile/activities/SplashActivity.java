package com.weigh.safemobile.activities;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.weigh.safemobile.R;
import com.weigh.safemobile.helpers.PreferenceManager;

public class SplashActivity extends AppCompatActivity {
    int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

          new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!PreferenceManager.getShownDisclaimer(SplashActivity.this)) {
                        Intent mIntent = new Intent(SplashActivity.this, DisclaimerActivity.class);
                        startActivity(mIntent);
                        finish();
                    } else {
                        Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(mIntent);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
    }
}
