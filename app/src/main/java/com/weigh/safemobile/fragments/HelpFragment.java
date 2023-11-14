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
import com.weigh.safemobile.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {
    private TextView guide;
    private TextView guideVideo;
    private TextView maintenance;
    private TextView warranty;
    private TextView question;
    private TextView contact;
    private ImageView btnBack;

    final static String STR_LINK_VIDEO = "https://youtu.be/ABNtxJTER1A";

    public HelpFragment() {
        // Required empty public constructor

    }

    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        guide = (TextView) view.findViewById(R.id.help_guide);
        guideVideo = (TextView) view.findViewById(R.id.help_video);
        maintenance = (TextView) view.findViewById(R.id.help_maintenance);
        warranty = (TextView) view.findViewById(R.id.help_warranty);
        contact = (TextView) view.findViewById(R.id.help_contact);
        question = (TextView) view.findViewById(R.id.help_ask);
        btnBack = (ImageView) view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed();
            }
        });


        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getActivity(), GuideActivity.class);
                mIntent.putExtra("fromActivity", "Help");
                startActivity(mIntent);
                //getActivity().finish();
            }
        });

        guideVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(STR_LINK_VIDEO));
                startActivity(browserIntent);
            }
        });

        maintenance.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(new MaintenanceFragment());
            }
        });

        warranty.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(new WarrantyFragment());
            }
        });

        contact.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(new ContactFragment());
            }
        });

        question.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(new QuestionFragment());
            }
        });

        return view;
    }

    public void backPressed() {
        ((MainActivity)getActivity()).restoreFragment();
    }
}
