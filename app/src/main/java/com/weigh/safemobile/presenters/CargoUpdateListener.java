package com.weigh.safemobile.presenters;


import com.weigh.safemobile.models.SetupCargoModel;

public interface CargoUpdateListener {
    void onEditDialogSave(int position, SetupCargoModel item);
    void onNewDialogSave(SetupCargoModel item);
    void onDeleteDialogSave(int position);
}
