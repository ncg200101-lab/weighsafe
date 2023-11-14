package com.weigh.safemobile.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SetupTrailerModel extends RealmObject {
    @PrimaryKey
    private int nRealmId;
    //private boolean sampleItem;
    private String name;
    private String trailerType;
    private int typeIndex;
    private boolean selected;
    private double emptyWeight;
    private double loadCapacity;
    private double cargoWeight;
    private String coupler;
    private int couplerIndex;

    public SetupTrailerModel() {
        this.nRealmId = 0;
        //this.sampleItem = false;
        this.name = "";
        this.trailerType = "";
        this.typeIndex = 0;
        this.selected = false;
        this.emptyWeight = 0;
        this.loadCapacity = 0;
        this.cargoWeight = 0;
        this.coupler = "";
        this.couplerIndex = 0;
    }

    public SetupTrailerModel(SetupTrailerModel s)
    {
        this.nRealmId = s.nRealmId;
        //this.sampleItem = s.sampleItem;
        this.name = s.name;
        this.trailerType = s.trailerType;
        this.typeIndex = s.typeIndex;
        this.selected = s.selected;
        this.emptyWeight = s.emptyWeight;
        this.loadCapacity = s.loadCapacity;
        this.cargoWeight = s.cargoWeight;
        this.coupler = s.coupler;
        this.couplerIndex = s.couplerIndex;
    }

    public int getnRealmId() {
        return nRealmId;
    }
    public void setnRealmId(int nRealmId) {
        this.nRealmId = nRealmId;
    }

    //public boolean isSampleItem() { return sampleItem;}
    //public void setSampleItem(boolean _item) { this.sampleItem = _item; }

    public String getName() {return this.name;}
    public void setName(String name) { this.name = name;}

    public String getTrailerType() {return this.trailerType;}
    public void setTrailerType(String _trailerType) { this.trailerType = _trailerType; }

    public int getTypeIndex() {return this.typeIndex;}
    public void setTypeIndex(int _index) { this.typeIndex = _index; }

    public boolean isSelected() {return this.selected;}
    public void setSelected(boolean _selected) {this.selected = _selected;}

    public double getEmptyWeight() {return this.emptyWeight;}
    public void setEmptyWeight(double val) {this.emptyWeight = val;}

    public double getLoadCapacity() {return this.loadCapacity;}
    public void setLoadCapacity(double val) {this.loadCapacity = val;}

    public double getCargoWeight() {return this.cargoWeight;}
    public void setCargoWeight(double val) {this.cargoWeight = val;}

    public String  getCoupler() {return this.coupler;}
    public void setCoupler(String val) {this.coupler = val;}

    public int getCouplerIndex() {return this.couplerIndex;}
    public void setCouplerIndex(int val) {this.couplerIndex = val;}
}
