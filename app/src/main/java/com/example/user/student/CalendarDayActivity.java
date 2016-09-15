package com.example.user.student;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Database.CalendarDB;
import Fragments.CalendarDayFragment;

public class CalendarDayActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    Toolbar toolbar;
    ViewPager pager;
    int day;
    int month;
    int year;
    PagerAdapter adapter;
    Calendar calendar;
    Calendar cal;
    Configuration config;
    CalendarDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_day_activity);

        toolbar = (Toolbar) findViewById(R.id.calendar_day_toolbar);
        database = new CalendarDB(this);

        day = getIntent().getIntExtra("day", 0);
        month = getIntent().getIntExtra("month", 0);
        year = getIntent().getIntExtra("year", 0);

        cal = Calendar.getInstance();
        calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        pager = (ViewPager) findViewById(R.id.calendar_day_pager);
        adapter = new PagerAdapter(getSupportFragmentManager());
        calendar.add(Calendar.DATE, -adapter.getCount()/2);

        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
        pager.setCurrentItem(adapter.getCount()/2);

        setupToolbar();
    }

    private void setupToolbar() {
        config = getResources().getConfiguration();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(day + "/" + (month+1) + "/" + year);
        toolbar.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_day_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.addEvent) {
            addEvent();
        }

        if (item.getItemId() == android.R.id.home) finish();

        return false;
    }

    private void addEvent() {
        Intent intent = new Intent(this, NewEventActivity.class);
        intent.putExtra("year", cal.get(Calendar.YEAR));
        intent.putExtra("month", cal.get(Calendar.MONTH));
        intent.putExtra("day", cal.get(Calendar.DAY_OF_MONTH));
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        cal.setTime(calendar.getTime());
        cal.add(Calendar.DATE, position);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        toolbar.setTitle(format.format(cal.getTime()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        addEvent();
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(calendar.getTime());
            cal.add(Calendar.DATE, position);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return new CalendarDayFragment(getApplicationContext(), database, cal.getTime());
        }

        @Override
        public int getCount() {
            return 365;
        }
    }
}
