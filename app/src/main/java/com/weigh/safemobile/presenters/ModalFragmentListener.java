package com.weigh.safemobile.presenters;

import androidx.fragment.app.DialogFragment;

public interface ModalFragmentListener {
    void onDialogSave(DialogFragment dialog, String setupName, boolean isNewSetup);
}