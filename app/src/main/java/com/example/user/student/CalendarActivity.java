package com.example.user.student;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Database.CalendarDB;
import Fragments.MonthFragment;

public class CalendarActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    ViewPager pager;
    MyPagerAdapter adapter;
    FragmentManager fm;
    MonthFragment[] fragments;
    final static int NUM_OF_FRAGMENTS = 26;
    Toolbar toolbar;
    FloatingActionButton fab;

    CalendarDB database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        database = new CalendarDB(this);

        fragments = new MonthFragment[NUM_OF_FRAGMENTS];
        for (int i = 0; i < NUM_OF_FRAGMENTS; i++) {
            fragments[i] = new MonthFragment(NUM_OF_FRAGMENTS - i - 1, database);
        }

        initializeViews();

        if (getIntent().getBooleanExtra("fromNoti", false)) {
            Intent intent = new Intent(this, CalendarDayActivity.class);
            Calendar calendar = (Calendar) getIntent().getExtras().get("calendar");
            intent.putExtra("calendar", calendar);
            startActivity(intent);
        }
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
        toolbar.setBackgroundResource(R.color.primary_calendar);
        toolbar.setElevation(72);

        pager = (ViewPager) findViewById(R.id.calendar_pager);
        fm = getSupportFragmentManager();
        adapter = new MyPagerAdapter(fm);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
        pager.setCurrentItem(NUM_OF_FRAGMENTS - 2);

        fab = (FloatingActionButton) findViewById(R.id.calendar_fab);
        fab.setOnClickListener(this);

        SimpleDateFormat format = new SimpleDateFormat("MMMM   MM/yy");
        Calendar calendar = Calendar.getInstance();
        String month = format.format(calendar.getTime());
        toolbar.setSubtitleTextColor(getColor(R.color.background_color));
        toolbar.setTitle(month);
      //  toolbar.setSubtitle(month);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        toolbar.setTitle(fragments[position].getMonth());
        //toolbar.setSubtitle(fragments[position].getMonth());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, NewEventActivity.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        intent.putExtra("calendar", calendar);

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

        if (id == R.id.backToMonth) {
            pager.setCurrentItem(adapter.getCount() - 2);
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        changeColor(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        changeColor(true);
    }

    private void changeColor(boolean isStarted) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(isStarted ? R.color.dark_calendar : R.color.primary_dark));
        }
    }
}