package com.se491.chef_ly.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.se491.chef_ly.BuildConfig;
import com.se491.chef_ly.R;
import com.se491.chef_ly.application.CheflyApplication;
import com.squareup.leakcanary.RefWatcher;

public class IntroActivity extends AppCompatActivity  implements IntroFragment.OnFragmentInteractionListener, View.OnClickListener{

    private TextView[] dots;
    private Button skip;
    private Button next;
    private int unselectedDot;
    private int selectedDot;
    private ViewPager pager;
    private LinearLayout dotGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        skip = (Button) findViewById(R.id.skipBtn);
        skip.setOnClickListener(this);
        next = (Button) findViewById(R.id.nextBtn);
        next.setOnClickListener(this);


        pager = (ViewPager) findViewById(R.id.introPager);

        IntroFragment one = IntroFragment.newInstance(1, R.drawable.intro1);
        IntroFragment two = IntroFragment.newInstance(2, R.drawable.intro2);
        IntroFragment three = IntroFragment.newInstance(3, R.drawable.intro3);
        IntroFragment four = IntroFragment.newInstance(4, R.drawable.intro4);
        final IntroFragment[] pages = {one,two,three,four};
        pager.setAdapter(new introPagerAdapter(getSupportFragmentManager(), pages));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == pages.length-1){
                    next.setText(getString(R.string.getCooking));
                    skip.setVisibility(View.GONE);
                    dotGroup.setVisibility(View.GONE);
                }else{
                    next.setText(getString(R.string.next));
                    skip.setVisibility(View.VISIBLE);
                    dotGroup.setVisibility(View.VISIBLE);
                }
                // Set Dots
                for(int i = 0; i< dots.length; i++){
                    if(i==position){
                        dots[i].setTextColor(selectedDot);
                    }else{
                        dots[i].setTextColor(unselectedDot);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        unselectedDot = getColor(getApplicationContext(), R.color.unselectedDot);
        selectedDot = getColor(getApplicationContext(), R.color.selectedDot);


        dotGroup = (LinearLayout) findViewById(R.id.dotsGroup);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,10,10,0);
        dots = new TextView[pages.length];

        for(int i = 0; i< pages.length; i++){
            TextView t = new TextView(getApplicationContext());
            t.setText(getString(R.string.dot));
            t.setTextSize(55);
            t.setLayoutParams(params);
            if(i==0){
                t.setTextColor(selectedDot);
            }else{
                t.setTextColor(unselectedDot);
            }
            dots[i] = t;
            dotGroup.addView(t);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        sharedPreferencesEditor.putBoolean("FirstTimeRun", true);
        sharedPreferencesEditor.apply();

        if(BuildConfig.DEBUG){
            RefWatcher refWatcher = CheflyApplication.getRefWatcher(this);
            refWatcher.watch(this);
        }

    }

    @SuppressWarnings("deprecation")
    private static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    @Override
    public void onFragmentInteraction(int page) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.nextBtn:
                int currentPage = pager.getCurrentItem();
                if(currentPage < pager.getAdapter().getCount()-1){
                    pager.setCurrentItem(currentPage+1);
                }else{
                    finish();
                }
                break;
            case R.id.skipBtn:
                finish();
                break;
            default:

        }
    }

    private class introPagerAdapter extends FragmentPagerAdapter  {
        private IntroFragment[] pages;

        introPagerAdapter(FragmentManager m, IntroFragment[] p) {
            super(m);
            pages = p;
        }

        @Override
        public int getCount() {
            return pages.length;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public Fragment getItem(int position) {
            return pages[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position);
        }
    }

}
