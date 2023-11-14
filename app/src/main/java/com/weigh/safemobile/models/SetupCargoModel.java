package com.weigh.safemobile.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SetupCargoModel extends RealmObject {

    @PrimaryKey
    private int nRealmId;
    //private boolean sampleItem;
    private String name;
    private boolean selected;
    private double cargoWeight;
    private String mType;

    public SetupCargoModel() {
        this.nRealmId = 0;
        //this.sampleItem = false;
        this.name = "";
        this.selected = false;
        this.cargoWeight = 0;
        this.mType = "";
    }

    public SetupCargoModel(SetupCargoModel s)
    {
        this.nRealmId = s.nRealmId;
        //this.sampleItem = s.sampleItem;
        this.name = s.name;
        this.selected = s.selected;
        this.cargoWeight = s.cargoWeight;
        this.mType = s.mType;
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

    public boolean isSelected() {return this.selected;}
    public void setSelected(boolean _selected) {this.selected = _selected;}

    public double getCargoWeight() {return this.cargoWeight;}
    public void setCargoWeight(double val) {this.cargoWeight = val;}

    public String getmType() {return this.mType;}
    public void setmType(String val) {this.mType = val;}
}
