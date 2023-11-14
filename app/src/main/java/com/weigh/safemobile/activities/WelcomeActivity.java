package com.weigh.safemobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.weigh.safemobile.R;
import com.weigh.safemobile.helpers.PreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {
    @BindView(R.id.btn_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setShownDisclaimer(WelcomeActivity.this, true);
                Intent mIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(mIntent);
                finish();
            }
        });

    }
}