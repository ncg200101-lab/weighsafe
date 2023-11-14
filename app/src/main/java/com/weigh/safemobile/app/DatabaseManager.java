package com.weigh.safemobile.app;

import android.util.Log;

import com.weigh.safemobile.models.CheckListModel;
import com.weigh.safemobile.models.Configuration;
import com.weigh.safemobile.models.NotesModel;
import com.weigh.safemobile.models.SetupCargoModel;
import com.weigh.safemobile.models.SetupHitchModel;
import com.weigh.safemobile.models.SetupModel;
import com.weigh.safemobile.models.SetupTrailerModel;
import com.weigh.safemobile.models.SetupVehicleModel;
import com.weigh.safemobile.models.VehiclesModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class DatabaseManager {

    public static int getLastId(Class mClass) {
        Realm realm = MainApplication.getRealmDatabaseInstance();
        AtomicInteger nLastId;
        try {
            Number id = realm.where(mClass).max("nRealmId");
            int nextId = (id == null) ? 1 : id.intValue() + 1;
            nLastId = new AtomicInteger(nextId);
            return nLastId.incrementAndGet();
        } catch (Exception e) {
            Log.e("TAG","last id Exception" + e.getMessage());
            return 1;
        } finally {
            if (!realm.isClosed())
                realm.close();
        }
    }

    public static Configuration getConfigurationByRealmId(int conversationId)
    {
        Configuration vModel = new Configuration();

        try {
            Realm realm1 = MainApplication.getRealmDatabaseInstance();
            vModel = realm1.where(Configuration.class)
                    .equalTo("nRealmId", conversationId)
                    .findFirst();
            realm1.close();
        } catch (Exception e) {
            Log.e("TAG", "getConfigurationByRealmId Exception : " + e.getMessage());
        }

        return vModel;
    }

    public static Boolean deleteConfiguration(Configuration model){
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmQuery<Configuration> mRealmQuery = realm.where(Configuration.class).equalTo("nRealmId", model.getnRealmId());
        if(!mRealmQuery.findAll().isEmpty()) {
            realm.executeTransaction(realm2 -> {
                RealmResults<Configuration> rows = mRealmQuery.findAll();
                rows.deleteAllFromRealm();
            });
            realm.close();
            return true;
        }

        return false;
    }

    public static RealmList<Configuration> getAllConfiguration(){
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmResults<Configuration> mVehiclesModelList = realm.where(Configuration.class).findAll().sort("nRealmId", Sort.ASCENDING);
        RealmList<Configuration> mVehicleList = new RealmList<Configuration>();
        if (mVehiclesModelList.size() > 0) {
            for (Configuration mVehicle : mVehiclesModelList) {
                mVehicleList.add(mVehicle);
            }
        }

        return mVehicleList;
    }

    public static Configuration addNewConfiguration(Configuration model, int Id) {
        Realm realm = MainApplication.getRealmDatabaseInstance();
        try {
            if(Id == 0){
                int lastID = getLastId(Configuration.class);
                realm.refresh();
                realm.executeTransaction(realm1 -> {
                    Configuration newConfiguration = new Configuration(model);

                    newConfiguration.setnRealmId(lastID);

                    realm1.copyToRealmOrUpdate(newConfiguration);
                });
                realm.close();
                return getConfigurationByRealmId(lastID);
            }
            else{
                RealmQuery<Configuration> mRealmQuery = realm.where(Configuration.class).equalTo("nRealmId", Id);
                if(!mRealmQuery.findAll().isEmpty()) {
                    Configuration _model = mRealmQuery.findAll().first();
                    realm.refresh();
                    realm.executeTransaction(realm2 -> {
                        _model.setName(model.getName());
                        _model.setnTongueWeight(model.getnTongueWeight());
                        _model.setnRearAxle(model.getnRearAxle());
                        _model.setnTowBallTrailerAxle(model.getnTowBallTrailerAxle());
                        _model.setnTowBallSpringBar(model.getnTowBallSpringBar());
                        _model.setnAdditionalTrailerWeight(model.getnAdditionalTrailerWeight());
                        _model.setnAdditionalVehicleWeight(model.getnAdditionalVehicleWeight());
                        _model.setnDrawbarPosition(model.getnDrawbarPosition());
                        _model.setnHolesVisible(model.getnHolesVisible());
                        _model.setnVehicleIndex(model.getnVehicleIndex());
                        _model.setnVehicleRealmId(model.getnVehicleRealmId());
                        _model.setnHitchIndex(model.getnHitchIndex());
                        _model.setnHitchRealmId(model.getnHitchRealmId());
                        _model.setnTrailerIndex(model.getnTrailerIndex());
                        _model.setnTrailerRealmId(model.getnTrailerRealmId());
                        _model.setnCargoIndexes(model.getnCargoIndexes());
                        _model.setnCargoRealmIDs(model.getnCargoRealmIDs());

                        realm2.copyToRealmOrUpdate(_model);
                    });
                    realm.close();
                    return getConfigurationByRealmId(Id);
                }
                else
                    return null;
            }
        } catch (Exception e) {
            Log.e("TAG", "Exception : " + e.getMessage());
        }

        return null;
    }

    public static boolean updateConfigurationModel(int modelId, Configuration updateModel) {
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<Configuration> mRealmQuery = realm.where(Configuration.class).equalTo("nRealmId", modelId);

            if(!mRealmQuery.findAll().isEmpty()) {

                Configuration _model = mRealmQuery.findAll().first();
                realm.refresh();

                realm.executeTransaction(realm2 -> {
                            _model.setName(updateModel.getName());
                            _model.setnTongueWeight(updateModel.getnTongueWeight());
                            _model.setnRearAxle(updateModel.getnRearAxle());
                            _model.setnTowBallTrailerAxle(updateModel.getnTowBallTrailerAxle());
                            _model.setnTowBallSpringBar(updateModel.getnTowBallSpringBar());
                            _model.setnAdditionalTrailerWeight(updateModel.getnAdditionalTrailerWeight());
                            _model.setnAdditionalVehicleWeight(updateModel.getnAdditionalVehicleWeight());
                            _model.setnDrawbarPosition(updateModel.getnDrawbarPosition());
                            _model.setnHolesVisible(updateModel.getnHolesVisible());
                            _model.setnVehicleIndex(updateModel.getnVehicleIndex());
                            _model.setnVehicleRealmId(updateModel.getnVehicleRealmId());
                            _model.setnHitchIndex(updateModel.getnHitchIndex());
                            _model.setnHitchRealmId(updateModel.getnHitchRealmId());
                            _model.setnTrailerIndex(updateModel.getnTrailerIndex());
                            _model.setnTrailerRealmId(updateModel.getnTrailerRealmId());
                            _model.setnCargoIndexes(updateModel.getnCargoIndexes());
                            _model.setnCargoRealmIDs(updateModel.getnCargoRealmIDs());

                            realm2.copyToRealmOrUpdate(_model);
                        }
                );
            }

            realm.close();
            return true;
        } catch (Exception e) {
            Log.e("TAG", "Configuration Exception : " + e.getMessage());
        }

        return false;
    }


    public static VehiclesModel getVehicleByRealmId(int conversationId)
    {
        VehiclesModel vModel = new VehiclesModel();

        try {
            Realm realm1 = MainApplication.getRealmDatabaseInstance();
            vModel = realm1.where(VehiclesModel.class)
                    .equalTo("nRealmId", conversationId)
                    .findFirst();
            realm1.close();
        } catch (Exception e) {
            Log.e("TAG", "getVehiclesModelByRealmId Exception : " + e.getMessage());
        }

        return vModel;
    }

    public static VehiclesModel addNewVehicle(String vehicle, String trailer, int id)
    {
        Realm realm = MainApplication.getRealmDatabaseInstance();
        try {
            if(id == 0){
                int lastID = getLastId(VehiclesModel.class);
                realm.refresh();
                realm.executeTransaction(realm1 -> {
                    VehiclesModel newVehiclesModel = new VehiclesModel();

                    newVehiclesModel.setnRealmId(lastID);
                    newVehiclesModel.setStrVehicle(vehicle);
                    newVehiclesModel.setStrTrailer(trailer);

                    realm1.copyToRealmOrUpdate(newVehiclesModel);
                });
                realm.close();
                return getVehicleByRealmId(lastID);
            }
            else{
                RealmQuery<VehiclesModel> mRealmQuery = realm.where(VehiclesModel.class).equalTo("nRealmId", id);
                if(!mRealmQuery.findAll().isEmpty()) {
                    VehiclesModel _model = mRealmQuery.findAll().first();
                    realm.refresh();
                    realm.executeTransaction(realm2 -> {
                        _model.setStrVehicle(vehicle);
                        _model.setStrTrailer(trailer);

                        realm2.copyToRealmOrUpdate(_model);
                    });
                    realm.close();
                    return getVehicleByRealmId(id);
                }
                else
                    return null;
            }
        } catch (Exception e) {
            Log.e("TAG", "addNewVehiclesModel Exception : " + e.getMessage());
        }

        return null;
    }

    public static RealmList<VehiclesModel> getAllVehicle(){
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmResults<VehiclesModel> mVehiclesModelList = realm.where(VehiclesModel.class).findAll().sort("nRealmId", Sort.ASCENDING);
        RealmList<VehiclesModel> mVehicleList = new RealmList<VehiclesModel>();
        if (mVehiclesModelList.size() > 0) {
            for (VehiclesModel mVehicle : mVehiclesModelList) {
                mVehicleList.add(mVehicle);
            }
        }

        return mVehicleList;
    }

    public static Boolean deleteVehicle(VehiclesModel model){
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmQuery<VehiclesModel> mRealmQuery = realm.where(VehiclesModel.class).equalTo("nRealmId", model.getnRealmId());
        if(!mRealmQuery.findAll().isEmpty()) {
            realm.executeTransaction(realm2 -> {
                RealmResults<VehiclesModel> rows = mRealmQuery.findAll();
                rows.deleteAllFromRealm();
            });
            realm.close();
            return true;
        }

        return false;
    }

    public static List<SetupVehicleModel> getAllSetupVehicles() {
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmResults<SetupVehicleModel> mSetupModelList = realm.where(SetupVehicleModel.class).findAll().sort("nRealmId", Sort.ASCENDING);
        List<SetupVehicleModel> mSetupList = new ArrayList<SetupVehicleModel>();
        if (mSetupModelList.size() > 0) {
            for (SetupVehicleModel mSetup : mSetupModelList) {
                mSetupList.add(mSetup);
            }
        }

        return mSetupList;
    }

    public static SetupVehicleModel getVehicleModelByRealmId(int conversationId){

        SetupVehicleModel vModel = new SetupVehicleModel();

        try {
            Realm realm1 = MainApplication.getRealmDatabaseInstance();
            vModel = realm1.where(SetupVehicleModel.class)
                    .equalTo("nRealmId", conversationId)
                    .findFirst();
            realm1.close();
        } catch (Exception e) {
            Log.e("TAG", "getVehiclesModelByRealmId Exception : " + e.getMessage());
        }

        return vModel;
    }

    public static Boolean deleteVehicleModel(int conversationId){
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmQuery<SetupVehicleModel> mRealmQuery = realm.where(SetupVehicleModel.class).equalTo("nRealmId", conversationId);
        if(!mRealmQuery.findAll().isEmpty()) {
            realm.executeTransaction(realm2 -> {
                RealmResults<SetupVehicleModel> rows = mRealmQuery.findAll();
                rows.deleteAllFromRealm();
            });
            realm.close();
            return true;
        }

        return false;
    }

    public static SetupVehicleModel addNewVehicleModel(SetupVehicleModel model) {
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();

            int lastID = getLastId(SetupVehicleModel.class);

            realm.refresh();
            realm.executeTransaction(realm2 -> {
                    SetupVehicleModel _model = new SetupVehicleModel(model);
                    _model.setnRealmId(lastID);
                    realm2.copyToRealmOrUpdate(_model);
                }
                );

            realm.close();
            return getVehicleModelByRealmId(lastID);
        } catch (Exception e) {
            Log.e("TAG", "SetupVehicleModel Exception : " + e.getMessage());
        }

        return null;
    }

    public static boolean updateVehicleModel(int modelId, SetupVehicleModel updateModel) {
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<SetupVehicleModel> mRealmQuery = realm.where(SetupVehicleModel.class).equalTo("nRealmId", modelId);

            if(!mRealmQuery.findAll().isEmpty()) {

                SetupVehicleModel _model = mRealmQuery.findAll().first();
                realm.refresh();

                realm.executeTransaction(realm2 -> {
                            _model.setmGCWR(updateModel.getmGCWR());
                            _model.setmMCC(updateModel.getmMCC());
                            _model.setmGVWR(updateModel.getmGVWR());
                            _model.setmDistributedMTW(updateModel.getmDistributedMTW());
                            _model.setmDistributedMGTW(updateModel.getmDistributedMGTW());
                            _model.setmMGTW(updateModel.getmMGTW());
                            _model.setmMTW(updateModel.getmMTW());
                            _model.setStrTitle(updateModel.getStrTitle());
                            _model.setDistributed(updateModel.isDistributed());
                            _model.setVin(updateModel.getVin());
                            _model.setYear(updateModel.getYear());
                            _model.setMake(updateModel.getMake());
                            _model.setModel(updateModel.getModel());
                            realm2.copyToRealmOrUpdate(_model);
                        }
                );
            }

            realm.close();
            return true;
        } catch (Exception e) {
            Log.e("TAG", "SetupVehicleModel Exception : " + e.getMessage());
        }

        return false;
    }

    public static RealmList<SetupHitchModel> getAllSetupHitches() {
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmResults<SetupHitchModel> mSetupModelList = realm.where(SetupHitchModel.class).findAll().sort("nRealmId", Sort.ASCENDING);
        RealmList<SetupHitchModel> mSetupList = new RealmList<SetupHitchModel>();
        if (mSetupModelList.size() > 0) {
            for (SetupHitchModel mSetup : mSetupModelList) {
                mSetupList.add(mSetup);
            }
        }

        return mSetupList;
    }

    public static SetupHitchModel getHitchModelByRealmId(int conversationId){

        SetupHitchModel vModel = new SetupHitchModel();

        try {
            Realm realm1 = MainApplication.getRealmDatabaseInstance();
            vModel = realm1.where(SetupHitchModel.class)
                    .equalTo("nRealmId", conversationId)
                    .findFirst();
            realm1.close();
        } catch (Exception e) {
            Log.e("TAG", "getHitchesModelByRealmId Exception : " + e.getMessage());
        }

        return vModel;
    }

    public static Boolean deleteHitchModel(int conversationId){
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmQuery<SetupHitchModel> mRealmQuery = realm.where(SetupHitchModel.class).equalTo("nRealmId", conversationId);
        if(!mRealmQuery.findAll().isEmpty()) {
            realm.executeTransaction(realm2 -> {
                RealmResults<SetupHitchModel> rows = mRealmQuery.findAll();
                rows.deleteAllFromRealm();
            });
            realm.close();
            return true;
        }

        return false;
    }

    public static SetupHitchModel addNewHitchModel(SetupHitchModel model) {
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();

            int lastID = getLastId(SetupHitchModel.class);

            realm.refresh();
            realm.executeTransaction(realm2 -> {
                        SetupHitchModel _model = new SetupHitchModel(model);
                        _model.setnRealmId(lastID);
                        realm2.copyToRealmOrUpdate(_model);
                    }
            );

            realm.close();
            return getHitchModelByRealmId(lastID);
        } catch (Exception e) {
            Log.e("TAG", "SetupHitchModel Exception : " + e.getMessage());
        }

        return null;
    }

    public static boolean updateHitchModel(int modelId, SetupHitchModel updateModel) {
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<SetupHitchModel> mRealmQuery = realm.where(SetupHitchModel.class).equalTo("nRealmId", modelId);

            if(!mRealmQuery.findAll().isEmpty()) {

                SetupHitchModel _model = mRealmQuery.findAll().first();
                realm.refresh();

                realm.executeTransaction(realm2 -> {
                            _model.setBrand(updateModel.getBrand());
                            _model.setHitchType(updateModel.getHitchType());
                            _model.setmBall(updateModel.getmBall());
                            _model.setmMaxGTWR(updateModel.getmMaxGTWR());
                            _model.setmMaxTongue(updateModel.getmMaxTongue());
                            _model.setmShank(updateModel.getmShank());
                            _model.setOnSafeHitch(updateModel.isOnSafeHitch());
                            _model.setSelected(updateModel.isSelected());
                            _model.setHitchIndex(updateModel.getHitchIndex());
                            _model.setTypeIndex(updateModel.getTypeIndex());
                            _model.setBallIndex(updateModel.getBallIndex());
                            _model.setmBall(updateModel.getmBall());
                            _model.setShankIndex(updateModel.getShankIndex());
                            _model.setmShank(updateModel.getmShank());
                            realm2.copyToRealmOrUpdate(_model);
                        }
                );
            }

            realm.close();
            return true;
        } catch (Exception e) {
            Log.e("TAG", "SetupVehicleModel Exception : " + e.getMessage());
        }

        return false;
    }

    public static RealmList<SetupTrailerModel> getAllSetupTrailers() {
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmResults<SetupTrailerModel> mSetupModelList = realm.where(SetupTrailerModel.class).findAll().sort("nRealmId", Sort.ASCENDING);
        RealmList<SetupTrailerModel> mSetupList = new RealmList<SetupTrailerModel>();
        if (mSetupModelList.size() > 0) {
            for (SetupTrailerModel mSetup : mSetupModelList) {
                mSetupList.add(mSetup);
            }
        }

        return mSetupList;
    }

    public static SetupTrailerModel getTrailerModelByRealmId(int conversationId){

        SetupTrailerModel vModel = new SetupTrailerModel();

        try {
            Realm realm1 = MainApplication.getRealmDatabaseInstance();
            vModel = realm1.where(SetupTrailerModel.class)
                    .equalTo("nRealmId", conversationId)
                    .findFirst();
            realm1.close();
        } catch (Exception e) {
            Log.e("TAG", "getTrailersModelByRealmId Exception : " + e.getMessage());
        }

        return vModel;
    }

    public static Boolean deleteTrailerModel(int conversationId){
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmQuery<SetupTrailerModel> mRealmQuery = realm.where(SetupTrailerModel.class).equalTo("nRealmId", conversationId);
        if(!mRealmQuery.findAll().isEmpty()) {
            realm.executeTransaction(realm2 -> {
                RealmResults<SetupTrailerModel> rows = mRealmQuery.findAll();
                rows.deleteAllFromRealm();
            });
            realm.close();
            return true;
        }

        return false;
    }

    public static SetupTrailerModel addNewTrailerModel(SetupTrailerModel model) {
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();

            int lastID = getLastId(SetupTrailerModel.class);

            realm.refresh();
            realm.executeTransaction(realm2 -> {
                SetupTrailerModel _model = new SetupTrailerModel(model);
                        _model.setnRealmId(lastID);
                        realm2.copyToRealmOrUpdate(_model);
                    }
            );

            realm.close();
            return getTrailerModelByRealmId(lastID);
        } catch (Exception e) {
            Log.e("TAG", "SetupTrailerModel Exception : " + e.getMessage());
        }

        return null;
    }

    public static boolean updateTrailerModel(int modelId, SetupTrailerModel updateModel) {
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<SetupTrailerModel> mRealmQuery = realm.where(SetupTrailerModel.class).equalTo("nRealmId", modelId);

            if(!mRealmQuery.findAll().isEmpty()) {

                SetupTrailerModel _model = mRealmQuery.findAll().first();
                realm.refresh();

                realm.executeTransaction(realm2 -> {
                            _model.setName(updateModel.getName());
                            _model.setTrailerType(updateModel.getTrailerType());
                            _model.setTypeIndex(updateModel.getTypeIndex());
                            _model.setCoupler(updateModel.getCoupler());
                            _model.setEmptyWeight(updateModel.getEmptyWeight());
                            _model.setLoadCapacity(updateModel.getLoadCapacity());
                            _model.setCargoWeight(updateModel.getCargoWeight());
                            _model.setSelected(updateModel.isSelected());
                            realm2.copyToRealmOrUpdate(_model);
                        }
                );
            }

            realm.close();
            return true;
        } catch (Exception e) {
            Log.e("TAG", "SetupTrailerModel Exception : " + e.getMessage());
        }

        return false;
    }

    public static RealmList<SetupCargoModel> getAllSetupCargos() {
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmResults<SetupCargoModel> mSetupModelList = realm.where(SetupCargoModel.class).findAll().sort("nRealmId", Sort.ASCENDING);
        RealmList<SetupCargoModel> mSetupList = new RealmList<SetupCargoModel>();
        if (mSetupModelList.size() > 0) {
            for (SetupCargoModel mSetup : mSetupModelList) {
                mSetupList.add(mSetup);
            }
        }

        return mSetupList;
    }

    public static SetupCargoModel getCargoModelByRealmId(int conversationId){

        SetupCargoModel vModel = new SetupCargoModel();

        try {
            Realm realm1 = MainApplication.getRealmDatabaseInstance();
            vModel = realm1.where(SetupCargoModel.class)
                    .equalTo("nRealmId", conversationId)
                    .findFirst();
            realm1.close();
        } catch (Exception e) {
            Log.e("TAG", "getCargosModelByRealmId Exception : " + e.getMessage());
        }

        return vModel;
    }

    public static Boolean deleteCargoModel(int conversationId){
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmQuery<SetupCargoModel> mRealmQuery = realm.where(SetupCargoModel.class).equalTo("nRealmId", conversationId);
        if(!mRealmQuery.findAll().isEmpty()) {
            realm.executeTransaction(realm2 -> {
                RealmResults<SetupCargoModel> rows = mRealmQuery.findAll();
                rows.deleteAllFromRealm();
            });
            realm.close();
            return true;
        }

        return false;
    }

    public static SetupCargoModel addNewCargoModel(SetupCargoModel model) {
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();

            int lastID = getLastId(SetupCargoModel.class);

            realm.refresh();
            realm.executeTransaction(realm2 -> {
                SetupCargoModel _model = new SetupCargoModel(model);
                        _model.setnRealmId(lastID);
                        realm2.copyToRealmOrUpdate(_model);
                    }
            );

            realm.close();
            return getCargoModelByRealmId(lastID);
        } catch (Exception e) {
            Log.e("TAG", "SetupCargoModel Exception : " + e.getMessage());
        }

        return null;
    }

    public static boolean updateCargoModel(int modelId, SetupCargoModel updateModel) {
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<SetupCargoModel> mRealmQuery = realm.where(SetupCargoModel.class).equalTo("nRealmId", modelId);

            if(!mRealmQuery.findAll().isEmpty()) {

                SetupCargoModel _model = mRealmQuery.findAll().first();
                realm.refresh();

                realm.executeTransaction(realm2 -> {
                            _model.setName(updateModel.getName());
                            _model.setmType(updateModel.getmType());
                            _model.setCargoWeight(updateModel.getCargoWeight());
                            _model.setSelected(updateModel.isSelected());
                            realm2.copyToRealmOrUpdate(_model);
                        }
                );
            }

            realm.close();
            return true;
        } catch (Exception e) {
            Log.e("TAG", "SetupCargoModel Exception : " + e.getMessage());
        }

        return false;
    }

    public static RealmList<SetupModel> getAllSetup(){
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmResults<SetupModel> mSetupModelList = realm.where(SetupModel.class).findAll().sort("nRealmId", Sort.ASCENDING);
        RealmList<SetupModel> mSetupList = new RealmList<SetupModel>();
        if (mSetupModelList.size() > 0) {
            for (SetupModel mSetup : mSetupModelList) {
                mSetupList.add(mSetup);
            }
        }

        return mSetupList;
    }

    public static SetupModel getSetupModelByVehicleId(int vehicleId){

        SetupModel _model = new SetupModel();

        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<SetupModel> mRealmQuery = realm.where(SetupModel.class).equalTo("nVehicleId", vehicleId);
            if(!mRealmQuery.findAll().isEmpty()) {
                _model = mRealmQuery.findAll().first();
            }
            realm.close();
        } catch (Exception e) {
            Log.e("TAG", "SetupModel Exception : " + e.getMessage());
        }

        return _model;
    }

    public static boolean saveSetupModel(int vehicleId, String param, String value){
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<SetupModel> mRealmQuery = realm.where(SetupModel.class).equalTo("nVehicleId", vehicleId);

            if(!mRealmQuery.findAll().isEmpty()) {

                SetupModel _model = mRealmQuery.findAll().first();
                realm.refresh();

                realm.executeTransaction(realm1 -> {

                    switch (param){
                        case "input_trailer_weight":
                            _model.setnTrailerWeight(value);
                            break;
                        case "input_tongue_weight":
                            _model.setnTongueWeight(value);
                            break;
                        case "input_receiver":
                            _model.setnRearAxle(value);
                            break;
                        case "input_spring_bar":
                            _model.setnSpringBar(value);
                            break;
                        case "input_trailer_alex":
                            _model.setnTrailerAxle(value);
                            break;
                        case "switch_drawbar_position":
                            _model.setnDrawbarPosition(value);
                            break;
                        case "visible_holes":
                            _model.setnHoles(value);
                            break;
                    }

                    realm1.copyToRealmOrUpdate(_model);
                });
            }
            else{
                int lastID = getLastId(SetupModel.class);

                realm.refresh();
                realm.executeTransaction(realm2 -> {
                    SetupModel _model = new SetupModel();
                    _model.setnRealmId(lastID);
                    _model.setnVehicleId(vehicleId);

                    switch (param){
                        case "input_trailer_weight":
                            _model.setnTrailerWeight(value);
                            break;
                        case "input_tongue_weight":
                            _model.setnTongueWeight(value);
                            break;
                        case "input_receiver":
                            _model.setnRearAxle(value);
                            break;
                        case "input_spring_bar":
                            _model.setnSpringBar(value);
                            break;
                        case "input_trailer_alex":
                            _model.setnTrailerAxle(value);
                            break;
                        case "switch_drawbar_position":
                            _model.setnDrawbarPosition(value);
                            break;
                        case "visible_holes":
                            _model.setnHoles(value);
                            break;
                    }

                    realm2.copyToRealmOrUpdate(_model);
                });
            }
            realm.close();
            return true;
        } catch (Exception e) {
            Log.e("TAG", "SetupModel Exception : " + e.getMessage());
        }

        return false;
    }

    public static RealmList<CheckListModel> getAllCheckList(){
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmResults<CheckListModel> mlList = realm.where(CheckListModel.class).findAll().sort("nRealmId", Sort.ASCENDING);
        RealmList<CheckListModel> cList = new RealmList<CheckListModel>();
        if (mlList.size() > 0) {
            for (CheckListModel mSetup : mlList) {
                cList.add(mSetup);
            }
        }

        return cList;
    }

    public static CheckListModel getCheckListModelByVehicleId(int vehicleId){

        CheckListModel _model = new CheckListModel();

        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<CheckListModel> mRealmQuery = realm.where(CheckListModel.class).equalTo("nVehicleId", vehicleId);
            if(!mRealmQuery.findAll().isEmpty()) {
                _model = mRealmQuery.findAll().first();
            }
            realm.close();
        } catch (Exception e) {
            Log.e("TAG", "CheckListModel Exception : " + e.getMessage());
        }

        return _model;
    }

    public static boolean saveCheckListModel(int vehicleId, String param, boolean state){
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<CheckListModel> mRealmQuery = realm.where(CheckListModel.class).equalTo("nVehicleId", vehicleId);
            if(!mRealmQuery.findAll().isEmpty()) {

                CheckListModel _model = mRealmQuery.findAll().first();
                realm.refresh();

                realm.executeTransaction(realm1 -> {

                    switch (param){
                        case "img_check_hand":
                            _model.setHandCrankWeight(state);
                            break;
                        case "img_check_trailer_connection":
                            _model.setTrailerConnection(state);
                            break;
                        case "img_check_load":
                            _model.setLoad(state);
                            break;
                        case "img_check_tires":
                            _model.setTires(state);
                            break;
                        case "img_check_lights":
                            _model.setLights(state);
                            break;
                        case "img_check_trailer_brakes":
                            _model.setTrailerBrakes(state);
                            break;
                        case "img_check_mirrors":
                            _model.setMirrors(state);
                            break;
                    }

                    realm1.copyToRealmOrUpdate(_model);
                });
            }
            else{
                int lastID = getLastId(CheckListModel.class);

                realm.refresh();
                realm.executeTransaction(realm2 -> {
                    CheckListModel _model = new CheckListModel();
                    _model.setnRealmId(lastID);
                    _model.setnVehicleId(vehicleId);

                    switch (param){
                        case "img_check_hand":
                            _model.setHandCrankWeight(state);
                            break;
                        case "img_check_trailer_connection":
                            _model.setTrailerConnection(state);
                            break;
                        case "img_check_load":
                            _model.setLoad(state);
                            break;
                        case "img_check_tires":
                            _model.setTires(state);
                            break;
                        case "img_check_lights":
                            _model.setLights(state);
                            break;
                        case "img_check_trailer_brakes":
                            _model.setTrailerBrakes(state);
                            break;
                        case "img_check_mirrors":
                            _model.setMirrors(state);
                            break;
                    }

                    realm2.copyToRealmOrUpdate(_model);
                });
            }
            realm.close();
            return true;
        } catch (Exception e) {
            Log.e("TAG", "CheckListModel Exception : " + e.getMessage());
        }

        return false;
    }

    public static boolean resetCheckListModelByVehicleId(int vehicleId){
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<CheckListModel> mRealmQuery = realm.where(CheckListModel.class).equalTo("nVehicleId", vehicleId);
            if(!mRealmQuery.findAll().isEmpty()) {
                CheckListModel _model = mRealmQuery.findAll().first();
                realm.refresh();

                realm.executeTransaction(realm1 -> {
                    CheckListModel _updateModel = new CheckListModel();
                    _updateModel.setnRealmId(_model.getnRealmId());
                    _updateModel.setnVehicleId(_model.getnVehicleId());

                    realm1.copyToRealmOrUpdate(_updateModel);
                });
                realm.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.e("TAG", "CheckListModel Exception : " + e.getMessage());
        }

        return false;
    }

    public static RealmList<NotesModel> getAllNotes(){
        Realm realm = MainApplication.getRealmDatabaseInstance();
        RealmResults<NotesModel> mlList = realm.where(NotesModel.class).findAll().sort("nRealmId", Sort.ASCENDING);
        RealmList<NotesModel> cList = new RealmList<NotesModel>();
        if (mlList.size() > 0) {
            for (NotesModel mSetup : mlList) {
                cList.add(mSetup);
            }
        }

        return cList;
    }

    public static NotesModel getNotesModelByVehicleId(int vehicleId){

        NotesModel _model = new NotesModel();

        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<NotesModel> mRealmQuery = realm.where(NotesModel.class).equalTo("nVehicleId", vehicleId);
            if(!mRealmQuery.findAll().isEmpty()) {
                _model = mRealmQuery.findAll().first();
            }
            realm.close();
        } catch (Exception e) {
            Log.e("TAG", "NotesModel Exception : " + e.getMessage());
        }

        return _model;
    }

    public static boolean saveNotesModel(int vehicleId, String note){
        try {
            Realm realm = MainApplication.getRealmDatabaseInstance();
            RealmQuery<NotesModel> mRealmQuery = realm.where(NotesModel.class).equalTo("nVehicleId", vehicleId);
            if(!mRealmQuery.findAll().isEmpty()) {

                NotesModel _model = mRealmQuery.findAll().first();
                realm.refresh();

                realm.executeTransaction(realm1 -> {

                    _model.setNotes(note);
                    realm1.copyToRealmOrUpdate(_model);
                });
            }
            else{
                int lastID = getLastId(NotesModel.class);

                realm.refresh();
                realm.executeTransaction(realm2 -> {
                    NotesModel _model = new NotesModel();
                    _model.setnRealmId(lastID);
                    _model.setnVehicleId(vehicleId);
                    _model.setNotes(note);

                    realm2.copyToRealmOrUpdate(_model);
                });
            }
            realm.close();
            return true;
        } catch (Exception e) {
            Log.e("TAG", "NotesModel Exception : " + e.getMessage());
        }

        return false;
    }
}
