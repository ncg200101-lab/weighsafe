package com.weigh.safemobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.weigh.safemobile.R;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.fragments.DetailModalFragment;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.SetupHitchModel;
import com.weigh.safemobile.models.SetupTrailerModel;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class NewTrailerActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;

    @BindView(R.id.setup_title)
    TextView txtTitle;

    @BindView(R.id.edit_btn_save)
    TextView edtBtnSave;

    @BindView(R.id.edit_panel)
    RelativeLayout edit_panel;

    @BindView(R.id.trailer_name_text)
    EditText input_trailer_name;

    @BindView(R.id.edit_gvwr)
    EditText input_gvwr;

    @BindView(R.id.edit_mcc)
    EditText input_mcc;

    @BindView(R.id.btn_group_coupler)
    SegmentedButtonGroup couplerGroup;

    @BindView(R.id.ll_delete_trailer)
    LinearLayout ll_delete;

    @BindView(R.id.spinner_trailer_type)
    Spinner trailerSpinner;

    @BindView(R.id.ll_dry_boat)
    LinearLayout ll_cargo_weight;

    @BindView(R.id.edit_dryboat)
    EditText input_cargoweight;

    @BindView(R.id.ll_coupler)
    LinearLayout ll_coupler;


    int trailerIndex = -1;
    boolean mEditTrailer = false;
    private SetupTrailerModel selectedTrailer;
    private int selectedRealmId;
    private int selectedPosition;

    String[] typeOptions = {"boat", "cargo", "flat", "horse", "rv"};

    static List<String> couplerOptions = Arrays.asList(new String[] {
            "2 5/16\"",
            "2\"",
            "1 7/8\""

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trailer);
        ButterKnife.bind(this);

        initializerView();
        initializerSave();
        initializeListener();
    }

    public void initializerView() {
        Intent intent = getIntent();

        mEditTrailer = intent.getBooleanExtra("editable", false);

        ll_cargo_weight.setVisibility(View.GONE);
        ll_coupler.setVisibility(View.VISIBLE);

        List<SetupHitchModel> hitchModels = MainApplication.getInstance().hitchModels;
        int currHitchIndex = PreferenceManager.getCurrentHitch(this);
        if (currHitchIndex > 0)
        {
            SetupHitchModel currHitch = hitchModels.get(currHitchIndex);
            if (currHitch.getHitchType().equals("Gooseneck") || currHitch.getHitchType().equals("Fifth Wheel"))
                ll_coupler.setVisibility(View.GONE);
        }


        if (mEditTrailer)
        {
            selectedRealmId = intent.getIntExtra("realmId", -1);
            selectedTrailer = DatabaseManager.getTrailerModelByRealmId(selectedRealmId);
            selectedPosition = intent.getIntExtra("position", -1);

            ll_delete.setVisibility(View.VISIBLE);
            txtTitle.setText(getText(R.string.text_edit_trailers));

            loadData();
        } else
        {
            selectedTrailer = null;
            selectedRealmId = -1;
            selectedPosition = -1;
            ll_delete.setVisibility(View.GONE);
            txtTitle.setText(getText(R.string.text_new_trailer));
        }

    }

    public void initializerSave() {
        edtBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields())
                {
                    if (mEditTrailer)
                    {
                        SetupTrailerModel saveModel = new SetupTrailerModel();
                        saveModel.setnRealmId(selectedTrailer.getnRealmId());

                        String trailerName = input_trailer_name.getText().toString();
                        if (trailerName.isEmpty() || trailerName.equals(""))
                        {
                            try {
                                Realm realm = MainApplication.getRealmDatabaseInstance();
                                int lastID = DatabaseManager.getLastId(SetupTrailerModel.class) + 1;
                                String saveTrailerName = "Trailer #" + lastID;
                                saveModel.setName(saveTrailerName);;
                                realm.close();
                            } catch (Exception e) {
                                Log.e("TAG", "SetupCargoModel Exception : " + e.getMessage());
                            }
                        }
                        else
                            saveModel.setName(input_trailer_name.getText().toString());

                        saveModel.setTrailerType(typeOptions[trailerSpinner.getSelectedItemPosition()]);
                        saveModel.setTypeIndex(trailerSpinner.getSelectedItemPosition());
                        saveModel.setEmptyWeight(Double.valueOf(input_gvwr.getText().toString()));
                        saveModel.setLoadCapacity(Double.valueOf(input_mcc.getText().toString()));

                        if (ll_cargo_weight.getVisibility() == View.VISIBLE)
                            saveModel.setCargoWeight(Double.valueOf(input_cargoweight.getText().toString()));

                        saveModel.setCouplerIndex(couplerGroup.getPosition());
                        saveModel.setCoupler(couplerGroup.getButtons().get(saveModel.getCouplerIndex()).getText().toString());

                        int nRealmId = selectedTrailer.getnRealmId();
                        DatabaseManager.updateTrailerModel(nRealmId, saveModel);
                        MainApplication.getInstance().getTrailerUpdateListener().onEditDialogSave(selectedPosition, saveModel);
                        finish();
                    }
                    else
                    {
                        SetupTrailerModel saveModel = new SetupTrailerModel();
                        String trailerName = input_trailer_name.getText().toString();
                        if (trailerName.isEmpty() || trailerName.equals(""))
                        {
                            try {
                                Realm realm = MainApplication.getRealmDatabaseInstance();
                                int lastID = DatabaseManager.getLastId(SetupTrailerModel.class) + 1;
                                String saveTrailerName = "Trailer #" + lastID;
                                saveModel.setName(saveTrailerName);;
                                realm.close();
                            } catch (Exception e) {
                                Log.e("TAG", "SetupCargoModel Exception : " + e.getMessage());
                            }
                        }
                        else
                            saveModel.setName(input_trailer_name.getText().toString());

                        saveModel.setTrailerType(typeOptions[trailerSpinner.getSelectedItemPosition()]);
                        saveModel.setTypeIndex(trailerSpinner.getSelectedItemPosition());
                        saveModel.setEmptyWeight(Double.valueOf(input_gvwr.getText().toString()));
                        saveModel.setLoadCapacity(Double.valueOf(input_mcc.getText().toString()));

                        if (ll_cargo_weight.getVisibility() == View.VISIBLE)
                            saveModel.setCargoWeight(Double.valueOf(input_cargoweight.getText().toString()));

                        saveModel.setCouplerIndex(couplerGroup.getPosition());
                        saveModel.setCoupler(couplerGroup.getButtons().get(saveModel.getCouplerIndex()).getText().toString());

                        SetupTrailerModel returnModel = DatabaseManager.addNewTrailerModel(saveModel);
                        MainApplication.getInstance().getTrailerUpdateListener().onNewDialogSave(returnModel);
                        finish();
                    }

                }
            }
        });
    }

    public void initializeListener() {
        trailerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    ll_cargo_weight.setVisibility(View.VISIBLE);
                else
                    ll_cargo_weight.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
            }
        });

        ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SetupTrailerModel> allTrailers = DatabaseManager.getAllSetupTrailers();
                if (allTrailers != null)
                {
                    if (allTrailers.size() == 1)
                    {
                        PrettyDialog pDialog = new PrettyDialog(NewTrailerActivity.this);
                        pDialog.setIcon(R.drawable.pdlg_icon_info);
                        pDialog.setIconTint(R.color.colorRed);
                        pDialog.setTitle(getString(R.string.text_warning));
                        pDialog.addButton(
                                "OK",					// button text
                                R.color.colorWhite,		// button text color
                                R.color.colorRed,		    // button background color
                                new PrettyDialogCallback() {	// button OnClick listener
                                    @Override
                                    public void onClick() {
                                        pDialog.dismiss();
                                    }
                                });

                        pDialog.setMessage(getString(R.string.text_delete_not_trailer));
                        pDialog.show();
                    } else if (allTrailers.size() > 1)
                    {
                        PrettyDialog pDialog = new PrettyDialog(NewTrailerActivity.this);
                        pDialog.setIcon(R.drawable.pdlg_icon_info);
                        pDialog.setIconTint(R.color.pdlg_color_yellow);
                        pDialog.setTitle(getString(R.string.text_confimration));
                        pDialog.addButton(
                                "DELETE",					// button text
                                R.color.pdlg_color_black,		// button text color
                                R.color.pdlg_color_yellow,		    // button background color
                                new PrettyDialogCallback() {	// button OnClick listener
                                    @Override
                                    public void onClick() {
                                        DatabaseManager.deleteTrailerModel(selectedTrailer.getnRealmId());
                                        MainApplication.getInstance().getTrailerUpdateListener().onDeleteDialogSave(selectedPosition);
                                        pDialog.dismiss();
                                        finish();
                                    }
                                });
                        pDialog.addButton(
                                "CANCEL",					// button text
                                R.color.pdlg_color_black,		// button text color
                                R.color.pdlg_color_gray,		    // button background color
                                new PrettyDialogCallback() {	// button OnClick listener
                                    @Override
                                    public void onClick() {
                                        pDialog.dismiss();
                                    }
                                });

                        pDialog.show();
                    }
                }
            }
        });


        input_gvwr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    String strValue = input_gvwr.getText().toString();
                    if (!strValue.isEmpty() && !strValue.equals(""))
                    {
                        int gvwrValue = Integer.valueOf(strValue);
                        if (gvwrValue != 0)
                        {
                            String strMcc = input_mcc.getText().toString();
                            int mccValue = (strMcc != null && !strMcc.isEmpty() && !strMcc.equals("")) ? Integer.valueOf(strMcc) : 0;
                            if (gvwrValue < mccValue)
                                showErrorMessage(getString(R.string.warning_gvwr_less), input_gvwr);
                        }
                    }
                }
            }
        });

        input_mcc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    String strValue = input_mcc.getText().toString();
                    if (!strValue.isEmpty() && !strValue.equals(""))
                    {
                        int mccValue = Integer.valueOf(strValue);
                        if (mccValue != 0)
                        {
                            String strGVWR = input_gvwr.getText().toString();
                            int gvwrValue = (strGVWR != null && !strGVWR.isEmpty() && !strGVWR.equals("")) ? Integer.valueOf(strGVWR) : 0;
                            if (mccValue > gvwrValue)
                                showErrorMessage(getString(R.string.warning_mcc_greater), input_mcc);
                        }
                    }
                }
            }
        });

    }

    private void loadData()
    {
        if (selectedTrailer == null)
            return;

        input_trailer_name.setText(selectedTrailer.getName());
        trailerSpinner.setSelection(selectedTrailer.getTypeIndex());

        if (selectedTrailer.getTrailerType().equals("boat"))
            ll_cargo_weight.setVisibility(View.VISIBLE);

        input_cargoweight.setText(String.valueOf(selectedTrailer.getCargoWeight()));
        input_gvwr.setText(String.valueOf(selectedTrailer.getEmptyWeight()));
        input_mcc.setText(String.valueOf(selectedTrailer.getLoadCapacity()));

        couplerGroup.setPosition(selectedTrailer.getCouplerIndex(), false);
    }

    private void showErrorMessage(String message, EditText inputField)
    {
        PrettyDialog pDialog = new PrettyDialog(this);
        pDialog.setIcon(R.drawable.pdlg_icon_info);
        pDialog.setIconTint(R.color.pdlg_color_red);
        pDialog.setTitle(getString(R.string.text_invalid_userinput));
        pDialog.addButton(
                "Close",					// button text
                R.color.colorWhite,		// button text color
                R.color.colorRed,		    // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        //inputField.setText("0");
                        inputField.setText("0", TextView.BufferType.EDITABLE); /*// May be unecessary... */
                        inputField.getText().clear();
                        inputField.requestFocus();
                        pDialog.dismiss();
                    }
                });

        pDialog.setMessage(message);
        pDialog.show();
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

        /*
        if (input_trailer_name.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.text_enter_name));
            pDialog.show();
            return false;
        }
         */

        if (input_gvwr.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.text_empty_weight));
            pDialog.show();
            return false;
        }

        if (ll_cargo_weight.getVisibility() == View.VISIBLE)
        {
            if (input_cargoweight.getText().toString().isEmpty()) {
                pDialog.setMessage(getString(R.string.text_empty_weight));
                pDialog.show();
                return false;
            }
        }

        if (input_mcc.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.text_load_capacity));
            pDialog.show();
            return false;
        }

        return true;
    }

    @OnClick({R.id.img_sub_1, R.id.img_sub_2})
    public void OnClickImage(View v) {
        DetailModalFragment dialog = null;

        int trailerPos = trailerSpinner.getSelectedItemPosition();

        String selectedType = "rv";
        if (trailerPos > 0)
            selectedType = typeOptions[trailerPos];

        switch (v.getId()) {
            case R.id.img_sub_1:
                int imageResource = getResources().getIdentifier(String.format("%s_%s", "trailer_gvwr", selectedType), "drawable", getPackageName());
                dialog = DetailModalFragment.newInstance(getString(R.string.text_max_gtwr), imageResource);
                break;
            case R.id.img_sub_2:
                int imageResource2 = getResources().getIdentifier(String.format("%s_%s", "trailer_loading", selectedType), "drawable", getPackageName());
                dialog = DetailModalFragment.newInstance(getString(R.string.text_mcc), imageResource2);
                break;
            case R.id.img_sub_dryboat:
                dialog = DetailModalFragment.newInstance(getString(R.string.trailer_dry_boat), R.drawable.truck_towboat_weight);
                break;
        }
        dialog.show(getSupportFragmentManager(), "detailModal");
    }

}