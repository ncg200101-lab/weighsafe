package com.weigh.safemobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.weigh.safemobile.R;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.models.SetupCargoModel;
import com.weigh.safemobile.models.SetupVehicleModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class NewCargoActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;

    @BindView(R.id.setup_title)
    TextView txtTitle;

    @BindView(R.id.edit_btn_save)
    TextView edtBtnSave;

    @BindView(R.id.edit_panel)
    RelativeLayout edit_panel;

    @BindView(R.id.cargo_name_text)
    EditText input_cargo_name;

    @BindView(R.id.edit_estimated_cargo)
    EditText input_cargo;

    @BindView(R.id.btn_group_cargo_location)
    SegmentedButtonGroup cargoGroup;

    @BindView(R.id.ll_delete_cargo)
    LinearLayout ll_delete;

    int cargoIndex = -1;

    boolean mEditCargo = false;
    private SetupCargoModel selectedCargo;
    private int selectedRealmId;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cargo);
        ButterKnife.bind(this);

        initializerView();
        initializerSave();
        initializeListener();
    }

    public void initializerView() {
        Intent intent = getIntent();
        mEditCargo = intent.getBooleanExtra("editable", false);

        if (mEditCargo)
        {
            selectedRealmId = intent.getIntExtra("realmId", -1);
            selectedCargo = DatabaseManager.getCargoModelByRealmId(selectedRealmId);
            selectedPosition = intent.getIntExtra("position", -1);

            ll_delete.setVisibility(View.VISIBLE);
            txtTitle.setText(getText(R.string.text_edit_cargos));

            loadData();
        } else
        {
            selectedCargo = null;
            selectedRealmId = -1;
            selectedPosition = -1;
            ll_delete.setVisibility(View.GONE);
            txtTitle.setText(getText(R.string.text_new_cargo));
        }
    }

    public void initializerSave() {
        edtBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields())
                {
                    if (mEditCargo)
                    {
                        SetupCargoModel saveModel = new SetupCargoModel();
                        saveModel.setnRealmId(selectedCargo.getnRealmId());
                        saveModel.setName(input_cargo_name.getText().toString());
                        saveModel.setCargoWeight(Double.valueOf(input_cargo.getText().toString()));

                        if (cargoGroup.getPosition() == 0)
                            saveModel.setmType("Vehicle");
                        else
                            saveModel.setmType("Trailer");

                        int nRealmId = selectedCargo.getnRealmId();
                        DatabaseManager.updateCargoModel(nRealmId, saveModel);
                        MainApplication.getInstance().getCargoUpdateListener().onEditDialogSave(selectedPosition, saveModel);
                        finish();
                    }
                    else
                    {
                        SetupCargoModel saveModel = new SetupCargoModel();
                        saveModel.setName(input_cargo_name.getText().toString());
                        saveModel.setCargoWeight(Double.valueOf(input_cargo.getText().toString()));

                        if (cargoGroup.getPosition() == 0)
                            saveModel.setmType("Vehicle");
                        else
                            saveModel.setmType("Trailer");

                        SetupCargoModel returnModel = DatabaseManager.addNewCargoModel(saveModel);
                        MainApplication.getInstance().getCargoUpdateListener().onNewDialogSave(returnModel);
                        finish();
                    }

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
            }
        });

        ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SetupVehicleModel> allVehicles = DatabaseManager.getAllSetupVehicles();
                if (allVehicles != null)
                {
                    if (allVehicles.size() == 1)
                    {
                        PrettyDialog pDialog = new PrettyDialog(NewCargoActivity.this);
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

                        pDialog.setMessage(getString(R.string.text_delete_not_cargo));
                        pDialog.show();
                    } else if (allVehicles.size() > 1)
                    {
                        PrettyDialog pDialog = new PrettyDialog(NewCargoActivity.this);
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
                                        DatabaseManager.deleteCargoModel(selectedCargo.getnRealmId());
                                        MainApplication.getInstance().getCargoUpdateListener().onDeleteDialogSave(selectedPosition);
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
    }

    private void loadData()
    {
        if (selectedCargo == null)
            return;

        input_cargo_name.setText(selectedCargo.getName());
        input_cargo.setText(String.valueOf(selectedCargo.getCargoWeight()));

        if (selectedCargo.getmType() == "Vehicle")
            cargoGroup.setPosition(0, false);
        else
            cargoGroup.setPosition(1, false);
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

        if (input_cargo_name.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.text_enter_name));
            pDialog.show();
            return false;
        }

        if (input_cargo.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.text_estimated_weight_cargo));
            pDialog.show();
            return false;
        }

        return true;
    }
}