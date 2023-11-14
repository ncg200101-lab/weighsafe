package com.weigh.safemobile.fragments2;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.addisonelliott.segmentedbutton.SegmentedButton;
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.MainActivity;
import com.weigh.safemobile.activities.MeasurementsActivity;
import com.weigh.safemobile.adapters.SetupConfigAdapter;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.fragments.HelpFragment;
import com.weigh.safemobile.fragments.ModalFragment;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.CheckListModel;
import com.weigh.safemobile.models.ConfigPusher;
import com.weigh.safemobile.models.Configuration;
import com.weigh.safemobile.models.SetupCargoModel;
import com.weigh.safemobile.models.SetupHitchModel;
import com.weigh.safemobile.models.SetupTrailerModel;
import com.weigh.safemobile.models.SetupVehicleModel;
import com.weigh.safemobile.presenters.MeasurementsListener;
import com.weigh.safemobile.presenters.ModalFragmentListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.realm.Realm;
import io.realm.RealmList;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class HomeFragment extends Fragment implements MeasurementsListener, ModalFragmentListener {

    @BindView(R.id.setup_panel)
    RelativeLayout setup_panel;
    @BindView(R.id.text_vehicle_title)
    TextView textVehicleTitle;
    @BindView(R.id.scrollview_setup)
    ScrollView sv;
    @BindView(R.id.btn_dropdown)
    ImageView btnDropdown;
    @BindView(R.id.vehicleListView)
    RelativeLayout vehicleListView;
    @BindView(R.id.vehiclesList)
    RecyclerView vehiclesList;
    @BindView(R.id.addText)
    TextView addText;

    @BindView(R.id.rl_hitchtitle)
    RelativeLayout rl_hitchtitle;
    @BindView(R.id.txt_hitchtitle)
    TextView text_hitchtitle;
    @BindView(R.id.rl_hitchmenu)
    RelativeLayout rl_hitchmenu;
    @BindView(R.id.txt_helpsetup)
    TextView link_helpsetup;

    @BindView(R.id.img_vehicle_setup)
    ImageView img_vehicle;

    @BindView(R.id.ll_weight_vr)
    LinearLayout ll_weight_vr;

    @BindView(R.id.ll_gvwr)
    LinearLayout ll_gvwr;
    @BindView(R.id.val_gvwr)
    TextView mTxtGVWR;

    @BindView(R.id.ll_gcwr)
    LinearLayout ll_gcwr;
    @BindView(R.id.val_gcwr)
    TextView mTxtGCWR;

    @BindView(R.id.ll_gtwr)
    LinearLayout ll_gtwr;
    @BindView(R.id.val_gtwr)
    TextView mTxtGTWR;

    @BindView(R.id.ll_cargoview)
    LinearLayout ll_cargoview;
    @BindView(R.id.val_cargo_vehicle)
    EditText input_cargo_vehicle;
    @BindView(R.id.btn_plus_cargo)
    Button btnPlusCargo;
    @BindView(R.id.btn_minus_cargo)
    Button btnMinusCargo;
    @BindView(R.id.val_cargo_trailer)
    EditText input_cargo_Trailer;
    @BindView(R.id.btn_plus_cargo_trailer)
    Button btnPlusCargoTrailer;
    @BindView(R.id.btn_minus_cargo_trailer)
    Button btnMinusCargoTrailer;
    @BindView(R.id.label_vehicle_adj)
    TextView mVehicleCargoAdj;
    @BindView(R.id.label_trailer_adj)
    TextView mTrailerCargoAdj;

    @BindView(R.id.ll_weight_estimated)
    LinearLayout ll_estimated_weight;

    @BindView(R.id.ll_est_gvw)
    LinearLayout ll_est_gvw;
    @BindView(R.id.val_est_gvw)
    TextView mTxtEstGVW;

    @BindView(R.id.ll_est_gcw)
    LinearLayout ll_est_gcw;
    @BindView(R.id.val_est_gcw)
    TextView mTxtEstGCW;

    @BindView(R.id.ll_est_gtw)
    LinearLayout ll_est_gtw;
    @BindView(R.id.val_est_gtw)
    TextView mTxtEstGTW;

    @BindView(R.id.ll_tongue_weight)
    LinearLayout ll_tongue_weight;
    @BindView(R.id.ll_actual_tongueweight)
    LinearLayout ll_actual_tongueweight;
    @BindView(R.id.lb_tongue_weight)
    TextView txt_tongue_weight;
    @BindView(R.id.val_actual_tongueweight)
    EditText input_actual_tongueweight;
    @BindView(R.id.ll_distributed_tongueweight)
    LinearLayout ll_distributed_tongueweight;
    @BindView(R.id.btn_calculate_dis)
    Button btnCalculateDis;

    @BindView(R.id.ll_calculate_results)
    LinearLayout ll_calculate_Results;
    @BindView(R.id.ll_calculate_box)
    LinearLayout ll_calculate_box;
    @BindView(R.id.val_min_weight)
    TextView txt_min_safe_weight;
    @BindView(R.id.val_safe_weight)
    TextView txt_safe_weight;
    @BindView(R.id.val_max_weight)
    TextView txt_max_safe_weight;
    @BindView(R.id.lb_zone_equalized)
    TextView txt_zone_equalized;
    @BindView(R.id.edit_measurements)
    TextView mEditMeasureView;

    @BindView(R.id.ll_safe_tongue_weight)
    LinearLayout ll_safe_tongue_weight;
    @BindView(R.id.val_min_tongueweight)
    TextView txt_min_tongueweight;
    @BindView(R.id.val_max_tongueweight)
    TextView txt_max_tongueweight;

    @BindView(R.id.ll_promo_Box)
    LinearLayout ll_promo_box;

    @BindView(R.id.ll_drawbar)
    LinearLayout ll_drawbar;
    @BindView(R.id.btngroup_drawbar_stepper)
    SegmentedButtonGroup btnDrawBarGroup;
    @BindView(R.id.val_holes)
    TextView txt_holes;
    @BindView(R.id.btn_holes_stepper)
    SegmentedButtonGroup btnHolesGroup;
    @BindView(R.id.img_left_satisfy)
    ImageView imgSatisfyLeft;
    @BindView(R.id.img_right_satisfy)
    ImageView imgSatisfyRight;


    private SetupConfigAdapter mConfigAdapter = null;
    private View mView;

    private RealmList<Configuration> mConfigList;
    private Configuration selectedConfiguration;
    public List<SetupVehicleModel> vehicleModels;
    public List<SetupHitchModel> hitchModels;
    public List<SetupTrailerModel> trailerModels;
    public List<SetupCargoModel> cargoModels;


    private double vehicleCargo = 0;
    private double cacheVehicleCargo = 0;
    private double additionalVehicleCargo = 0;

    private double trailerCargo = 0;
    private double cacheTrailerCargo = 0;
    private double additionalTrailerCargo = 0;

    private EditText input_new_setup, input_edit_setup;

    private double holesMinValue = 0;
    private double holesMaxValue = 0;
    private double holesValue = 0;

    private Handler repeatUpdateHandler = new Handler();
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;

    private boolean mVCargoIncrement = false;
    private boolean mVCargoDecrement = false;

    private boolean mTCargoIncrement = false;
    private boolean mTCargoDecrement = false;

    private ConfigPusher editPusher;

    private SimpleTooltip popTip, popTipTW;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null)
        {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, mView);
            initializerView();
            initializeListener();
        }

        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
            return;

        mConfigList = MainApplication.getInstance().mConfigList;
        selectedConfiguration = MainApplication.getInstance().selectedConfiguration;
        mConfigAdapter = new SetupConfigAdapter(mConfigList, getActivity());
        vehiclesList.setAdapter(mConfigAdapter);
        mConfigAdapter.notifyDataSetChanged();

        vehicleModels = MainApplication.getInstance().vehicleModels;
        hitchModels = MainApplication.getInstance().hitchModels;
        trailerModels = MainApplication.getInstance().trailerModels;
        cargoModels = MainApplication.getInstance().cargoModels;

        initConfiguration();

        if (mConfigList.size() > 0 && (vehicleModels.size() == 0 || trailerModels.size() == 0 || hitchModels.size() == 0))
        {
            PrettyDialog pDialog = new PrettyDialog(getActivity());
            pDialog.setIcon(R.drawable.pdlg_icon_info);
            pDialog.setIconTint(R.color.pdlg_color_red);
            pDialog.setTitle(getString(R.string.text_incorrect_setup));
            pDialog.addButton(
                    "Go to Setup",			// button text
                    R.color.colorWhite,		// button text color
                    R.color.colorRed,		    // button background color
                    new PrettyDialogCallback() {	// button OnClick listener
                        @Override
                        public void onClick() {
                            pDialog.dismiss();
                            ((MainActivity)getActivity()).GoSetupVehicle();
                        }
                    });

            pDialog.setMessage(getString(R.string.text_incorrect_note));
            pDialog.show();
        } else if (mConfigList.size() == 0) {
            initNewSetup(true);
        } else {
            int positionId = PreferenceManager.getPositionID(getActivity());
            //mConfigAdapter.updateItem(positionId, selectedConfiguration);

            if (PreferenceManager.getInstallationGuide(getActivity()))
            {
                PrettyDialog pDialog = new PrettyDialog(getActivity());
                pDialog.setIcon(R.drawable.pdlg_icon_info);
                pDialog.setIconTint(R.color.pdlg_color_red);
                pDialog.setTitle(getString(R.string.title_hitch_notice));
                pDialog.addButton(
                        "SKIP",			// button text
                        R.color.colorWhite,		    // button text color
                        R.color.grey30,		    // button background color
                        new PrettyDialogCallback() {	// button OnClick listener
                            @Override
                            public void onClick() {
                                PreferenceManager.setInstallationGuide(getActivity(), false);
                                pDialog.dismiss();
                            }
                        });
                pDialog.addButton(
                        "HELP/SETUP", // button text
                        R.color.colorWhite,		    // button text color
                        R.color.pdlg_color_red,		        // button background color
                        new PrettyDialogCallback() {	// button OnClick listener
                            @Override
                            public void onClick() {
                                dismissPopupTip();
                                ((MainActivity)getActivity()).replaceFragment(HelpFragment.newInstance());
                                pDialog.dismiss();
                            }
                        });
                pDialog.setMessage(getString(R.string.text_wizard_trailer_message));
                pDialog.show();
            }

            refreshView(true);
        }

    }

    private void initializerView() {
        sv.setVerticalScrollBarEnabled(false);
        sv.setHorizontalScrollBarEnabled(false);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        vehiclesList.setLayoutManager(mLinearLayoutManager);
        vehiclesList.setItemAnimator(new DefaultItemAnimator());
        vehiclesList.getItemAnimator().setChangeDuration(0);

        //fix slow recyclerview start
        vehiclesList.setHasFixedSize(true);
        vehiclesList.setItemViewCacheSize(10);
        vehiclesList.setDrawingCacheEnabled(true);
        vehiclesList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mConfigList = MainApplication.getInstance().mConfigList;
        selectedConfiguration = MainApplication.getInstance().selectedConfiguration;
        /*
        vehicleModels = MainApplication.getInstance().vehicleModels;
        hitchModels = MainApplication.getInstance().hitchModels;
        trailerModels = MainApplication.getInstance().trailerModels;
        cargoModels = MainApplication.getInstance().cargoModels;
         */

        mConfigAdapter = new SetupConfigAdapter(mConfigList, getActivity());
        vehiclesList.setAdapter(mConfigAdapter);
        mConfigAdapter.notifyDataSetChanged();

        imgSatisfyLeft.setVisibility(View.INVISIBLE);
        imgSatisfyRight.setVisibility(View.INVISIBLE);

        initConfiguration();

        //PreferenceManager.setWizardOn(getActivity(), false);
    }

    public void initializeListener() {
        ll_promo_box.setClickable(true);

        ll_promo_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlText = "https://www.weigh-safe.com";
                int hitchIndex = PreferenceManager.getCurrentHitch(getActivity());

                List<SetupHitchModel> hitchModels = MainApplication.getInstance().hitchModels;
                SetupHitchModel _hitchModel = hitchModels.get(hitchIndex);

                switch (_hitchModel.getHitchType())
                {
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
                        urlText = "https://www.weigh-safe.com";
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlText));
                startActivity(browserIntent);
            }
        });

        btnCalculateDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               calculateDtw();
            }
        });

        SegmentedButton btnHolesMinus = btnHolesGroup.getButtons().get(0);
        SegmentedButton btnHolesPlus = btnHolesGroup.getButtons().get(1);

        btnHolesMinus.setOnLongClickListener(
                new View.OnLongClickListener(){
                    public boolean onLongClick(View arg0) {
                        mAutoIncrement = false;
                        mAutoDecrement = true;
                        if (holesValue > holesMinValue)
                            repeatUpdateHandler.post( new RptUpdater() );
                        return false;
                    }
                }
        );

        btnHolesMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement();
            }
        });

        btnHolesMinus.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement ){
                    mAutoDecrement = false;
                }
                return false;
            }
        });

        btnHolesPlus.setOnLongClickListener(
                new View.OnLongClickListener(){
                    public boolean onLongClick(View arg0) {
                        mAutoIncrement = true;
                        mAutoDecrement = false;
                        if (holesValue < holesMaxValue)
                            repeatUpdateHandler.post( new RptUpdater() );
                        return false;
                    }
                }
        );

        btnHolesPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment();
            }
        });

        btnHolesPlus.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement ){
                    mAutoIncrement = false;
                }
                return false;
            }
        });

        btnDrawBarGroup.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                selectedConfiguration.setnDrawbarPosition(position);
                DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);
            }
        });

        btnPlusCargo.setOnLongClickListener(
                new View.OnLongClickListener(){
                    public boolean onLongClick(View arg0) {
                        mVCargoIncrement = true;
                        mVCargoDecrement = false;
                        if (vehicleCargo < 99999)
                            repeatUpdateHandler.post( new VehicleCargoUpdater() );
                        return false;
                    }
                }
        );

        btnPlusCargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementVehicleCargo();
            }
        });

        btnPlusCargo.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && mVCargoIncrement ){
                    mVCargoIncrement = false;
                }
                return false;
            }
        });

        btnMinusCargo.setOnLongClickListener(
                new View.OnLongClickListener(){
                    public boolean onLongClick(View arg0) {
                        mVCargoIncrement = false;
                        mVCargoDecrement = true;
                        if (vehicleCargo > 0)
                            repeatUpdateHandler.post( new VehicleCargoUpdater() );
                        return false;
                    }
                }
        );

        btnMinusCargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementVehicleCargo();
            }
        });

        btnMinusCargo.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && mVCargoDecrement ){
                    mVCargoDecrement = false;
                }
                return false;
            }
        });

        btnPlusCargoTrailer.setOnLongClickListener(
                new View.OnLongClickListener(){
                    public boolean onLongClick(View arg0) {
                        mTCargoIncrement = true;
                        mTCargoDecrement = false;
                        if (trailerCargo < 99999)
                            repeatUpdateHandler.post( new TrailerCargoUpdater() );
                        return false;
                    }
                }
        );

        btnPlusCargoTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementTrailerCargo();
            }
        });

        btnPlusCargoTrailer.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && mTCargoIncrement ){
                    mTCargoIncrement = false;
                }
                return false;
            }
        });

        btnMinusCargoTrailer.setOnLongClickListener(
                new View.OnLongClickListener(){
                    public boolean onLongClick(View arg0) {
                        mTCargoIncrement = false;
                        mTCargoDecrement = true;
                        if (trailerCargo > 0)
                            repeatUpdateHandler.post( new TrailerCargoUpdater() );
                        return false;
                    }
                }
        );

        btnMinusCargoTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementTrailerCargo();
            }
        });

        btnMinusCargoTrailer.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && mTCargoDecrement ){
                    mTCargoDecrement = false;
                }
                return false;
            }
        });

        link_helpsetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(HelpFragment.newInstance());
            }
        });

        /*
        input_cargo_vehicle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (input_cargo_vehicle.getText().toString().isEmpty()) {
                    PrettyDialog pDialog = new PrettyDialog(getActivity());
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
                                    input_cargo_vehicle.requestFocus();
                                    pDialog.dismiss();
                                }
                            });
                    pDialog.setMessage(getString(R.string.text_enter_cargo_value));
                    pDialog.show();
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

         */

        input_cargo_vehicle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (input_cargo_vehicle.getText().toString().isEmpty()) {
                        PrettyDialog pDialog = new PrettyDialog(getActivity());
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
                                        input_cargo_vehicle.requestFocus();
                                        pDialog.dismiss();
                                    }
                                });
                        pDialog.setMessage(getString(R.string.text_enter_cargo_value));
                        pDialog.show();
                    }
                    else
                    {
                        vehicleCargo = Double.valueOf(input_cargo_vehicle.getText().toString());
                        additionalVehicleCargo = vehicleCargo - cacheVehicleCargo;

                        selectedConfiguration.setnAdditionalVehicleWeight(additionalVehicleCargo);
                        DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);

                        if (additionalVehicleCargo != 0) {
                            if (additionalVehicleCargo > 0)
                                mVehicleCargoAdj.setText(String.format("Cargo Adj. +%s", fmt(additionalVehicleCargo)));
                            else
                                mVehicleCargoAdj.setText(String.format("Cargo Adj. %s", fmt(additionalVehicleCargo)));
                        } else
                            mVehicleCargoAdj.setText("");

                        ll_distributed_tongueweight.setVisibility(View.GONE);
                        ll_calculate_Results.setVisibility(View.GONE);
                        refreshView(false);
                    }
                }
            }

        });

        /*
        input_cargo_Trailer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (input_cargo_Trailer.getText().toString().isEmpty()) {
                    PrettyDialog pDialog = new PrettyDialog(getActivity());
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
                                    input_cargo_Trailer.requestFocus();
                                    pDialog.dismiss();
                                }
                            });
                    pDialog.setMessage(getString(R.string.text_enter_cargo_value));
                    pDialog.show();
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
         */

        input_cargo_Trailer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (input_cargo_Trailer.getText().toString().isEmpty()) {
                        PrettyDialog pDialog = new PrettyDialog(getActivity());
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
                                        input_cargo_Trailer.requestFocus();
                                        pDialog.dismiss();
                                    }
                                });
                        pDialog.setMessage(getString(R.string.text_enter_cargo_value));
                        pDialog.show();
                    }
                    else
                    {
                        trailerCargo = Double.valueOf(input_cargo_Trailer.getText().toString());
                        additionalTrailerCargo = trailerCargo - cacheTrailerCargo;

                        selectedConfiguration.setnAdditionalTrailerWeight(additionalTrailerCargo);
                        DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);

                        if (additionalTrailerCargo != 0) {
                            if (additionalTrailerCargo > 0)
                                mTrailerCargoAdj.setText(String.format("Cargo Adj. +%s", fmt(additionalTrailerCargo)));
                            else
                                mTrailerCargoAdj.setText(String.format("Cargo Adj. %s", fmt(additionalTrailerCargo)));
                        } else
                            mTrailerCargoAdj.setText("");

                        ll_distributed_tongueweight.setVisibility(View.GONE);
                        ll_calculate_Results.setVisibility(View.GONE);
                        refreshView(false);
                    }
                }
            }
        });

        input_actual_tongueweight.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!input_actual_tongueweight.getText().toString().isEmpty() &&
                            !input_actual_tongueweight.getText().toString().equals("")) {
                        double tw = Double.valueOf(input_actual_tongueweight.getText().toString());
                        selectedConfiguration.setnTongueWeight(tw);
                        DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);

                        BottomNavigationView BottomView = ((MainActivity)getActivity()).getBottomNavigationView();
                        View anchorView = BottomView.findViewById(R.id.tab_checklist);

                        if (popTip != null) {
                            popTip.dismiss();
                            popTip = null;
                        }

                        popTip = new SimpleTooltip.Builder(getActivity())
                                .anchorView(anchorView)
                                //.arrowColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
                                .text(getString(R.string.pop_text_checklist))
                                //.textColor(ContextCompat.getColor(getContext(), R.color.colorWhite))
                                .gravity(Gravity.TOP)
                                .animated(false)
                                .dismissOnOutsideTouch(false)
                                .dismissOnInsideTouch(false)
                                //.backgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
                                //.padding(R.dimen.tooltip_padding)
                                //.maxWidth(R.dimen.simpletooltip_max_width)
                                .build();
                        popTip.show();
                    }

                    int trailerIndex = PreferenceManager.getCurrentTrailer(getActivity());
                    int hitchIndex = PreferenceManager.getCurrentHitch(getActivity());

                    SetupTrailerModel _trailerModel = trailerModels.get(trailerIndex);
                    SetupHitchModel _hitchModel = hitchModels.get(hitchIndex);

                    double trailerEmptyWeightValue = _trailerModel.getEmptyWeight();
                    double estTrailerWeight = trailerEmptyWeightValue - _trailerModel.getLoadCapacity() + trailerCargo;
                    if (_trailerModel.getCargoWeight() > 0)
                        estTrailerWeight += _trailerModel.getCargoWeight();

                    double twMultiplierMin = 0.1;
                    double twMultiplierMax = 0.15;
                    if (_hitchModel.getHitchType().equals("Gooseneck") || _hitchModel.getHitchType().equals("Fifth Wheel")) {
                        twMultiplierMin = 0.15;
                        twMultiplierMax = 0.25;
                    }

                    double tw = selectedConfiguration.getnTongueWeight();

                    imgSatisfyLeft.setVisibility(View.VISIBLE);
                    imgSatisfyRight.setVisibility(View.VISIBLE);

                    if ( tw < Math.floor(estTrailerWeight * twMultiplierMin) ||
                            tw > Math.ceil(estTrailerWeight * twMultiplierMax))
                    {
                        PrettyDialog pDialog = new PrettyDialog(getActivity());
                        pDialog.setIcon(R.drawable.pdlg_icon_info);
                        pDialog.setIconTint(R.color.pdlg_color_black);
                        pDialog.setTitle(getString(R.string.text_unsafe_tow_title));
                        pDialog.addButton(
                                "OK",			// button text
                                R.color.colorBlack,		// button text color
                                R.color.pdlg_color_yellow,		    // button background color
                                new PrettyDialogCallback() {	// button OnClick listener
                                    @Override
                                    public void onClick() {
                                        pDialog.dismiss();
                                    }
                                });

                        pDialog.setMessage("Your tongue weight is outside of our recommend \n Safe tongue Weight Range. \n We recommend repositioning the cargo in the trailer or remove cargo.");
                        pDialog.show();

                        input_actual_tongueweight.setBackgroundResource(R.drawable.dashed_edit_yellow);

                    } else
                        input_actual_tongueweight.setBackgroundResource(R.drawable.dashed_edit_box);

                    ll_promo_box.setVisibility(View.VISIBLE);
                    ll_distributed_tongueweight.setVisibility(View.GONE);
                    ll_calculate_Results.setVisibility(View.GONE);
                    refreshView(false);
                    return true;
                }
                return false;
            }
        });

        mEditMeasureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeasurementsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void calculateDtw() {
        input_actual_tongueweight.setBackgroundResource(R.drawable.dashed_edit_box);
        PrettyDialog pDialog = new PrettyDialog(getActivity());
        pDialog.setIcon(R.drawable.pdlg_icon_info);
        pDialog.setIconTint(R.color.pdlg_color_yellow);
        pDialog.setTitle(getString(R.string.text_incorrect_configuration));
        pDialog.addButton(
                "OK",			// button text
                R.color.colorBlack,		// button text color
                R.color.pdlg_color_yellow,		    // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        pDialog.dismiss();
                    }
                });


        if (selectedConfiguration.getnTongueWeight() == 0)
        {
            pDialog.setMessage("Tongue is required to calculate distributed tongue weight.");
            pDialog.show();
            input_actual_tongueweight.setBackgroundResource(R.drawable.dashed_edit_yellow);
        } else if (selectedConfiguration.getnTowBallSpringBar() == 0 || selectedConfiguration.getnTowBallTrailerAxle() == 0 ||
                selectedConfiguration.getnRearAxle() == 0) {

            Intent intent = new Intent(getActivity(), MeasurementsActivity.class);
            MainApplication.getInstance().setMeasurementsListener(HomeFragment.this);
            startActivity(intent);
        } else if (selectedConfiguration.getnTowBallSpringBar() != 0 && selectedConfiguration.getnTowBallTrailerAxle() != 0 &&
                selectedConfiguration.getnRearAxle() != 0) {

            double nominator = 1- (selectedConfiguration.getnTowBallSpringBar() / selectedConfiguration.getnTowBallTrailerAxle());
            double denominator = (selectedConfiguration.getnTowBallSpringBar() / selectedConfiguration.getnRearAxle()) + (selectedConfiguration.getnTowBallSpringBar() / selectedConfiguration.getnTowBallTrailerAxle());

            double result = 1 + (nominator / denominator);
            double tw = selectedConfiguration.getnTongueWeight();

            double minPercent = 0.95;
            double targetPercent = 1;
            double maxPercent = 1.05;

            if (tw >= 500 && tw <= 999) {
                minPercent = 0.85;
                targetPercent = 0.90;
                maxPercent = 0.95;
            } else if (tw >= 1000 && tw <= 1499) {
                minPercent = 0.75;
                targetPercent = 0.80;
                maxPercent = 0.85;
            } else if (tw >= 1499) {
                minPercent = 0.70;
                targetPercent = 0.75;
                maxPercent = 0.80;
            }

            double FALR = tw * result;

            ll_distributed_tongueweight.setVisibility(View.VISIBLE);
            ll_calculate_Results.setVisibility(View.VISIBLE);

            int minRes = (int)(Math.ceil(FALR * minPercent /50) * 50);
            txt_min_safe_weight.setText(String.valueOf(minRes));

            int targetRes = (int)(Math.ceil(FALR * targetPercent /50) * 50);
            txt_safe_weight.setText(String.valueOf(targetRes));

            int maxRes = (int)(Math.ceil(FALR * maxPercent /50) * 50);
            txt_max_safe_weight.setText(String.valueOf(maxRes));

            sv.post(new Runnable() {
                @Override
                public void run() {
                    sv.scrollTo(0, ll_distributed_tongueweight.getBottom());
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        /*
        if (mConfigAdapter != null) {
            mConfigAdapter.saveStates(outState);
        }
         */
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Only if you need to restore open/close state when
        // the orientation is changed
        /*
        if (mConfigAdapter != null) {
            mConfigAdapter.restoreStates(savedInstanceState);
        }
         */
    }

    public void initConfiguration()
    {
        if (mConfigList.size() == 0)
            textVehicleTitle.setText("Tow Configuration #1");
        else {
            textVehicleTitle.setText(selectedConfiguration.getName());
        }
    }

    @Override
    public void onDialogSave(DialogFragment dialog, String setupName, boolean isNewSetup) {
        if (isNewSetup)
            createNewConfiguration(setupName);
        else
            editConfiguration(setupName, editPusher);
    }

    public void initNewSetup(boolean isStart)
    {
        MainApplication.getInstance().setModalFragmentListener(HomeFragment.this);

        if (isStart) {
            DialogFragment dialog = ModalFragment.newInstance("Let's get started!", getString(R.string.text_setup_note), "");
            dialog.setCancelable(false);
            dialog.show(getActivity().getSupportFragmentManager(), "ModalFragment");
        }
        else {
            DialogFragment dialog = ModalFragment.newInstance("New Tow Configuration", getString(R.string.text_setup_note_b), "");
            dialog.setCancelable(false);
            dialog.show(getActivity().getSupportFragmentManager(), "ModalFragment");
        }
    }

    public void editSetup(ConfigPusher pusher)
    {
        DialogFragment dialog = ModalFragment.newInstance(getString(R.string.edit_title), getString(R.string.text_change_setup), pusher.getConfigModel().getName());
        dialog.setCancelable(false);
        dialog.show(getActivity().getSupportFragmentManager(), "ModalFragment");
        editPusher = pusher;
    }

    public void selectSetup(ConfigPusher pusher)
    {
        dismissPopupTip();

        Configuration selectConfig = new Configuration(pusher.getConfigModel());
        int currIndex = pusher.getSelectedVehiclePosition();

        PreferenceManager.setPositionID(getActivity(), currIndex);

        SetupVehicleModel _currentVehicle = vehicleModels.get(selectConfig.getnVehicleIndex());
        if (selectConfig.getnVehicleRealmId() == _currentVehicle.getnRealmId())
            PreferenceManager.setCurrentVehicle(getActivity(), selectConfig.getnVehicleIndex());
        else
        {
            boolean isFound = false;

            for (int i=0; i<vehicleModels.size(); i++)
            {
                SetupVehicleModel _findModel = vehicleModels.get(i);
                if (_findModel.getnRealmId() == selectConfig.getnVehicleRealmId())
                {
                    selectConfig.setnVehicleIndex(i);
                    PreferenceManager.setCurrentVehicle(getActivity(), i);
                    isFound = true;
                    break;
                }
            }

            if (!isFound)
            {
                selectConfig.setnVehicleIndex(0);
                selectConfig.setnVehicleRealmId(vehicleModels.get(0).getnRealmId());
                PreferenceManager.setCurrentVehicle(getActivity(), 0);
            }
        }


        SetupHitchModel _currentHitch = hitchModels.get(selectConfig.getnHitchIndex());

        if (selectConfig.getnHitchRealmId() == _currentHitch.getnRealmId())
            PreferenceManager.setCurrentHitch(getActivity(), selectConfig.getnHitchIndex());
        else {
            boolean isFound = false;
            for (int i = 0; i < hitchModels.size(); i++) {
                SetupHitchModel _findModel = hitchModels.get(i);
                if (_findModel.getnRealmId() == selectConfig.getnHitchRealmId()) {
                    selectConfig.setnHitchIndex(i);
                    PreferenceManager.setCurrentHitch(getActivity(), i);
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                selectConfig.setnHitchIndex(0);
                selectConfig.setnHitchRealmId(hitchModels.get(0).getnRealmId());
                PreferenceManager.setCurrentHitch(getActivity(), 0);
            }
        }

        SetupTrailerModel _currentTrailer = trailerModels.get(selectConfig.getnTrailerIndex());
        if (selectConfig.getnTrailerRealmId() == _currentTrailer.getnRealmId())
            PreferenceManager.setCurrentTrailer(getActivity(), selectConfig.getnTrailerIndex());
        else
        {
            boolean isFound = false;
            for (int i=0; i<trailerModels.size(); i++)
            {
                SetupTrailerModel _findModel = trailerModels.get(i);
                if (_findModel.getnRealmId() == selectConfig.getnTrailerRealmId())
                {
                    selectConfig.setnTrailerIndex(i);
                    PreferenceManager.setCurrentTrailer(getActivity(), i);
                    isFound = true;
                    break;
                }
            }

            if (!isFound)
            {
                selectConfig.setnTrailerIndex(0);
                selectConfig.setnTrailerRealmId(trailerModels.get(0).getnRealmId());
                PreferenceManager.setCurrentTrailer(getActivity(), 0);
            }
        }

        if (selectConfig.getnCargoIndexes().size() > 0)
        {
            RealmList<Integer> cargoIndexes = new RealmList<>();
            List<String> cargoList = new ArrayList<>();
            RealmList<Integer> cargoRealmIdList = new RealmList<>();

            for (int i=0; i<selectConfig.getnCargoIndexes().size(); i++)
            {
                int cargoIndex = selectConfig.getnCargoIndexes().get(i);
                SetupCargoModel _currentCargo = cargoModels.get(cargoIndex);
                int selectCargoRealmId = selectConfig.getnCargoRealmIDs().get(i);
                if (selectCargoRealmId == _currentCargo.getnRealmId()) {
                    cargoIndexes.add(cargoIndex);
                    cargoList.add(String.valueOf(cargoIndex));
                    cargoRealmIdList.add(selectCargoRealmId);
                }
                else
                {
                    boolean isFound = false;
                    for (int j=0; j<cargoModels.size(); j++)
                    {
                        SetupCargoModel _findModel = cargoModels.get(j);
                        if (_findModel.getnRealmId() == selectCargoRealmId)
                        {
                            cargoIndexes.add(j);
                            cargoList.add(String.valueOf(j));
                            cargoRealmIdList.add(selectCargoRealmId);
                            isFound = true;
                            break;
                        }
                    }
                }
            }

            selectConfig.setnCargoIndexes(cargoIndexes);
            selectConfig.setnCargoRealmIDs(cargoRealmIdList);
            PreferenceManager.setCurrentCargoList(getActivity(), cargoList);

        } else {
            selectConfig.setnCargoIndexes(new RealmList<>());
            selectConfig.setnCargoRealmIDs(new RealmList<>());
            PreferenceManager.setCurrentCargoList(getActivity(), new ArrayList<>());
        }

        mConfigAdapter.updateItem(currIndex, selectConfig);
        DatabaseManager.updateConfigurationModel(selectConfig.getnRealmId(), selectConfig);

        selectedConfiguration = selectConfig;
        MainApplication.getInstance().selectedConfiguration = selectConfig;
        textVehicleTitle.setText(selectConfig.getName());
        MainApplication.getInstance().getConfigurationChangeListener().OnChangeConfiguraton();

        refreshView(true);
    }

    public void createNewConfiguration(String name)
    {
        Configuration config = new Configuration();

        if (!name.isEmpty() && !name.equals(""))
            config.setName(name);
        else {
            try {
                Realm realm = MainApplication.getRealmDatabaseInstance();
                int lastID = DatabaseManager.getLastId(Configuration.class) + 1;
                config.setName(String.format("Tow Configuration #%d", lastID));
                realm.close();
            } catch (Exception e) {
                Log.e("TAG", "Configuration Name Exception : " + e.getMessage());
            }
        }

        textVehicleTitle.setText(name);

        List<String> cargoList = new ArrayList<>();

        PreferenceManager.setCurrentVehicle(getActivity(), 0);
        PreferenceManager.setCurrentHitch(getActivity(), 0);
        PreferenceManager.setCurrentTrailer(getActivity(), 0);
        PreferenceManager.setCurrentCargoList(getActivity(), cargoList);


        if (vehicleModels.size() != 0) {
            config.setnVehicleIndex(0);
            config.setnVehicleRealmId(vehicleModels.get(0).getnRealmId());
        }

        if (hitchModels.size() != 0) {
            config.setnHitchIndex(0);
            config.setnHitchRealmId(hitchModels.get(0).getnRealmId());
        }

        if (trailerModels.size() != 0) {
            config.setnTrailerIndex(0);
            config.setnTrailerRealmId(trailerModels.get(0).getnRealmId());
        }

        Configuration finalConfig = new Configuration(DatabaseManager.addNewConfiguration(config, 0));
        mConfigAdapter.addItem(finalConfig);
        MainApplication.getInstance().selectedConfiguration = finalConfig;
        selectedConfiguration = finalConfig;

        if (mConfigList.size() == 0)
            PreferenceManager.setPositionID(getActivity(), 0);
        else
            PreferenceManager.setPositionID(getActivity(), mConfigList.size() -1);

        if (vehicleModels.size() !=0 && hitchModels.size() !=0 && trailerModels.size() != 0)
            MainApplication.getInstance().getConfigurationChangeListener().OnChangeConfiguraton();

        if (mConfigList.size() > 0 && (vehicleModels.size() == 0 || trailerModels.size() == 0 || hitchModels.size() == 0))
        {
            ((MainActivity)getActivity()).GoSetupVehicle();
            /*
            PrettyDialog pDialog = new PrettyDialog(getActivity());
            pDialog.setIcon(R.drawable.pdlg_icon_info);
            pDialog.setIconTint(R.color.pdlg_color_red);
            pDialog.setTitle(getString(R.string.text_incorrect_setup));
            pDialog.addButton(
                    "Go to Setup",			// button text
                    R.color.colorWhite,		// button text color
                    R.color.colorRed,		    // button background color
                    new PrettyDialogCallback() {	// button OnClick listener
                        @Override
                        public void onClick() {
                            pDialog.dismiss();

                        }
                    });

            pDialog.setMessage(getString(R.string.text_incorrect_note));
            pDialog.show();
             */
        } else {

            ((MainActivity)getActivity()).GoSetupVehicle();
            //refreshView(true);
        }
    }

    public void editConfiguration(String name, ConfigPusher pusher)
    {
        Configuration editConfig = pusher.getConfigModel();
        if (name.isEmpty() || name.equals(""))
        {
            try {
                Realm realm = MainApplication.getRealmDatabaseInstance();
                int lastID = DatabaseManager.getLastId(Configuration.class) + 1;
                editConfig.setName(String.format("Tow Configuration #%d", lastID));
                realm.close();
            } catch (Exception e) {
                Log.e("TAG", "Configuration Name Exception : " + e.getMessage());
            }
        }
        else
            editConfig.setName(name);

        DatabaseManager.updateConfigurationModel(editConfig.getnRealmId(), editConfig);

        mConfigAdapter.updateItem(pusher.getSelectedVehiclePosition(), editConfig);

        int currIndex = PreferenceManager.getPositionID(getActivity());
        if (pusher.getSelectedVehiclePosition() == currIndex)
            textVehicleTitle.setText(name);
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        dismissPopupTip();
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @OnClick(R.id.addVehicle)
    public void onClickAddVehicle(View v){
        disableDropdown();
        initNewSetup(false);
    }

    public void disableDropdown(){
        vehicleListView.setVisibility(View.GONE);
        btnDropdown.setImageResource(R.drawable.icon_dropdown);
    }

    public void setPosition(Configuration positionModel){
        for(int i = 0 ; i < mConfigList.size() ; i++){
            if(mConfigList.get(i).getnRealmId() == positionModel.getnRealmId()){
                PreferenceManager.setPositionID(getActivity(), i);
                //selectedConfiguration = new Configuration(positionModel);
            }
        }
    }

    private void setConfigTitle(Configuration configModel){
        setPosition(configModel);
        textVehicleTitle.setText(configModel.getName());
        refreshView(true);
    }


    private void refreshView(boolean resetCargo)
    {
        int vehicleIndex = PreferenceManager.getCurrentVehicle(getActivity());
        int trailerIndex = PreferenceManager.getCurrentTrailer(getActivity());
        int hitchIndex = PreferenceManager.getCurrentHitch(getActivity());

        SetupVehicleModel _vehicleModel = vehicleModels.get(vehicleIndex);
        SetupTrailerModel _trailerModel = trailerModels.get(trailerIndex);
        SetupHitchModel _hitchModel = hitchModels.get(hitchIndex);

        List<String> CargoIndexes = PreferenceManager.getCurrentCargoList(getActivity());

        int currentConfigIndex = PreferenceManager.getPositionID(getActivity());

        Configuration configuration = mConfigList.get(currentConfigIndex);

        if (resetCargo)
        {
            vehicleCargo = 0;
            trailerCargo = 0;
            for (String index : CargoIndexes)
            {
                SetupCargoModel _model = cargoModels.get(Integer.valueOf(index));
                if (_model.getmType().equals("Vehicle"))
                    vehicleCargo += _model.getCargoWeight();
                else
                    trailerCargo += _model.getCargoWeight();
            }

            cacheVehicleCargo = vehicleCargo;
            cacheTrailerCargo = trailerCargo;

            vehicleCargo += configuration.getnAdditionalVehicleWeight();
            trailerCargo += configuration.getnAdditionalTrailerWeight();

            if (vehicleCargo < 0)
                vehicleCargo = 0;

            if (trailerCargo < 0)
                trailerCargo = 0;
        }

        if (configuration.getnAdditionalVehicleWeight() != 0)
        {
            double avwValue = configuration.getnAdditionalVehicleWeight();
            if (avwValue > 0)
                mVehicleCargoAdj.setText(String.format("Cargo Adj. +%s", fmt(avwValue)));
            else
                mVehicleCargoAdj.setText(String.format("Cargo Adj. %s", fmt(avwValue)));
        } else
            mVehicleCargoAdj.setText("");

        if (configuration.getnAdditionalTrailerWeight() != 0)
        {
            double atwValue = configuration.getnAdditionalTrailerWeight();
            if (atwValue > 0)
                mTrailerCargoAdj.setText(String.format("Cargo Adj. +%s", fmt(atwValue)));
            else
                mTrailerCargoAdj.setText(String.format("Cargo Adj. %s", fmt(atwValue)));
        } else
            mTrailerCargoAdj.setText("");

        text_hitchtitle.setText(_hitchModel.getHitchType());
        text_hitchtitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey60));
        imgSatisfyLeft.setVisibility(View.INVISIBLE);
        imgSatisfyRight.setVisibility(View.INVISIBLE);

        if (_hitchModel.getHitchType().equals("Weight Distribution") && _hitchModel.isOnSafeHitch())
            ll_distributed_tongueweight.setVisibility(View.VISIBLE);
        else
            ll_distributed_tongueweight.setVisibility(View.GONE);

        if ((_hitchModel.isOnSafeHitch() && _hitchModel.getBrand().equals("Weigh Safe 180 Hitch")) ||
                !_hitchModel.isOnSafeHitch())
            txt_tongue_weight.setText(getString(R.string.lb_estimated_tongue));
        else
            txt_tongue_weight.setText(getString(R.string.lb_actual_tongue));

        switch (_hitchModel.getHitchType())
        {
            case "Weight Distribution":
                int imageResource = getResources().getIdentifier(String.format("%s_%s", "trailer_setup_bumper", _trailerModel.getTrailerType()), "drawable", getActivity().getPackageName());
                if (imageResource > 0)
                    img_vehicle.setImageResource(imageResource);
                else
                    img_vehicle.setImageResource(R.drawable.trailer_setup_bumper_rv);
                break;
            case "Gooseneck":
                imageResource = getResources().getIdentifier(String.format("%s_%s", "trailer_setup_gooseneck", _trailerModel.getTrailerType()), "drawable", getActivity().getPackageName());
                if (imageResource > 0)
                    img_vehicle.setImageResource(imageResource);
                else
                    img_vehicle.setImageResource(R.drawable.trailer_setup_gooseneck_flat);
                break;
            case "Fifth Wheel":
                imageResource = getResources().getIdentifier(String.format("%s_%s", "trailer_setup_fifthwheel", _trailerModel.getTrailerType()), "drawable", getActivity().getPackageName());
                if (imageResource > 0)
                    img_vehicle.setImageResource(imageResource);
                else
                    img_vehicle.setImageResource(R.drawable.trailer_setup_fifthwheel_rv);
                break;
            default:
                imageResource = getResources().getIdentifier(String.format("%s_%s", "trailer_setup_bumper", _trailerModel.getTrailerType()), "drawable", getActivity().getPackageName());
                if (imageResource > 0)
                    img_vehicle.setImageResource(imageResource);
                else
                    img_vehicle.setImageResource(R.drawable.trailer_setup_bumper_flat);
                break;
        }

        ll_calculate_Results.setVisibility(View.GONE);

        if (_hitchModel.isOnSafeHitch()) {
            rl_hitchmenu.setVisibility(View.GONE);
            if (_hitchModel.getHitchType().equals("Weight Distribution"))
                rl_hitchmenu.setVisibility(View.VISIBLE);
            ll_promo_box.setVisibility(View.GONE);
        } else {
            rl_hitchmenu.setVisibility(View.GONE);
            ll_promo_box.setVisibility(View.VISIBLE);
        }

        double val_vehicleGVWR = _vehicleModel.getmGVWR();
        mTxtGVWR.setText(fmt(val_vehicleGVWR));

        double val_trailerEmpty = _trailerModel.getEmptyWeight();
        mTxtGTWR.setText(fmt(val_trailerEmpty));

        input_cargo_vehicle.setText(fmt(vehicleCargo));
        input_cargo_Trailer.setText(fmt(trailerCargo));

        double val_estTrailerWeight = val_trailerEmpty - _trailerModel.getLoadCapacity() + trailerCargo;
        if (_trailerModel.getCargoWeight() > 0)
            val_estTrailerWeight += _trailerModel.getCargoWeight();

        double twMultiplerMin = 0.1;
        double twMultiplerMax = 0.15;

        if (_hitchModel.getHitchType().equals("Gooseneck") || _hitchModel.getHitchType().equals("Fifth Wheel")) {
            twMultiplerMin = 0.15;
            twMultiplerMax = 0.25;
        }

        txt_min_tongueweight.setText(fmt(Math.floor(val_estTrailerWeight * twMultiplerMin)));
        txt_max_tongueweight.setText(fmt(Math.ceil(val_estTrailerWeight * twMultiplerMax)));

        mTxtEstGTW.setText(fmt(val_estTrailerWeight));

        double val_estVehicleWeight = 0;

        double tw= configuration.getnTongueWeight();

        if (tw > -1)
        {
            if (tw == 0) {
                input_actual_tongueweight.setText("");
                String popText = getString(R.string.pop_text_enter_tw_1);
                if (_hitchModel.isOnSafeHitch() && _hitchModel.getHitchType().equals("Weight Distribution"))
                    popText = getString(R.string.pop_text_enter_tw_2);

                if (popTipTW != null) {
                    popTipTW.dismiss();
                    popTipTW = null;
                }

                popTipTW = new SimpleTooltip.Builder(getActivity())
                        .anchorView(ll_tongue_weight)
                        .overlayOffset(20.0f)
                        //.arrowColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
                        .text(popText)
                        //.textColor(ContextCompat.getColor(getContext(), R.color.colorWhite))
                        .gravity(Gravity.TOP)
                        .animated(true)
                        .dismissOnOutsideTouch(false)
                        .dismissOnInsideTouch(false)
                        .backgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
                        //.padding(R.dimen.tooltip_padding)
                        //.maxWidth(R.dimen.simpletooltip_max_width)
                        .build();
                popTipTW.show();
            }
            else
                input_actual_tongueweight.setText(fmt(tw));

            val_estVehicleWeight = (val_vehicleGVWR - _vehicleModel.getmMCC()) + tw + vehicleCargo;
            mTxtEstGVW.setText(fmt(val_estVehicleWeight));
            mTxtEstGCW.setText(fmt(val_estTrailerWeight + val_estVehicleWeight));

            input_actual_tongueweight.setBackgroundResource(R.drawable.dashed_edit_box);

            imgSatisfyLeft.setVisibility(View.VISIBLE);
            imgSatisfyRight.setVisibility(View.VISIBLE);
            String homeTitle = String.format("Proper %s Setup", _hitchModel.getHitchType());
            text_hitchtitle.setText(homeTitle);
            text_hitchtitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorThumbsUp));
            imgSatisfyLeft.setImageResource(R.drawable.ic_hand_thumbsup_24);
            imgSatisfyRight.setImageResource(R.drawable.ic_hand_thumbsup_24);

            if (tw < Math.floor(val_estTrailerWeight * twMultiplerMin) || tw > Math.ceil(val_estTrailerWeight * twMultiplerMax)) {
                input_actual_tongueweight.setBackgroundResource(R.drawable.dashed_edit_yellow);

                homeTitle = String.format("Improper %s Setup", _hitchModel.getHitchType());
                text_hitchtitle.setText(homeTitle);
                text_hitchtitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                imgSatisfyLeft.setImageResource(R.drawable.ic_hand_thumbsdown_24);
                imgSatisfyRight.setImageResource(R.drawable.ic_hand_thumbsdown_24);
            }
        } else
            mTxtEstGCW.setText(fmt(val_estTrailerWeight));

        mTxtGCWR.setText(fmt(_vehicleModel.getmGCWR()));

        double weightRatingCapacity = 0;
        if (_hitchModel.getHitchType().equals("Weight Distribution"))
            weightRatingCapacity = _vehicleModel.getmDistributedMGTW();
        else
            weightRatingCapacity = _vehicleModel.getmMGTW();

        List<Double> mGTWRList = new ArrayList<>();
        mGTWRList.add(_hitchModel.getmMaxGTWR());
        mGTWRList.add(_trailerModel.getEmptyWeight());

        if (weightRatingCapacity != 0)
            mGTWRList.add(weightRatingCapacity);

        double minMgtwr = Collections.min(mGTWRList);

        PrettyDialog pDialog = new PrettyDialog(getActivity());
        pDialog.setIcon(R.drawable.pdlg_icon_info);
        pDialog.setIconTint(R.color.pdlg_color_yellow);
        pDialog.setTitle(getString(R.string.text_unsafe_tow_title));
        pDialog.addButton(
                "OK",			// button text
                R.color.colorBlack,		// button text color
                R.color.pdlg_color_yellow,		    // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        pDialog.dismiss();
                    }
                });

        ll_gtwr.setBackgroundColor(Color.WHITE);
        ll_est_gtw.setBackgroundColor(Color.WHITE);

        if (val_estTrailerWeight > minMgtwr) {
            int minIndex = mGTWRList.indexOf(minMgtwr);
            String mgtwrMsg = "";
            if (minIndex == 0)
                mgtwrMsg = "vehicle's receiver";
            else if (minIndex == 1)
                mgtwrMsg = "tow hitch";
            else
                mgtwrMsg = "trailer";

            if (tw > 0) {
                pDialog.setMessage("Your Gross Trailer Weight exceeds the Max Gross Trailer Weight Rating of your " + mgtwrMsg + ". We recommend adjusting cargo for a safe tow.");
                pDialog.show();
            }

            String homeTitle = String.format("Improper %s Setup", _hitchModel.getHitchType());
            text_hitchtitle.setText(homeTitle);
            text_hitchtitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            imgSatisfyLeft.setImageResource(R.drawable.ic_hand_thumbsdown_24);
            imgSatisfyRight.setImageResource(R.drawable.ic_hand_thumbsdown_24);

            ll_gtwr.setBackgroundColor(Color.YELLOW);
            ll_est_gtw.setBackgroundColor(Color.YELLOW);
        }

        ll_gcwr.setBackgroundColor(Color.WHITE);
        ll_est_gcw.setBackgroundColor(Color.WHITE);

        double estSumWeight = val_estTrailerWeight + val_estVehicleWeight;
        if (estSumWeight > _vehicleModel.getmGCWR()) {
            if (tw > 0)
            {
                pDialog.setMessage("Your Gross Combined Weight exceeds the Gross Combined Weight Rating of your vehicle. We recommend adjusting your set up for a safe tow.");
                pDialog.show();
            }

            String homeTitle = String.format("Improper %s Setup", _hitchModel.getHitchType());
            text_hitchtitle.setText(homeTitle);
            text_hitchtitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            imgSatisfyLeft.setImageResource(R.drawable.ic_hand_thumbsdown_24);
            imgSatisfyRight.setImageResource(R.drawable.ic_hand_thumbsdown_24);

            ll_gcwr.setBackgroundColor(Color.YELLOW);
            ll_est_gcw.setBackgroundColor(Color.YELLOW);
        }

        ll_gvwr.setBackgroundColor(Color.WHITE);
        ll_est_gvw.setBackgroundColor(Color.WHITE);

        if (val_estVehicleWeight > val_vehicleGVWR) {
            if (tw > 0)
            {
                pDialog.setMessage("Your Gross Vehicle Weight exceeds the Gross Vehicle Weight Rating of your vehicle. We recommend adjusting your vehicle cargo for a safe tow.");
                pDialog.show();

            }

            String homeTitle = String.format("Improper %s Setup", _hitchModel.getHitchType());
            text_hitchtitle.setText(homeTitle);
            text_hitchtitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            imgSatisfyLeft.setImageResource(R.drawable.ic_hand_thumbsdown_24);
            imgSatisfyRight.setImageResource(R.drawable.ic_hand_thumbsdown_24);

            ll_gvwr.setBackgroundColor(Color.YELLOW);
            ll_est_gvw.setBackgroundColor(Color.YELLOW);
        }

        CheckListModel checkListModel = DatabaseManager.getCheckListModelByVehicleId(selectedConfiguration.getnRealmId());
        if (!checkListModel.isAllChecked())
        {
            BottomNavigationView BottomView = ((MainActivity)getActivity()).getBottomNavigationView();
            View anchorView = BottomView.findViewById(R.id.tab_checklist);

            if (popTip != null) {
                popTip.dismiss();
                popTip = null;
            }

            popTip = new SimpleTooltip.Builder(getActivity())
                    .anchorView(anchorView)
                    //.arrowColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
                    .text(getString(R.string.pop_text_checklist))
                    //.textColor(ContextCompat.getColor(getContext(), R.color.colorWhite))
                    .gravity(Gravity.TOP)
                    .animated(false)
                    .dismissOnOutsideTouch(false)
                    .dismissOnInsideTouch(false)
                    //.backgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
                    //.padding(R.dimen.tooltip_padding)
                    //.maxWidth(R.dimen.simpletooltip_max_width)
                    .build();
            popTip.show();
        }

        if (_hitchModel.getHitchType().equals("Weight Distribution") || _hitchModel.getHitchType().equals("Bumper Pull")) {
            if (tw > -1) {
                double maxDistributedCapacity = _hitchModel.getmMaxTongue();
                double val_vehicleMDTWR = _vehicleModel.getmDistributedMTW();

                if (val_vehicleMDTWR != 0 && val_vehicleMDTWR < maxDistributedCapacity)
                    maxDistributedCapacity = val_vehicleMDTWR;

                if (_hitchModel.getHitchType().equals("Weight Distribution") && tw > maxDistributedCapacity)
                {
                    pDialog.setMessage("Your Tongue Weight exceeds the MAX Tongue Weight Rating of your vehicle receiver. We recommend adjusting your trailer or cargo for a safe tow.");
                    pDialog.show();

                    String homeTitle = String.format("Improper %s Setup", _hitchModel.getHitchType());
                    text_hitchtitle.setText(homeTitle);
                    text_hitchtitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    imgSatisfyLeft.setImageResource(R.drawable.ic_hand_thumbsdown_24);
                    imgSatisfyRight.setImageResource(R.drawable.ic_hand_thumbsdown_24);
                }

                double maxBumperCapacity = _hitchModel.getmMaxTongue();
                double val_vehicleMBTWR = _vehicleModel.getmMTW();

                if (val_vehicleMBTWR != 0 && val_vehicleMBTWR < maxBumperCapacity)
                    maxBumperCapacity = val_vehicleMBTWR;

                if (_hitchModel.getHitchType().equals("Bumper Pull") && tw > maxBumperCapacity)
                {
                    pDialog.setMessage("Your Tongue Weight exceeds the MAX Tongue Weight Rating of your vehicle receiver. We recommend adjusting your trailer or cargo for a safe tow.");

                    String homeTitle = String.format("Improper %s Setup", _hitchModel.getHitchType());
                    text_hitchtitle.setText(homeTitle);
                    text_hitchtitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    imgSatisfyLeft.setImageResource(R.drawable.ic_hand_thumbsdown_24);
                    imgSatisfyRight.setImageResource(R.drawable.ic_hand_thumbsdown_24);
                }

            }
        }

        ll_drawbar.setVisibility(View.GONE);

        if (_hitchModel.isOnSafeHitch() && (_hitchModel.getHitchType().equals("Weight Distribution")))
                //|| (_hitchModel.getHitchType().equals("Bumper Pull") && !_hitchModel.getBrand().equals("Fixed Height Ball Mount") &&
                //!_hitchModel.getBrand().equals("Universal Tow Ball")))) {
            ll_drawbar.setVisibility(View.VISIBLE);

        holesValue = configuration.getnHolesVisible();

        holesMinValue = 0;
        holesMaxValue = 8;
        if (_hitchModel.isOnSafeHitch() && _hitchModel.getHitchType().equals("Weight Distribution")) {
            holesMinValue = 0.5;
            holesMaxValue = 7.5;
        }

        if (configuration.getnHolesVisible() == -0.5) {
            txt_holes.setText("0.5 Holes");
        }
        else {
            if (configuration.getnHolesVisible() == 1)
                txt_holes.setText(String.format("%s Hole", fmt(configuration.getnHolesVisible())));
            else
                txt_holes.setText(String.format("%s Holes", fmt(configuration.getnHolesVisible())));
        }

        btnDrawBarGroup.setPosition(configuration.getnDrawbarPosition(), false);
    }

    @OnClick(R.id.btn_dropdown)
    public void onClickBtnDropdownMenu(View v){
        if (vehicleListView.getVisibility() == View.VISIBLE) {
            vehicleListView.setVisibility(View.GONE);
            btnDropdown.setImageResource(R.drawable.drop_down);
        } else {
            btnDropdown.setImageResource(R.drawable.drop_up);
            vehicleListView.setVisibility(View.VISIBLE);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ConfigPusher pusher) {
        switch (pusher.getAction()) {
            case AppConstants.EVENT_BUS_SELECTED_VEHICLE_POSITION:
                disableDropdown();
                //Working Point 1
                if (((MainActivity)getActivity()).getCurrentPosition() == 0)
                    selectSetup(pusher);
                break;
            case AppConstants.EVENT_BUS_UPDATE_VEHICLE:
                disableDropdown();
                editSetup(pusher);
                break;
            case AppConstants.EVENT_BUS_DEL_VEHICLE:
                vehiclesList.requestLayout();
                break;
        }
    }

    @Override
    public void onUpdateValue() {
        calculateDtw();
    }

    private void increment()
    {
        holesValue += 1;
        if (holesValue > holesMaxValue)
            holesValue = holesMaxValue;

        if (holesValue == -0.5)
            txt_holes.setText("0.5 Holes");
        else {
            int hitchIndex = PreferenceManager.getCurrentHitch(getActivity());
            SetupHitchModel _hitchModel = hitchModels.get(hitchIndex);

            if((int)holesValue == 1)
                txt_holes.setText((int)holesValue + " Hole");
            else
                txt_holes.setText((int)holesValue + " Holes");

            if (_hitchModel.isOnSafeHitch() && _hitchModel.getHitchType().equals("Weight Distribution"))
            {
                if(holesValue == 1)
                    txt_holes.setText(fmt(holesValue) + " Hole");
                else
                    txt_holes.setText(fmt(holesValue) + " Holes");
            }
        }

        selectedConfiguration.setnHolesVisible(holesValue);
        DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);
    }

    private void decrement()
    {
        holesValue -= 1;
        if (holesValue < holesMinValue)
            holesValue = holesMinValue;

        if (holesValue == -0.5)
            txt_holes.setText("0.5 Holes");
        else {
            int hitchIndex = PreferenceManager.getCurrentHitch(getActivity());
            SetupHitchModel _hitchModel = hitchModels.get(hitchIndex);

            if((int)holesValue == 1)
                txt_holes.setText((int)holesValue + " Hole");
            else
                txt_holes.setText((int)holesValue + " Holes");

            if (_hitchModel.isOnSafeHitch() && _hitchModel.getHitchType().equals("Weight Distribution"))
            {
                if(holesValue == 1)
                    txt_holes.setText(fmt(holesValue) + " Hole");
                else
                    txt_holes.setText(fmt(holesValue) + " Holes");
            }
        }

        selectedConfiguration.setnHolesVisible(holesValue);
        DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);
    }

    private void incrementVehicleCargo()
    {
        vehicleCargo += 50;
        if (vehicleCargo > 99999)
            vehicleCargo = 99999;

        additionalVehicleCargo = vehicleCargo - cacheVehicleCargo;

        selectedConfiguration.setnAdditionalVehicleWeight(additionalVehicleCargo);
        DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);

        ll_distributed_tongueweight.setVisibility(View.GONE);
        ll_calculate_Results.setVisibility(View.GONE);
        refreshView(false);
    }

    private void decrementVehicleCargo()
    {
        vehicleCargo -= 50;
        if (vehicleCargo < 0)
            vehicleCargo = 0;

        additionalVehicleCargo = vehicleCargo - cacheVehicleCargo;

        selectedConfiguration.setnAdditionalVehicleWeight(additionalVehicleCargo);
        DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);

        ll_distributed_tongueweight.setVisibility(View.GONE);
        ll_calculate_Results.setVisibility(View.GONE);
        refreshView(false);
    }

    private void incrementTrailerCargo()
    {
        trailerCargo += 50;
        if (trailerCargo > 99999)
            trailerCargo = 99999;

        additionalTrailerCargo = trailerCargo - cacheTrailerCargo;

        selectedConfiguration.setnAdditionalTrailerWeight(additionalTrailerCargo);
        DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);

        ll_distributed_tongueweight.setVisibility(View.GONE);
        ll_calculate_Results.setVisibility(View.GONE);
        refreshView(false);
    }

    private void decrementTrailerCargo()
    {
        trailerCargo -= 50;
        if (trailerCargo < 0)
            trailerCargo = 0;

        additionalTrailerCargo = trailerCargo - cacheTrailerCargo;

        selectedConfiguration.setnAdditionalTrailerWeight(additionalTrailerCargo);
        DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);

        ll_distributed_tongueweight.setVisibility(View.GONE);
        ll_calculate_Results.setVisibility(View.GONE);
        refreshView(false);
    }

    public void dismissPopupTip()
    {
        if (popTip != null) {
            popTip.dismiss();
            popTip = null;
        }

        if (popTipTW != null) {
            popTipTW.dismiss();
            popTipTW = null;
        }
    }


    public static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    public class RptUpdater implements Runnable {
        public void run() {
            if( mAutoIncrement ){
                increment();
                repeatUpdateHandler.postDelayed( new RptUpdater(), 100 );
            } else if( mAutoDecrement ){
                decrement();
                repeatUpdateHandler.postDelayed( new RptUpdater(), 100 );
            }
        }
    }

    public class VehicleCargoUpdater implements Runnable {
        public void run() {
            if( mVCargoIncrement ){
                incrementVehicleCargo();
                repeatUpdateHandler.postDelayed( new VehicleCargoUpdater(), 100 );
            } else if( mVCargoDecrement ){
                decrementVehicleCargo();
                repeatUpdateHandler.postDelayed( new VehicleCargoUpdater(), 100 );
            }
        }
    }

    public class TrailerCargoUpdater implements Runnable {
        public void run() {
            if( mTCargoIncrement ){
                incrementTrailerCargo();
                repeatUpdateHandler.postDelayed( new TrailerCargoUpdater(), 100 );
            } else if( mTCargoDecrement ){
                decrementTrailerCargo();
                repeatUpdateHandler.postDelayed( new TrailerCargoUpdater(), 100 );
            }
        }
    }
}