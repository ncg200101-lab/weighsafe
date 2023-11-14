package com.weigh.safemobile.models;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SetupVehicleModel extends RealmObject implements Serializable {

    @PrimaryKey
    private int nRealmId;
    //private boolean sampleItem;
    private String strTitle;
    private String strVinNumber;
    private double mGVWR;
    private double mGCWR;
    private double mMCC;
    private double mMGTW;
    private double mMTW;
    private boolean distributed;
    private double mDistributedMGTW;
    private double mDistributedMTW;
    private String vin;
    private String year;
    private String make;
    private String model;


    public SetupVehicleModel() {
        this.nRealmId = 0;
        //this.sampleItem = false;
        this.strTitle = "";
        this.mGVWR = 0;
        this.mGCWR = 0;
        this.mMCC = 0;
        this.mMGTW = 0;
        this.mMTW = 0;
        this.distributed = false;
        this.mDistributedMGTW = 0;
        this.mDistributedMTW = 0;
        this.vin = "";
        this.year = "";
        this.make = "";
        this.model = "";
    }

    public SetupVehicleModel(SetupVehicleModel s)
    {
        this.nRealmId = s.nRealmId;
        //this.sampleItem = s.sampleItem;
        this.strTitle = s.strTitle;
        this.mGVWR = s.mGVWR;
        this.mGCWR = s.mGCWR;
        this.mMCC = s.mMCC;
        this.mMTW = s.mMTW;
        this.mMGTW = s.mMGTW;
        this.distributed = s.distributed;
        this.mDistributedMTW = s.mDistributedMTW;
        this.mDistributedMGTW = s.mDistributedMGTW;
        this.vin = s.vin;
        this.year = s.year;
        this.make = s.make;
        this.model = s.model;
    }

    public int getnRealmId() {
        return nRealmId;
    }
    public void setnRealmId(int nRealmId) {
        this.nRealmId = nRealmId;
    }

    //public boolean isSampleItem() { return sampleItem;}
    //public void setSampleItem(boolean _item) { this.sampleItem = _item; }

    public String getStrTitle() {return this.strTitle;}
    public void setStrTitle(String strTitle) { this.strTitle = strTitle;}

    public double getmGVWR() {return this.mGVWR;}
    public void setmGVWR(double val) {this.mGVWR = val;}

    public double getmGCWR() {return this.mGCWR;}
    public void setmGCWR(double val) {this.mGCWR = val;}

    public double getmMCC() {return this.mMCC;}
    public void setmMCC(double val) {this.mMCC = val;}

    public double getmMGTW() {return this.mMGTW;}
    public void setmMGTW(double val) {this.mMGTW = val;}

    public double getmMTW() {return this.mMTW;}
    public void setmMTW(double val) {this.mMTW = val;}

    public boolean isDistributed() {return this.distributed;}
    public void setDistributed(boolean _distributed) {this.distributed = _distributed;}

    public double getmDistributedMGTW() {return this.mDistributedMGTW;}
    public void setmDistributedMGTW(double val) {this.mDistributedMGTW = val;}

    public double getmDistributedMTW() {return this.mDistributedMTW;}
    public void setmDistributedMTW(double val) {this.mDistributedMTW = val;}

    public String getVin() {return this.vin;}
    public void setVin(String vin) {this.vin = vin;}

    public String getYear() {return this.year;}
    public void setYear(String year) {this.year = year;}

    public String getMake() {return this.make;}
    public void setMake(String make) {this.make = make;}

    public String getModel() {return this.model;}
    public void setModel(String model) {this.model = model;}

}
