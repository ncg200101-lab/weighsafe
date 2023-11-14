package com.weigh.safemobile.fragments2;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.MainActivity;
import com.weigh.safemobile.activities.NewVehicleActivity;
import com.weigh.safemobile.adapters.NewVehicleAdapter;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Configuration;
import com.weigh.safemobile.models.Pusher;
import com.weigh.safemobile.models.SetupVehicleModel;
import com.weigh.safemobile.presenters.GoVehicleListener;
import com.weigh.safemobile.presenters.VehicleUpdateListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VehicleSetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehicleSetupFragment extends Fragment implements VehicleUpdateListener, GoVehicleListener {
    @BindView(R.id.root_scroll)
    NestedScrollView mScrollView;

    @BindView(R.id.recycler_vehicle)
    RecyclerView vehicleList;

    @BindView(R.id.fab_new)
    ImageButton btnAddNew;

    @BindView(R.id.ll_towing_vehicles)
    LinearLayout ll_no_vehicles;

    private NewVehicleAdapter mVehicleAdapter = null;

    private List<SetupVehicleModel> models;
    private List<SetupVehicleModel> mVehicleList = new ArrayList<>();
    private View mView;

    private boolean viewDidApepar;
    //private boolean emptyList;

    public static VehicleSetupFragment newInstance() {
        VehicleSetupFragment fragment = new VehicleSetupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null)
        {
            mView = inflater.inflate(R.layout.fragment2_vehicle_setup, container, false);
            ButterKnife.bind(this, mView);
            initializerView();
            initializerSave();
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
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
            return;

        if (MainApplication.getInstance().mVehicleNotify) {
            mVehicleAdapter.notifyDataSetChanged();
            MainApplication.getInstance().mVehicleNotify = false;
        }

        if (((MainActivity)getActivity()).getSelectedPage() != 1)
            return;
        if (MainApplication.getInstance().mVehicleWizard)
            return;

        if (viewDidApepar)
        {
            boolean isWizardOn = PreferenceManager.getWizardOn(getActivity());

            if (isWizardOn) {
                if (mVehicleList.size() > 0)
                    showWizardDialog();
                else
                {
                    Intent intent = new Intent(getActivity(), NewVehicleActivity.class);
                    intent.putExtra("editable", false);
                    MainApplication.getInstance().setVehicleUpdateListener(VehicleSetupFragment.this);
                    startActivity(intent);
                }
            } else
            {
                if (mVehicleList.size() == 0)
                {
                    Intent intent = new Intent(getActivity(), NewVehicleActivity.class);
                    intent.putExtra("editable", false);
                    MainApplication.getInstance().setVehicleUpdateListener(VehicleSetupFragment.this);
                    startActivity(intent);
                }
            }
            viewDidApepar = false;
        }
    }

    public void notifyAllData()
    {
        new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               mVehicleAdapter.notifyDataSetChanged();
           }
       }, 500);

    }

    private void initializerView() {

        //emptyList = false;
        viewDidApepar = true;
        MainApplication.getInstance().setGoVehicleListener(this);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        vehicleList.setLayoutManager(mLinearLayoutManager);
        vehicleList.setItemAnimator(new DefaultItemAnimator());
        vehicleList.getItemAnimator().setChangeDuration(0);

        //fix slow recyclerview start
        vehicleList.setHasFixedSize(true);
        vehicleList.setItemViewCacheSize(10);
        vehicleList.setDrawingCacheEnabled(true);
        vehicleList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        vehicleList.addItemDecoration(new DividerItemDecoration(vehicleList.getContext(), DividerItemDecoration.VERTICAL));

        ///fix slow recyclerview end
        fetchAllVehicles();

        mVehicleAdapter = new NewVehicleAdapter(this, mVehicleList);
        vehicleList.setAdapter(mVehicleAdapter);


    }

    private void fetchAllVehicles() {
        mVehicleList.clear();

        models = MainApplication.getInstance().vehicleModels;
        if (models == null || models.size() == 0)
            ll_no_vehicles.setVisibility(View.VISIBLE);
        else
        {
            ll_no_vehicles.setVisibility(View.GONE);
            mVehicleList.addAll(models);
        }


        /*
        if (models == null || models.size() < 1)
        {
            emptyList = true;
            mVehicleList.addAll(MainApplication.getInstance().defaultVehicles);
        } else
        {
            emptyList = false;
            mVehicleList.addAll(models);
        }
         */
    }

    public void initializerSave() {
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewVehicleActivity.class);
                intent.putExtra("editable", false);
                MainApplication.getInstance().setVehicleUpdateListener(VehicleSetupFragment.this);
                startActivity(intent);
            }
        });

        vehicleList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void showWizardDialog()
    {
        PrettyDialog pDialog = new PrettyDialog(getActivity());
        pDialog.setIcon(R.drawable.pdlg_icon_info);
        pDialog.setIconTint(R.color.pdlg_color_red);
        pDialog.setTitle(getString(R.string.text_wizard_vehicle_title));
        pDialog.addButton(
                "Add Vehicle",			// button text
                R.color.colorWhite,		    // button text color
                R.color.colorRed,		    // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(getActivity(), NewVehicleActivity.class);
                        intent.putExtra("editable", false);
                        MainApplication.getInstance().setVehicleUpdateListener(VehicleSetupFragment.this);
                        startActivity(intent);
                        pDialog.dismiss();
                    }
                });
        pDialog.addButton(
                "Keep Vehicle & Skip", // button text
                R.color.colorWhite,		    // button text color
                R.color.grey30,		        // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEXT_SETUP, 1));
                        pDialog.dismiss();
                    }
                });
        pDialog.setMessage(getString(R.string.text_wizard_vehicle_message));
        pDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onEditDialogSave(int position, SetupVehicleModel item) {
        //mVehicleList.set(position, item);
        mVehicleAdapter.updateItem(position, item);
    }

    @Override
    public void onNewDialogSave(SetupVehicleModel item) {
        int itemCount = models.size();
        /*
        if (emptyList == true)
        {
            mVehicleAdapter.removeAll();
            mVehicleAdapter.addItem(item);
            models.add(item);
        }
        else
        {
            mVehicleAdapter.addItem(item);
            models.add(item);
        }

        emptyList = false;
         */
        if (PreferenceManager.getWizardOn(getActivity()))
        {
            PreferenceManager.setCurrentVehicle(getActivity(), itemCount);

            int selectedPos = PreferenceManager.getPositionID(getActivity());
            Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
            currConfig.setnVehicleIndex(itemCount);
            currConfig.setnVehicleRealmId(item.getnRealmId());
            MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
            DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);
        }

        ll_no_vehicles.setVisibility(View.GONE);
        mVehicleAdapter.addItem(item);
        models.add(item);

        if (PreferenceManager.getWizardOn(getActivity()))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEXT_SETUP, 1));
                }
            }, 300);
        }
    }

    @Override
    public void onDeleteDialogSave(int position) {

        models.remove(position);

        int currVehicleIndex = PreferenceManager.getCurrentVehicle(getActivity());
        if (position < currVehicleIndex)
        {
            PreferenceManager.setCurrentVehicle(getActivity(), currVehicleIndex - 1);

            int selectedPos = PreferenceManager.getPositionID(getActivity());
            Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
            currConfig.setnHitchIndex(currVehicleIndex - 1);
            MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
            DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);
        } else if (position == currVehicleIndex)
        {
            PreferenceManager.setCurrentVehicle(getActivity(), 0);

            int selectedPos = PreferenceManager.getPositionID(getActivity());
            Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
            currConfig.setnVehicleIndex(0);
            currConfig.setnVehicleRealmId(models.get(0).getnRealmId());
            MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
            DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);
        }

        mVehicleAdapter.removeItem(position);

        if (models.size() == 0)
            ll_no_vehicles.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Pusher pusher) {
        switch (pusher.getAction()) {
            case AppConstants.EVENT_BUS_ACTIVE_VEHICLE :
                /*
                if (models.size() == 0)
                {
                    SetupVehicleModel _model = mVehicleList.get(pusher.getSelectedPosition());
                    _model.setSampleItem(false);
                    _model.setDistributed(false);
                    _model = DatabaseManager.addNewVehicleModel(_model);
                    models.add(_model);

                    int selectedPos = PreferenceManager.getPositionID(getActivity());

                    Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
                    currConfig.setnVehicleIndex(0);
                    currConfig.setnVehicleRealmId(_model.getnRealmId());
                    MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
                    DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);

                    PreferenceManager.setCurrentVehicle(getActivity(), 0);
                    mVehicleAdapter.removeAll();
                    mVehicleAdapter.addItem(_model);

                    //emptyList = false;

                    PrettyDialog pDialog = new PrettyDialog(getActivity());
                    pDialog.setIcon(R.drawable.pdlg_icon_success);
                    pDialog.setIconTint(R.color.pdlg_color_red);
                    pDialog.setTitle(getString(R.string.text_sample_vehicle_saved));
                    pDialog.addButton(
                            "NEXT",					// button text
                            R.color.colorWhite,		// button text color
                            R.color.colorRed,		    // button background color
                            new PrettyDialogCallback() {	// button OnClick listener
                                @Override
                                public void onClick() {
                                    EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEXT_SETUP, 1));
                                    pDialog.dismiss();
                                }
                            });
                    pDialog.setMessage(_model.getStrTitle() + " vehicle has been added to your list. Manually activate the appropriate vehicle by tapping the SET button or tap the name to edit the vehicle options.");
                    pDialog.show();
                } else
                {

                 */
                    PreferenceManager.setCurrentVehicle(getActivity(), pusher.getSelectedPosition());

                    int selectedPos = PreferenceManager.getPositionID(getActivity());
                    Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
                    currConfig.setnVehicleIndex(pusher.getSelectedPosition());
                    currConfig.setnVehicleRealmId(pusher.get_vehicleModel().getnRealmId());
                    MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
                    DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);

                    //mVehicleAdapter.notifyItemChanged(pusher.getSelectedPosition());
                    mVehicleAdapter.notifyDataSetChanged();
                //}
                break;
        }
    }

    @Override
    public void onWizardCheck() {
        boolean isWizardOn = PreferenceManager.getWizardOn(getActivity());

        if (isWizardOn) {
            if (mVehicleList.size() > 0)
                showWizardDialog();
            else
            {
                Intent intent = new Intent(getActivity(), NewVehicleActivity.class);
                intent.putExtra("editable", false);
                MainApplication.getInstance().setVehicleUpdateListener(VehicleSetupFragment.this);
                startActivity(intent);
            }
        } else
        {
            if (mVehicleList.size() == 0)
            {
                Intent intent = new Intent(getActivity(), NewVehicleActivity.class);
                intent.putExtra("editable", false);
                MainApplication.getInstance().setVehicleUpdateListener(VehicleSetupFragment.this);
                startActivity(intent);
            }
        }

        MainApplication.getInstance().mVehicleWizard = false;
        viewDidApepar = false;
    }
}