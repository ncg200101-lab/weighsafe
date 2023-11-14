package com.weigh.safemobile.models;

import java.util.HashMap;
import java.util.Map;

public class VinModel {
    private String name;
    private String year;
    private String make;
    private String model;
    private String vin;

    private Map<String, String> specData;
    private int viewType;
    private String headerString;

    public VinModel() { }

    public VinModel(String _name, String _year, String _make, String _model, String _vin) {
        this.name = _name;
        this.year = _year;
        this.make = _make;
        this.model = _model;
        this.vin = _vin;
        specData = new HashMap<>();
    }

    public String getName() {return name;}
    public void setName(String _name) {this.name = _name;}

    public String getYear() {return year;}
    public void setYear(String _year) {this.year = _year;}

    public String getMake() {return make;}
    public void setMake(String _make) {this.make = _make;}

    public String getModel() {return model;}
    public void setModel(String _model) {this.model = _model;}

    public String getVin() {return vin;}
    public void setVin(String _vin) {this.vin = _vin;}

    public Map<String, String> getSpecData() {return specData;}

    public void setSpecData(Map<String, String> _data) {
        this.specData.clear();
        this.specData.putAll(_data);
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getHeaderString() {
        return headerString;
    }

    public void setHeaderString(String headerString) {
        this.headerString = headerString;
    }
}
