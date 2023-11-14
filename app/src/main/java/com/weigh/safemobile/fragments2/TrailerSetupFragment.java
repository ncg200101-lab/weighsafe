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
import com.weigh.safemobile.activities.NewTrailerActivity;
import com.weigh.safemobile.adapters.NewTrailerAdapter;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Configuration;
import com.weigh.safemobile.models.Pusher;
import com.weigh.safemobile.models.SetupHitchModel;
import com.weigh.safemobile.models.SetupTrailerModel;
import com.weigh.safemobile.presenters.GoTrailerListener;
import com.weigh.safemobile.presenters.TrailerUpdateListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrailerSetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrailerSetupFragment extends Fragment implements TrailerUpdateListener, GoTrailerListener {

    @BindView(R.id.root_scroll)
    NestedScrollView mScrollView;

    @BindView(R.id.recycler_trailer)
    RecyclerView trailerListView;

    @BindView(R.id.fab_new)
    ImageButton btnAddNew;

    @BindView(R.id.ll_no_trailers)
    LinearLayout ll_no_trailers;

    private NewTrailerAdapter mTrailerAdapter = null;

    private List<SetupTrailerModel> models;
    private List<SetupTrailerModel> mTrailerList = new ArrayList<>();
    private View mView;

    static List<String> sizeOptions = Arrays.asList(new String[] {
            "2.3125",
            "2",
            "1.875"

    });

    private boolean viewDidAppear;

    public static TrailerSetupFragment newInstance() {
        TrailerSetupFragment fragment = new TrailerSetupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null)
        {
            mView = inflater.inflate(R.layout.fragment2_trailer_setup, container, false);
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

        if (MainApplication.getInstance().mTrailerNotify) {
            mTrailerAdapter.notifyDataSetChanged();
            MainApplication.getInstance().mTrailerNotify = false;
        }

        if (MainApplication.getInstance().mTrailerWizard)
            return;

        if (viewDidAppear)
        {
            boolean isWizardOn = PreferenceManager.getWizardOn(getActivity());
            if (isWizardOn) {
                if (mTrailerList.size() > 0)
                    showWizardDialog();
                else
                {
                    Intent intent = new Intent(getActivity(), NewTrailerActivity.class);
                    intent.putExtra("editable", false);
                    MainApplication.getInstance().setTrailerUpdateListener(TrailerSetupFragment.this);
                    startActivity(intent);
                }
            } else
            {
                if (mTrailerList.size() == 0)
                {
                    Intent intent = new Intent(getActivity(), NewTrailerActivity.class);
                    intent.putExtra("editable", false);
                    MainApplication.getInstance().setTrailerUpdateListener(TrailerSetupFragment.this);
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
                mTrailerAdapter.notifyDataSetChanged();
            }
        }, 500);
    }

    private void initializerView() {
        //emptyList = false;
        viewDidAppear = true;
        MainApplication.getInstance().setGoTrailerListener(this);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        trailerListView.setLayoutManager(mLinearLayoutManager);
        trailerListView.setItemAnimator(new DefaultItemAnimator());
        trailerListView.getItemAnimator().setChangeDuration(0);

        //fix slow recyclerview start
        trailerListView.setHasFixedSize(true);
        trailerListView.setItemViewCacheSize(10);
        trailerListView.setDrawingCacheEnabled(true);
        trailerListView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        trailerListView.addItemDecoration(new DividerItemDecoration(trailerListView.getContext(), DividerItemDecoration.VERTICAL));
        ///fix slow recyclerview end

        fetchAllTrailers();

        mTrailerAdapter = new NewTrailerAdapter(this, mTrailerList);
        trailerListView.setAdapter(mTrailerAdapter);

    }

    private void fetchAllTrailers() {
        mTrailerList.clear();

        models = MainApplication.getInstance().trailerModels;

        if (models == null || models.size() == 0)
            ll_no_trailers.setVisibility(View.VISIBLE);
        else
        {
            ll_no_trailers.setVisibility(View.GONE);
            mTrailerList.addAll(models);
        }

        /*
        if (models == null || models.size() < 1)
        {
            emptyList = true;
            mTrailerList.addAll(MainApplication.getInstance().defaultTrailers);
        } else
        {
            emptyList = false;
            mTrailerList.addAll(models);
        }
         */
    }

    public void initializerSave() {
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewTrailerActivity.class);
                intent.putExtra("editable", false);
                MainApplication.getInstance().setTrailerUpdateListener(TrailerSetupFragment.this);
                startActivity(intent);
            }
        });

        trailerListView.setOnTouchListener(new View.OnTouchListener() {
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
        pDialog.setTitle(getString(R.string.text_wizard_trailer_title));
        pDialog.addButton(
                "Add Trailer",			// button text
                R.color.colorWhite,		    // button text color
                R.color.colorRed,		    // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(getActivity(), NewTrailerActivity.class);
                        intent.putExtra("editable", false);
                        MainApplication.getInstance().setTrailerUpdateListener(TrailerSetupFragment.this);
                        startActivity(intent);
                        pDialog.dismiss();
                    }
                });
        pDialog.addButton(
                "Keep Trailer & Skip", // button text
                R.color.colorWhite,		    // button text color
                R.color.grey30,		        // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        PreferenceManager.setWizardOn(getActivity(), false);

                        MainApplication.getInstance().mCongrats = false;
                        EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEXT_SETUP, 3));

                        pDialog.dismiss();
                    }
                });
        pDialog.setMessage(getString(R.string.text_wizard_trailer_message));
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
    public void onEditDialogSave(int position, SetupTrailerModel item) {
        //mVehicleList.set(position, item);
        mTrailerAdapter.updateItem(position, item);

    }

    @Override
    public void onNewDialogSave(SetupTrailerModel item) {
        int itemCount = models.size();
        /*
        if (emptyList == true)
        {
            mTrailerAdapter.removeAll();
            mTrailerAdapter.addItem(item);
            models.add(item);
        }
        else
        {
            mTrailerAdapter.addItem(item);
            models.add(item);
        }

        emptyList = false;
         */
        if (PreferenceManager.getWizardOn(getActivity()))
        {
            PreferenceManager.setCurrentTrailer(getActivity(), itemCount);

            int selectedPos = PreferenceManager.getPositionID(getActivity());
            Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
            currConfig.setnTrailerIndex(itemCount);
            currConfig.setnTrailerRealmId(item.getnRealmId());
            MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
            DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);
        }

        ll_no_trailers.setVisibility(View.GONE);
        mTrailerAdapter.addItem(item);
        models.add(item);


        if (PreferenceManager.getWizardOn(getActivity()))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    PreferenceManager.setWizardOn(getActivity(), false);
                    if (itemCount == 0)
                        MainApplication.getInstance().mCongrats = true;
                    else
                        MainApplication.getInstance().mCongrats = false;

                    EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEXT_SETUP, 3));
                }
            }, 300);
        }
    }

    @Override
    public void onDeleteDialogSave(int position) {
        models.remove(position);

        int currTrailerIndex = PreferenceManager.getCurrentTrailer(getActivity());
        if (position < currTrailerIndex)
        {
            PreferenceManager.setCurrentTrailer(getActivity(), currTrailerIndex - 1);

            int selectedPos = PreferenceManager.getPositionID(getActivity());
            Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
            currConfig.setnTrailerIndex(currTrailerIndex - 1);
            MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
            DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);
        } else if (position == currTrailerIndex)
        {
            PreferenceManager.setCurrentTrailer(getActivity(), 0);

            int selectedPos = PreferenceManager.getPositionID(getActivity());
            Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
            currConfig.setnTrailerIndex(0);
            currConfig.setnTrailerRealmId(models.get(0).getnRealmId());
            MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
            DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);
        }

        mTrailerAdapter.removeItem(position);
        if (models.size() == 0)
            ll_no_trailers.setVisibility(View.VISIBLE);
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
            case AppConstants.EVENT_BUS_ACTIVE_TRAILER:
                /*
                if (models.size() == 0)
                {
                    SetupTrailerModel _model = mTrailerList.get(pusher.getSelectedPosition());
                    _model.setSampleItem(false);
                    _model = DatabaseManager.addNewTrailerModel(_model);
                    models.add(_model);

                    int selectedPos = PreferenceManager.getPositionID(getActivity());

                    Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
                    currConfig.setnTrailerIndex(0);
                    currConfig.setnTrailerRealmId(_model.getnRealmId());
                    MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
                    DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);

                    PreferenceManager.setCurrentTrailer(getActivity(), 0);
                    mTrailerAdapter.removeAll();
                    mTrailerAdapter.addItem(_model);

                    emptyList = false;

                    PrettyDialog pDialog = new PrettyDialog(getActivity());
                    pDialog.setIcon(R.drawable.pdlg_icon_success);
                    pDialog.setIconTint(R.color.pdlg_color_red);
                    pDialog.setTitle(getString(R.string.text_sample_trailer_saved));
                    pDialog.addButton(
                            "NEXT",					// button text
                            R.color.colorWhite,		// button text color
                            R.color.colorRed,		    // button background color
                            new PrettyDialogCallback() {	// button OnClick listener
                                @Override
                                public void onClick() {
                                    EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEXT_SETUP, 3));
                                    pDialog.dismiss();
                                }
                            });
                    pDialog.setMessage(_model.getName() + " has been added to your list. Manually activate the appropriate trailer by tapping the SET button or tap the name to edit the trailer options");
                    pDialog.show();
                } else
                 */
                //{
                    List<SetupHitchModel> hitchModels = MainApplication.getInstance().hitchModels;
                    int currHitchIndex = PreferenceManager.getCurrentHitch(getActivity());
                    if (currHitchIndex >= 0) {
                        SetupHitchModel currHitch = hitchModels.get(currHitchIndex);
                        double hitchBallSize = Double.valueOf(sizeOptions.get(currHitch.getBallIndex()));
                        double trailerCouplerSize = Double.valueOf(sizeOptions.get(pusher.get_trailerModel().getCouplerIndex()));

                        if (trailerCouplerSize >= hitchBallSize) {
                            PreferenceManager.setCurrentTrailer(getActivity(), pusher.getSelectedPosition());

                            int selectedPos = PreferenceManager.getPositionID(getActivity());
                            Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
                            currConfig.setnTrailerIndex(pusher.getSelectedPosition());
                            currConfig.setnTrailerRealmId(pusher.get_trailerModel().getnRealmId());
                            MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
                            DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);

                            //mTrailerAdapter.notifyItemChanged(pusher.getSelectedPosition());
                            mTrailerAdapter.notifyDataSetChanged();
                        } else {
                            PrettyDialog pDialog = new PrettyDialog(getActivity());
                            pDialog.setIcon(R.drawable.pdlg_icon_info);
                            pDialog.setIconTint(R.color.pdlg_color_black);
                            pDialog.setTitle(getString(R.string.text_hitch_configuration));
                            pDialog.addButton(
                                    "Ok",                        // button text
                                    R.color.colorBlack,                // button text color
                                    R.color.pdlg_color_yellow,        // button background color
                                    new PrettyDialogCallback() {    // button OnClick listener
                                        @Override
                                        public void onClick() {
                                            pDialog.dismiss();
                                        }
                                    });

                            pDialog.setMessage(getString(R.string.text_hitch_config_subtitle));
                            pDialog.show();
                        }
                    }

                //}
                break;
        }
    }

    @Override
    public void onWizardCheck() {
        boolean isWizardOn = PreferenceManager.getWizardOn(getActivity());
        if (isWizardOn) {
            if (mTrailerList.size() > 0)
                showWizardDialog();
            else
            {
                Intent intent = new Intent(getActivity(), NewTrailerActivity.class);
                intent.putExtra("editable", false);
                MainApplication.getInstance().setTrailerUpdateListener(TrailerSetupFragment.this);
                startActivity(intent);
            }
        } else
        {
            if (mTrailerList.size() == 0)
            {
                Intent intent = new Intent(getActivity(), NewTrailerActivity.class);
                intent.putExtra("editable", false);
                MainApplication.getInstance().setTrailerUpdateListener(TrailerSetupFragment.this);
                startActivity(intent);
            }
        }

        MainApplication.getInstance().mTrailerWizard = false;
        viewDidAppear = false;
    }
}