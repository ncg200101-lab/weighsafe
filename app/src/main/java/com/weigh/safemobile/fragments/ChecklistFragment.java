package com.weigh.safemobile.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.MainActivity;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.helpers.AppHelper;
import com.weigh.safemobile.models.CheckListModel;
import com.weigh.safemobile.models.Configuration;
import com.weigh.safemobile.models.Pusher;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class ChecklistFragment extends Fragment {
    @BindView(R.id.text_checklist_title)
    TextView textChecklistTitle;
    @BindView(R.id.btn_reset)
    Button btnReset;

    @BindView(R.id.scrollview_checklist)
    ScrollView sv;

    @BindView(R.id.text_sub_2)
    TextView text_sub_2;
    @BindView(R.id.text_verify_vehicle_1)
    TextView text_verify_vehicle_1;
    @BindView(R.id.text_verify_vehicle_2)
    TextView text_verify_vehicle_2;
    @BindView(R.id.text_verify_vehicle_3)
    TextView text_verify_vehicle_3;
    @BindView(R.id.text_verify_vehicle_4)
    TextView text_verify_vehicle_4;
    @BindView(R.id.text_sub_3)
    TextView text_sub_3;
    @BindView(R.id.text_verify_weight)
    TextView text_verify_weight;
    @BindView(R.id.text_verify_distance)
    TextView text_verify_distance;
    @BindView(R.id.text_verify_all)
    TextView text_verify_all;
    @BindView(R.id.text_sub_4)
    TextView text_sub_4;
    @BindView(R.id.text_verify_tires)
    TextView text_verify_tires;
    @BindView(R.id.text_sub_5)
    TextView text_sub_5;
    @BindView(R.id.text_lights)
    TextView text_lights;
    @BindView(R.id.text_sub_6)
    TextView text_sub_6;
    @BindView(R.id.text_trailer_if_brake)
    TextView text_trailer_if_brake;
    @BindView(R.id.text_sub_7)
    TextView text_sub_7;
    @BindView(R.id.text_verify_side_view)
    TextView text_verify_side_view;

    //@BindView(R.id.img_check_hand)ImageView img_check_hand;
    @BindView(R.id.img_check_trailer_connection)
    ImageView img_check_trailer_connection;
    @BindView(R.id.img_check_load)
    ImageView img_check_load;
    @BindView(R.id.img_check_tires)
    ImageView img_check_tires;
    @BindView(R.id.img_check_lights)
    ImageView img_check_lights;
    @BindView(R.id.img_check_trailer_brakes)
    ImageView img_check_trailer_brakes;
    @BindView(R.id.img_check_mirrors)
    ImageView img_check_mirrors;


    View mView;
    private int selectedVehicleId = 0;
    public static ChecklistFragment newInstance() {
        ChecklistFragment fragment = new ChecklistFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (mView == null)
        {
            mView = inflater.inflate(R.layout.fragment_checklist, container, false);
            ButterKnife.bind(this, mView);
            initializerView();
        }

        return mView;
    }

    private void initializerView() {
        sv.setVerticalScrollBarEnabled(false);
        sv.setHorizontalScrollBarEnabled(false);

        textChecklistTitle.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        btnReset.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));

        text_sub_2.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_verify_vehicle_1.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_verify_vehicle_2.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_verify_vehicle_3.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_verify_vehicle_4.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_sub_3.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_verify_weight.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_verify_distance.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_verify_all.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_sub_4.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_verify_tires.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Medium"));
        text_sub_5.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_lights.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Medium"));
        text_sub_6.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_trailer_if_brake.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Medium"));
        text_sub_7.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_verify_side_view.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Medium"));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
            return;

        Configuration selectedConfig = MainApplication.getInstance().selectedConfiguration;
        selectedVehicleId = selectedConfig.getnRealmId();

        if (selectedVehicleId != -1) {
            textChecklistTitle.setText("Tow Checklist: " + selectedConfig.getName() );
            setCheckList();
        }
        else
            initCheckBox();
    }

    public void initCheckBox() {
        //img_check_hand.setImageResource(R.drawable.unselected);
        img_check_trailer_connection.setImageResource(R.drawable.unselected);
        img_check_load.setImageResource(R.drawable.unselected);
        img_check_tires.setImageResource(R.drawable.unselected);
        img_check_lights.setImageResource(R.drawable.unselected);
        img_check_trailer_brakes.setImageResource(R.drawable.unselected);
        img_check_mirrors.setImageResource(R.drawable.unselected);
        //text_hand_crank_weight.setText(R.string.text_lbs_lg);
    }



    public void setCheckList(){
        CheckListModel checkListModel = DatabaseManager.getCheckListModelByVehicleId(selectedVehicleId);

        if (checkListModel.isTrailerConnection()) {
            img_check_trailer_connection.setImageResource(R.drawable.selected);
        } else {
            img_check_trailer_connection.setImageResource(R.drawable.unselected);
        }

        if (checkListModel.isLoad()) {
            img_check_load.setImageResource(R.drawable.selected);
        } else {
            img_check_load.setImageResource(R.drawable.unselected);
        }

        if (checkListModel.isTires()) {
            img_check_tires.setImageResource(R.drawable.selected);
        } else {
            img_check_tires.setImageResource(R.drawable.unselected);
        }

        if (checkListModel.isLights()) {
            img_check_lights.setImageResource(R.drawable.selected);
        } else {
            img_check_lights.setImageResource(R.drawable.unselected);
        }

        if (checkListModel.isTrailerBrakes()) {
            img_check_trailer_brakes.setImageResource(R.drawable.selected);
        } else {
            img_check_trailer_brakes.setImageResource(R.drawable.unselected);
        }

        if (checkListModel.isMirrors()) {
            img_check_mirrors.setImageResource(R.drawable.selected);
        } else {
            img_check_mirrors.setImageResource(R.drawable.unselected);
        }
    }


    @OnClick(R.id.btn_reset)
    public void onClickReset(View v) {
        if(DatabaseManager.resetCheckListModelByVehicleId(selectedVehicleId)){
            setCheckList();
        }
    }

    @OnClick({R.id.rl_image_check_2, R.id.rl_image_check_3, R.id.rl_image_check_4, R.id.rl_image_check_5, R.id.rl_image_check_6, R.id.rl_image_check_7})
    public void onClickCheck(View v) {
        CheckListModel checkListModel = DatabaseManager.getCheckListModelByVehicleId(selectedVehicleId);

        switch (v.getId()) {

            case R.id.rl_image_check_2:
                if(checkListModel.isTrailerConnection()){
                    updateState(false, "img_check_trailer_connection");
                }
                else{
                    updateState(true, "img_check_trailer_connection");
                }
                break;
            case R.id.rl_image_check_3:
                if(checkListModel.isLoad()){
                    updateState(false, "img_check_load");
                }
                else{
                    updateState(true, "img_check_load");
                }
                break;
            case R.id.rl_image_check_4:
                if(checkListModel.isTires()){
                    updateState(false, "img_check_tires");
                }
                else{
                    updateState(true, "img_check_tires");
                }
                break;
            case R.id.rl_image_check_5:
                if(checkListModel.isLights()){
                    updateState(false, "img_check_lights");
                }
                else{
                    updateState(true, "img_check_lights");
                }
                break;
            case R.id.rl_image_check_6:
                if(checkListModel.isTrailerBrakes()){
                    updateState(false, "img_check_trailer_brakes");
                }
                else{
                    updateState(true, "img_check_trailer_brakes");
                }
                break;
            case R.id.rl_image_check_7:
                if(checkListModel.isMirrors()){
                    updateState(false, "img_check_mirrors");
                }
                else{
                    updateState(true, "img_check_mirrors");
                }
                break;
        }
    }

    public void updateState(Boolean state, String param){
        if(DatabaseManager.saveCheckListModel(selectedVehicleId, param, state))
            setCheckList();

        CheckListModel checkListModel = DatabaseManager.getCheckListModelByVehicleId(selectedVehicleId);
        if (checkListModel.isAllChecked())
        {
            PrettyDialog pDialog = new PrettyDialog(getActivity());
            pDialog.setIcon(R.drawable.pdlg_icon_success);
            pDialog.setIconTint(R.color.pdlg_color_green);
            pDialog.setTitle(getString(R.string.tow_congrats));
            pDialog.addButton(
                    "Ok",			// button text
                    R.color.colorWhite,		    // button text color
                    R.color.colorRed,		    // button background color
                    new PrettyDialogCallback() {	// button OnClick listener
                        @Override
                        public void onClick() {
                            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEXT_SETUP, 0));
                            ((MainActivity)getActivity()).selectFragmentByPager(1);
                            pDialog.dismiss();
                        }
                    });

            pDialog.setMessage(getString(R.string.tow_congrats_subtitle));
            pDialog.show();
        }
    }
}