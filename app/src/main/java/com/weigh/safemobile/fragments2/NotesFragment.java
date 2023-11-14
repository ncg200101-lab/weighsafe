package com.weigh.safemobile.fragments2;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.helpers.AppHelper;
import com.weigh.safemobile.models.Configuration;
import com.weigh.safemobile.models.NotesModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class NotesFragment extends Fragment {
    @BindView(R.id.setup_note)
    EditText input_note;
    @BindView(R.id.text_notes_title)
    TextView textTitle;
    @BindView(R.id.btn_save)
    Button btnSave;


    private View mView;
    private int selectedVehicleId = 0;

    public NotesFragment() {
        // Required empty public constructor
    }


    public static NotesFragment newInstance() {
        NotesFragment fragment = new NotesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null)
        {
            mView = inflater.inflate(R.layout.fragment2_notes, container, false);
            ButterKnife.bind(this, mView);
            initializerView();
            initializeListener();
        }

        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
            return;

        Configuration selectedConfig = MainApplication.getInstance().selectedConfiguration;
        selectedVehicleId = selectedConfig.getnRealmId();

        if (selectedVehicleId != -1) {
            textTitle.setText("Notes: " + selectedConfig.getName() );
            setNote();
        }
        else
            input_note.setText("");

        btnSave.setVisibility(View.INVISIBLE);
        btnSave.requestFocus();
        input_note.clearFocus();
    }

    private void initializerView() {
        input_note.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        textTitle.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        btnSave.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
    }

    private void initializeListener() {
        input_note.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    btnSave.setVisibility(View.VISIBLE);
                    input_note.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pdlg_color_yellow));
                } else {
                    btnSave.setVisibility(View.INVISIBLE);
                    input_note.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                }
            }
        });
    }

    public void setNote(){
        NotesModel noteListModel = DatabaseManager.getNotesModelByVehicleId(selectedVehicleId);
        input_note.setText(noteListModel.getNotes());
    }

    @OnClick(R.id.btn_save)
    public void onClickSave(View v) {
        String note = input_note.getText().toString();
        DatabaseManager.saveNotesModel(selectedVehicleId, note);

        btnSave.setVisibility(View.INVISIBLE);
        input_note.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        input_note.clearFocus();

        PrettyDialog pDialog = new PrettyDialog(getActivity());
        pDialog.setIcon(R.drawable.pdlg_icon_success);
        pDialog.setIconTint(R.color.pdlg_color_yellow);
        pDialog.addButton(
                "Close",			// button text
                R.color.colorBlack,		// button text color
                R.color.pdlg_color_yellow,	   // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        pDialog.dismiss();
                    }
                });

        pDialog.setMessage(getString(R.string.text_note_saved));
        pDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}