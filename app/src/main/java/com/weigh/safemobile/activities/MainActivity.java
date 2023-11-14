package com.weigh.safemobile.activities;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.weigh.safemobile.R;
import com.weigh.safemobile.adapters.SectionStatePagerAdapter;
import com.weigh.safemobile.adapters.ViewPagerAdapter;
import com.weigh.safemobile.app.MainApplication;
import com.weigh.safemobile.fragments.ChecklistFragment;
import com.weigh.safemobile.fragments.HelpFragment;
import com.weigh.safemobile.fragments2.HomeFragment;
import com.weigh.safemobile.fragments2.NotesFragment;
import com.weigh.safemobile.fragments2.Setup2Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.contentView)
    RelativeLayout contentView;

    private Fragment activeFragment = new Fragment();
    private ViewPager mViewPager;
    private SectionStatePagerAdapter mSectionStatePaperAdapter;
    private Fragment homeFragment;
    private Fragment setupFragment;
    private Fragment checkListFragment;
    private Fragment noteFragment;
    private MenuItem prevMenuItem;
    private ViewPagerAdapter adapter;

    private boolean isKeyboardShowing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializerView();
    }

    private void onKeyboardVisibilityChanged(boolean opened) {
        if (opened)
        {
            if (homeFragment != null)
                ((HomeFragment)homeFragment).dismissPopupTip();
        }
    }

    private void initializerView() {
        mViewPager = (ViewPager) findViewById(R.id.pager_layout);
        setupViewPager(mViewPager);

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    Rect r = new Rect();
                    contentView.getWindowVisibleDisplayFrame(r);
                    int screenHeight = contentView.getRootView().getHeight();

                    // r.bottom is the position above soft keypad or device button.
                    // if keypad is shown, the r.bottom is smaller than that before.
                    int keypadHeight = screenHeight - r.bottom;

                    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                        // keyboard is opened
                        if (!isKeyboardShowing) {
                            isKeyboardShowing = true;
                            onKeyboardVisibilityChanged(true);
                        }
                    }
                    else {
                        // keyboard is closed
                        if (isKeyboardShowing) {
                            isKeyboardShowing = false;
                            onKeyboardVisibilityChanged(false);
                        }
                    }
                }
            });

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.tab_home:
                            mViewPager.setCurrentItem(0);
                            break;
                        case R.id.tab_setup:
                            mViewPager.setCurrentItem(1);
                            break;
                        case R.id.tab_checklist:
                            mViewPager.setCurrentItem(2);
                            break;
                        case R.id.tab_notes:
                            mViewPager.setCurrentItem(3);
                            break;
                    }
                    return false;
                }
            });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0)
                    ((HomeFragment)homeFragment).dismissPopupTip();


                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

                //Input Flag for Setup2 resume
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public BottomNavigationView getBottomNavigationView() { return bottomNavigationView; }

    public void selectFragmentByPager(int position)
    {
        mViewPager.setCurrentItem(position);
    }

    public void GoSetupVehicle()
    {
        mViewPager.setCurrentItem(1);
        ((Setup2Fragment)adapter.getItem(1)).selectFragmentByPager(0);

        MainApplication.getInstance().mVehicleWizard = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainApplication.getInstance().getGoVehicleListener().onWizardCheck();
            }
        }, 800);
    }

    public int getSelectedPage() { return mViewPager.getCurrentItem(); }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState){
        super.onRestoreInstanceState(inState);
    }

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem() == 0) {
            if (adapter.getItem(0) instanceof HelpFragment) {
                ((HelpFragment) adapter.getItem(0)).backPressed();
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        homeFragment = HomeFragment.newInstance();
        setupFragment = Setup2Fragment.newInstance();
        checkListFragment = ChecklistFragment.newInstance();
        noteFragment = NotesFragment.newInstance();

        adapter.addFragment(homeFragment);
        adapter.addFragment(setupFragment);
        adapter.addFragment(checkListFragment);
        adapter.addFragment(noteFragment);
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        mViewPager.setCurrentItem(fragmentNumber, false);
    }

    public int getCurrentPosition() { return mViewPager.getCurrentItem(); }

    public void replaceFragment(Fragment fragment)
    {
        adapter.replaceFragment(fragment, 0);
    }

    public void restoreFragment()
    {
        adapter.restoreFragment();
    }
}
