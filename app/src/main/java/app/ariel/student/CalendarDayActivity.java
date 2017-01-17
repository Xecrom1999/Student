package app.ariel.student;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ariel.student.student.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import Database.CalendarDB;
import Fragments.CalendarDayFragment;
import Interfaces.EventDateListener;

public class CalendarDayActivity extends AppCompatActivity implements View.OnClickListener, EventDateListener {

    Toolbar toolbar;
    ViewPager pager;
    PagerAdapter adapter;
    Calendar calendar;
    Calendar mCalendar;
    Configuration config;
    CalendarDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_day_activity);

        toolbar = (Toolbar) findViewById(R.id.calendar_day_toolbar);
        database = new CalendarDB(this);

        Intent intent = getIntent();

        calendar = (Calendar) intent.getExtras().get("calendar");
        if (calendar == null) return;

        mCalendar = (Calendar) intent.getExtras().get("calendar");

        pager = (ViewPager) findViewById(R.id.calendar_day_pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), this);
        calendar.add(Calendar.DATE, adapter.getCount()/2);

        pager.setAdapter(adapter);
        pager.setCurrentItem(adapter.getCount()/2);

        setupToolbar();

        AdView mAdView = (AdView) findViewById(R.id.day_activity_adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Helper.setupAd(this);
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
        toolbar.setBackgroundResource(R.color.primary_color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(72);
        }
        toolbar.setAlpha(0.6f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_day_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.addEvent) addEvent();

        if (item.getItemId() == android.R.id.home) onBackPressed();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
