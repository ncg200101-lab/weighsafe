package com.weigh.safemobile.fragments;


import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.MainActivity;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.helpers.AppHelper;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.SetupModel;
import com.weigh.safemobile.models.VehiclesModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.realm.RealmList;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    @BindView(R.id.edit_panel)
    RelativeLayout edit_panel;

    @BindView(R.id.scrollbar_edit)
    ScrollView sv;
    @BindView(R.id.img_sub_1)
    ImageView img_sub_1;
    @BindView(R.id.img_sub_2)
    ImageView img_sub_2;
    @BindView(R.id.img_sub_3)
    ImageView img_sub_3;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.edit_btn_save)
    TextView edit_btn_save;

    @BindView(R.id.vehicle_name_text)
    EditText vehicle_name_text;
    @BindView(R.id.vehicleError)
    TextInputLayout vehicleError;

    @BindView(R.id.trailer_name_text)
    EditText trailer_name_text;
    @BindView(R.id.trailerError)
    TextInputLayout trailerError;

    @BindView(R.id.axle_inch_text)
    EditText input_receiver;
    @BindView(R.id.axleError)
    TextInputLayout axleError;

    @BindView(R.id.spring_inch_text)
    EditText input_spring_bar;
    @BindView(R.id.springError)
    TextInputLayout springError;

    @BindView(R.id.trailer_inch_text)
    EditText input_trailer_alex;
    @BindView(R.id.trailerinchError)
    TextInputLayout trailerinchError;

    @BindView(R.id.edit_title)
    TextView edit_title;

    @BindView(R.id.btn_rise)
    RadioButton btn_rise;
    @BindView(R.id.btn_drop)
    RadioButton btn_drop;

    @BindView(R.id.holes_content_text)
    TextView holes_content_text;

    @BindView(R.id.btn_minus)
    TextView btn_minus;
    @BindView(R.id.btn_plus)
    TextView btn_plus;

    @BindView(R.id.segmented1)
    SegmentedGroup segmented_1;
    @BindView(R.id.segmented2)
    SegmentedGroup segmented_2;

    View view;
    private int selectedVehicleId = 0;
    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);
        initializerView();
        initializerSave();

        initializeListener();

        return view;
    }

    public void initializerView() {
        sv.setVerticalScrollBarEnabled(false);
        sv.setHorizontalScrollBarEnabled(false);

        //segmented_1.

        btn_rise.setChecked(true);
        //holes_content_text.setText("1.5");
        String key = getArguments().getString("key");
        if(key == "new") {
            edit_title.setText("New Setup");

        }else {
            RealmList<VehiclesModel> mVehicleList = DatabaseManager.getAllVehicle();

            if (mVehicleList.size() > 0) {
                VehiclesModel vModel = mVehicleList.get(PreferenceManager.getPositionID(view.getContext()));
                selectedVehicleId = vModel.getnRealmId();
                SetupModel setupModel = DatabaseManager.getSetupModelByVehicleId(selectedVehicleId);
                if (vModel != null) {
                    vehicle_name_text.setText(vModel.getStrVehicle());
                    trailer_name_text.setText(vModel.getStrTrailer());
                }

                if (setupModel != null) {
                    System.out.println(setupModel.getnRearAxle());
                    System.out.println(setupModel.getnDrawbarPosition());
                    input_receiver.setText(setupModel.getnRearAxle());
                    input_spring_bar.setText(setupModel.getnSpringBar());
                    input_trailer_alex.setText(setupModel.getnTrailerAxle());
                    if(setupModel.getnDrawbarPosition().equals("Rise")) { btn_rise.setChecked(true); }
                    else if(setupModel.getnDrawbarPosition().equals("Drop")) {btn_drop.setChecked(true); }
                    if(!setupModel.getnHoles().toString().isEmpty()) { holes_content_text.setText(setupModel.getnHoles()); }
                }
                System.out.println(selectedVehicleId);
            } else {

            }
        }
    }

    public void initializeListener()
    {
        vehicle_name_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (vehicle_name_text.getText().toString().isEmpty()) {
                    vehicleError.setError(getString(R.string.text_error_1));
                } else
                {
                    vehicleError.setErrorEnabled(false);
                }
            }
        });

        vehicle_name_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (vehicle_name_text.getText().toString().isEmpty()) {
                        vehicleError.setError(getString(R.string.text_error_1));
                    } else
                    {
                        vehicleError.setErrorEnabled(false);
                    }
                }
            }
        });

        trailer_name_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (trailer_name_text.getText().toString().isEmpty()) {
                    trailerError.setError(getString(R.string.text_error_1));
                } else
                {
                    trailerError.setErrorEnabled(false);
                }
            }
        });

        trailer_name_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (trailer_name_text.getText().toString().isEmpty()) {
                        trailerError.setError(getString(R.string.text_error_1));
                    } else
                    {
                        trailerError.setErrorEnabled(false);
                    }
                }
            }
        });

        input_receiver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input_receiver.getText().toString().isEmpty()) {
                    axleError.setError(getString(R.string.text_error_1));
                } else
                {
                    double RearVal = Double.valueOf(input_receiver.getText().toString());
                    if (RearVal > 70)
                        axleError.setError(getString(R.string.text_error_rear_range));
                    else
                        axleError.setErrorEnabled(false);
                }
            }
        });
        input_receiver.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (input_receiver.getText().toString().isEmpty()) {
                        axleError.setError(getString(R.string.text_error_1));
                    } else
                    {
                        double RearVal = Double.valueOf(input_receiver.getText().toString());
                        if (RearVal > 70)
                            axleError.setError(getString(R.string.text_error_rear_range));
                        else
                            axleError.setErrorEnabled(false);
                    }
                }
            }
        });

        input_spring_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input_spring_bar.getText().toString().isEmpty()) {
                    springError.setError(getString(R.string.text_error_1));
                }
                else {
                    double SpringVal = Double.valueOf(input_spring_bar.getText().toString());
                    if (SpringVal > 32 || SpringVal < 24)
                        springError.setError(getString(R.string.text_error_spring_Range));
                    else
                        springError.setErrorEnabled(false);
                }
            }
        });

        input_spring_bar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (input_spring_bar.getText().toString().isEmpty()) {
                        springError.setError(getString(R.string.text_error_1));
                    }
                     else
                    {
                        double SpringVal = Double.valueOf(input_spring_bar.getText().toString());
                        if (SpringVal > 32 || SpringVal < 24)
                            springError.setError(getString(R.string.text_error_spring_Range));
                        else
                            springError.setErrorEnabled(false);
                    }
                }
            }
        });

        input_trailer_alex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input_trailer_alex.getText().toString().isEmpty()) {
                    trailerinchError.setError(getString(R.string.text_error_1));
                } else
                {
                    trailerinchError.setErrorEnabled(false);
                }
            }
        });

        input_trailer_alex.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (input_trailer_alex.getText().toString().isEmpty()) {
                        trailerinchError.setError(getString(R.string.text_error_1));
                    } else
                    {
                        trailerinchError.setErrorEnabled(false);
                    }
                }
            }
        });

    }

    public void initializerSave() {
        edit_panel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_left);
                transaction.addToBackStack(null);
                transaction.replace(R.id.frame_layout, new SetupFragment());
                transaction.commit();
            }
        });

        edit_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String draw_pos = "Rise";
                if(btn_rise.isChecked()) { draw_pos = "Rise"; }
                else if(btn_drop.isChecked()) { draw_pos ="Drop"; }

                boolean isValid = true;
                if (vehicle_name_text.getText().toString().isEmpty()) {
                    vehicleError.setError(getString(R.string.text_error_1));
                    isValid = false;
                }

                if (trailer_name_text.getText().toString().isEmpty()) {
                    trailerError.setError(getString(R.string.text_error_1));
                    isValid = false;
                }

                if (input_receiver.getText().toString().isEmpty()) {
                    axleError.setError(getString(R.string.text_error_1));
                    isValid = false;
                }

                if (input_spring_bar.getText().toString().isEmpty()) {
                    springError.setError(getString(R.string.text_error_1));
                    isValid = false;
                }

                if (input_trailer_alex.getText().toString().isEmpty()) {
                    trailerinchError.setError(getString(R.string.text_error_1));
                    isValid = false;
                }

                if (!isValid)
                {
                    AppHelper.CustomToast(getActivity(), getString(R.string.text_error_1));
                    return;
                } else
                {
                    vehicleError.setErrorEnabled(false);
                    trailerError.setErrorEnabled(false);
                    axleError.setErrorEnabled(false);
                    springError.setErrorEnabled(false);
                    trailerinchError.setErrorEnabled(false);
                }

                double rearVal = Double.valueOf(input_receiver.getText().toString());
                if (rearVal > 70) {
                    axleError.setError(getString(R.string.text_error_rear_range));
                    AppHelper.CustomToast(getActivity(), getString(R.string.text_error_rear_range));
                    return;
                }
                else
                    axleError.setErrorEnabled(false);

                double SpringVal = Double.valueOf(input_spring_bar.getText().toString());
                if (SpringVal > 32 || SpringVal < 24) {
                    springError.setError(getString(R.string.text_error_spring_Range));
                    AppHelper.CustomToast(getActivity(), getString(R.string.text_error_spring_Range));
                    return;
                }
                else
                    springError.setErrorEnabled(false);

                if (!vehicle_name_text.getText().toString().isEmpty() && !trailer_name_text.getText().toString().isEmpty()) {
                    VehiclesModel newVModel = DatabaseManager.addNewVehicle(vehicle_name_text.getText().toString(), trailer_name_text.getText().toString(), selectedVehicleId);
                    if (selectedVehicleId == 0) selectedVehicleId = newVModel.getnRealmId();
                    if (!input_receiver.getText().toString().isEmpty() && !input_spring_bar.getText().toString().isEmpty() && !input_trailer_alex.getText().toString().isEmpty()) {
                        DatabaseManager.saveSetupModel(selectedVehicleId, "input_receiver", input_receiver.getText().toString());
                        DatabaseManager.saveSetupModel(selectedVehicleId, "input_spring_bar", input_spring_bar.getText().toString());
                        DatabaseManager.saveSetupModel(selectedVehicleId, "input_trailer_alex", input_trailer_alex.getText().toString());
                        DatabaseManager.saveSetupModel(selectedVehicleId, "switch_drawbar_position", draw_pos);
                        DatabaseManager.saveSetupModel(selectedVehicleId, "visible_holes", holes_content_text.getText().toString());
                        AppHelper.CustomToast(getActivity(), "Saved successfully.");
                    } else {
                        //AppHelper.CustomToast(getActivity(), "Save failed.");
                    }
                } else {
                    //AppHelper.CustomToast(getActivity(), "Save failed.");
                }
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double holes = Double.parseDouble(holes_content_text.getText().toString());
                if (holes <1) holes -= 0.5; else holes -= 1;
                if(holes<0) holes = 0.0;
                holes_content_text.setText(String.valueOf(holes));
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double holes = Double.parseDouble(holes_content_text.getText().toString());
                if(holes>0) holes+=1; else holes += 0.5;
                if(holes>7.5) holes = 7.5;
                holes_content_text.setText(String.valueOf(holes));
            }
        });
    }



    @OnClick({R.id.img_sub_1, R.id.img_sub_2, R.id.img_sub_3})
    public void OnClickImage(View v) {
        DetailModalFragment dialog = null;
        switch (v.getId()) {
            case R.id.img_sub_1:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_rear_axle_to_end_of_receiver), R.drawable.variable_b_topview);
                break;
            case R.id.img_sub_2:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_tow_ball_to_end_of_spring_bars), R.drawable.variable_c_topview);
                break;
            case R.id.img_sub_3:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_tow_ball_to_trailer_axle), R.drawable.variable_d_topview);
                break;
        }
        dialog.show(getFragmentManager(), "detailModal");
    }

}
