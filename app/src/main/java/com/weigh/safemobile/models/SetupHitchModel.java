package com.weigh.safemobile.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SetupHitchModel extends RealmObject {

    @PrimaryKey
    private int nRealmId;
    //private boolean sampleItem;
    private String brand;
    private String hitchType;
    private int hitchIndex;
    private int typeIndex;
    private boolean selected;
    private double mMaxGTWR;
    private double mMaxTongue;
    private String mBall;
    private int ballIndex;
    private String mShank;
    private int shankIndex;
    private boolean OnSafeHitch;

    public SetupHitchModel() {
        this.nRealmId = 0;
        //this.sampleItem = false;
        this.brand = "";
        this.hitchType = "";
        this.hitchIndex = 0;
        this.typeIndex = 0;
        this.selected = false;
        this.mMaxGTWR = 0;
        this.mMaxTongue = 0;
        this.mBall = "";
        this.ballIndex = 0;
        this.mShank = "";
        this.shankIndex = 0;
        this.OnSafeHitch = false;
    }

    public SetupHitchModel(SetupHitchModel s)
    {
        this.nRealmId = s.nRealmId;
        //this.sampleItem = s.sampleItem;
        this.brand = s.brand;
        this.hitchType = s.hitchType;
        this.hitchIndex = s.hitchIndex;
        this.typeIndex = s.typeIndex;
        this.selected = s.selected;
        this.mMaxGTWR = s.mMaxGTWR;
        this.mMaxTongue = s.mMaxTongue;
        this.mBall = s.mBall;
        this.ballIndex = s.ballIndex;
        this.mShank = s.mShank;
        this.shankIndex = s.shankIndex;
        this.OnSafeHitch = s.OnSafeHitch;
    }

    public int getnRealmId() {
        return nRealmId;
    }
    public void setnRealmId(int nRealmId) {
        this.nRealmId = nRealmId;
    }

    //public boolean isSampleItem() { return sampleItem;}
    //public void setSampleItem(boolean _item) { this.sampleItem = _item; }

    public String getBrand() {return this.brand;}
    public void setBrand(String brand) { this.brand = brand;}

    public String getHitchType() {return this.hitchType;}
    public void setHitchType(String hitchType) {this.hitchType = hitchType;}

    public int getHitchIndex() {return this.hitchIndex;}
    public void setHitchIndex(int Index) {this.hitchIndex = Index;}

    public int getTypeIndex() {return this.typeIndex;}
    public void setTypeIndex(int Index) {this.typeIndex = Index;}


    public boolean isSelected() {return this.selected;}
    public void setSelected(boolean _selected) {this.selected = _selected;}

    public double getmMaxGTWR() {return this.mMaxGTWR;}
    public void setmMaxGTWR(double maxGTWR) {this.mMaxGTWR = maxGTWR;}

    public double getmMaxTongue() {return this.mMaxTongue;}
    public void setmMaxTongue(double val) {this.mMaxTongue = val;}

    public String getmBall() {return this.mBall;}
    public void setmBall(String val) {this.mBall = val;}

    public String getmShank() {return this.mShank;}
    public void setmShank(String val) {this.mShank = val;}

    public int getBallIndex() {return this.ballIndex;}
    public void setBallIndex(int Index) {this.ballIndex = Index;}

    public int getShankIndex() {return this.shankIndex;}
    public void setShankIndex(int Index) {this.shankIndex = Index;}

    public boolean isOnSafeHitch() {return OnSafeHitch;}
    public void setOnSafeHitch(boolean _onSafe) {this.OnSafeHitch = _onSafe;}
}
