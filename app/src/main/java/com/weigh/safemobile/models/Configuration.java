package com.weigh.safemobile.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Configuration extends RealmObject {
    @PrimaryKey
    private int nRealmId;
    private String name;
    private double nTongueWeight;
    private double nTowBallSpringBar;
    private double nTowBallTrailerAxle;
    private double nRearAxle;
    private double nAdditionalVehicleWeight;
    private double nAdditionalTrailerWeight;
    private int nDrawbarPosition;
    private double nHolesVisible;

    private int nVehicleIndex;
    private int nVehicleRealmId;

    private int nHitchIndex;
    private int nHitchRealmId;

    private int nTrailerIndex;
    private int nTrailerRealmId;

    private RealmList<Integer> nCargoIndexes;
    private RealmList<Integer> nCargoRealmIDs;

    public Configuration(){
        this.nRealmId = 0;
        this.name = "";
        this.nTongueWeight = 0;
        this.nTowBallSpringBar = 0;
        this.nTowBallTrailerAxle = 0;
        this.nRearAxle = 0;
        this.nAdditionalTrailerWeight = 0;
        this.nAdditionalVehicleWeight = 0;
        this.nDrawbarPosition = 0;
        this.nHolesVisible = 0;
        this.nVehicleIndex = -1;
        this.nVehicleRealmId = -1;
        this.nHitchIndex = -1;
        this.nHitchRealmId = -1;
        this.nTrailerIndex = -1;
        this.nTrailerRealmId = -1;
        this.nCargoIndexes = new RealmList<>();
        this.nCargoRealmIDs = new RealmList<>();

    }

    public Configuration(Configuration s){
        this.nRealmId = s.nRealmId;
        this.name = s.name;
        this.nTongueWeight = s.nTongueWeight;
        this.nTowBallSpringBar = s.nTowBallSpringBar;
        this.nTowBallTrailerAxle = s.nTowBallTrailerAxle;
        this.nRearAxle = s.nRearAxle;
        this.nAdditionalTrailerWeight = s.nAdditionalTrailerWeight;
        this.nAdditionalVehicleWeight = s.nAdditionalVehicleWeight;
        this.nDrawbarPosition = s.nDrawbarPosition;
        this.nHolesVisible = s.nHolesVisible;
        this.nVehicleIndex = s.nVehicleIndex;
        this.nVehicleRealmId = s.nVehicleRealmId;
        this.nHitchIndex = s.nHitchIndex;
        this.nHitchRealmId = s.nHitchRealmId;
        this.nTrailerIndex = s.nTrailerIndex;
        this.nTrailerRealmId = s.nTrailerRealmId;
        this.nCargoIndexes = new RealmList<>();
        this.nCargoIndexes.addAll(s.nCargoIndexes);
        this.nCargoRealmIDs = new RealmList<>();
        this.nCargoRealmIDs.addAll(s.nCargoRealmIDs);
    }

    public int getnRealmId() {
        return nRealmId;
    }
    public void setnRealmId(int nRealmId) {
        this.nRealmId = nRealmId;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public double getnTongueWeight() {return nTongueWeight;}
    public void setnTongueWeight(double tongueWeight) {this.nTongueWeight = tongueWeight;}

    public double getnTowBallSpringBar() {return nTowBallSpringBar;}
    public void setnTowBallSpringBar(double nTowBallSpringBar) {this.nTowBallSpringBar = nTowBallSpringBar;}

    public double getnTowBallTrailerAxle() {return nTowBallTrailerAxle;}
    public void setnTowBallTrailerAxle(double nTowBallTrailerAxle) {this.nTowBallTrailerAxle = nTowBallTrailerAxle;}

    public double getnRearAxle() {return nRearAxle;}
    public void setnRearAxle(double nRearAxle) {this.nRearAxle = nRearAxle;}

    public double getnAdditionalVehicleWeight() {return nAdditionalVehicleWeight;}
    public void setnAdditionalVehicleWeight(double nAdditionalVehicleWeight) {this.nAdditionalVehicleWeight = nAdditionalVehicleWeight;}

    public double getnAdditionalTrailerWeight() {return nAdditionalTrailerWeight;}
    public void setnAdditionalTrailerWeight(double nAdditionalTrailerWeight) {this.nAdditionalTrailerWeight = nAdditionalTrailerWeight;}

    public int getnDrawbarPosition() {return nDrawbarPosition;}
    public void setnDrawbarPosition(int nDrawbarPosition) {this.nDrawbarPosition = nDrawbarPosition;}

    public double getnHolesVisible() {return nHolesVisible;}
    public void setnHolesVisible(double nHolesVisible) {this.nHolesVisible = nHolesVisible;}

    public int getnVehicleIndex() {return nVehicleIndex;}
    public void setnVehicleIndex(int nVehicleIndex) {this.nVehicleIndex = nVehicleIndex;}

    public int getnVehicleRealmId() {return nVehicleRealmId;}
    public void setnVehicleRealmId(int nVehicleRealmId) {this.nVehicleRealmId = nVehicleRealmId;}

    public int getnHitchIndex() {return nHitchIndex;}
    public void setnHitchIndex(int nHitchIndex) {this.nHitchIndex = nHitchIndex;}

    public int getnHitchRealmId() {return nHitchRealmId;}
    public void setnHitchRealmId(int nHitchRealmId) {this.nHitchRealmId = nHitchRealmId;}

    public int getnTrailerIndex() {return nTrailerIndex;}
    public void setnTrailerIndex(int nTrailerIndex) {this.nTrailerIndex = nTrailerIndex;}

    public int getnTrailerRealmId() {return nTrailerRealmId;}
    public void setnTrailerRealmId(int nTrailerRealmId) {this.nTrailerRealmId = nTrailerRealmId;}

    public RealmList<Integer> getnCargoIndexes() {return nCargoIndexes;}
    public void setnCargoIndexes(RealmList<Integer> _indexes) {
        this.nCargoIndexes = new RealmList<>();
        this.nCargoIndexes.addAll(_indexes);
    }

    public RealmList<Integer> getnCargoRealmIDs() {return nCargoRealmIDs;}
    public void setnCargoRealmIDs(RealmList<Integer> _IDs) {
        this.nCargoRealmIDs = new RealmList<>();
        this.nCargoRealmIDs.addAll(_IDs);
    }
}
