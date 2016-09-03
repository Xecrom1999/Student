package com.example.user.student;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Fragments.CalendarDayFragment;

public class CalendarDayActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    Toolbar toolbar;
    ViewPager pager;
    int day;
    int month;
    int year;
    PagerAdapter adapter;
    Calendar calendar;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_day_activity);

        toolbar = (Toolbar) findViewById(R.id.calendar_day_toolbar);

        day = getIntent().getIntExtra("day", 0);
        month = getIntent().getIntExtra("month", 0);
        year = getIntent().getIntExtra("year", 0);

        cal = Calendar.getInstance();
        calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        toolbar.setTitle(day + "/" + (month+1) + "/" + year);
        setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.calendar_day_pager);
        adapter = new PagerAdapter(getSupportFragmentManager());
        calendar.add(Calendar.DATE, -adapter.getCount()/2);

        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
        pager.setCurrentItem(adapter.getCount()/2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_day_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.addEvent) {
            Intent intent = new Intent(this, NewEventActivity.class);
            intent.putExtra("year", cal.get(Calendar.YEAR));
            intent.putExtra("month", cal.get(Calendar.MONTH));
            intent.putExtra("day", cal.get(Calendar.DAY_OF_MONTH));
            startActivity(intent);
        }

        return false;
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

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(calendar.getTime());
            cal.add(Calendar.DATE, position);
            return new CalendarDayFragment(cal.getTime());
        }

        @Override
        public int getCount() {
            return 366;
        }
    }
}
