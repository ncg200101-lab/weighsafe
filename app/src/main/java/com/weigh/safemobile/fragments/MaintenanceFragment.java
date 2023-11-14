package com.weigh.safemobile.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaintenanceFragment extends Fragment {


    public MaintenanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance, container, false);
        ImageView back = view.findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(HelpFragment.newInstance());
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


}
