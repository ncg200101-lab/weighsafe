package com.weigh.safemobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.addisonelliott.segmentedbutton.SegmentedButton;
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.weigh.safemobile.R;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.fragments.DetailModalFragment;
import com.weigh.safemobile.models.HitchModelOptions;
import com.weigh.safemobile.models.SetupHitchModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class NewHitchActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;

    @BindView(R.id.setup_title)
    TextView txtTitle;

    @BindView(R.id.edit_btn_save)
    TextView edtBtnSave;

    @BindView(R.id.ll_weigh_safe_check)
    LinearLayout llweighSafe;

    @BindView(R.id.toggle_weighsafe)
    LabeledSwitch switchWeigh;

    @BindView(R.id.link_how_weigh_safe)
    TextView linkHowto;

    @BindView(R.id.edit_panel)
    RelativeLayout edit_panel;

    @BindView(R.id.ll_hitch_model)
    LinearLayout ll_hitch_model;
    @BindView(R.id.spinner_hitch_model)
    Spinner hitchSpinner;

    @BindView(R.id.link_installation_guide)
    TextView linkInstallationGuide;

    @BindView(R.id.ll_name_hitch)
    LinearLayout ll_name_hitch;
    @BindView(R.id.hitch_name_text)
    EditText input_hitch_name;

    @BindView(R.id.ll_type)
    LinearLayout ll_type;
    @BindView(R.id.spinner_hitch_type)
    Spinner typeSpinner;

    @BindView(R.id.ll_max_gtwr)
    LinearLayout ll_max_gtwr;
    @BindView(R.id.edit_max_gtwr)
    EditText input_max_gtwr;

    @BindView(R.id.ll_mtw)
    LinearLayout ll_mtw;
    @BindView(R.id.edit_max_tongue_weight)
    EditText input_max_tw;

    @BindView(R.id.ll_ball_size)
    LinearLayout ll_ball_size;
    @BindView(R.id.btn_group_ballsize)
    SegmentedButtonGroup btnBallSizes;

    @BindView(R.id.ll_shank_size)
    LinearLayout ll_shank_size;
    @BindView(R.id.btn_group_shanksize)
    SegmentedButtonGroup btnShankSizes;

    @BindView(R.id.ll_delete_hitch)
    LinearLayout ll_delete;

    final static String STR_LINK_HOW_TO = "https://www.weigh-safe.com";

    final static List<String> modelNames = Arrays.asList(new String[] {
            "Weigh Safe Drop Hitch",
            "Steel Weigh Safe Drop Hitch",
            "Weigh Safe 180 Hitch",
            "Universal Tow Ball",
            "Fixed Height Ball Mount",
            "True Tow Weight Distribution Hitch",
            "Safe Load OEM Gooseneck Ball",
            "Safe Load BW Insert Gooseneck"
    });

    List<HitchModelOptions> modelOptions = new ArrayList<>();
    int activeModel;

    final static List<String> typeOptions = Arrays.asList(new String[] {
            "Bumper Pull",
            "Weight Distribution",
            "Gooseneck",
            "Fifth Wheel"
    });

    static List<String> ballSizeOptions = Arrays.asList(new String[] {
            "2 5/16\"",
            "2\"",
            "1 7/8\""

    });

    static List<String> shankSizeOptions = Arrays.asList(new String[] {
            "2\"",
            "2.5\"",
            "3\""
    });

    boolean mEditHitch = false;
    boolean mFirstSelect = false;
    boolean mFirstSelectHitch = false;

    private SetupHitchModel selectedHitch;
    private int selectedRealmId;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hitch);
        ButterKnife.bind(this);

        initializerView();
        initializerSave();
        initializeListener();
    }

    public void initializerView() {

        Intent intent = getIntent();
        mEditHitch = intent.getBooleanExtra("editable", false);

        initModelOptions();

        if (mEditHitch)
        {
            selectedRealmId = intent.getIntExtra("realmId", -1);
            selectedHitch = DatabaseManager.getHitchModelByRealmId(selectedRealmId);
            selectedPosition = intent.getIntExtra("position", -1);

            ll_delete.setVisibility(View.VISIBLE);
            txtTitle.setText(getText(R.string.text_edit_hitchs));
        } else
        {
            selectedHitch = null;
            selectedRealmId = -1;
            selectedPosition = -1;

            ll_delete.setVisibility(View.GONE);
            txtTitle.setText(getText(R.string.text_new_hitch));
        }

        switchWeigh.setOn(false);

        typeSpinner.setSelection(0);

        btnBallSizes.setPosition(0, false);
        btnShankSizes.setPosition(0, false);

        activeModel = 0;

        loadData();

        if (switchWeigh.isOn())
        {
            ll_hitch_model.setVisibility(View.VISIBLE);
            ll_type.setVisibility(View.GONE);
            ll_name_hitch.setVisibility(View.GONE);
            ll_max_gtwr.setVisibility(View.GONE);
            ll_mtw.setVisibility(View.GONE);
            linkInstallationGuide.setVisibility(View.VISIBLE);

        } else
        {
            ll_hitch_model.setVisibility(View.GONE);
            ll_type.setVisibility(View.VISIBLE);
            ll_name_hitch.setVisibility(View.VISIBLE);
            ll_max_gtwr.setVisibility(View.VISIBLE);
            ll_mtw.setVisibility(View.VISIBLE);
            linkInstallationGuide.setVisibility(View.GONE);
        }
    }

    public void initializerSave() {
        edtBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields())
                {
                    if (mEditHitch)
                    {
                        SetupHitchModel saveModel = new SetupHitchModel();
                        saveModel.setnRealmId(selectedHitch.getnRealmId());

                        saveModel.setOnSafeHitch(switchWeigh.isOn());
                        saveModel.setHitchType(typeSpinner.getSelectedItem().toString());

                        if (switchWeigh.isOn())
                        {
                            saveModel.setBrand(hitchSpinner.getSelectedItem().toString());
                            saveModel.setHitchIndex(hitchSpinner.getSelectedItemPosition());

                            if (modelOptions.get(activeModel).getMgtwr() != null && modelOptions.get(activeModel).getMgtwr().get("none") != null) {
                                double mGTWR = Double.valueOf((Integer)modelOptions.get(activeModel).getMgtwr().get("none"));
                                saveModel.setmMaxGTWR(mGTWR);
                            } else {
                                if (modelOptions.get(activeModel).getType().equals("Weight Distribution")) {
                                    modelOptions.get(activeModel).getMgtwr().get("shank");

                                    List<Integer> shankWeight = (List<Integer>) modelOptions.get(activeModel).getMgtwr().get("shank");
                                    double mGTWR = Double.valueOf(shankWeight.get(btnShankSizes.getPosition()));
                                    saveModel.setmMaxGTWR(mGTWR);
                                } else
                                {
                                    List<Integer> ballWeight = (List<Integer>) modelOptions.get(activeModel).getMgtwr().get("ball");
                                    double ballValue = Double.valueOf(ballWeight.get(btnBallSizes.getPosition()));
                                    double shankValue;

                                    List<Integer> shankWeight = (List<Integer>) modelOptions.get(activeModel).getMgtwr().get("shank");
                                    if (btnBallSizes.getPosition() == 2)
                                        shankValue = Double.valueOf(shankWeight.get(btnShankSizes.getPosition()));
                                    else
                                        shankValue = 0;

                                    saveModel.setmMaxGTWR(ballValue + shankValue);
                                }
                            }

                            if (modelOptions.get(activeModel).getMtw() != null && modelOptions.get(activeModel).getMtw().get("none") != null) {
                                double mTW = Double.valueOf((Integer)modelOptions.get(activeModel).getMtw().get("none"));
                                saveModel.setmMaxTongue(mTW);
                            } else {
                                List<Integer> shankWeight = (List<Integer>)modelOptions.get(activeModel).getMtw().get("shank");
                                double mTW = Double.valueOf(shankWeight.get(btnShankSizes.getPosition()));
                                saveModel.setmMaxTongue(mTW);
                            }

                            saveModel.setBrand(modelOptions.get(activeModel).getName());
                            saveModel.setHitchType(modelOptions.get(activeModel).getType());
                        }
                        else
                        {
                            saveModel.setBrand(input_hitch_name.getText().toString());
                            saveModel.setHitchType(typeSpinner.getSelectedItem().toString());
                            saveModel.setTypeIndex(typeSpinner.getSelectedItemPosition());
                            saveModel.setmMaxGTWR(Double.valueOf(input_max_gtwr.getText().toString()));
                            saveModel.setmMaxTongue(Double.valueOf(input_max_tw.getText().toString()));
                        }

                        if (ll_ball_size.getVisibility() == View.GONE)
                        {
                            saveModel.setmBall("n/a");
                            saveModel.setBallIndex(0);
                        }
                        else
                        {
                            int ballPos = btnBallSizes.getPosition();
                            saveModel.setmBall(ballSizeOptions.get(ballPos));
                            saveModel.setBallIndex(ballPos);
                        }

                        if (ll_shank_size.getVisibility() == View.GONE)
                        {
                            saveModel.setmShank("n/a");
                            saveModel.setShankIndex(0);
                        }
                        else
                        {
                            int shankPos = btnShankSizes.getPosition();
                            saveModel.setmShank(shankSizeOptions.get(shankPos));
                            saveModel.setShankIndex(shankPos);
                        }

                        int nRealmId = selectedHitch.getnRealmId();
                        DatabaseManager.updateHitchModel(nRealmId, saveModel);
                        MainApplication.getInstance().getHitchUpdateListener().onEditDialogSave(selectedPosition, saveModel);
                        finish();
                    }
                    else
                    {
                        SetupHitchModel saveModel = new SetupHitchModel();
                        saveModel.setOnSafeHitch(switchWeigh.isOn());

                        if (switchWeigh.isOn())
                        {
                            saveModel.setBrand(hitchSpinner.getSelectedItem().toString());
                            saveModel.setHitchIndex(hitchSpinner.getSelectedItemPosition());

                            if (modelOptions.get(activeModel).getMgtwr() != null && modelOptions.get(activeModel).getMgtwr().get("none") != null) {
                                double mGTWR = Double.valueOf((Integer)modelOptions.get(activeModel).getMgtwr().get("none"));
                                saveModel.setmMaxGTWR(mGTWR);
                            } else {
                                if (modelOptions.get(activeModel).getType().equals("Weight Distribution")) {
                                    modelOptions.get(activeModel).getMgtwr().get("shank");

                                    List<Integer> shankWeight = (List<Integer>) modelOptions.get(activeModel).getMgtwr().get("shank");
                                    double mGTWR = Double.valueOf(shankWeight.get(btnShankSizes.getPosition()));
                                    saveModel.setmMaxGTWR(mGTWR);
                                } else
                                {
                                    List<Integer> ballWeight = (List<Integer>) modelOptions.get(activeModel).getMgtwr().get("ball");
                                    double ballValue = Double.valueOf(ballWeight.get(btnBallSizes.getPosition()));
                                    double shankValue;

                                    List<Integer> shankWeight = (List<Integer>) modelOptions.get(activeModel).getMgtwr().get("shank");
                                    if (btnBallSizes.getPosition() == 2)
                                        shankValue = Double.valueOf(shankWeight.get(btnShankSizes.getPosition()));
                                    else
                                        shankValue = 0;

                                    saveModel.setmMaxGTWR(ballValue + shankValue);
                                }
                            }

                            if (modelOptions.get(activeModel).getMtw() != null && modelOptions.get(activeModel).getMtw().get("none") != null) {
                                double mTW = Double.valueOf((Integer)modelOptions.get(activeModel).getMtw().get("none"));
                                saveModel.setmMaxTongue(mTW);
                            } else {
                                List<Integer> shankWeight = (List<Integer>)modelOptions.get(activeModel).getMtw().get("shank");
                                double mTW = Double.valueOf(shankWeight.get(btnShankSizes.getPosition()));
                                saveModel.setmMaxTongue(mTW);
                            }

                            saveModel.setBrand(modelOptions.get(activeModel).getName());
                            saveModel.setHitchType(modelOptions.get(activeModel).getType());
                        }
                        else
                        {
                            saveModel.setBrand(input_hitch_name.getText().toString());

                            saveModel.setHitchType(typeSpinner.getSelectedItem().toString());
                            saveModel.setTypeIndex(typeSpinner.getSelectedItemPosition());

                            saveModel.setmMaxGTWR(Double.valueOf(input_max_gtwr.getText().toString()));
                            saveModel.setmMaxTongue(Double.valueOf(input_max_tw.getText().toString()));
                        }

                        if (ll_ball_size.getVisibility() == View.GONE)
                        {
                            saveModel.setmBall("n/a");
                            saveModel.setBallIndex(0);
                        }
                        else
                        {
                            int ballPos = btnBallSizes.getPosition();
                            saveModel.setmBall(ballSizeOptions.get(ballPos));
                            saveModel.setBallIndex(ballPos);
                        }

                        if (ll_shank_size.getVisibility() == View.GONE)
                        {
                            saveModel.setmShank("n/a");
                            saveModel.setShankIndex(0);
                        }
                        else
                        {
                            int shankPos = btnShankSizes.getPosition();
                            saveModel.setmShank(shankSizeOptions.get(shankPos));
                            saveModel.setShankIndex(shankPos);
                        }

                        SetupHitchModel returnModel = DatabaseManager.addNewHitchModel(saveModel);
                        MainApplication.getInstance().getHitchUpdateListener().onNewDialogSave(returnModel);
                        finish();
                    }

                }
            }
        });
    }

    public void initializeListener() {


        hitchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mFirstSelectHitch)
                    selectHitchModel(position);
                mFirstSelectHitch = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mFirstSelect)
                    selectType(position);
                mFirstSelect = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switchWeigh.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                if (isOn)
                {
                    ll_hitch_model.setVisibility(View.VISIBLE);
                    ll_type.setVisibility(View.GONE);
                    ll_name_hitch.setVisibility(View.GONE);
                    ll_max_gtwr.setVisibility(View.GONE);
                    ll_mtw.setVisibility(View.GONE);
                    linkInstallationGuide.setVisibility(View.VISIBLE);
                } else
                {
                    ll_hitch_model.setVisibility(View.GONE);
                    ll_type.setVisibility(View.VISIBLE);
                    ll_name_hitch.setVisibility(View.VISIBLE);
                    ll_max_gtwr.setVisibility(View.VISIBLE);
                    ll_mtw.setVisibility(View.VISIBLE);
                    linkInstallationGuide.setVisibility(View.GONE);
                }
            }
        });

        linkHowto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlText = STR_LINK_HOW_TO;
                String typeField = typeSpinner.getSelectedItem().toString();
                switch (typeField) {
                    case "Weight Distribution":
                        urlText = urlText + "/product/true-tow-weight-distribution-hitch/";
                        break;
                    case "Bumper Pull":
                        urlText = urlText + "/ball-mounts/";
                        break;
                    case "Gooseneck":
                        urlText = urlText + "/gooseneck/";
                        break;
                    default:
                        urlText = STR_LINK_HOW_TO;
                        break;
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlText));
                startActivity(browserIntent);
            }
        });

        linkInstallationGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HitchModelOptions model = modelOptions.get(activeModel);
                if (!model.getGuide().isEmpty())
                {
                    String url = model.getGuide();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
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
                List<SetupHitchModel> allVehicles = DatabaseManager.getAllSetupHitches();
                if (allVehicles != null)
                {
                    if (allVehicles.size() == 1)
                    {
                        PrettyDialog pDialog = new PrettyDialog(NewHitchActivity.this);
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

                        pDialog.setMessage(getString(R.string.text_delete_not_hitch));
                        pDialog.show();
                    } else if (allVehicles.size() > 1)
                    {
                        PrettyDialog pDialog = new PrettyDialog(NewHitchActivity.this);
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
                                        DatabaseManager.deleteHitchModel(selectedHitch.getnRealmId());
                                        MainApplication.getInstance().getHitchUpdateListener().onDeleteDialogSave(selectedPosition);
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

    private void initModelOptions()
    {
        //Model 1 - Weigh Safe Drop Hitch
        HitchModelOptions model1 = new HitchModelOptions();
        model1.setName("Weigh Safe Drop Hitch");
        model1.setType("Bumper Pull");

        HashMap<String, Object> mgtwr = new HashMap<>();
        List<Integer> ballOptions = Arrays.asList(new Integer[] {7500, 8000, 12500});
        List<Integer> shankOptions = Arrays.asList(new Integer[] {0, 6000, 8500});
        mgtwr.put("ball", ballOptions);
        mgtwr.put("shank", shankOptions);
        model1.setMgtwr(mgtwr);

        HashMap<String, Object> mtw = new HashMap<>();
        shankOptions = Arrays.asList(new Integer[] {1500, 2200, 2200});
        mtw.put("shank", shankOptions);
        model1.setMtw(mtw);
        model1.setGuide(getString(R.string.guide_weigh_safe_drop_hitch));

        //Model 2- Steel Weigh Safe Drop Hitch
        HitchModelOptions model2 = new HitchModelOptions();
        model2.setName("Steel Weigh Safe Drop Hitch");
        model2.setType("Bumper Pull");

        HashMap<String, Object> mgtwr2 = new HashMap<>();
        ballOptions = Arrays.asList(new Integer[] {7500, 8000, 12500});
        shankOptions = Arrays.asList(new Integer[] {0, 6000});
        mgtwr2.put("ball", ballOptions);
        mgtwr2.put("shank", shankOptions);
        model2.setMgtwr(mgtwr2);

        HashMap<String, Object> mtw2 = new HashMap<>();
        shankOptions = Arrays.asList(new Integer[] {1500, 2200});
        mtw2.put("shank", shankOptions);
        model2.setMtw(mtw2);
        model2.setGuide(getString(R.string.guide_weigh_safe_drop_hitch));

        //Model 3- Weigh Safe 180 Hitch
        HitchModelOptions model3 = new HitchModelOptions();
        model3.setName("Weigh Safe 180 Hitch");
        model3.setType("Bumper Pull");

        HashMap<String, Object> mgtwr3 = new HashMap<>();
        ballOptions = Arrays.asList(new Integer[] {7500, 8000, 12500});
        shankOptions = Arrays.asList(new Integer[] {0, 6000, 8500});
        mgtwr3.put("ball", ballOptions);
        mgtwr3.put("shank", shankOptions);
        model3.setMgtwr(mgtwr3);

        HashMap<String, Object> mtw3 = new HashMap<>();
        shankOptions = Arrays.asList(new Integer[] {1500, 2200, 2200});
        mtw3.put("shank", shankOptions);
        model3.setMtw(mtw3);
        model3.setGuide(getString(R.string.guide_weigh_safe_180_hitch));

        //Model 4 - Universal Tow Ball
        HitchModelOptions model4 = new HitchModelOptions();
        model4.setName("Universal Tow Ball");
        model4.setType("Bumper Pull");

        HashMap<String, Object> mgtwr4 = new HashMap<>();
        mgtwr4.put("none", 100000);
        model4.setMgtwr(mgtwr4);

        HashMap<String, Object> mtw4 = new HashMap<>();
        mtw4.put("none", 1500);
        model4.setMtw(mtw4);
        model4.setGuide(getString(R.string.guide_universal_tow_ball));

        //Model 5 - Fixed Height Ball Mount
        HitchModelOptions model5 = new HitchModelOptions();
        model5.setName("Fixed Height Ball Mount");
        model5.setType("Bumper Pull");

        HashMap<String, Object> mgtwr5 = new HashMap<>();
        mgtwr5.put("none", 100000);
        model5.setMgtwr(mgtwr5);

        HashMap<String, Object> mtw5 = new HashMap<>();
        mtw5.put("none", 1500);
        model5.setMtw(mtw5);
        model5.setGuide(getString(R.string.guide_fixed_height_ball_mount));

        //Model 6 - True Tow Weight Distribution Hitch
        HitchModelOptions model6 = new HitchModelOptions();
        model6.setName("True Tow Weight Distribution Hitch");
        model6.setType("Weight Distribution");

        HashMap<String, Object> mgtwr6 = new HashMap<>();
        shankOptions = Arrays.asList(new Integer[] {15000, 20000});
        mgtwr6.put("shank", shankOptions);
        model6.setMgtwr(mgtwr6);

        HashMap<String, Object> mtw6 = new HashMap<>();
        shankOptions = Arrays.asList(new Integer[] {1500, 2000});
        mtw6.put("shank", shankOptions);
        model6.setMtw(mtw6);
        model6.setGuide(getString(R.string.guide_true_tow_weight_hitch));

        //Model 7 - Safe Load OEM Gooseneck Ball
        HitchModelOptions model7 = new HitchModelOptions();
        model7.setName("Safe Load OEM Gooseneck Ball");
        model7.setType("Gooseneck");

        HashMap<String, Object> mgtwr7 = new HashMap<>();
        mgtwr7.put("none", 30000);
        model7.setMgtwr(mgtwr7);

        HashMap<String, Object> mtw7 = new HashMap<>();
        mtw7.put("none", 7500);
        model7.setMtw(mtw7);
        model7.setGuide(getString(R.string.guide_safe_load_oem_gooseneck));

        //Model 8 - Safe Load ISR Gooseneck
        HitchModelOptions model8 = new HitchModelOptions();
        model8.setName("Safe Load ISR Gooseneck");
        model8.setType("Gooseneck");

        HashMap<String, Object> mgtwr8 = new HashMap<>();
        mgtwr8.put("none", 30000);
        model8.setMgtwr(mgtwr8);

        HashMap<String, Object> mtw8 = new HashMap<>();
        mtw8.put("none", 7500);
        model8.setMtw(mtw8);
        model8.setGuide(getString(R.string.guide_safe_load_isr_gooseneck));

        //Model 9 - Safe Load BW Insert Gooseneck
        HitchModelOptions model9 = new HitchModelOptions();
        model9.setName("Safe Load BW Insert Gooseneck");
        model9.setType("Gooseneck");

        HashMap<String, Object> mgtwr9 = new HashMap<>();
        mgtwr9.put("none", 30000);
        model9.setMgtwr(mgtwr9);

        HashMap<String, Object> mtw9 = new HashMap<>();
        mtw9.put("none", 7500);
        model9.setMtw(mtw9);
        model9.setGuide("");

        modelOptions.add(model1);
        modelOptions.add(model2);
        modelOptions.add(model3);
        modelOptions.add(model4);
        modelOptions.add(model5);
        modelOptions.add(model6);
        modelOptions.add(model7);
        modelOptions.add(model8);
        modelOptions.add(model9);
    }

    private void loadData()
    {
        if (selectedHitch == null)
            return;

        switchWeigh.setOn(selectedHitch.isOnSafeHitch());

        input_max_tw.setText(String.valueOf(selectedHitch.getmMaxTongue()));
        input_max_gtwr.setText(String.valueOf(selectedHitch.getmMaxGTWR()));

        if (switchWeigh.isOn()) {
            if (selectedHitch.getHitchIndex() != -1)
            {
                hitchSpinner.setSelection(selectedHitch.getHitchIndex());
                mFirstSelectHitch = true;
                selectHitchModel(selectedHitch.getHitchIndex());
            } else
            {
                hitchSpinner.setSelection(0);
                mFirstSelectHitch = true;
                selectHitchModel(0);
            }

            int ballPos = selectedHitch.getBallIndex();
            btnBallSizes.setPosition(ballPos, false);
            btnShankSizes.setPosition(selectedHitch.getShankIndex(), false);
        }
        else {
            input_hitch_name.setText(selectedHitch.getBrand());
            typeSpinner.setSelection(selectedHitch.getTypeIndex());
            mFirstSelect = true;
            selectType(selectedHitch.getTypeIndex());
        }
    }

    private void selectType(int row)
    {
        ViewGroup container = (ViewGroup)btnBallSizes.getChildAt(0);
        ViewGroup buttonLayout = (ViewGroup)container.getChildAt(0);
        ViewGroup dividerLayout = (ViewGroup)container.getChildAt(2);

        buttonLayout.removeAllViews();
        dividerLayout.removeAllViews();

        btnBallSizes.getButtons().clear();

        ballSizeOptions = Arrays.asList(new String[] {
                "2 5/16\"",
                "2\"",
                "1 7/8\""
        });

        switch (row)
        {
            case 2: {
                ll_ball_size.setVisibility(View.VISIBLE);
                ll_shank_size.setVisibility(View.GONE);
                ballSizeOptions = Arrays.asList(new String[] {
                        "2 5/16\"",
                        "2\""
                });
                break;
            }
            case 3: {
                ll_ball_size.setVisibility(View.GONE);
                ll_shank_size.setVisibility(View.GONE);
                break;
            }
            default: {
                ll_ball_size.setVisibility(View.VISIBLE);
                ll_shank_size.setVisibility(View.VISIBLE);
                break;
            }
        }

        int nIndex = 0;
        for (String ballSize : ballSizeOptions) {
            SegmentedButton btnNew = new SegmentedButton(NewHitchActivity.this);
            btnNew.setSelectedTextColor(ContextCompat.getColor(NewHitchActivity.this, R.color.colorBlack));
            btnNew.setText(ballSize);
            btnNew.setTextColor(ContextCompat.getColor(NewHitchActivity.this, R.color.colorBlack));

            float sp = 14f * getResources().getDisplayMetrics().scaledDensity;
            btnNew.setTextSize(sp);

            SegmentedButtonGroup.LayoutParams params = new SegmentedButtonGroup.LayoutParams(0, SegmentedButtonGroup.LayoutParams.MATCH_PARENT, 1.0f);

            //int padding_in_px = 5;  // 6 dps
            //final float scale = getResources().getDisplayMetrics().density;
            //int padding_in_dp = (int) ((padding_in_px - 0.5f) / scale);

            btnNew.setPadding(5, 5, 5, 5);
            btnNew.setLayoutParams(params);
            btnBallSizes.addView(btnNew, params);
            nIndex++;
        }

        btnBallSizes.invalidate();
        btnBallSizes.setPosition(0, false);

        nIndex = 0;
    }

    private void selectHitchModel(int row)
    {
        hitchSpinner.setSelection(row);

        ViewGroup container = (ViewGroup)btnShankSizes.getChildAt(0);
        ViewGroup buttonLayout = (ViewGroup)container.getChildAt(0);
        ViewGroup dividerLayout = (ViewGroup)container.getChildAt(2);

        switch (row)
        {
            case 1:{
                ll_ball_size.setVisibility(View.VISIBLE);
                ll_shank_size.setVisibility(View.VISIBLE);
                shankSizeOptions = Arrays.asList(new String[] {
                        "2\"",
                        "2.5\""
                });

                if (activeModel != 5) {
                    //btnShankSizes.addView();
                    buttonLayout.removeViewAt(2);
                    dividerLayout.removeViewAt(1);
                    btnShankSizes.setPosition(0, false);
                }

                break;
            }
            case 3:case 4:case 6:case 7:case 8: {
                ll_ball_size.setVisibility(View.GONE);
                ll_shank_size.setVisibility(View.GONE);

                if (activeModel == 5 || activeModel == 1)
                {
                    SegmentedButton btnNew = new SegmentedButton(this);
                    btnNew.setSelectedTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                    btnNew.setText("3\"");
                    btnNew.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));

                    float sp = 14f * getResources().getDisplayMetrics().scaledDensity;
                    btnNew.setTextSize(sp);
                    //btnNew.setTextSize(15);

                    SegmentedButtonGroup.LayoutParams params = new SegmentedButtonGroup.LayoutParams(0, SegmentedButtonGroup.LayoutParams.MATCH_PARENT, 1.0f);
                    //btnNew.setPadding(5, 5, 5, 5);
                    btnNew.setPadding(5, 5, 5, 5);
                    btnNew.setLayoutParams(params);

                    btnShankSizes.addView(btnNew, params);
                    btnShankSizes.setPosition(0, false);
                }
                break;
            }
            case 5: {
                ll_ball_size.setVisibility(View.GONE);
                ll_shank_size.setVisibility(View.VISIBLE);
                shankSizeOptions = Arrays.asList(new String[] {
                        "2\"",
                        "2.5\""
                });

                if (activeModel != 5) {
                    buttonLayout.removeViewAt(2);
                    dividerLayout.removeViewAt(1);
                    btnShankSizes.setPosition(0, false);
                }
                break;
            }
            default: {
                ll_ball_size.setVisibility(View.VISIBLE);
                ll_shank_size.setVisibility(View.VISIBLE);
                shankSizeOptions = Arrays.asList(new String[] {
                        "2\"",
                        "2.5\"",
                        "3\""
                });

                if (activeModel == 5 || activeModel == 1) {
                    SegmentedButton btnNew = new SegmentedButton(this);
                    btnNew.setSelectedTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                    btnNew.setText("3\"");
                    btnNew.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                    float sp = 14f * getResources().getDisplayMetrics().scaledDensity;
                    btnNew.setTextSize(sp);
                    //btnNew.setTextSize(15);

                    SegmentedButtonGroup.LayoutParams params = new SegmentedButtonGroup.LayoutParams(0, SegmentedButtonGroup.LayoutParams.MATCH_PARENT, 1.0f);
                    //btnNew.setPadding(5, 5, 5, 5);
                    btnNew.setPadding(5, 5, 5, 5);
                    btnNew.setLayoutParams(params);

                    btnShankSizes.addView(btnNew, params);
                    btnShankSizes.setPosition(0, false);
                }
                break;
            }
        }

        activeModel = row;
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

        if (!switchWeigh.isOn())
        {
            if (input_hitch_name.getText().toString().isEmpty()) {
                pDialog.setMessage(getString(R.string.text_enter_name));
                pDialog.show();
                return false;
            }

            if (input_max_gtwr.getText().toString().isEmpty()) {
                pDialog.setMessage(getString(R.string.text_mgtw_1));
                pDialog.show();
                return false;
            }

            if (input_max_tw.getText().toString().isEmpty()) {
                pDialog.setMessage(getString(R.string.text_mtw_1));
                pDialog.show();
                return false;
            }
        }

        return true;
    }

    @OnClick({R.id.img_sub_type, R.id.img_sub_gtwr, R.id.img_sub_mtw})
    public void OnClickImage(View v) {
        DetailModalFragment dialog = null;
        switch (v.getId()) {
            case R.id.img_sub_type:
                dialog = DetailModalFragment.newInstance(getString(R.string.lb_hitch_type), R.drawable.hitch_type);
                break;
            case R.id.img_sub_gtwr:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_max_gtwr), R.drawable.hitch_rating);
                break;
            case R.id.img_sub_mtw:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_max_tongue_weight), R.drawable.hitch_rating);
                break;
        }
        dialog.show(getSupportFragmentManager(), "detailModal");
    }
}