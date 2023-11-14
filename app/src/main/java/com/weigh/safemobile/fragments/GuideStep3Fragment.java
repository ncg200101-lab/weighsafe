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
import android.widget.RadioButton;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.GuideActivity;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.models.Configuration;


import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.WINDOW_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideStep3Fragment extends Fragment {

    @BindView(R.id.step_linearlayout)
    LinearLayout step_linearlayout;
    @BindView(R.id.guide_btn_rise)
    RadioButton guide_btn_rise;
    @BindView(R.id.guide_btn_drop)
    RadioButton guide_btn_drop;
    @BindView(R.id.guide_btn_minus)
    RadioButton guide_btn_minus;
    @BindView(R.id.guide_btn_plus)
    RadioButton guide_btn_plus;
    @BindView(R.id.guide_holes_content_text)
    TextView guide_holes_text;
    @BindView(R.id.close_guide)
    TextView close_guide;

    @BindView(R.id.img_back_page)
    ImageView imgBackPage;
    @BindView(R.id.go_back)
    TextView go_back;
    @BindView(R.id.img_next_page)
    ImageView imgNextPage;
    @BindView(R.id.go_next)
    TextView go_next;

    private Configuration selectedConfiguration;

    public GuideStep3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guide_step3, container, false);
        ButterKnife.bind(this, v);

        selectedConfiguration = MainApplication.getInstance().selectedConfiguration;
        initializerListener();
        init();
        return v;
    }

    public void initializerListener() {
        guide_btn_rise.setChecked(true);
        close_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GuideActivity)getActivity()).FinishActivity();
                close_guide.setClickable(false);
            }
        });

        guide_btn_rise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedConfiguration.setnDrawbarPosition(1);
                DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);
            }
        });

        guide_btn_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedConfiguration.setnDrawbarPosition(0);
                DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);
            }
        });

        guide_btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double holes = Double.parseDouble(guide_holes_text.getText().toString());
                if (holes <1) holes -= 0.5; else holes -= 1;
                if(holes<0) holes = 0.0;
                guide_holes_text.setText(String.valueOf(holes));

                selectedConfiguration.setnHolesVisible(holes);
                DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);
            }
        });

        guide_btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double holes = Double.parseDouble(guide_holes_text.getText().toString());
                if(holes>0) holes+=1; else holes += 0.5;
                if(holes>7.5) holes = 7.5;
                guide_holes_text.setText(String.valueOf(holes));
                selectedConfiguration.setnHolesVisible(holes);
                DatabaseManager.updateConfigurationModel(selectedConfiguration.getnRealmId(), selectedConfiguration);
            }
        });

        imgBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(2);
            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(2);
            }
        });

        imgNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(4);
            }
        });

        go_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(4);
            }
        });
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
