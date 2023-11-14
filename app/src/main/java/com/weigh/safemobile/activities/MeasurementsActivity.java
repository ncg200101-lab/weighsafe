package com.weigh.safemobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.fragments.DetailModalFragment;
import com.weigh.safemobile.models.Configuration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class MeasurementsActivity extends AppCompatActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;

    @BindView(R.id.setup_title)
    TextView txtTitle;

    @BindView(R.id.edit_btn_save)
    TextView edtBtnSave;

    @BindView(R.id.edit_panel)
    RelativeLayout edit_panel;

    @BindView(R.id.axle_inch_text)
    EditText input_rear_axle;

    @BindView(R.id.spring_inch_text)
    EditText input_towball_spring_bar;

    @BindView(R.id.trailer_inch_text)
    EditText input_towball_trailer_axle;

    private RealmList<Configuration> mConfigList = new RealmList<>();
    private Configuration selectedConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurements);

        ButterKnife.bind(this);

        initializerView();
        initializerSave();
        initializeListener();
    }

    public void initializerView() {
        mConfigList = MainApplication.getInstance().mConfigList;
        //int currentConfigIndex = PreferenceManager.getPositionID(this);
        selectedConfiguration = MainApplication.getInstance().selectedConfiguration;

        double rearField = selectedConfiguration.getnRearAxle();
        double springField = selectedConfiguration.getnTowBallSpringBar();
        double trailerField = selectedConfiguration.getnTowBallTrailerAxle();

        input_rear_axle.setText(String.valueOf(rearField));
        input_towball_spring_bar.setText(String.valueOf(springField));
        input_towball_trailer_axle.setText(String.valueOf(trailerField));

    }

    public void initializerSave() {
        edtBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields())
                {
                    double rear_axle = Double.valueOf(input_rear_axle.getText().toString());
                    double spring_bar = Double.valueOf(input_towball_spring_bar.getText().toString());
                    double trailer_axle = Double.valueOf(input_towball_trailer_axle.getText().toString());

                    selectedConfiguration.setnRearAxle(rear_axle);
                    selectedConfiguration.setnTowBallSpringBar(spring_bar);
                    selectedConfiguration.setnTowBallTrailerAxle(trailer_axle);

                    DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);
                    finish();
                    MainApplication.getInstance().getMeasurementsListener().onUpdateValue();
                }
            }
        });
    }

    public void initializeListener() {
        edit_panel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MainApplication.getInstance().getMeasurementsListener().onUpdateValue();
            }
        });
    }

    private boolean validateFields()
    {
        PrettyDialog pDialog = new PrettyDialog(this);
        pDialog.setIcon(R.drawable.pdlg_icon_info);
        pDialog.setIconTint(R.color.pdlg_color_red);
        pDialog.setTitle(getString(R.string.text_required_field));
        pDialog.addButton(
                "OK",					// button text
                R.color.pdlg_color_white,		// button text color
                R.color.pdlg_color_red,		    // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        pDialog.dismiss();
                    }
                });

        if (input_rear_axle.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.axle_inch_label));
            pDialog.show();
            return false;
        }

        if (input_towball_spring_bar.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.spring_inch_label));
            pDialog.show();
            return false;
        }

        if (input_towball_trailer_axle.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.trailer_inch_label));
            pDialog.show();
            return false;
        }

        double rear_axle = Double.valueOf(input_rear_axle.getText().toString());
        double spring_bar = Double.valueOf(input_towball_spring_bar.getText().toString());

        if (rear_axle < 0 || rear_axle > 70) {
            pDialog.setMessage(getString(R.string.text_error_rear_range));
            pDialog.show();
            return false;
        }

        if (spring_bar < 24 || spring_bar > 32) {
            pDialog.setMessage(getString(R.string.text_error_spring_Range));
            pDialog.show();
            return false;
        }

        return true;
    }

    @OnClick({R.id.img_sub_1, R.id.img_sub_2, R.id.img_sub_3})
    public void OnClickImage(View v) {
        DetailModalFragment dialog = null;
        switch (v.getId()) {
            case R.id.img_sub_1:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_rear_axle_to_end_of_receiver), R.drawable.variable_b_topview);
                break;
            case R.id.img_sub_2:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_tow_ball_to_end_of_spring_bars), R.drawable.variable_c_topview);
                break;
            case R.id.img_sub_3:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_tow_ball_to_trailer_axle), R.drawable.variable_d_topview);
                break;
        }
        dialog.show(getSupportFragmentManager(), "detailModal");
    }
}