package com.weigh.safemobile.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.weigh.safemobile.R;
import com.weigh.safemobile.adapters.VehicleAdapter;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.helpers.AppHelper;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Pusher;
import com.weigh.safemobile.models.SetupModel;
import com.weigh.safemobile.models.VehiclesModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;

import java.lang.Math;
import java.lang.Double;
public class SetupFragment extends Fragment {

    @BindView(R.id.setup_panel)
    RelativeLayout setup_panel;

    @BindView(R.id.text_vehicle_title)
    TextView textVehicleTitle;

    @BindView(R.id.scrollview_setup)
    ScrollView sv;

    @BindView(R.id.text_current_load)
    TextView text_current_load;
    @BindView(R.id.text_lbs)
    TextView text_lbs;
    @BindView(R.id.text_sub_1)
    TextView text_sub_1;

    @BindView(R.id.trailerError)
    TextInputLayout trailerError;

    @BindView(R.id.input_trailer_weight)
    EditText input_trailer_weight; /*****/

    @BindView(R.id.text_sub_2)
    TextView text_sub_2;

    @BindView(R.id.tongueError)
    TextInputLayout tongueError;

    @BindView(R.id.input_tongue_weight)
    EditText input_tongue_weight;  /*****/

    @BindView(R.id.btn_dropdown)
    ImageView btnDropdown;
    @BindView(R.id.vehicleListView)
    RelativeLayout vehicleListView;
    @BindView(R.id.vehiclesList)
    RecyclerView vehiclesList;
    @BindView(R.id.addText)
    TextView addText;
    @BindView(R.id.text_safe_tongue_weight_range)
    TextView text_safe_tongue_weight_range;
    @BindView(R.id.text_safe_tongue_weight)
    TextView text_safe_tongue_weight;
    @BindView(R.id.text_tongue_weight_verify)
    TextView text_tongue_weight_verify;
    @BindView(R.id.rl_confirm)
    RelativeLayout rl_confirm;

    @BindView(R.id.target_result)
    TextView targetResult;

    @BindView(R.id.min_result)
    TextView minResult;

    @BindView(R.id.max_result)
    TextView maxResult;

    @BindView(R.id.calculate)
    Button calculate;

    @BindView(R.id.position_res)
    TextView position_res;
    @BindView(R.id.holes_res)
    TextView holes_res;

    private VehicleAdapter mVehicleAdapter = null;
    private RealmList<VehiclesModel> mVehicleList = new RealmList<>();
    private RealmList<SetupModel> mSetupList = new RealmList<>();
    private View mView;
    private VehiclesModel selectedVehiclesModel = new VehiclesModel();

    public static SetupFragment newInstance() {
        SetupFragment fragment = new SetupFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_setup, container, false);
        ButterKnife.bind(this, mView);
        initializerView();
        initializerSave();
        return mView;
    }

    private void initializerView() {
        sv.setVerticalScrollBarEnabled(false);
        sv.setHorizontalScrollBarEnabled(false);

        textVehicleTitle.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        addText.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));

        text_current_load.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_lbs.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_sub_1.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        input_trailer_weight.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Medium"));
        text_sub_2.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        input_tongue_weight.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Medium"));

        text_safe_tongue_weight.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_safe_tongue_weight_range.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Bold"));
        text_tongue_weight_verify.setTypeface(AppHelper.setTypeFace(getActivity(), "Roboto-Medium"));

        targetResult.setTypeface(null, Typeface.BOLD);
        maxResult.setTypeface(null, Typeface.BOLD);
        minResult.setTypeface(null, Typeface.BOLD);

        rl_confirm.setVisibility(View.GONE);
        /** add vehicle and list view init */

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        setHasOptionsMenu(true);
        vehiclesList.setLayoutManager(mLinearLayoutManager);
        vehiclesList.setItemAnimator(new DefaultItemAnimator());
        vehiclesList.getItemAnimator().setChangeDuration(0);

        //fix slow recyclerview start
        vehiclesList.setHasFixedSize(true);
        vehiclesList.setItemViewCacheSize(10);
        vehiclesList.setDrawingCacheEnabled(true);
        vehiclesList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        ///fix slow recyclerview end

        /*******/
        mVehicleList = DatabaseManager.getAllVehicle();
        mVehicleAdapter = new VehicleAdapter(mVehicleList);
        vehiclesList.setAdapter(mVehicleAdapter);
//        vehiclesList.swapAdapter(mVehicleAdapter, true);

        /** setup model **/
        mSetupList = DatabaseManager.getAllSetup();

        if (mVehicleList.size() == 0){
            mVehicleList.add(DatabaseManager.addNewVehicle("My Vehicle", "Trailer", 0));
        }

        /** set label in setup header*/
        if(mVehicleList.size() > 0) {
            setVehicleTitle(new VehiclesModel(mVehicleList.get(PreferenceManager.getPositionID(getActivity()))));
        }
        else{
            textVehicleTitle.setText(R.string.text_dummy_vehicle_title);
            initEditView();
        }

//        int vehicleId = selectedVehiclesModel.getnRealmId();
//        SetupModel model = DatabaseManager.getSetupModelByVehicleId(vehicleId);
//        calculateCrankWeight(model);
    }

    public void initializerSave(){
        setup_panel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(view);
                return false;
            }
        });

        input_trailer_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input_trailer_weight.getText().toString().isEmpty()) {
                    trailerError.setError(getString(R.string.text_error_1));
                }
                else
                    trailerError.setErrorEnabled(false);
            }
        });

        input_trailer_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (input_trailer_weight.getText().toString().isEmpty()) {
                        trailerError.setError(getString(R.string.text_error_1));
                    }
                    else
                    {
                        trailerError.setErrorEnabled(false);
                        saveValue("input_trailer_weight");
                    }
                }
            }
        });

        input_tongue_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input_tongue_weight.getText().toString().isEmpty()) {
                    tongueError.setError(getString(R.string.text_error_1));
                }
                else
                {
                    tongueError.setErrorEnabled(false);
                }
            }
        });

        input_tongue_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    confirmFunction();
                }
                else {
                    if (input_tongue_weight.getText().toString().isEmpty()) {
                        tongueError.setError(getString(R.string.text_error_1));
                    }
                    else
                    {
                        tongueError.setErrorEnabled(false);
                    }
                }
                //if (!hasFocus) {
                    //saveValue("input_tongue_weight");
                    //validateFunction();
                //}
            }
        });

        input_tongue_weight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do your stuff here
                    if (input_tongue_weight.getText().toString().isEmpty())
                        tongueError.setError(getString(R.string.text_error_1));
                    else
                    {
                        tongueError.setErrorEnabled(false);
                        saveValue("input_tongue_weight");
                        validateFunction();
                    }
                }
                return false;
            }
        });

    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void confirmFunction(){
        if( !input_trailer_weight.getText().toString().trim().isEmpty()){
            rl_confirm.setVisibility(View.VISIBLE);
            Double trailer_weight = Double.parseDouble(input_trailer_weight.getText().toString());

            long min = Math.round(trailer_weight*0.1);
            long max = Math.round(trailer_weight * 0.15);

            String temp = min + ".0 - " + max + ".0 lbs";
            text_safe_tongue_weight.setText(temp);
        }
        else{
            rl_confirm.setVisibility(View.GONE);
        }
    }

    private void validateFunction(){
        if( !input_trailer_weight.getText().toString().trim().isEmpty() && !input_tongue_weight.getText().toString().trim().isEmpty() ){
            Double trailer_weight = Double.parseDouble(input_trailer_weight.getText().toString());
            Double tongue_weight = Double.parseDouble(input_tongue_weight.getText().toString());

            long min = Math.round(trailer_weight * 0.1);
            long max = Math.round(trailer_weight * 0.15);
            saveValue("input_tongue_weight");
            if(tongue_weight >= min && tongue_weight <= max){
                //saveValue("input_tongue_weight");
            }
            else{
                input_tongue_weight.setError(getString(R.string.text_error_2));

                String message = "Your tongue weight should be between "+ min +".0 lbs and "+ max + ".0 lbs to ensure a safe tow";
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Unsafe Tongue Weight");
                builder.setMessage(message);
                builder.setPositiveButton("Ok", (dialog, whichButton) -> {
                    //input_tongue_weight.setText("");
                });

                builder.show();

            }
        }

    }

    public void saveValue(String param){

        int vehicleId = selectedVehiclesModel.getnRealmId();
        String value = "";

        if(vehicleId == 0){
            AppHelper.CustomToast(getActivity(), "Please select vehicle.");
            return;
        }

        switch (param) {
            case "input_trailer_weight":
                value = input_trailer_weight.getText().toString().isEmpty() ? "" : input_trailer_weight.getText().toString();
                break;
            case "input_tongue_weight":
                value = input_tongue_weight.getText().toString().isEmpty() ? "" : input_tongue_weight.getText().toString();
                break;
        }

        Boolean isSuccess = DatabaseManager.saveSetupModel(vehicleId, param, value);
        if(isSuccess)
            AppHelper.CustomToast(getActivity(), "The data was successfully saved.");
        else
            AppHelper.CustomToast(getActivity(), "Save failed");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (mVehicleAdapter != null) {
            mVehicleAdapter.saveStates(outState);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Only if you need to restore open/close state when
        // the orientation is changed
        if (mVehicleAdapter != null) {
            mVehicleAdapter.restoreStates(savedInstanceState);
        }
    }

    private void setVehicleTitle(VehiclesModel vehiclesModel){
        setPosition(vehiclesModel);

        int vehicleId = vehiclesModel.getnRealmId();
        SetupModel _setup = DatabaseManager.getSetupModelByVehicleId(vehicleId);

        String _label = vehiclesModel.getStrVehicle()+" + "+vehiclesModel.getStrTrailer();
        textVehicleTitle.setText(_label);

        if(_setup !=  null){
            position_res.setText(_setup.getnDrawbarPosition());
            holes_res.setText(_setup.getnHoles());
            input_trailer_weight.setText(_setup.getnTrailerWeight());
            input_tongue_weight.setText(_setup.getnTongueWeight());
        }
        else{
            initEditView();
        }

        confirmFunction();
    }

    private void initEditView(){
        input_trailer_weight.setText("");
        input_tongue_weight.setText("");
    }

    @OnClick(R.id.btn_dropdown)
    public void onClickBtnDropdownMenu(View v){
        if (vehicleListView.getVisibility() == View.VISIBLE) {
            vehicleListView.setVisibility(View.GONE);
            btnDropdown.setImageResource(R.drawable.drop_down);
        } else {
            btnDropdown.setImageResource(R.drawable.drop_up);
            vehicleListView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.img_sub_1, R.id.img_sub_2})
    public void onClickInfo(View v){
        DetailModalFragment dialog = null;
        switch (v.getId()){
            case R.id.img_sub_1:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_estimated_gross_trailer_weight), R.drawable.gross_trailer_weight);
                break;
            case R.id.img_sub_2:
                dialog = DetailModalFragment.newInstance(getString(R.string.text_tongue_weight), R.drawable.tongue_weight);
                break;
//            case R.id.img_sub_3:
//                dialog = DetailModalFragment.newInstance(getString(R.string.text_rear_axle_to_end_of_receiver), R.drawable.variable_b_topview);
//                break;
//            case R.id.img_sub_4:
//                dialog = DetailModalFragment.newInstance(getString(R.string.text_tow_ball_to_end_of_spring_bars), R.drawable.variable_c_topview);
//                break;
//            case R.id.img_sub_5:
//                dialog = DetailModalFragment.newInstance(getString(R.string.text_tow_ball_to_trailer_axle), R.drawable.variable_d_topview);
//                break;
        }
        dialog.show(getFragmentManager(), "detailModal");
    }

    @OnClick(R.id.calculate)
    public void onCalc(View v) {
        int vehicleId = selectedVehiclesModel.getnRealmId();
        if(vehicleId == 0){
            AppHelper.CustomToast(getActivity(), "Please select vehicle.");
            return;
        }

        boolean isValid = true;

        if (input_trailer_weight.getText().toString().isEmpty())
        {
            trailerError.setError(getString(R.string.text_error_1));
            isValid = false;
        }

        if (input_tongue_weight.getText().toString().isEmpty())
        {
            tongueError.setError(getString(R.string.text_error_1));
            isValid = false;
        }

        if (!isValid)
        {
            AppHelper.CustomToast(getActivity(), getString(R.string.text_error_1));
            return;
        }
        else
        {
            trailerError.setErrorEnabled(false);
            tongueError.setErrorEnabled(false);
        }

        validateFunction();
        //input_tongue_weight.clearFocus();
        calculate.setFocusable(true);

        SetupModel model = DatabaseManager.getSetupModelByVehicleId(vehicleId);
        calculateCrankWeight(model);
    }

    public void calculateCrankWeight(SetupModel model){
        Double towBallToEndOfSpringBars = model.getnSpringBar() == "" || model.getnSpringBar().trim().isEmpty() == true ? 0 : Double.parseDouble(model.getnSpringBar());
        Double towBallToTrailerAxle = model.getnTrailerAxle() == "" || model.getnTrailerAxle().trim().isEmpty() == true ? 0 : Double.parseDouble(model.getnTrailerAxle());
        Double rearAxleToEndOfReceiver = model.getnRearAxle() == "" || model.getnRearAxle().trim().isEmpty() == true ? 0 : Double.parseDouble(model.getnRearAxle());
        Double tongueWeight = model.getnTongueWeight() == "" || model.getnTongueWeight().trim().isEmpty() == true ? 0 : Double.parseDouble(model.getnTongueWeight());

        if( towBallToEndOfSpringBars != 0 && towBallToTrailerAxle != 0 && rearAxleToEndOfReceiver != 0 && tongueWeight != 0 ){
            Double nominator = 1 - towBallToEndOfSpringBars/ towBallToTrailerAxle;
            Double denominator = towBallToEndOfSpringBars / rearAxleToEndOfReceiver + towBallToEndOfSpringBars / towBallToTrailerAxle;
            Double rate = 1 + (nominator / denominator);
            int FALR = (int)Math.round(tongueWeight * rate);

            int targetRes = 0;
            int minRes = 0;
            int maxRes = 0;

            if (tongueWeight >= 0 && tongueWeight <500)
            {
                minRes = (int)(Math.ceil(FALR * 0.95 / 50) * 50);
                targetRes = (int)(Math.ceil(FALR * 1.0 / 50) *50);
                maxRes = (int)(Math.ceil(FALR * 1.05 / 50) * 50);
            } else if (tongueWeight >= 500 && tongueWeight < 1000)
            {
                minRes = (int)(Math.ceil(FALR * 0.85 / 50) * 50);
                targetRes = (int)(Math.ceil(FALR * 0.9 / 50) * 50);
                maxRes =  (int)(Math.ceil(FALR * 0.95 / 50) * 50);
            } else if (tongueWeight >= 1000 && tongueWeight < 1500)
            {
                minRes = (int)(Math.ceil(FALR * 0.75 / 50) * 50);
                targetRes = (int)(Math.ceil(FALR * 0.8 / 50) * 50);
                maxRes =  (int)(Math.ceil(FALR * 0.85 / 50) * 50);
            } else if (tongueWeight >= 1500)
            {
                minRes = (int)(Math.ceil(FALR * 0.7 / 50) * 50);
                targetRes = (int)(Math.ceil(FALR * 0.75 / 50) * 50);
                maxRes = (int)(Math.ceil(FALR * 0.8 / 50) * 50);
            } 

            //String targetRes = String.valueOf(DTW_res) + " lbs";
            targetResult.setText(String.valueOf(targetRes));
            minResult.setText(String.valueOf(minRes));
            maxResult.setText(String.valueOf(maxRes));
            //text_hand_crank_weight.setText(view);
        }
        else{
            AppHelper.CustomToast(getActivity(), "Unable to calculate Distributed Tongue Weight. Vehicle setup is not completed.");
            //text_hand_crank_weight.setText(R.string.text_lbs_lg);
        }
    }

    @OnClick(R.id.addVehicle)
    public void onClickAddVehicle(View v){
        disableDropdown();
        initEditView();
//        DialogFragment dialog = new ModalFragment();
//        dialog.show(getActivity().getSupportFragmentManager(), "ModalFragment");
        FragmentTransaction transaction = (getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString("key", "new");
        fragment.setArguments(args);
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    public void updateVehicle(VehiclesModel model){
        disableDropdown();
        //DialogFragment dialog = ModalFragment.newInstance(model.getStrVehicle(), model.getStrTrailer(), model.getnRealmId());
        //dialog.show(getActivity().getSupportFragmentManager(), "ModalFragment");
    }

    public void disableDropdown(){
        vehicleListView.setVisibility(View.GONE);
        btnDropdown.setImageResource(R.drawable.icon_dropdown);
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void setPosition(VehiclesModel positionModel){
        targetResult.setText("---");
        minResult.setText("---");
        maxResult.setText("---");

        for(int i = 0 ; i < mVehicleList.size() ; i++){
            if(mVehicleList.get(i).getnRealmId() == positionModel.getnRealmId()){
                PreferenceManager.setPositionID(getActivity(), i);
                selectedVehiclesModel = new VehiclesModel(positionModel);
            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Pusher pusher) {
        switch (pusher.getAction()) {
            case AppConstants.EVENT_BUS_SELECTED_VEHICLE_POSITION:
                VehiclesModel _mVehicleModel = pusher.getVehiclesModel();
                setVehicleTitle(_mVehicleModel);
                disableDropdown();

                Log.e("TAG", " selete size => "+mVehicleList.size());
                break;
            case AppConstants.EVENT_BUS_ADD_VEHICLE:
                if(pusher.getVehiclesModel() != null){
                    AppHelper.CustomToast(getActivity(), "The data was successfully saved.");
                    mVehicleAdapter.addItem(pusher.getVehiclesModel());
                    setVehicleTitle(pusher.getVehiclesModel());
                }
                else
                    AppHelper.CustomToast(getActivity(), "Save failed");

                Log.e("TAG", " add size => "+mVehicleList.size());
                break;
            case AppConstants.EVENT_BUS_EDIT_VEHICLE:
                if(pusher.getVehiclesModel() != null){
                    AppHelper.CustomToast(getActivity(), "The data was successfully saved.");
                    mVehicleAdapter.updateItem(pusher.getVehiclesModel());
                    setVehicleTitle(pusher.getVehiclesModel());
                }
                else
                    AppHelper.CustomToast(getActivity(), "Save failed");

                Log.e("TAG", " update size => "+mVehicleList.size());
                break;
            case AppConstants.EVENT_BUS_UPDATE_VEHICLE:
                updateVehicle(pusher.getVehiclesModel());
                break;
            case AppConstants.EVENT_BUS_DEL_VEHICLE:
                vehiclesList.requestLayout();
//                disableDropdown();
                break;
        }
    }
}
