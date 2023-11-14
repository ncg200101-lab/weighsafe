package com.weigh.safemobile.models;

public class Pusher {
    private String action;
    private int selectedPosition;
    private int position;
    private VehiclesModel vehiclesModel;
    private SetupVehicleModel _vehicleModel;
    private SetupHitchModel _hitchModel;
    private SetupTrailerModel _trailerModel;
    private SetupCargoModel _cargoModel;

    public Pusher(String action, int selectedVehiclePosition, VehiclesModel vehiclesModel) {
        this.action = action;
        this.selectedPosition = selectedVehiclePosition;
        this.vehiclesModel = vehiclesModel;
    }

    public Pusher(String action, VehiclesModel vehiclesModel) {
        this.action = action;
        this.vehiclesModel = vehiclesModel;
    }

    public Pusher(String action, int fragmentPosition) {
        this.action = action;
        this.position = fragmentPosition;
    }

    public Pusher(String action, int selectedPosition, SetupVehicleModel vehicleModel) {
        this.action = action;
        this.selectedPosition = selectedPosition;
        this._vehicleModel = vehicleModel;
    }

    public Pusher(String action, int selectedPosition, SetupHitchModel hitchModel) {
        this.action = action;
        this.selectedPosition = selectedPosition;
        this._hitchModel = hitchModel;
    }

    public Pusher(String action, int selectedPosition, SetupTrailerModel trailerModel) {
        this.action = action;
        this.selectedPosition = selectedPosition;
        this._trailerModel = trailerModel;
    }

    public Pusher(String action, int selectedPosition, SetupCargoModel cargoModel) {
        this.action = action;
        this.selectedPosition = selectedPosition;
        this._cargoModel = cargoModel;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public String getAction() {
        return action;
    }

    public VehiclesModel getVehiclesModel() {
        return vehiclesModel;
    }

    public SetupVehicleModel get_vehicleModel() {return  _vehicleModel; }

    public SetupHitchModel get_hitchModel() {return  _hitchModel; }

    public SetupTrailerModel get_trailerModel() {return  _trailerModel; }

    public SetupCargoModel get_cargoModel() { return _cargoModel; }

    public int getPosition() { return position; }
}
