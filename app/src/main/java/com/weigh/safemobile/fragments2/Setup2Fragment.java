package com.weigh.safemobile.fragments2;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.weigh.safemobile.R;
import com.weigh.safemobile.adapters.SetupConfigAdapter;
import com.weigh.safemobile.adapters.SetupPagerAdapter;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.fragments.ModalFragment;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.ConfigPusher;
import com.weigh.safemobile.models.Configuration;
import com.weigh.safemobile.models.Pusher;
import com.weigh.safemobile.models.SetupCargoModel;
import com.weigh.safemobile.models.SetupHitchModel;
import com.weigh.safemobile.models.SetupTrailerModel;
import com.weigh.safemobile.models.SetupVehicleModel;
import com.weigh.safemobile.presenters.ConfigurationChangeListener;
import com.weigh.safemobile.presenters.ModalFragmentListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Setup2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setup2Fragment extends Fragment implements ConfigurationChangeListener, ModalFragmentListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.setup_viewPager)
    ViewPager viewPager;

    @BindView(R.id.text_vehicle_title)
    TextView textVehicleTitle;
    @BindView(R.id.btn_dropdown)
    ImageView btnDropdown;
    @BindView(R.id.vehicleListView)
    RelativeLayout vehicleListView;
    @BindView(R.id.vehiclesList)
    RecyclerView vehiclesList;
    @BindView(R.id.addText)
    TextView addText;

    private View mView;
    private SetupPagerAdapter viewPagerAdapter;

    private SetupConfigAdapter mConfigAdapter = null;

    private RealmList<Configuration> mConfigList;
    private Configuration selectedConfiguration;
    public List<SetupVehicleModel> vehicleModels;
    public List<SetupHitchModel> hitchModels;
    public List<SetupTrailerModel> trailerModels;
    public List<SetupCargoModel> cargoModels;

    private ConfigPusher editPusher;

    public static Setup2Fragment newInstance() {
        Setup2Fragment fragment = new Setup2Fragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mView == null)
        {
            mView = inflater.inflate(R.layout.fragment2_setup2, container, false);
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

        mConfigList = MainApplication.getInstance().mConfigList;
        selectedConfiguration = MainApplication.getInstance().selectedConfiguration;
        mConfigAdapter = new SetupConfigAdapter(mConfigList, getActivity());
        vehiclesList.setAdapter(mConfigAdapter);
        mConfigAdapter.notifyDataSetChanged();

        initConfiguration();
    }

    private void initializerView() {
        viewPagerAdapter = new SetupPagerAdapter(getFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                viewPagerAdapter.addFragment(VehicleSetupFragment.newInstance(), "Vehicle");
                viewPagerAdapter.addFragment(HitchSetupFragment.newInstance(), "Hitch");
                viewPagerAdapter.addFragment(TrailerSetupFragment.newInstance(), "Trailer");
                viewPagerAdapter.addFragment(CargoSetupFragment.newInstance(), "Cargo");

                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });

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
        vehicleModels = MainApplication.getInstance().vehicleModels;
        hitchModels = MainApplication.getInstance().hitchModels;
        trailerModels = MainApplication.getInstance().trailerModels;
        cargoModels = MainApplication.getInstance().cargoModels;

        mConfigAdapter = new SetupConfigAdapter(mConfigList, getActivity());
        vehiclesList.setAdapter(mConfigAdapter);
        mConfigAdapter.notifyDataSetChanged();

        MainApplication.getInstance().setConfigurationChangeListener(Setup2Fragment.this);
        initConfiguration();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Input Flag for Setup2 resume
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void initializerSave(){

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Pusher pusher) {
        switch (pusher.getAction()) {
            case AppConstants.EVENT_BUS_NEXT_SETUP:
                int pos = pusher.getPosition();

                if (pos == 1)
                {
                    MainApplication.getInstance().mHitchWizard = true;
                    viewPager.setCurrentItem(pos);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainApplication.getInstance().getGoHitchListener().onWizardCheck();
                        }
                    }, 800);
                } else if (pos == 2)
                {
                    MainApplication.getInstance().mTrailerWizard = true;
                    viewPager.setCurrentItem(pos);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainApplication.getInstance().getGoTrailerListener().onWizardCheck();
                        }
                    }, 800);
                } else if (pos == 3)
                {
                    viewPager.setCurrentItem(pos);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainApplication.getInstance().getGoCargoListener().onCongratsCheck();
                        }
                    }, 800);
                } else
                    viewPager.setCurrentItem(pos);

                break;
        }
    }

    @Override
    public void OnChangeConfiguraton() {
        int pos = viewPager.getCurrentItem();
        switch (pos)
        {
            case 0:
                ((VehicleSetupFragment)viewPagerAdapter.getItem(0)).notifyAllData();
                MainApplication.getInstance().mHitchNotify = true;
                MainApplication.getInstance().mTrailerNotify = true;
                MainApplication.getInstance().mCargoNotify = true;
                break;
            case 1:
                ((HitchSetupFragment)viewPagerAdapter.getItem(1)).notifyAllData();
                MainApplication.getInstance().mVehicleNotify = true;
                MainApplication.getInstance().mTrailerNotify = true;
                MainApplication.getInstance().mCargoNotify = true;
                break;
            case 2 :
                ((TrailerSetupFragment)viewPagerAdapter.getItem(2)).notifyAllData();
                MainApplication.getInstance().mVehicleNotify = true;
                MainApplication.getInstance().mHitchNotify = true;
                MainApplication.getInstance().mCargoNotify = true;
                break;
            case 3:
                ((CargoSetupFragment)viewPagerAdapter.getItem(3)).notifyAllData();
                MainApplication.getInstance().mVehicleNotify = true;
                MainApplication.getInstance().mHitchNotify = true;
                MainApplication.getInstance().mTrailerNotify = true;
                break;
        }


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
        MainApplication.getInstance().setModalFragmentListener(Setup2Fragment.this);

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

        viewPager.setCurrentItem(0);

        MainApplication.getInstance().mVehicleWizard = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainApplication.getInstance().getGoVehicleListener().onWizardCheck();
            }
        }, 800);
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

    public void selectFragmentByPager(int position)
    {
        viewPager.setCurrentItem(position);
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

}