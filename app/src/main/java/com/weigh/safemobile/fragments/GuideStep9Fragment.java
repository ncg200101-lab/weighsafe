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

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.WINDOW_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideStep9Fragment extends Fragment {
    @BindView(R.id.step_linearlayout)
    LinearLayout step_linearlayout;

    @BindView(R.id.close_guide)
    TextView close_guide;

    @BindView(R.id.img_back_page)
    ImageView imgBackPage;
    @BindView(R.id.go_back)
    TextView go_back;

    public GuideStep9Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guide_step9, container, false);
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
                ((GuideActivity)getActivity()).setViewPager(8);
            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(8);
            }
        });

        init();
        return v;
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
