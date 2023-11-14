package com.weigh.safemobile.models;

public class ConfigPusher {
    private String action;
    private int selectedVehiclePosition;
    private Configuration configModel;

    public ConfigPusher(String action, int selectedVehiclePosition, Configuration configModel) {
        this.action = action;
        this.selectedVehiclePosition = selectedVehiclePosition;
        this.configModel = configModel;
    }

    public ConfigPusher(String action, Configuration vehiclesModel) {
        this.action = action;
        this.configModel = vehiclesModel;
    }

    public int getSelectedVehiclePosition() {
        return selectedVehiclePosition;
    }

    public String getAction() {
        return action;
    }

    public Configuration getConfigModel() {
        return configModel;
    }
}
