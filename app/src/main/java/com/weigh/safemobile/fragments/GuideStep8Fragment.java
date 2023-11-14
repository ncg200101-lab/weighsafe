package com.weigh.safemobile.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.GuideActivity;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.helpers.AppHelper;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Configuration;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.WINDOW_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideStep8Fragment extends Fragment{
    @BindView(R.id.step_linearlayout)
    LinearLayout step_linearlayout;

    @BindView(R.id.close_guide)
    TextView close_guide;
    @BindView(R.id.guide_result)
    TextView guide_result;

    @BindView(R.id.ll_calculate_box)
    LinearLayout ll_calculate_box;
    @BindView(R.id.val_min_weight)
    TextView input_min_weight;
    @BindView(R.id.val_safe_weight)
    TextView input_safe_weight;
    @BindView(R.id.val_max_weight)
    TextView input_max_weight;

    @BindView(R.id.img_back_page)
    ImageView imgBackPage;
    @BindView(R.id.go_back)
    TextView go_back;
    @BindView(R.id.img_next_page)
    ImageView imgNextPage;
    @BindView(R.id.go_next)
    TextView go_next;

    private Configuration selectedConfiguration;

//    @BindView(R.id.guide_vehicle_name)
//    EditText guide_vehicle_name;
//    @BindView(R.id.guide_trailer_name)
//    EditText guide_trailer_name;
//    @BindView(R.id.guide_trailer_weight)
//    EditText guide_trailer_weight;
//    @BindView(R.id.guide_tongue_weight)
//    EditText guide_tongue_weight;
//    @BindView(R.id.guide_axle_inch)
//    EditText guide_axle_inch;
//    @BindView(R.id.guide_spring_inch)
//    EditText guide_spring_inch;
//    @BindView(R.id.guide_trailer_inch)
//    EditText guide_trailer_inch;

    View v;
    public GuideStep8Fragment() {
        // Required empty public constructor
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ll_calculate_box.setVisibility(View.GONE);
            displayResult();
            // User is viewing the fragment,
            // or fragment is inside the screen
            System.out.println("*************focus***************");
        }
        else {
            // User is not viewing the fragment,
            // or fragment is our of the screen
            System.out.println("----------unfocus------------");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_guide_step8, container, false);
        ButterKnife.bind(this, v);


        close_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GuideActivity)getActivity()).FinishActivity();
                close_guide.setClickable(false);
            }
        });

        imgBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(7);
            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(7);
            }
        });

        imgNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(9);
            }
        });

        go_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(9);
            }
        });

        selectedConfiguration = MainApplication.getInstance().selectedConfiguration;
        ll_calculate_box.setVisibility(View.GONE);
        displayResult();
        init();
        return v;
    }



    public void displayResult() {
        GuideActivity mParent = (GuideActivity)getActivity();

        calculateCrankWeight();
    }

    public void calculateCrankWeight(){
        GuideActivity mParent = (GuideActivity)getActivity();

        Double towBallToEndOfSpringBars = selectedConfiguration.getnTowBallSpringBar();
        Double towBallToTrailerAxle = selectedConfiguration.getnTowBallTrailerAxle();
        Double rearAxleToEndOfReceiver = selectedConfiguration.getnRearAxle();
        Double tongueWeight = selectedConfiguration.getnTongueWeight();

        if( towBallToEndOfSpringBars != 0 && towBallToTrailerAxle != 0 && rearAxleToEndOfReceiver != 0 && tongueWeight != 0 ){
            Double nominator = 1 - towBallToEndOfSpringBars/ towBallToTrailerAxle;
            Double denominator = towBallToEndOfSpringBars / rearAxleToEndOfReceiver + towBallToEndOfSpringBars / towBallToTrailerAxle;
            Double rate = 1 + (nominator / denominator);

            double minPercent = 0.95;
            double targetPercent = 1;
            double maxPercent = 1.05;

            if (tongueWeight >= 500 && tongueWeight <= 999) {
                minPercent = 0.85;
                targetPercent = 0.90;
                maxPercent = 0.95;
            } else if (tongueWeight >= 1000 && tongueWeight <= 1499) {
                minPercent = 0.75;
                targetPercent = 0.80;
                maxPercent = 0.85;
            } else if (tongueWeight >= 1499) {
                minPercent = 0.70;
                targetPercent = 0.75;
                maxPercent = 0.80;
            }

            guide_result.setVisibility(View.GONE);
            ll_calculate_box.setVisibility(View.VISIBLE);

            int FALR = (int)Math.round(tongueWeight * rate);

            int minRes = (int)(Math.ceil(FALR * minPercent /50) * 50);
            input_min_weight.setText(String.valueOf(minRes));

            int targetRes = (int)(Math.ceil(FALR * targetPercent /50) * 50);
            input_safe_weight.setText(String.valueOf(targetRes));

            int maxRes = (int)(Math.ceil(FALR * maxPercent /50) * 50);
            input_max_weight.setText(String.valueOf(maxRes));

            PreferenceManager.setInstallationGuide(getActivity(), false);
            /*int DTW_res = ((int)FALR/50+1) * 50;
            String res = String.valueOf(DTW_res) + " lbs";
             */
        }
        else{
            guide_result.setVisibility(View.VISIBLE);
            ll_calculate_box.setVisibility(View.GONE);
            AppHelper.CustomToast(getActivity(), "Unable to calculate Distributed Tongue Weight. All fields in previous page must be filled in.");
        }
    }
    public void init() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int widthInDP = dm.widthPixels-Math.round(30*dm.density);

        ViewGroup.LayoutParams params = step_linearlayout.getLayoutParams();
        params.width = widthInDP;
        step_linearlayout.setLayoutParams(params);
        System.out.println(dm.widthPixels);
    }

}
