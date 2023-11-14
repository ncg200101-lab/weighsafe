package com.weigh.safemobile.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {


    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ImageView back = view.findViewById(R.id.btn_back);
        TextView question_1 = view.findViewById(R.id.question_1);
        TextView question_2 = view.findViewById(R.id.question_2);
        TextView question_3 = view.findViewById(R.id.question_3);
        TextView question_4 = view.findViewById(R.id.question_4);
        TextView question_5 = view.findViewById(R.id.question_5);
        TextView question_6 = view.findViewById(R.id.question_6);
        TextView question_7 = view.findViewById(R.id.question_7);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(HelpFragment.newInstance());
            }
        });


        question_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(AnswerFragment.newInstance("question1"));

            }
        });

        question_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(AnswerFragment.newInstance("question2"));
            }
        });

        question_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(AnswerFragment.newInstance("question3"));
            }
        });

        question_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(AnswerFragment.newInstance("question4"));
            }
        });

        question_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(AnswerFragment.newInstance("question5"));
            }
        });

        question_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(AnswerFragment.newInstance("question6"));
            }
        });

        question_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(AnswerFragment.newInstance("question7"));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
