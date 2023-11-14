package com.weigh.safemobile.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CheckListModel extends RealmObject {
    @PrimaryKey
    private int nRealmId;
    private int nVehicleId; // primary key of Vehicle Model
    private boolean handCrankWeight;
    private boolean trailerConnection;
    private boolean load;
    private boolean tires;
    private boolean lights;
    private boolean trailerBrakes;
    private boolean mirrors;

    public CheckListModel(){
        this.nRealmId = 0;
        this.nVehicleId = 0;
        this.handCrankWeight = false;
        this.trailerConnection = false;
        this.load = false;
        this.tires = false;
        this.lights = false;
        this.trailerBrakes = false;
        this.mirrors = false;
    }

    public CheckListModel(CheckListModel checkListModel){
        this.nRealmId = checkListModel.nRealmId;
        this.nVehicleId = checkListModel.nVehicleId;
        this.handCrankWeight = checkListModel.handCrankWeight;
        this.trailerConnection = checkListModel.trailerConnection;
        this.load = checkListModel.load;
        this.tires = checkListModel.tires;
        this.lights = checkListModel.lights;
        this.trailerBrakes = checkListModel.trailerBrakes;
        this.mirrors = checkListModel.mirrors;
    }

    public int getnRealmId() {
        return nRealmId;
    }

    public void setnRealmId(int nRealmId) {
        this.nRealmId = nRealmId;
    }

    public int getnVehicleId() {
        return nVehicleId;
    }

    public void setnVehicleId(int nVehicleId) {
        this.nVehicleId = nVehicleId;
    }

    public boolean isHandCrankWeight() {
        return handCrankWeight;
    }

    public void setHandCrankWeight(boolean handCrankWeight) {
        this.handCrankWeight = handCrankWeight;
    }

    public boolean isTrailerConnection() {
        return trailerConnection;
    }

    public void setTrailerConnection(boolean trailerConnection) {
        this.trailerConnection = trailerConnection;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public boolean isTires() {
        return tires;
    }

    public void setTires(boolean tires) {
        this.tires = tires;
    }

    public boolean isLights() {
        return lights;
    }

    public void setLights(boolean lights) {
        this.lights = lights;
    }

    public boolean isTrailerBrakes() {
        return trailerBrakes;
    }

    public void setTrailerBrakes(boolean trailerBrakes) {
        this.trailerBrakes = trailerBrakes;
    }

    public boolean isMirrors() {
        return mirrors;
    }

    public void setMirrors(boolean mirrors) {
        this.mirrors = mirrors;
    }

    public boolean isAllChecked() {
        if (trailerConnection && load && tires && lights && trailerBrakes && mirrors)
            return true;
        else
            return false;
    }
}
