package com.weigh.safemobile.models;

import java.util.HashMap;

public class HitchModelOptions {
    private String name;
    private String type;
    private HashMap<String, Object> mgtwr;
    private HashMap<String, Object> mtw;
    private String guide;

    public HitchModelOptions()
    {

    }

    public HitchModelOptions(String _name, String _type, HashMap<String, Object> _mgtwr, HashMap<String, Object> _mtw, String _guide)
    {
        this.name = _name;
        this.type = _type;
        this.mgtwr = _mgtwr;
        this.mtw = _mtw;
        this.guide = _guide;
    }

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public String getType() {return this.type;}
    public void setType(String type) {this.type = type;}

    public String getGuide() {return this.guide;}
    public void setGuide(String guide) {this.guide = guide;}

    public HashMap<String, Object> getMgtwr() {return this.mgtwr;}
    public void setMgtwr(HashMap<String, Object> mgtwr) {this.mgtwr = mgtwr;}

    public HashMap<String, Object> getMtw() {return this.mtw;}
    public void setMtw(HashMap<String, Object> mtw) {this.mtw = mtw;}

}
