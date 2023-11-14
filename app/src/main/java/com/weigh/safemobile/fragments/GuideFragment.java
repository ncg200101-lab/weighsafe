package com.weigh.safemobile.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.GuideActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {

    @BindView(R.id.skip_guide)
    TextView skip_guide;

    @BindView(R.id.start_guide)
    TextView start_guide;

    @BindView(R.id.img_start_guide)
    ImageView img_start_guide;

    final static String STR_LINK_VIDEO = "https://youtu.be/ABNtxJTER1A";

    public GuideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guide, container, false);
        ButterKnife.bind(this, v);
        skip_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        start_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(1);
            }
        });

        img_start_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity)getActivity()).setViewPager(1);
            }
        });
        return v;
    }

    // Watch video
    @OnClick(R.id.watch_video) void watchVideo(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(STR_LINK_VIDEO));
        startActivity(browserIntent);
    }

}
