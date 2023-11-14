package com.weigh.safemobile.helpers;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class OnFocusChangeListenerMin implements OnFocusChangeListener {

    private int min;

    public OnFocusChangeListenerMin(int min) {
        this.min = min;
    }

    public OnFocusChangeListenerMin(String min) {
        this.min = Integer.parseInt(min);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus) {
            String val = ((EditText)v).getText().toString();
            if(!TextUtils.isEmpty(val)){
                if(Integer.valueOf(val)<min){
                    //Notify user that the value is not good
                }

            }
        }
    }
}