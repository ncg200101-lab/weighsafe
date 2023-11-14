package com.weigh.safemobile.presenters;


import com.weigh.safemobile.models.SetupTrailerModel;

public interface TrailerUpdateListener {

    void onEditDialogSave(int position, SetupTrailerModel item);
    void onNewDialogSave(SetupTrailerModel item);
    void onDeleteDialogSave(int position);
}
