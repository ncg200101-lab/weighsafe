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
public class AnswerFragment extends Fragment {

    private static final String ARG_PARAM = "KEY";
    private String strKey;

    public AnswerFragment() {
        // Required empty public constructor
    }

    public static AnswerFragment newInstance(String strKey) {
        AnswerFragment fragment = new AnswerFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM, strKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strKey = getArguments().getString(ARG_PARAM);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answer, container, false);

        ImageView back = view.findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(new QuestionFragment());
            }
        });

        TextView answer_title = view.findViewById(R.id.answer_title);
        TextView answer_content = view.findViewById(R.id.answer_content);

        switch (strKey) {
            case "question1":
                answer_title.setText(R.string.question_1);
                answer_content.setText(R.string.answer_1);
                break;
            case "question2":
                answer_title.setText(R.string.question_2);
                answer_content.setText(R.string.answer_2);
                break;
            case "question3":
                answer_title.setText(R.string.question_3);
                answer_content.setText(R.string.answer_3);
                break;
            case "question4":
                answer_title.setText(R.string.question_4);
                answer_content.setText(R.string.answer_4);
                break;
            case "question5":
                answer_title.setText(R.string.question_5);
                answer_content.setText(R.string.answer_5);
                break;
            case "question6":
                answer_title.setText(R.string.question_6);
                answer_content.setText(R.string.answer_6);
                break;
            case "question7":
                answer_title.setText(R.string.question_7);
                answer_content.setText(R.string.answer_7);
                break;
        }

        System.out.println(strKey);
        return view;
    }

}
