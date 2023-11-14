package com.weigh.safemobile.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;
import com.weigh.safemobile.R;
import com.weigh.safemobile.adapters.SectionStatePagerAdapter;
import com.weigh.safemobile.fragments.GuideFragment;
import com.weigh.safemobile.fragments.GuideStep1Fragment;
import com.weigh.safemobile.fragments.GuideStep2Fragment;
import com.weigh.safemobile.fragments.GuideStep3Fragment;
import com.weigh.safemobile.fragments.GuideStep4Fragment;
import com.weigh.safemobile.fragments.GuideStep5Fragment;
import com.weigh.safemobile.fragments.GuideStep6Fragment;
import com.weigh.safemobile.fragments.GuideStep7Fragment;
import com.weigh.safemobile.fragments.GuideStep8Fragment;
import com.weigh.safemobile.fragments.GuideStep9Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuideActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private GuideStep6Fragment mFragment;
    private String mFromActivity;
    private String vehicleName, trailerName, trailerWeight, tongueWeight, axleinch, springinch, trailerinch, drawPos, visibleHoles;

    @BindView(R.id.dot0)
    ImageView dot0;
    @BindView(R.id.dot1)
    ImageView dot1;
    @BindView(R.id.dot2)
    ImageView dot2;
    @BindView(R.id.dot3)
    ImageView dot3;
    @BindView(R.id.dot4)
    ImageView dot4;
    @BindView(R.id.dot5)
    ImageView dot5;
    @BindView(R.id.dot6)
    ImageView dot6;
    @BindView(R.id.dot7)
    ImageView dot7;
    @BindView(R.id.dot8)
    ImageView dot8;
    @BindView(R.id.dot9)
    ImageView dot9;
    @BindView(R.id.progress_loader)
    ProgressBar pSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //status bar hide
        hideStatebar();

        vehicleName = "";
        trailerName = "";
        trailerWeight = "0";
        tongueWeight = "0";
        axleinch ="";
        springinch = "";
        trailerinch = "";
        drawPos = "Ride";
        visibleHoles = "0.0";

        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        mViewPager = (ViewPager) findViewById(R.id.myViewPager);

        Intent mIntent = getIntent();
        mFromActivity = mIntent.getStringExtra("fromActivity");

        setupViewPager(mViewPager);

        // Create an object of page transformer
        BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();

        // Enable / Disable scaling while flipping. If true, then next page will scale in (zoom in). By default, its true.
        bookFlipPageTransformer.setEnableScale(true);

        // The amount of scale the page will zoom. By default, its 5 percent.
        bookFlipPageTransformer.setScaleAmountPercent(10f);

        // Assign the page transformer to the ViewPager.
        mViewPager.setPageTransformer(true, bookFlipPageTransformer);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //System.out.println(positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        initIndicator();
                        dot0.setImageResource(R.drawable.current_position_icon);
                        break;
                    case 1:
                        initIndicator();
                        dot1.setImageResource(R.drawable.current_position_icon);
                        break;
                    case 2:
                        initIndicator();
                        dot2.setImageResource(R.drawable.current_position_icon);
                        break;
                    case 3:
                        initIndicator();
                        dot3.setImageResource(R.drawable.current_position_icon);
                        break;
                    case 4:
                        initIndicator();
                        dot4.setImageResource(R.drawable.current_position_icon);
                        break;
                    case 5:
                        initIndicator();
                        dot5.setImageResource(R.drawable.current_position_icon);
                        break;
                    case 6:
                        initIndicator();
                        dot6.setImageResource(R.drawable.current_position_icon);
                        break;
                    case 7:
                        if (isValidGuide6())
                        {
                            initIndicator();
                            dot7.setImageResource(R.drawable.current_position_icon);
                        }
                        else
                        {
                            mViewPager.setCurrentItem(6);
                        }
                        break;
                    case 8:
                        initIndicator();
                        dot8.setImageResource(R.drawable.current_position_icon);
                        break;
                    case 9:
                        initIndicator();
                        dot9.setImageResource(R.drawable.current_position_icon);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //setViewPager(0);
    }


    public void setTongueWeight(String _str) { tongueWeight = _str;};
    public String getTongueWeight() {return tongueWeight; }

    public void setAxleinch(String _str) { axleinch = _str; };
    public String getAxleinch() {return axleinch; }

    public void setSpringinch(String _str) { springinch = _str;};
    public String getSpringinch() {return springinch; }

    public void setTrailerinch(String _str) { trailerinch = _str;}
    public String getTrailerinch() {return trailerinch; }

    public void setDrawPos(String _str) { drawPos = _str;};
    public String getDrawPos() {return drawPos; }

    public void setVisibleHoles(String _str) { visibleHoles = _str;};
    public String getVisibleHoles() {return visibleHoles; }

    private void hideStatebar() {
        //status bar hide
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GuideFragment());
        adapter.addFragment(new GuideStep1Fragment());
        adapter.addFragment(new GuideStep2Fragment());
        adapter.addFragment(new GuideStep3Fragment());
        adapter.addFragment(new GuideStep4Fragment());
        adapter.addFragment(new GuideStep5Fragment());
        mFragment = new GuideStep6Fragment(this);
        adapter.addFragment(mFragment);
        adapter.addFragment(new GuideStep7Fragment());
        adapter.addFragment(new GuideStep8Fragment());
        adapter.addFragment(new GuideStep9Fragment());
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        mViewPager.setCurrentItem(fragmentNumber, true);
    }

    public void initIndicator() {
        dot0.setImageResource(R.drawable.disible_position_icon);
        dot1.setImageResource(R.drawable.disible_position_icon);
        dot2.setImageResource(R.drawable.disible_position_icon);
        dot3.setImageResource(R.drawable.disible_position_icon);
        dot4.setImageResource(R.drawable.disible_position_icon);
        dot5.setImageResource(R.drawable.disible_position_icon);
        dot6.setImageResource(R.drawable.disible_position_icon);
        dot7.setImageResource(R.drawable.disible_position_icon);
        dot8.setImageResource(R.drawable.disible_position_icon);
        dot9.setImageResource(R.drawable.disible_position_icon);
    }

    private boolean isValidGuide6() {
        if (mFragment != null)
            return mFragment.validateFields();

        return true;
    }

    public void FinishActivity()
    {
        finish();
    }

    @Override
    public void onBackPressed() {
            //super.onBackPressed();
    }
}


