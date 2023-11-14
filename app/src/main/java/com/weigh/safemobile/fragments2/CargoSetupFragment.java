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
import android.widget.RelativeLayout;

import com.weigh.safemobile.R;
import com.weigh.safemobile.activities.MainActivity;
import com.weigh.safemobile.activities.NewCargoActivity;
import com.weigh.safemobile.adapters.NewCargoAdapter;
import com.weigh.safemobile.app.AppConstants;
import com.weigh.safemobile.app.DatabaseManager;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.helpers.PreferenceManager;
import com.weigh.safemobile.models.Configuration;
import com.weigh.safemobile.models.Pusher;
import com.weigh.safemobile.models.SetupCargoModel;
import com.weigh.safemobile.presenters.CargoUpdateListener;
import com.weigh.safemobile.presenters.GoCargoListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CargoSetupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CargoSetupFragment extends Fragment implements CargoUpdateListener, GoCargoListener {

    @BindView(R.id.root_scroll)
    NestedScrollView mScrollView;

    @BindView(R.id.recycler_cargo)
    RecyclerView cargoListView;

    @BindView(R.id.fab_new)
    ImageButton btnAddNew;

    @BindView(R.id.ll_no_cargo)
    LinearLayout ll_no_cargo;

    @BindView(R.id.rl_note)
    RelativeLayout rl_note;

    private NewCargoAdapter mCargoAdapter = null;
    private List<SetupCargoModel> models;
    private List<SetupCargoModel> mCargoList = new ArrayList<>();
    private View mView;

    //private boolean emptyList;
    //private List<Integer> defaultsSelected = new ArrayList<>();

    public static CargoSetupFragment newInstance() {
        CargoSetupFragment fragment = new CargoSetupFragment();
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
            mView = inflater.inflate(R.layout.fragment2_cargo_setup, container, false);
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

        if (MainApplication.getInstance().mCargoNotify) {
            mCargoAdapter.notifyDataSetChanged();
            MainApplication.getInstance().mCargoNotify = false;
        }
    }

    public void notifyAllData()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCargoAdapter.notifyDataSetChanged();
            }
        }, 500);
    }

    private void initializerView() {
        MainApplication.getInstance().setGoCargoListener(this);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cargoListView.setLayoutManager(mLinearLayoutManager);
        cargoListView.setItemAnimator(new DefaultItemAnimator());
        cargoListView.getItemAnimator().setChangeDuration(0);

        //fix slow recyclerview start
        cargoListView.setHasFixedSize(true);
        cargoListView.setItemViewCacheSize(10);
        cargoListView.setDrawingCacheEnabled(true);
        cargoListView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        cargoListView.addItemDecoration(new DividerItemDecoration(cargoListView.getContext(), DividerItemDecoration.VERTICAL));
        ///fix slow recyclerview end

        fetchAllCargos();

        mCargoAdapter = new NewCargoAdapter(this, mCargoList);
        cargoListView.setAdapter(mCargoAdapter);
    }

    private void showGoHomeDialog()
    {
        MainApplication.getInstance().mCongrats = false;

        PrettyDialog pDialog = new PrettyDialog(getActivity());
        pDialog.setIcon(R.drawable.pdlg_icon_success);
        pDialog.setIconTint(R.color.pdlg_color_gray);
        pDialog.setTitle(getString(R.string.tow_congrats));
        pDialog.addButton(
                "Go to Home",			// button text
                R.color.colorWhite,		    // button text color
                R.color.colorRed,		    // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_NEXT_SETUP, 0));
                        ((MainActivity)getActivity()).selectFragmentByPager(0);
                        pDialog.dismiss();
                    }
                });
        pDialog.addButton(
                "Close", // button text
                R.color.colorWhite,		    // button text color
                R.color.grey30,		        // button background color
                new PrettyDialogCallback() {	// button OnClick listener
                    @Override
                    public void onClick() {
                        pDialog.dismiss();
                    }
                });


        Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
        String strSuccess = String.format("You have completed your first town configuration - %s. %s", currConfig.getName(), getString(R.string.tow_congrats_subtitle2));
        pDialog.setMessage(strSuccess);
        pDialog.show();
    }

    private void fetchAllCargos() {
        mCargoList.clear();

        models = MainApplication.getInstance().cargoModels;

        if (models == null || models.size() == 0) {
            ll_no_cargo.setVisibility(View.VISIBLE);
            rl_note.setVisibility(View.VISIBLE);
        }
        else
        {
            ll_no_cargo.setVisibility(View.GONE);
            rl_note.setVisibility(View.GONE);
            mCargoList.addAll(models);
        }

        /*
        if (models == null || models.size() < 1)
        {
            emptyList = true;
            mCargoList.addAll(MainApplication.getInstance().defaultCargos);
        } else
        {
            emptyList = false;
            mCargoList.addAll(models);
        }
         */
    }

    public void initializerSave() {
        cargoListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (defaultsSelected.size() > 0)
                {
                    RealmList<Integer> nsArray = new RealmList<>();
                    RealmList<Integer> nsRealmArray = new RealmList<>();
                    List<String> cargoIndexes = new ArrayList<>();

                    int defaultCnt = 0;

                    for (int index : defaultsSelected)
                    {
                        SetupCargoModel _model = mCargoList.get(index);
                        //_model.setSampleItem(false);
                        _model = DatabaseManager.addNewCargoModel(_model);
                        models.add(_model);

                        nsArray.add(defaultCnt);
                        nsRealmArray.add(_model.getnRealmId());
                        cargoIndexes.add(String.valueOf(defaultCnt));
                        defaultCnt++;
                    }

                    int selectedPos = PreferenceManager.getPositionID(getActivity());

                    Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
                    currConfig.setnCargoIndexes(nsArray);
                    currConfig.setnCargoRealmIDs(nsRealmArray);
                    MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
                    DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);

                    PreferenceManager.setCurrentCargoList(getActivity(), cargoIndexes);

                    PrettyDialog pDialog = new PrettyDialog(getActivity());
                    pDialog.setIcon(R.drawable.pdlg_icon_success);
                    pDialog.setIconTint(R.color.pdlg_color_red);
                    pDialog.setTitle(getString(R.string.text_sample_cargo_saved));
                    pDialog.addButton(
                            "NEXT",					// button text
                            R.color.colorWhite,		// button text color
                            R.color.colorRed,		    // button background color
                            new PrettyDialogCallback() {	// button OnClick listener
                                @Override
                                public void onClick() {
                                    pDialog.dismiss();
                                }
                            });
                    pDialog.setMessage(defaultsSelected.size() + " cargo(s) added to your list. Manually activate the appropriate cargo by tapping the SET button or tap the name to edit the weight or type of cargo.");
                    pDialog.show();

                    defaultsSelected.clear();
                    mCargoAdapter.removeAll();
                    mCargoAdapter.addAll(models);
                    //emptyList = false;
                } else
                 */
                {
                    Intent intent = new Intent(getActivity(), NewCargoActivity.class);
                    intent.putExtra("editable", false);
                    MainApplication.getInstance().setCargoUpdateListener(CargoSetupFragment.this);
                    startActivity(intent);
                }
            }
        });
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
    public void onEditDialogSave(int position, SetupCargoModel item) {
        mCargoAdapter.updateItem(position, item);
    }

    @Override
    public void onNewDialogSave(SetupCargoModel item) {
        List<String> cargoActive = PreferenceManager.getCurrentCargoList(getActivity());
        cargoActive.add(String.valueOf(models.size()));

        models.add(item);
        PreferenceManager.setCurrentCargoList(getActivity(), cargoActive);

        RealmList<Integer> nsArray = new RealmList<>();
        RealmList<Integer> nsRealmArray = new RealmList<>();

        for (String cargo : cargoActive) {
            nsArray.add(Integer.valueOf(cargo));
            nsRealmArray.add(models.get(Integer.valueOf(cargo)).getnRealmId());
        }

        int selectedPos = PreferenceManager.getPositionID(getActivity());
        Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
        currConfig.setnCargoIndexes(nsArray);
        currConfig.setnCargoRealmIDs(nsRealmArray);

        MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
        DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);

        mCargoAdapter.addItem(item);

        ll_no_cargo.setVisibility(View.GONE);
        rl_note.setVisibility(View.GONE);
    }

    @Override
    public void onDeleteDialogSave(int position) {
        models.remove(position);
        mCargoAdapter.removeItem(position);

        if (models.size() == 0) {
            ll_no_cargo.setVisibility(View.VISIBLE);
            rl_note.setVisibility(View.VISIBLE);
        }

        List<String> cargoIndexes = PreferenceManager.getCurrentCargoList(getActivity());
        List<String> newIndexes = new ArrayList<>();
        RealmList<Integer> nsArray = new RealmList<>();
        RealmList<Integer> nsRealmArray = new RealmList<>();

        for (int i=0; i<cargoIndexes.size(); i++)
        {
            int cargoIndex = Integer.valueOf(cargoIndexes.get(i));
            if (cargoIndex > position)
            {
                cargoIndex -= 1;
                newIndexes.add(String.valueOf(cargoIndex));
                nsArray.add(cargoIndex);
                nsRealmArray.add(models.get(cargoIndex).getnRealmId());
            } else if (cargoIndex < position)
                newIndexes.add(String.valueOf(cargoIndex));
                nsArray.add(cargoIndex);
                nsRealmArray.add(models.get(cargoIndex).getnRealmId());
        }

        if (newIndexes.size() == 0) {
            newIndexes.add("0");
            nsArray.add(0);
            nsRealmArray.add(models.get(0).getnRealmId());
        }

        PreferenceManager.setCurrentCargoList(getActivity(), newIndexes);

        int selectedPos = PreferenceManager.getPositionID(getActivity());
        Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
        currConfig.setnCargoIndexes(nsArray);
        currConfig.setnCargoRealmIDs(nsRealmArray);

        MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
        DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);

        mCargoAdapter.notifyDataSetChanged();
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
            case AppConstants.EVENT_BUS_ACTIVE_CARGO:
                List<String> cargoActive = PreferenceManager.getCurrentCargoList(getActivity());
                if (cargoActive.contains(String.valueOf(pusher.getSelectedPosition())))
                    cargoActive.remove(String.valueOf(pusher.getSelectedPosition()));
                else
                    cargoActive.add(String.valueOf(pusher.getSelectedPosition()));

                PreferenceManager.setCurrentCargoList(getActivity(), cargoActive);

                RealmList<Integer> nsArray = new RealmList<>();
                RealmList<Integer> nsRealmArray = new RealmList<>();

                for (String cargo : cargoActive) {
                    nsArray.add(Integer.valueOf(cargo));
                    nsRealmArray.add(models.get(Integer.valueOf(cargo)).getnRealmId());
                }

                int selectedPos = PreferenceManager.getPositionID(getActivity());
                Configuration currConfig = MainApplication.getInstance().selectedConfiguration;
                currConfig.setnCargoIndexes(nsArray);
                currConfig.setnCargoRealmIDs(nsRealmArray);

                MainApplication.getInstance().mConfigList.set(selectedPos, currConfig);
                DatabaseManager.updateConfigurationModel(currConfig.getnRealmId(), currConfig);

                mCargoAdapter.notifyItemChanged(pusher.getSelectedPosition());
                break;
        }
    }

    @Override
    public void onCongratsCheck() {
        if (MainApplication.getInstance().mCongrats && mCargoList.size() == 0)
            showGoHomeDialog();
    }
}