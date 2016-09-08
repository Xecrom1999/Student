package com.example.user.student;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Fragments.MonthFragment;

public class CalendarActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    ViewPager pager;
    MyPagerAdapter adapter;
    FragmentManager fm;
    MonthFragment[] fragments;
    final static int NUM_OF_FRAGMENTS = 25;
    Toolbar toolbar;
    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        fragments = new MonthFragment[NUM_OF_FRAGMENTS];
        for (int i = 0; i < NUM_OF_FRAGMENTS; i++) {
            fragments[i] = new MonthFragment(i);
        }

        initializeViews();
    }

    private void initializeViews() {

        toolbar = (Toolbar) findViewById(R.id.calendar_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Configuration config = getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        getSupportActionBar().setTitle("Calendar");

        pager = (ViewPager) findViewById(R.id.calendar_pager);
        fm = getSupportFragmentManager();
        adapter = new MyPagerAdapter(fm);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);

        fab = (FloatingActionButton) findViewById(R.id.calendar_fab);
        fab.setOnClickListener(this);

        SimpleDateFormat format = new SimpleDateFormat("MMMM    MM/yy");
        Calendar calendar = Calendar.getInstance();
        String month = format.format(calendar.getTime());
        toolbar.setTitle(getString(R.string.calendar_string) + "            " + month);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        toolbar.setTitle(getString(R.string.calendar_string) + "            " + fragments[position].getMonth());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setEventTime(View view) {
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return NUM_OF_FRAGMENTS;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
}