package com.weigh.safemobile.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SetupModel extends RealmObject {

    @PrimaryKey
    private int nRealmId;
    private int nVehicleId; // primary key of Vehicle Model
    private String nTrailerWeight;
    private String nTongueWeight;
    private String nRearAxle;
    private String nSpringBar;
    private String nTrailerAxle;
    private String nDrawbarPosition;
    private String nHoles;

    public SetupModel(){
        this.nRealmId = 0;
        this.nVehicleId = 0;
        this.nTrailerWeight = "";
        this.nTongueWeight = "";
        this.nRearAxle = "";
        this.nSpringBar = "";
        this.nTrailerAxle = "";
        this.nDrawbarPosition = "Rise";
        this.nHoles = "1.5";
    }

    public SetupModel(SetupModel s){
        this.nRealmId = s.nRealmId;
        this.nVehicleId = s.nVehicleId;
        this.nTrailerWeight = s.nTrailerWeight;
        this.nTongueWeight = s.nTongueWeight;
        this.nRearAxle = s.nRearAxle;
        this.nSpringBar = s.nSpringBar;
        this.nTrailerAxle = s.nTrailerAxle;
        this.nDrawbarPosition = s.nDrawbarPosition;
        this.nHoles = s.nHoles;
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

    public String getnTrailerWeight() {
        return nTrailerWeight;
    }

    public void setnTrailerWeight(String nTrailerWeight) {
        this.nTrailerWeight = nTrailerWeight;
    }

    public String getnTongueWeight() {
        return nTongueWeight;
    }

    public void setnTongueWeight(String nTongueWeight) {
        this.nTongueWeight = nTongueWeight;
    }

    public String getnRearAxle() {
        return nRearAxle;
    }

    public void setnRearAxle(String nRearAxle) {
        this.nRearAxle = nRearAxle;
    }

    public String getnSpringBar() {
        return nSpringBar;
    }

    public void setnSpringBar(String nSpringBar) {
        this.nSpringBar = nSpringBar;
    }

    public String getnTrailerAxle() {
        return nTrailerAxle;
    }

    public void setnTrailerAxle(String nTrailerAxle) {
        this.nTrailerAxle = nTrailerAxle;
    }

    public String getnDrawbarPosition() {return  nDrawbarPosition; }

    public void setnDrawbarPosition(String nDrawbarPosition) { this.nDrawbarPosition = nDrawbarPosition; }

    public String getnHoles() { return nHoles; }

    public void setnHoles(String nHoles) { this.nHoles = nHoles; }
}
