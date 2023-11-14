package com.weigh.safemobile.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class VehiclesModel extends RealmObject {
    @PrimaryKey
    private int nRealmId;
    private String strVehicle;
    private String strTrailer;

    public VehiclesModel(){
        this.nRealmId = 0;
        this.strVehicle = "";
        this.strTrailer = "";
    }

    public VehiclesModel(VehiclesModel v){
        this.nRealmId = v.nRealmId;
        this.strVehicle = v.strVehicle;
        this.strTrailer = v.strTrailer;
    }

    public int getnRealmId() {
        return nRealmId;
    }

    public void setnRealmId(int nRealmId) {
        this.nRealmId = nRealmId;
    }

    public String getStrVehicle() {
        return strVehicle;
    }

    public void setStrVehicle(String strVehicle) {
        this.strVehicle = strVehicle;
    }

    public String getStrTrailer() {
        return strTrailer;
    }

    public void setStrTrailer(String strTrailer) {
        this.strTrailer = strTrailer;
    }
}
