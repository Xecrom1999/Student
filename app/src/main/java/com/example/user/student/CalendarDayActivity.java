package com.example.user.student;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
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
import Fragments.CalendarDayFragment;
import Interfaces.CalendarDayListener;
import Interfaces.EventDateListener;

public class CalendarDayActivity extends AppCompatActivity implements View.OnClickListener, EventDateListener {

    Toolbar toolbar;
    ViewPager pager;
    PagerAdapter adapter;
    Calendar calendar;
    Calendar mCalendar;
    Configuration config;
    CalendarDB database;
    final static SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_day_activity);

        toolbar = (Toolbar) findViewById(R.id.calendar_day_toolbar);
        database = new CalendarDB(this);

        Intent intent = getIntent();

        calendar = (Calendar) intent.getExtras().get("calendar");
        if (calendar == null) {
            Log.d("MYLOG", "Null");
            return;
        }
        mCalendar = (Calendar) intent.getExtras().get("calendar");

        pager = (ViewPager) findViewById(R.id.calendar_day_pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), this);
        calendar.add(Calendar.DATE, adapter.getCount()/2);

        pager.setAdapter(adapter);
        pager.setCurrentItem(adapter.getCount()/2);

        setupToolbar();
    }

    @Override
    public Calendar getCurrentDate() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar.getTime());
        cal.add(Calendar.DATE, -pager.getCurrentItem());

        adapter.notifyDataSetChanged();

        return cal;
    }

    @Override
    public void update() {

        int position = pager.getCurrentItem();

        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this));

        pager.setCurrentItem(position);
    }

    private void setupToolbar() {
        config = getResources().getConfiguration();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(getString(R.string.today_events_string));
        toolbar.setBackgroundResource(R.color.primary_calendar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_day_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.addEvent) addEvent();

        if (item.getItemId() == android.R.id.home) finish();

        return false;
    }

    private void addEvent() {
        Intent intent = new Intent(this, NewEventActivity.class);

        intent.putExtra("calendar", getCurrentDate());

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        addEvent();
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

    public class PagerAdapter extends FragmentStatePagerAdapter {

        EventDateListener listener;

        public PagerAdapter(FragmentManager fm, EventDateListener listener) {
            super(fm);

            this.listener = listener;
        }

        @Override
        public Fragment getItem(int position) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(calendar.getTime());
            cal.add(Calendar.DATE, -position);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return new CalendarDayFragment(getApplicationContext(), database, cal, listener);
        }

        @Override
        public int getCount() {
            return 365;
        }
    }
}
