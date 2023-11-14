package com.weigh.safemobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.weigh.safemobile.R;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.fragments.DetailModalFragment;
import com.weigh.safemobile.models.SetupVehicleModel;
import com.weigh.safemobile.models.VinModel;
import com.weigh.safemobile.presenters.VinDataListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class NewVehicleActivity extends AppCompatActivity implements VinDataListener {
    @BindView(R.id.btn_back)
    ImageView btnBack;

    @BindView(R.id.setup_title)
    TextView txtTitle;

    @BindView(R.id.edit_btn_save)
    TextView edtBtnSave;

    @BindView(R.id.scrollbar_edit)
    ScrollView sv;

    @BindView(R.id.edit_panel)
    RelativeLayout edit_panel;

    @BindView(R.id.vehicle_name_text)
    EditText input_vehicle_name;

    @BindView(R.id.edit_gvwr)
    EditText input_gvwr;
    @BindView(R.id.img_sub_1)
    ImageView img_gvwr_info;

    @BindView(R.id.edit_mcc)
    EditText input_mcc;
    @BindView(R.id.img_sub_2)
    ImageView img_mcc_info;

    @BindView(R.id.edit_gcwr)
    EditText input_gcwr;
    @BindView(R.id.img_sub_3)
    ImageView img_gcwr;

    @BindView(R.id.img_sub_rear)
    ImageView img_sub_rear;

    @BindView(R.id.edit_mgtw)
    EditText input_mgtw;

    @BindView(R.id.edit_mtw)
    EditText input_mtw;

    @BindView(R.id.btn_group_yes_no)
    SegmentedButtonGroup btnGroup;

    @BindView(R.id.edit_distributed_mgtw)
    EditText input_distributed_mgtw;

    @BindView(R.id.edit_distributed_mtw)
    EditText input_distributed_mtw;

    @BindView(R.id.ll_delete_vehicle)
    LinearLayout ll_delete;

    @BindView(R.id.ll_distributed_mgtw)
    LinearLayout ll_distributed_mgtw;

    @BindView(R.id.ll_distributed_mtw)
    LinearLayout ll_distributed_mtw;

    @BindView(R.id.btn_vin_lookup)
    Button btnVinLookup;

    boolean mDistributed = false;
    boolean mEditVehicle = false;
    private SetupVehicleModel selectedVehicle;
    private SetupVehicleModel saveModel;
    private int selectedRealmId;
    private int selectedPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vehicle);
        ButterKnife.bind(this);

        initializerView();
        initializerSave();
        initializeListener();
    }


    public void initializerView() {
        sv.setVerticalScrollBarEnabled(false);
        sv.setHorizontalScrollBarEnabled(false);

        Intent intent = getIntent();
        mEditVehicle = intent.getBooleanExtra("editable", false);

        if (mEditVehicle)
        {
            selectedRealmId = intent.getIntExtra("realmId", -1);
            selectedVehicle = DatabaseManager.getVehicleModelByRealmId(selectedRealmId);
            selectedPosition = intent.getIntExtra("position", -1);

            ll_delete.setVisibility(View.VISIBLE);
            txtTitle.setText(getText(R.string.text_edit_vehicles));

            loadData();
        } else
        {
            selectedVehicle = null;
            selectedRealmId = -1;
            selectedPosition = -1;
            ll_delete.setVisibility(View.GONE);
            txtTitle.setText(getText(R.string.text_new_vehicle));
        }

        ll_distributed_mgtw.setVisibility(View.GONE);
        ll_distributed_mtw.setVisibility(View.GONE);

        btnGroup.setPosition(0, false);
        saveModel = new SetupVehicleModel();
    }

    public void initializerSave() {
        edtBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields())
                {
                    if (mEditVehicle)
                    {
                        //SetupVehicleModel saveModel = new SetupVehicleModel();
                        saveModel.setnRealmId(selectedVehicle.getnRealmId());
                        saveModel.setVin(selectedVehicle.getVin());
                        saveModel.setYear(selectedVehicle.getYear());
                        saveModel.setMake(selectedVehicle.getMake());
                        saveModel.setModel(selectedVehicle.getModel());

                        saveModel.setStrTitle(input_vehicle_name.getText().toString());
                        saveModel.setmGVWR(Double.valueOf(input_gvwr.getText().toString()));
                        saveModel.setmMCC(Double.valueOf(input_mcc.getText().toString()));
                        saveModel.setmGCWR(Double.valueOf(input_gcwr.getText().toString()));
                        saveModel.setmMGTW(Double.valueOf(input_mgtw.getText().toString()));
                        saveModel.setmMTW(Double.valueOf(input_mtw.getText().toString()));

                        if (btnGroup.getPosition() == 1) {
                            saveModel.setDistributed(true);
                            saveModel.setmDistributedMGTW(Double.valueOf(input_distributed_mgtw.getText().toString()));
                            saveModel.setmDistributedMGTW(Double.valueOf(input_distributed_mgtw.getText().toString()));
                        }
                        else
                        {
                            saveModel.setDistributed(false);
                            saveModel.setmDistributedMGTW(0);
                            saveModel.setmDistributedMGTW(0);
                        }

                        int nRealmId = selectedVehicle.getnRealmId();
                        DatabaseManager.updateVehicleModel(nRealmId, saveModel);
                        MainApplication.getInstance().getVehicleUpdateListener().onEditDialogSave(selectedPosition, saveModel);
                        finish();
                    }
                    else
                    {
                        //SetupVehicleModel saveModel = new SetupVehicleModel();
                        saveModel.setStrTitle(input_vehicle_name.getText().toString());
                        saveModel.setmGVWR(Double.valueOf(input_gvwr.getText().toString()));
                        saveModel.setmMCC(Double.valueOf(input_mcc.getText().toString()));
                        saveModel.setmGCWR(Double.valueOf(input_gcwr.getText().toString()));
                        saveModel.setmMGTW(Double.valueOf(input_mgtw.getText().toString()));
                        saveModel.setmMTW(Double.valueOf(input_mtw.getText().toString()));

                        if (btnGroup.getPosition() == 1) {
                            saveModel.setDistributed(true);
                            saveModel.setmDistributedMGTW(Double.valueOf(input_distributed_mgtw.getText().toString()));
                            saveModel.setmDistributedMGTW(Double.valueOf(input_distributed_mgtw.getText().toString()));
                        }
                        else
                        {
                            saveModel.setDistributed(false);
                            saveModel.setmDistributedMGTW(0);
                            saveModel.setmDistributedMGTW(0);
                        }


                        SetupVehicleModel returnModel = DatabaseManager.addNewVehicleModel(saveModel);
                        MainApplication.getInstance().getVehicleUpdateListener().onNewDialogSave(returnModel);
                        finish();
                    }

                }
            }
        });
    }

    public void initializeListener() {
        btnVinLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewVehicleActivity.this, VinLookupActivity.class);
                startActivity(intent);
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

        btnGroup.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                if (position == 0)
                {
                    mDistributed = false;
                    ll_distributed_mgtw.setVisibility(View.GONE);
                    ll_distributed_mtw.setVisibility(View.GONE);
                } else if (position == 1)
                {
                    mDistributed = true;
                    ll_distributed_mgtw.setVisibility(View.VISIBLE);
                    ll_distributed_mtw.setVisibility(View.VISIBLE);
                }
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
                        PrettyDialog pDialog = new PrettyDialog(NewVehicleActivity.this);
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


                        pDialog.setMessage(getString(R.string.text_delete_not_vehicle));
                    } else if (allVehicles.size() > 1)
                    {
                        PrettyDialog pDialog = new PrettyDialog(NewVehicleActivity.this);
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
                                        DatabaseManager.deleteVehicleModel(selectedVehicle.getnRealmId());
                                        MainApplication.getInstance().getVehicleUpdateListener().onDeleteDialogSave(selectedPosition);
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

        input_mgtw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    String strValue = input_mgtw.getText().toString();

                    if (!strValue.isEmpty() && !strValue.equals(""))
                    {
                        double maxTrailerWeight = Double.valueOf(strValue) * 0.1;
                        int mtwValue = (int)maxTrailerWeight;
                        input_mtw.setText(String.valueOf(mtwValue));
                    }
                }
            }
        });

        input_distributed_mgtw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    String strValue = input_distributed_mgtw.getText().toString();
                    if (!strValue.isEmpty() && !strValue.equals(""))
                    {
                        double dmgtWeight = Double.valueOf(strValue) * 0.1;
                        int dmtWeight = (int)dmgtWeight;
                        input_distributed_mtw.setText(String.valueOf(dmtWeight));
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

        input_gcwr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    String strValue = input_gcwr.getText().toString();

                    if (!strValue.isEmpty() && !strValue.equals(""))
                    {
                        int gcwrValue = Integer.valueOf(strValue);
                        if (gcwrValue != 0)
                        {
                            String strGVWR = input_gvwr.getText().toString();
                            int gvwrValue = (strGVWR != null && !strGVWR.isEmpty() && !strGVWR.equals("")) ? Integer.valueOf(strGVWR) : 0;
                            if (gcwrValue < gvwrValue)
                                showErrorMessage(getString(R.string.warning_gcwr_less), input_gcwr);
                        }
                    }
                }
            }
        });
    }

    @OnClick({R.id.img_sub_1, R.id.img_sub_2, R.id.img_sub_3, R.id.img_sub_rear})
    public void OnClickImage(View v) {
        DetailModalFragment dialog = null;
        switch (v.getId()) {
            case R.id.img_sub_1:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_gvwr_1), R.drawable.truck_gvwr);
                break;
            case R.id.img_sub_2:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_mcc), R.drawable.truck_towvehicleloading);
                break;
            case R.id.img_sub_3:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_gcwr), R.drawable.truck_towvehiclegcw);
                break;
            case R.id.img_sub_rear:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_type_of_towing_receivers), R.drawable.type_of_towing_receivers);
                break;
        }
        dialog.show(getSupportFragmentManager(), "detailModal");
    }

    private void loadData()
    {
        if (selectedVehicle == null)
            return;

        input_vehicle_name.setText(selectedVehicle.getStrTitle());
        input_gvwr.setText(String.valueOf(selectedVehicle.getmGVWR()));
        input_mcc.setText(String.valueOf(selectedVehicle.getmMCC()));
        input_gcwr.setText(String.valueOf(selectedVehicle.getmGCWR()));
        input_mgtw.setText(String.valueOf(selectedVehicle.getmMGTW()));
        input_mtw.setText(String.valueOf(selectedVehicle.getmMTW()));

        if (selectedVehicle.isDistributed()) {
            btnGroup.setPosition(1, false);
            input_distributed_mgtw.setText(String.valueOf(selectedVehicle.getmDistributedMGTW()));
            input_distributed_mtw.setText(String.valueOf(selectedVehicle.getmDistributedMTW()));
        }
        else
            btnGroup.setPosition(0, false);
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
            R.color.colorWhite,		// button text color
            R.color.colorRed,		    // button background color
            new PrettyDialogCallback() {	// button OnClick listener
                @Override
                public void onClick() {
                    pDialog.dismiss();
                }
            });

        if (input_vehicle_name.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.text_enter_name));
            pDialog.show();
            return false;
        }

        if (input_gvwr.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.text_gcwr_1));
            pDialog.show();
            return false;
        }

        if (input_mcc.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.text_mcc));
            pDialog.show();
            return false;
        }

        if (input_gcwr.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.text_gcwr_1));
            pDialog.show();
            return false;
        }

        if (input_mgtw.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.text_mgtw_1));
            pDialog.show();
            return false;
        }

        if (input_mtw.getText().toString().isEmpty()) {
            pDialog.setMessage(getString(R.string.text_mtw_1));
            pDialog.show();
        }

        return true;
    }

    @Override
    public void onVinDataUpdate(VinModel data) {
        String strName = input_vehicle_name.getText().toString();
        if (!strName.isEmpty())
        {
            String vinCombinedName = data.getYear() + " " + data.getMake() + " " + data.getModel();
            input_vehicle_name.setText(vinCombinedName);
        }

        input_gvwr.setEnabled(false);
        String strGVWR = data.getSpecData().get("Gross_Vehicle_Weight_Rating");
        input_gvwr.setText(strGVWR);

        input_gcwr.setEnabled(false);
        String strGCWR = data.getSpecData().get("Gross_Combined_Weight_Rating");
        input_gcwr.setText(strGCWR);

        input_mcc.setEnabled(false);
        String strMCC = data.getSpecData().get("Max_Payload");
        input_mcc.setText(strMCC);

        if (saveModel != null) {
            saveModel.setVin(data.getVin());
            saveModel.setYear(data.getYear());
            saveModel.setMake(data.getMake());
            saveModel.setModel(data.getModel());
        }
    }
}