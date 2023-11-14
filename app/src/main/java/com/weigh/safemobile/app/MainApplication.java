package com.weigh.safemobile.app;

import androidx.multidex.MultiDexApplication;

import com.weigh.safemobile.R;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Configuration;
import com.weigh.safemobile.models.SetupCargoModel;
import com.weigh.safemobile.models.SetupHitchModel;
import com.weigh.safemobile.models.SetupTrailerModel;
import com.weigh.safemobile.models.SetupVehicleModel;
import com.weigh.safemobile.presenters.CargoUpdateListener;
import com.weigh.safemobile.presenters.ConfigurationChangeListener;
import com.weigh.safemobile.presenters.GoCargoListener;
import com.weigh.safemobile.presenters.GoHitchListener;
import com.weigh.safemobile.presenters.GoTrailerListener;
import com.weigh.safemobile.presenters.GoVehicleListener;
import com.weigh.safemobile.presenters.HitchUpdateListener;
import com.weigh.safemobile.presenters.MeasurementsListener;
import com.weigh.safemobile.presenters.ModalFragmentListener;
import com.weigh.safemobile.presenters.TrailerUpdateListener;
import com.weigh.safemobile.presenters.VehicleUpdateListener;
import com.weigh.safemobile.presenters.VinDataListener;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class MainApplication extends MultiDexApplication {

    static MainApplication mInstance;

    //public List<SetupVehicleModel> defaultVehicles;
    //public List<SetupHitchModel> defaultHitches;
    //public List<SetupCargoModel> defaultCargos;
    //public List<SetupTrailerModel> defaultTrailers;

    private VehicleUpdateListener vehicleUpdateListener;
    private HitchUpdateListener hitchUpdateListener;
    private TrailerUpdateListener trailerUpdateListener;
    private CargoUpdateListener cargoUpdateListener;
    private MeasurementsListener measurementsListener;
    private ModalFragmentListener modalFragmentListener;
    private ConfigurationChangeListener configurationChangeListener;
    private VinDataListener vindataListener;

    private GoVehicleListener goVehicleListener;
    private GoHitchListener goHitchListener;
    private GoTrailerListener goTrailerListener;
    private GoCargoListener goCargoListener;

    public RealmList<Configuration> mConfigList = new RealmList<>();
    public Configuration selectedConfiguration;

    public List<SetupVehicleModel> vehicleModels;
    public List<SetupHitchModel> hitchModels;
    public List<SetupTrailerModel> trailerModels;
    public List<SetupCargoModel> cargoModels;

    public boolean mVehicleNotify = false;
    public boolean mHitchNotify = false;
    public boolean mTrailerNotify = false;
    public boolean mCargoNotify = false;

    public boolean mCongrats = false;
    public boolean mVehicleWizard = false;
    public boolean mHitchWizard = false;
    public boolean mTrailerWizard = false;

    @Override
    public void onCreate() {
        super.onCreate();
        setmInstance(this);

        //defaultVehicles = new ArrayList<>();
        //defaultHitches = new ArrayList<>();
        //defaultCargos = new ArrayList<>();
        //defaultTrailers = new ArrayList<>();

        //setDefaultVehicles();
        //setDefaultHitches();
        //setDefaultTrailers();
        //setDefaultCargos();

        initRealm();
        initAllListFromDatabase();
    }

    public static synchronized MainApplication getInstance() {
        return mInstance;
    }

    public void setmInstance(MainApplication mInstance) {
        MainApplication.mInstance = mInstance;
    }

    public void initRealm() {
        Realm.init(this);
    }

    public void initAllListFromDatabase()
    {
        mConfigList = DatabaseManager.getAllConfiguration();
        vehicleModels = DatabaseManager.getAllSetupVehicles();
        hitchModels = DatabaseManager.getAllSetupHitches();
        trailerModels = DatabaseManager.getAllSetupTrailers();
        cargoModels = DatabaseManager.getAllSetupCargos();

        initConfiguration();
    }

    public void initConfiguration()
    {
        int currentConfigIndex = PreferenceManager.getPositionID(getApplicationContext());
        if (currentConfigIndex < 0) {
            PreferenceManager.setPositionID(getApplicationContext(), 0);
            currentConfigIndex = 0;
        }


        if (mConfigList != null && mConfigList.size() > 0)
            selectedConfiguration = new Configuration(mConfigList.get(currentConfigIndex));
        else
            selectedConfiguration = null;
    }

    public void setVehicleUpdateListener(VehicleUpdateListener listener)
    {
        vehicleUpdateListener = listener;
    }

    public VehicleUpdateListener getVehicleUpdateListener() {return vehicleUpdateListener;}

    public void setHitchUpdateListener(HitchUpdateListener listener)
    {
        hitchUpdateListener = listener;
    }

    public HitchUpdateListener getHitchUpdateListener() {return hitchUpdateListener;}

    public void setTrailerUpdateListener(TrailerUpdateListener listener)
    {
        trailerUpdateListener = listener;
    }

    public TrailerUpdateListener getTrailerUpdateListener() {return trailerUpdateListener;}

    public void setCargoUpdateListener(CargoUpdateListener listener)
    {
        cargoUpdateListener = listener;
    }

    public CargoUpdateListener getCargoUpdateListener() {return cargoUpdateListener;}

    public void setMeasurementsListener(MeasurementsListener listener)
    {
        measurementsListener = listener;
    }

    public MeasurementsListener getMeasurementsListener() {return measurementsListener;}

    public void setModalFragmentListener(ModalFragmentListener listener)
    {
        modalFragmentListener = listener;
    }

    public ModalFragmentListener getModalFragmentListener() {return modalFragmentListener;}

    public void setConfigurationChangeListener(ConfigurationChangeListener listener)
    {
        configurationChangeListener = listener;
    }

    public ConfigurationChangeListener getConfigurationChangeListener() {return configurationChangeListener;}

    public void setVindataListener(VinDataListener listener)
    {
        vindataListener = listener;
    }

    public VinDataListener getVindataListener() {return vindataListener;}

    public void setGoVehicleListener(GoVehicleListener listener) { goVehicleListener = listener; }
    public GoVehicleListener getGoVehicleListener() { return goVehicleListener; }

    public void setGoHitchListener(GoHitchListener listener) { goHitchListener = listener; }
    public GoHitchListener getGoHitchListener() { return goHitchListener; }

    public void setGoTrailerListener(GoTrailerListener listener) { goTrailerListener = listener; }
    public GoTrailerListener getGoTrailerListener() { return goTrailerListener; }

    public void setGoCargoListener(GoCargoListener listener) { goCargoListener = listener; }
    public GoCargoListener getGoCargoListener() { return goCargoListener; }


    private static RealmConfiguration getRealmDatabaseConfiguration() {
        return new RealmConfiguration.Builder().name(getInstance().getString(R.string.app_name) + ".realm").deleteRealmIfMigrationNeeded().build();
    }

    public static Realm getRealmDatabaseInstance() {
        return Realm.getInstance(getRealmDatabaseConfiguration());
    }

    public static boolean DeleteRealmDatabaseInstance() {
        return Realm.deleteRealm(getRealmDatabaseConfiguration());
    }

    /*
    public void setDefaultVehicles() {
        if (defaultVehicles == null || defaultVehicles.size() < 1)
        {
            SetupVehicleModel vehicle1 = new SetupVehicleModel();
            vehicle1.setStrTitle("GMC Sierra 1500 (sample)");
            vehicle1.setmGVWR(7200);
            vehicle1.setmGCWR(15000);
            vehicle1.setmMCC(1357);
            vehicle1.setmMTW(12000);
            vehicle1.setmMGTW(1200);
            vehicle1.setSampleItem(true);
            vehicle1.setmDistributedMGTW(0);
            vehicle1.setmDistributedMTW(0);

            defaultVehicles.add(0, vehicle1);

            SetupVehicleModel vehicle2 = new SetupVehicleModel();
            vehicle2.setStrTitle("Ram 2500 (sample)");
            vehicle2.setmGVWR(8565);
            vehicle2.setmGCWR(22500);
            vehicle2.setmMCC(2950);
            vehicle2.setmMTW(15000);
            vehicle2.setmMGTW(1500);
            vehicle2.setSampleItem(true);
            vehicle2.setmDistributedMGTW(0);
            vehicle2.setmDistributedMTW(0);

            defaultVehicles.add(1, vehicle2);

            SetupVehicleModel vehicle3 = new SetupVehicleModel();
            vehicle3.setStrTitle("Ford F-350 (Sample)");
            vehicle3.setmGVWR(10100);
            vehicle3.setmGCWR(20000);
            vehicle3.setmMCC(3893);
            vehicle3.setmMTW(15000);
            vehicle3.setmMGTW(1500);
            vehicle3.setSampleItem(true);
            vehicle3.setmDistributedMGTW(0);
            vehicle3.setmDistributedMTW(0);

            defaultVehicles.add(2, vehicle3);
        }
    }
     */

    /*
    public void setDefaultHitches() {
        if (defaultHitches == null || defaultHitches.size() < 1)
        {
            SetupHitchModel hitch1 = new SetupHitchModel();
            hitch1.setSampleItem(true);
            hitch1.setBrand("Weigh Safe Drop Hitch");
            hitch1.setHitchType("Bumper Pull");
            hitch1.setmMaxTongue(2200);
            hitch1.setmMaxGTWR(18500);
            hitch1.setmBall("2 5/16\"");
            hitch1.setmShank("2.5\"");
            hitch1.setOnSafeHitch(true);

            defaultHitches.add(0, hitch1);

            SetupHitchModel hitch2 = new SetupHitchModel();
            hitch2.setSampleItem(true);
            hitch2.setBrand("Weigh Safe 180 Hitch");
            hitch2.setHitchType("Bumper Pull");
            hitch2.setmMaxTongue(1500);
            hitch2.setmMaxGTWR(8000);
            hitch2.setmBall("2\"");
            hitch2.setmShank("2\"");
            hitch2.setOnSafeHitch(true);

            defaultHitches.add(1, hitch2);

            SetupHitchModel hitch3 = new SetupHitchModel();
            hitch3.setSampleItem(true);
            hitch3.setBrand("True Tow Weight Distribution Hitch");
            hitch3.setHitchType("Weight Distribution");
            hitch3.setmMaxTongue(2000);
            hitch3.setmMaxGTWR(20000);
            hitch3.setmBall("2\"");
            hitch3.setmShank("2.5\"");
            hitch3.setOnSafeHitch(true);

            defaultHitches.add(2, hitch3);
        }
    }
     */

    /*
    public void setDefaultTrailers() {
        if (defaultTrailers == null || defaultTrailers.size() < 1)
        {
            SetupTrailerModel trailer1 = new SetupTrailerModel();
            trailer1.setSampleItem(true);
            trailer1.setName("Ragent Toy Hauler (sample)");
            trailer1.setEmptyWeight(9800);
            trailer1.setLoadCapacity(2357);
            trailer1.setCoupler("1 7/8\"");

            defaultTrailers.add(0, trailer1);

            SetupTrailerModel trailer2 = new SetupTrailerModel();
            trailer2.setSampleItem(true);
            trailer2.setName("Travel Trailer (sample)");
            trailer2.setEmptyWeight(13570);
            trailer2.setLoadCapacity(3720);
            trailer2.setCoupler("2\"");

            defaultTrailers.add(1, trailer2);

            SetupTrailerModel trailer3 = new SetupTrailerModel();
            trailer3.setSampleItem(true);
            trailer3.setName("Enclosed Cargo Trailer (sample)");    
            trailer3.setEmptyWeight(7000);
            trailer3.setLoadCapacity(4230);
            trailer3.setCoupler("2 5/16\"");

            defaultTrailers.add(2, trailer3);
        }
    }
     */

    /*
    private void setDefaultCargos() {
        if (defaultCargos == null || defaultCargos.size() < 1)
        {
            SetupCargoModel cargos1 = new SetupCargoModel();
            cargos1.setSampleItem(true);
            cargos1.setName("Passengers");
            cargos1.setCargoWeight(500);
            cargos1.setmType("Vehicle");

            defaultCargos.add(0, cargos1);

            SetupCargoModel cargos2 = new SetupCargoModel();
            cargos2.setSampleItem(true);
            cargos2.setName("ATV(s)");
            cargos2.setCargoWeight(1100);
            cargos2.setmType("Trailer");

            defaultCargos.add(1, cargos2);

            SetupCargoModel cargos3 = new SetupCargoModel();
            cargos3.setSampleItem(true);
            cargos3.setName("UTV");
            cargos3.setCargoWeight(2000);
            cargos3.setmType("Trailer");

            defaultCargos.add(2, cargos3);

            SetupCargoModel cargos4 = new SetupCargoModel();
            cargos4.setSampleItem(true);
            cargos4.setName("Fresh Water Tank");
            cargos4.setCargoWeight(400);
            cargos4.setmType("Trailer");

            defaultCargos.add(3, cargos4);

            SetupCargoModel cargos5 = new SetupCargoModel();
            cargos5.setSampleItem(true);
            cargos5.setName("Luggage");
            cargos5.setCargoWeight(150);
            cargos5.setmType("Vehicle");

            defaultCargos.add(4, cargos5);

            SetupCargoModel cargos6 = new SetupCargoModel();
            cargos6.setSampleItem(true);
            cargos6.setName("Miscellaneous");
            cargos6.setCargoWeight(250);
            cargos6.setmType("Trailer");

            defaultCargos.add(5, cargos6);
        }
    }
     */
}
