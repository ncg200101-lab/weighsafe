package com.weigh.safemobile.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.helpers.AppHelper;
import com.weigh.safemobile.helpers.PreferenceManager;

public class ModalFragment extends DialogFragment {

    private String strName;
    private String strTitle;
    private String strMessage;

    private int vehicleId = 0;
    private TextView txtTitle;
    private TextView txtMessage;
    private EditText input_name;

    private Button btnSave;
    private Button btnCancel;
    private Space spaceBtn;

    private static final String ARG_PARAM1 = "TITLE";
    private static final String ARG_PARAM2 = "MESSAGE";
    private static final String ARG_PARAM3 = "NAME";

    public static ModalFragment newInstance(String title, String message,String name) {
        ModalFragment fragment = new ModalFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, message);
        args.putString(ARG_PARAM3, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strTitle = getArguments().getString(ARG_PARAM1);
            strMessage = getArguments().getString(ARG_PARAM2);
            strName = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_modal);
        dialog.setCancelable(false);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        input_name = (EditText) dialog.findViewById(R.id.input_setup_name);
        txtTitle = (TextView) dialog.findViewById(R.id.text_title);
        txtMessage = (TextView) dialog.findViewById(R.id.text_message);

        txtTitle.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Medium"));
        txtMessage.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Medium"));
        input_name.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Medium"));

        if (!strName.equals("") && !strName.isEmpty())
            input_name.setHint(getString(R.string.place_holder_Edit));

        txtTitle.setText(strTitle);
        txtMessage.setText(strMessage);
        input_name.setText(strName);

        btnSave = (Button) dialog.findViewById(R.id.btn_save);
        btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        spaceBtn = (Space) dialog.findViewById(R.id.space_btn);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setupName = input_name.getText().toString();

                if(validateText(setupName)) {
                    if (strName.isEmpty() || strName.equals("")) {
                        MainApplication.getInstance().getModalFragmentListener().onDialogSave(ModalFragment.this, setupName, true);
                        PreferenceManager.setWizardOn(getActivity(), true);
                    }
                    else
                        MainApplication.getInstance().getModalFragmentListener().onDialogSave(ModalFragment.this, setupName, false);
                }
                (ModalFragment.this).getDialog().dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (ModalFragment.this).getDialog().dismiss();

                if (strName.isEmpty() || strName.equals(""))
                    PreferenceManager.setWizardOn(getActivity(), false);
            }
        });

        if (strMessage.equals(getString(R.string.text_setup_note))) {
            btnCancel.setVisibility(View.GONE);
            spaceBtn.setVisibility(View.GONE);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
            );
            btnSave.setLayoutParams(param);
            btnSave.setText("NEXT");
            input_name.setHint(getString(R.string.place_holder_setup_a).toString());
        }


        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    public boolean validateText(String name){
        if(name.trim().isEmpty()){
            AppHelper.CustomToast(getContext(), "Enter Setup Name.");
            input_name.setError(getString(R.string.text_error_1));
            return false;
        }

        return true;
    }
}
