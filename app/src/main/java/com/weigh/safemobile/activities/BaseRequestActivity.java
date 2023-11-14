package com.weigh.safemobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.weigh.safemobile.helpers.VolleyHelper;

import java.util.ArrayList;

public class BaseRequestActivity extends AppCompatActivity {

    private ArrayList<VolleyHelper> volleyHelpers;

    @Override
    protected void onDestroy() {
        if(volleyHelpers != null && volleyHelpers.isEmpty()){
            for(VolleyHelper volleyHelper : volleyHelpers){
                volleyHelper.setCancelled(true);
            }
        }
        super.onDestroy();
    }

    public void addHelper(VolleyHelper volleyHelper){
        if(volleyHelpers == null){
            volleyHelpers = new ArrayList<>();
        }
        volleyHelpers.add(volleyHelper);
    }
}
