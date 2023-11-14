package com.weigh.safemobile.presenters;


import com.weigh.safemobile.models.SetupHitchModel;

public interface HitchUpdateListener {
    void onEditDialogSave(int position, SetupHitchModel item);
    void onNewDialogSave(SetupHitchModel item);
    void onDeleteDialogSave(int position);
}
