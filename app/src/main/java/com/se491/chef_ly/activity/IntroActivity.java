package com.se491.chef_ly.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.se491.chef_ly.R;

public class IntroActivity extends AppCompatActivity  implements IntroFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ViewPager pager = (ViewPager) findViewById(R.id.introPager);

        IntroFragment one = IntroFragment.newInstance(1, R.drawable.cheflyintroslides);
        //IntroFragment two = IntroFragment.newInstance(2, R.drawable.cheflyintroslides2);
        //IntroFragment three = IntroFragment.newInstance(3, R.drawable.cheflyintroslides3);
        //IntroFragment four = IntroFragment.newInstance(4, R.drawable.cheflyintroslides4);
       // IntroFragment[] pages = {one,two,three,four};
        //pager.setAdapter(new introPagerAdapter(getSupportFragmentManager(), pages));

        //todo

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
