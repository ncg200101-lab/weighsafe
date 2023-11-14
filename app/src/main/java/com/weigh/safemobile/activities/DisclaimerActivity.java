package com.weigh.safemobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.fragments.PdfViewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisclaimerActivity extends AppCompatActivity {
    @BindView(R.id.txt_view_terms)
    TextView txtViewTerms;

    @BindView(R.id.btn_accept_Terms)
    Button btnAcceptTerms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        ButterKnife.bind(this);

        initializeListener();
    }

    public void initializeListener() {
        txtViewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new PdfViewFragment();
                dialog.setCancelable(false);
                dialog.show(DisclaimerActivity.this.getSupportFragmentManager(), "PDFFragment");
            }
        });

        btnAcceptTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(DisclaimerActivity.this, WelcomeActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
    }
}