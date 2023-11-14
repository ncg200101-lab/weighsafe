package com.weigh.safemobile.presenters;

import com.weigh.safemobile.models.SetupVehicleModel;

public interface VehicleUpdateListener {
    void onEditDialogSave(int position, SetupVehicleModel item);
    void onNewDialogSave(SetupVehicleModel item);
    void onDeleteDialogSave(int position);
}
