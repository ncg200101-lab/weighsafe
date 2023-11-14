package com.weigh.safemobile.fragments;


import android.content.Context;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.GuideActivity;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.helpers.AppHelper;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Configuration;
import com.weigh.safemobile.models.SetupCargoModel;
import com.weigh.safemobile.models.SetupTrailerModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideStep6Fragment extends Fragment {
    @BindView(R.id.step6_panel)
    LinearLayout step6_panel;

    @BindView(R.id.close_guide)
    TextView close_guide;


    @BindView(R.id.guide_tongue_weight)
    EditText guide_tongue_weight;
    @BindView(R.id.tongueWeightError)
    TextInputLayout tongue_weight_error;

    @BindView(R.id.guide_axle_inch)
    EditText guide_axle_inch;
    @BindView(R.id.axle_inch_Error)
    TextInputLayout axle_inch_error;

    @BindView(R.id.guide_spring_inch)
    EditText guide_spring_inch;
    @BindView(R.id.spring_inch_error)
    TextInputLayout spring_inch_error;

    @BindView(R.id.guide_trailer_inch)
    EditText guide_trailer_inch;
    @BindView(R.id.trailer_inch_error)
    TextInputLayout trailer_inch_error;

    @BindView(R.id.guide_safe_tongue_weight)
    TextView guide_safe_tongue_weight;

    @BindView(R.id.img_back_page)
    ImageView imgBackPage;
    @BindView(R.id.go_back)
    TextView go_back;
    @BindView(R.id.img_next_page)
    ImageView imgNextPage;
    @BindView(R.id.go_next)
    TextView go_next;

    private Context mContext;
    private Configuration selectedConfiguration;

    public GuideStep6Fragment(Context context) {
        // Required empty public constructor
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guide_step6, container, false);
        ButterKnife.bind(this, v);
        close_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields())
                {

                    ((GuideActivity)getActivity()).FinishActivity();
                    close_guide.setClickable(false);
                }
            }
        });

        imgBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(5);
            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(5);
            }
        });

        imgNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(7);
            }
        });

        go_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(7);
            }
        });

        selectedConfiguration = MainApplication.getInstance().selectedConfiguration;
        initializerListener();

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            /*
            SharedPreferences sharedpreferences;
            String MyPREFERENCES = "MyPrefs" ;
            sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.commit();
             */
        }
        else {

        }
    }

    private void initializerListener() {
        step6_panel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

        guide_tongue_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (guide_tongue_weight.getText().toString().isEmpty())
                    tongue_weight_error.setError(getString(R.string.text_error_1));
                else
                {
                    tongue_weight_error.setErrorEnabled(false);
                }
            }
        });
        guide_tongue_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    ((GuideActivity)getActivity()).setTongueWeight(guide_tongue_weight.getText().toString());

                    if (guide_tongue_weight.getText().toString().isEmpty())
                        tongue_weight_error.setError(getString(R.string.text_error_1));
                    else
                    {
                        tongue_weight_error.setErrorEnabled(false);
                        confirmTongueWeight();
                    }
                }
            }
        });

        guide_axle_inch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (guide_axle_inch.getText().toString().isEmpty())
                    axle_inch_error.setError(getString(R.string.text_error_1));
                else {
                    double RearVal = Double.valueOf(guide_axle_inch.getText().toString());
                    if (RearVal > 70)
                        axle_inch_error.setError(getString(R.string.text_error_rear_range));
                    else
                        axle_inch_error.setErrorEnabled(false);
                }
            }
        });

        guide_axle_inch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    ((GuideActivity)getActivity()).setAxleinch(guide_axle_inch.getText().toString());

                    if (guide_axle_inch.getText().toString().isEmpty())
                        axle_inch_error.setError(getString(R.string.text_error_1));
                    else {
                        double RearVal = Double.valueOf(guide_axle_inch.getText().toString());
                        if (RearVal > 70)
                            axle_inch_error.setError(getString(R.string.text_error_rear_range));
                        else
                            axle_inch_error.setErrorEnabled(false);
                    }
                }
            }
        });

        guide_spring_inch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (guide_spring_inch.getText().toString().isEmpty())
                    spring_inch_error.setError(getString(R.string.text_error_1));
                else {
                    double SpringVal = Double.valueOf(guide_spring_inch.getText().toString());
                    if (SpringVal > 32 || SpringVal < 24)
                        spring_inch_error.setError(getString(R.string.text_error_spring_Range));
                    else
                        spring_inch_error.setErrorEnabled(false);
                }
            }
        });

        guide_spring_inch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    ((GuideActivity)getActivity()).setSpringinch(guide_spring_inch.getText().toString());

                    if (guide_spring_inch.getText().toString().isEmpty())
                        spring_inch_error.setError(getString(R.string.text_error_1));
                    else {
                        double SpringVal = Double.valueOf(guide_spring_inch.getText().toString());
                        if (SpringVal > 32 || SpringVal < 24)
                            spring_inch_error.setError(getString(R.string.text_error_spring_Range));
                        else
                            spring_inch_error.setErrorEnabled(false);
                    }

                }
            }
        });

        guide_trailer_inch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (guide_trailer_inch.getText().toString().isEmpty())
                    trailer_inch_error.setError(getString(R.string.text_error_1));
                else
                    trailer_inch_error.setErrorEnabled(false);
            }
        });

        guide_trailer_inch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    ((GuideActivity)getActivity()).setTrailerinch(guide_trailer_inch.getText().toString());

                    if (guide_trailer_inch.getText().toString().isEmpty())
                        trailer_inch_error.setError(getString(R.string.text_error_1));
                    else
                        trailer_inch_error.setErrorEnabled(false);

                }
            }
        });
    }

    private boolean confirmTongueWeight() {
        if(!guide_tongue_weight.getText().toString().isEmpty()) {
            int currTrailerIndex = PreferenceManager.getCurrentTrailer(mContext);
            List<SetupTrailerModel> models = MainApplication.getInstance().trailerModels;
            SetupTrailerModel currTrailer = models.get(currTrailerIndex);

            double trailerEmptyWeightValue = currTrailer.getEmptyWeight();

            List<String> cargoIndexes = PreferenceManager.getCurrentCargoList(mContext);
            List<SetupCargoModel> cargoModels = MainApplication.getInstance().cargoModels;
            double trailerCargo = 0;
            for (String index : cargoIndexes)
            {
                int numberIndex = Integer.valueOf(index);
                if (cargoModels.get(numberIndex).getmType().equals("Vehicle"))
                    trailerCargo += cargoModels.get(numberIndex).getCargoWeight();
            }

            trailerCargo += selectedConfiguration.getnAdditionalTrailerWeight();
            if (trailerCargo < 0)
                trailerCargo = 0;

            double gtw = trailerEmptyWeightValue - currTrailer.getLoadCapacity() + trailerCargo;
            double lowerRange = gtw * 0.1;
            double upperRange = gtw * 0.15;



            Double tongue_weight = Double.parseDouble(guide_tongue_weight.getText().toString());

            if (tongue_weight >= lowerRange && tongue_weight <= upperRange) {
                //saveValue("input_tongue_weight");
                tongue_weight_error.setErrorEnabled(false);
                guide_safe_tongue_weight.setVisibility(View.GONE);
            } else {
                guide_safe_tongue_weight.setVisibility(View.VISIBLE);
                tongue_weight_error.setError(getString(R.string.text_error_2));
                String message = "Your tongue weight should be between " + Math.round(lowerRange) + " lbs and " + Math.round(upperRange) + " lbs to ensure a safe tow";
                guide_safe_tongue_weight.setText(message);
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Unsafe Tongue Weight");
                builder.setMessage(message);
                builder.setPositiveButton("Ok", (dialog, whichButton) -> {

                });
                builder.show();
                 */

                return false;
            }
        }

        return true;
    }

    public boolean validateFields() {
        boolean isValid = true;

        if (guide_tongue_weight.getText().toString().isEmpty())
        {
            tongue_weight_error.setError(getString(R.string.text_error_1));
            isValid = false;
        }

        if (guide_axle_inch.getText().toString().isEmpty()) {
            axle_inch_error.setError(getString(R.string.text_error_1));
            isValid = false;
        }

        if (guide_spring_inch.getText().toString().isEmpty()) {
            spring_inch_error.setError(getString(R.string.text_error_1));
            isValid = false;
        }

        if (guide_trailer_inch.getText().toString().isEmpty()) {
            trailer_inch_error.setError(getString(R.string.text_error_1));
            isValid = false;
        }

        if (!isValid)
        {
            AppHelper.CustomToast(mContext, getString(R.string.text_error_1));
            return false;
        }

        double rearVal = Double.valueOf(guide_axle_inch.getText().toString());
        if (rearVal > 70) {
            axle_inch_error.setError(getString(R.string.text_error_rear_range));
            AppHelper.CustomToast(mContext, getString(R.string.text_error_rear_range));
            return false;
        }
        else
            axle_inch_error.setErrorEnabled(false);

        double SpringVal = Double.valueOf(guide_spring_inch.getText().toString());
        if (SpringVal > 32 || SpringVal < 24) {
            spring_inch_error.setError(getString(R.string.text_error_spring_Range));
            AppHelper.CustomToast(mContext, getString(R.string.text_error_spring_Range));
            return false;
        }
        else
            spring_inch_error.setErrorEnabled(false);

        boolean isValidTongue = confirmTongueWeight();

        if (!isValidTongue)
            return false;

        selectedConfiguration.setnTongueWeight(Double.valueOf(guide_tongue_weight.getText().toString()));
        selectedConfiguration.setnRearAxle(rearVal);
        selectedConfiguration.setnTowBallSpringBar(SpringVal);
        selectedConfiguration.setnTowBallTrailerAxle(Double.valueOf(guide_trailer_inch.getText().toString()));
        DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);
        return true;
    }

    public void init() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int widthInDP = dm.widthPixels-Math.round(30*dm.density);

        ViewGroup.LayoutParams params = step6_panel.getLayoutParams();
        params.width = widthInDP;
        step6_panel.setLayoutParams(params);
        System.out.println(dm.widthPixels);
    }

}
