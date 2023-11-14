package com.weigh.safemobile.fragments2;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.NewHitchActivity;
import com.weigh.safemobile.adapters.NewHitchAdapter;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Configuration;
import com.weigh.safemobile.models.Pusher;
import com.weigh.safemobile.models.SetupHitchModel;
import com.weigh.safemobile.presenters.GoHitchListener;
import com.weigh.safemobile.presenters.HitchUpdateListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HitchSetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HitchSetupFragment extends Fragment implements HitchUpdateListener, GoHitchListener {

    @BindView(R.id.root_scroll)
    NestedScrollView mScrollView;

    @BindView(R.id.recycler_hitch)
    RecyclerView hitchListView;

    @BindView(R.id.fab_new)
    ImageButton btnAddNew;

    @BindView(R.id.ll_no_hitches)
    LinearLayout ll_no_hitches;

    private NewHitchAdapter mHitchAdapter = null;

    private List<SetupHitchModel> models;
    private List<SetupHitchModel> mHitchList = new ArrayList<>();
    private View mView;

    private boolean viewDidAppear;
    //private boolean emptyList;

    public static HitchSetupFragment newInstance() {
        HitchSetupFragment fragment = new HitchSetupFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null)
        {
            mView = inflater.inflate(R.layout.fragment2_hitch_setup, container, false);
            ButterKnife.bind(this, mView);
            initializerView();
            initializerSave();
        }

        return mView;
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

        if (MainApplication.getInstance().mHitchNotify) {
            mHitchAdapter.notifyDataSetChanged();
            MainApplication.getInstance().mHitchNotify = false;
        }

        if (MainApplication.getInstance().mHitchWizard)
            return;

        if (viewDidAppear)
        {
            boolean isWizardOn = PreferenceManager.getWizardOn(getActivity());
            if (isWizardOn) {
                if (mHitchList.size() > 0)
                    showWizardDialog();
                else
                {
                    Intent intent = new Intent(getActivity(), NewHitchActivity.class);
                    intent.putExtra("editable", false);
                    MainApplication.getInstance().setHitchUpdateListener(HitchSetupFragment.this);
                    startActivity(intent);
                }
            } else
            {
                if (mHitchList.size() == 0)
                {
                    Intent intent = new Intent(getActivity(), NewHitchActivity.class);
                    intent.putExtra("editable", false);
                    MainApplication.getInstance().setHitchUpdateListener(HitchSetupFragment.this);
                    startActivity(intent);
                }
            }
            viewDidAppear = false;
        }
    }

    public void notifyAllData()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mHitchAdapter.notifyDataSetChanged();
            }
        }, 500);
    }

    private void initializerView() {
        //emptyList = false;
        viewDidAppear = true;
        MainApplication.getInstance().setGoHitchListener(this);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hitchListView.setLayoutManager(mLinearLayoutManager);
        hitchListView.setItemAnimator(new DefaultItemAnimator());
        hitchListView.getItemAnimator().setChangeDuration(0);

        //fix slow recyclerview start
        hitchListView.setHasFixedSize(true);
        hitchListView.setItemViewCacheSize(10);
        hitchListView.setDrawingCacheEnabled(true);
        hitchListView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        hitchListView.addItemDecoration(new DividerItemDecoration(hitchListView.getContext(), DividerItemDecoration.VERTICAL));
        ///fix slow recyclerview end

        fetchAllHitches();

        mHitchAdapter = new NewHitchAdapter(this,mHitchList);
        hitchListView.setAdapter(mHitchAdapter);

    }

    private void fetchAllHitches() {

        mHitchList.clear();

        models = MainApplication.getInstance().hitchModels;
        if (models == null || models.size() == 0)
            ll_no_hitches.setVisibility(View.VISIBLE);
        else
        {
            ll_no_hitches.setVisibility(View.GONE);
            mHitchList.addAll(models);
        }

        /*
        if (models == null || models.size() < 1)
        {
            emptyList = true;
            mHitchList.addAll(MainApplication.getInstance().defaultHitches);
        } else
        {
            emptyList = false;
            mHitchList.addAll(models);
        }

         */
    }

    public void initializerSave() {
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewHitchActivity.class);
                intent.putExtra("editable", false);
                MainApplication.getInstance().setHitchUpdateListener(HitchSetupFragment.this);
                startActivity(intent);
            }
        });

        hitchListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void showWizardDialog()
    {
        PrettyDialog pDialog = new PrettyDialog(getActivity());
        pDialog.setIcon(R.drawable.pdlg_icon_info);
        pDialog.setIconTint(R.color.pdlg_color_red);
        pDialog.setTitle(getString(R.string.text_wizard_hitch_title));
        pDialog.addButton(
                "Add Hitch",			// button text
                R.color.colorWhite,		    // button text color
                R.color.colorRed,		    // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(getActivity(), NewHitchActivity.class);
                        intent.putExtra("editable", false);
                        MainApplication.getInstance().setHitchUpdateListener(HitchSetupFragment.this);
                        startActivity(intent);
                        pDialog.dismiss();
                    }
                });
        pDialog.addButton(
                "Keep Hitch & Skip", // button text
                R.color.colorWhite,		    // button text color
                R.color.grey30,		        // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEXT_SETUP, 2));
                        pDialog.dismiss();
                    }
                });
        pDialog.setMessage(getString(R.string.text_wizard_hitch_message));
        pDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onEditDialogSave(int position, SetupHitchModel item) {
        //mHitchList.set(position, item);
        mHitchAdapter.updateItem(position, item);
    }

    @Override
    public void onNewDialogSave(SetupHitchModel item) {
        /*
        if (emptyList == true)
        {
            mHitchAdapter.removeAll();
            mHitchAdapter.addItem(item);
            models.add(item);
        }
        else
        {
            mHitchAdapter.addItem(item);
            models.add(item);
        }

        emptyList = false;
         */
        int itemCount = models.size();


        if (item.isOnSafeHitch() && item.getBrand().equals("True Tow Weight Distribution Hitch"))
            PreferenceManager.setInstallationGuide(getActivity(), true);

        if (PreferenceManager.getWizardOn(getActivity()))
        {
            PreferenceManager.setCurrentHitch(getActivity(), itemCount);

            int selectedPos = PreferenceManager.getPositionID(getActivity());
            Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
            currConfig.setnHitchIndex(itemCount);
            currConfig.setnHitchRealmId(item.getnRealmId());
            MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
            DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);
        }

        ll_no_hitches.setVisibility(View.GONE);
        mHitchAdapter.addItem(item);
        models.add(item);

        if (PreferenceManager.getWizardOn(getActivity()))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEXT_SETUP, 2));
                }
            }, 300);
        }
    }

    @Override
    public void onDeleteDialogSave(int position) {
        models.remove(position);

        int currHitchIndex = PreferenceManager.getCurrentHitch(getActivity());
        if (position < currHitchIndex)
        {
            PreferenceManager.setCurrentHitch(getActivity(), currHitchIndex - 1);

            int selectedPos = PreferenceManager.getPositionID(getActivity());
            Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
            currConfig.setnHitchIndex(currHitchIndex - 1);
            MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
            DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);
        } else if (position == currHitchIndex)
        {
            PreferenceManager.setCurrentHitch(getActivity(), 0);

            int selectedPos = PreferenceManager.getPositionID(getActivity());
            Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
            currConfig.setnHitchIndex(0);
            currConfig.setnHitchRealmId(models.get(0).getnRealmId());
            MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
            DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);
        }

        mHitchAdapter.removeItem(position);
        if (models.size() == 0)
            ll_no_hitches.setVisibility(View.VISIBLE);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Pusher pusher) {
        switch (pusher.getAction()) {
            case AppConstants.EVENT_BUS_ACTIVE_HITCH:
                /*
                if (models.size() == 0)
                {
                    SetupHitchModel _model = mHitchList.get(pusher.getSelectedPosition());
                    _model.setSampleItem(false);
                    _model.setOnSafeHitch(true);
                    _model = DatabaseManager.addNewHitchModel(_model);
                    models.add(_model);

                    int selectedPos = PreferenceManager.getPositionID(getActivity());

                    Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
                    currConfig.setnHitchIndex(0);
                    currConfig.setnHitchRealmId(_model.getnRealmId());
                    MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
                    DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);

                    PreferenceManager.setCurrentHitch(getActivity(), 0);
                    mHitchAdapter.removeAll();
                    mHitchAdapter.addItem(_model);
                    emptyList = false;

                    PrettyDialog pDialog = new PrettyDialog(getActivity());
                    pDialog.setIcon(R.drawable.pdlg_icon_success);
                    pDialog.setIconTint(R.color.pdlg_color_red);
                    pDialog.setTitle(getString(R.string.text_sample_hitch_saved));
                    pDialog.addButton(
                            "NEXT",					// button text
                            R.color.colorWhite,		// button text color
                            R.color.colorRed,		    // button background color
                            new PrettyDialogCallback() {	// button OnClick listener
                                @Override
                                public void onClick() {
                                    EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEXT_SETUP, 2));
                                    pDialog.dismiss();
                                }
                            });
                    pDialog.setMessage(_model.getBrand() + " has been added to your list. Manually activate the appropriate hitch by tapping the SET button or tap the name to edit the hitch options.");
                    pDialog.show();
                } else
                {
                 */
                    PreferenceManager.setCurrentHitch(getActivity(), pusher.getSelectedPosition());

                    int selectedPos = PreferenceManager.getPositionID(getActivity());
                    Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
                    currConfig.setnHitchIndex(pusher.getSelectedPosition());
                    currConfig.setnHitchRealmId(pusher.get_hitchModel().getnRealmId());

                    SetupHitchModel item = pusher.get_hitchModel();
                    if (item.getBrand().equals("True Tow Weight Distribution Hitch"))
                        currConfig.setnHolesVisible(0.5);
                    else
                        currConfig.setnHolesVisible(0);

                    MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
                    DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);
                    //mHitchAdapter.notifyItemChanged(pusher.getSelectedPosition());
                    mHitchAdapter.notifyDataSetChanged();
                //}
                break;
        }
    }

    @Override
    public void onWizardCheck() {
        boolean isWizardOn = PreferenceManager.getWizardOn(getActivity());
        if (isWizardOn) {
            if (mHitchList.size() > 0)
                showWizardDialog();
            else
            {
                Intent intent = new Intent(getActivity(), NewHitchActivity.class);
                intent.putExtra("editable", false);
                MainApplication.getInstance().setHitchUpdateListener(HitchSetupFragment.this);
                startActivity(intent);
            }
        } else
        {
            if (mHitchList.size() == 0)
            {
                Intent intent = new Intent(getActivity(), NewHitchActivity.class);
                intent.putExtra("editable", false);
                MainApplication.getInstance().setHitchUpdateListener(HitchSetupFragment.this);
                startActivity(intent);
            }
        }

        viewDidAppear = false;
        MainApplication.getInstance().mHitchWizard = false;
    }
}