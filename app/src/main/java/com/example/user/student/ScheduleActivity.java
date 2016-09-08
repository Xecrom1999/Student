package com.example.user.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

import Database.ScheduleDB;
import Fragments.DayFragment;
import Interfaces.EditModeListener;

/**
 * Created by gamrian on 03/09/2016.
 */
public class ScheduleActivity extends AppCompatActivity implements EditModeListener {

    PagerAdapter adapter;
    static ViewPager viewPager;
    DayFragment[] daysFragments;
    ScheduleDB dataBase;
    TabLayout tabLayout;
    Toolbar toolbar;
    boolean rtl;
    Configuration config;
    final int DAYS_NUM = 6;
    boolean editMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        editMode = false;

        setupToolbar();

        dataBase = new ScheduleDB(this);

        adapter = new PagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.schedule_pager);

        daysFragments = new DayFragment[DAYS_NUM];
        rtl = (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL);
        if (rtl) for (int i = 0; i < DAYS_NUM; i++) daysFragments[i] = new DayFragment(i, dataBase, this);
        else for (int i = 0; i < DAYS_NUM; i++) daysFragments[i] = new DayFragment(DAYS_NUM - 1 - i, dataBase, this);

        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Calendar calendar = Calendar.getInstance();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            viewPager.setCurrentItem(DAYS_NUM - (calendar.get(Calendar.DAY_OF_WEEK)));
        else viewPager.setCurrentItem(calendar.get(Calendar.DAY_OF_WEEK) - 1);
    }

    private void setupToolbar() {
        config = getResources().getConfiguration();
        toolbar = (Toolbar) findViewById(R.id.schedule_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(R.string.schedule_string);
    }

    private int getPosition() {
        return viewPager.getCurrentItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.view_id:
                changeView();
                break;
            case R.id.delete_day_id:
                userDialog(false);
                break;
            case R.id.delete_all_id:
                userDialog(true);
                break;
            case R.id.edit_id:
                toggleEditMode();
                item.setIcon(editMode ? R.mipmap.ic_done : R.drawable.ic_edit);
                break;
        }
        return true;
    }

    private void toggleEditMode() {
        editMode = !editMode;

        for (int i = 0; i < DAYS_NUM; i++) daysFragments[i].toggleEditMode();
    }

    private void userDialog(final boolean choice) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        if (!choice)
                            daysFragments[getPosition()].deleteDay();

                        else {
                            for (int i = 0; i < DAYS_NUM; i++)
                                try {
                                    daysFragments[i].deleteDay();
                                } catch (NullPointerException e) {
                                }
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(choice ? R.string.delete_all_string : R.string.delete_day_string) + "?").setPositiveButton(getString(R.string.delete_string), dialogClickListener)
                .setNegativeButton(getString(R.string.cancel_string), dialogClickListener).show();
    }

    private void changeView() {
        Intent intent = new Intent(this, WeekViewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    public void addNewLesson(View view) {
        Intent intent = new Intent(this, NewLessonActivity.class);
        intent.putExtra("isNew", true);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.in_from_bottom, R.anim.stay_in_place);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null)
        daysFragments[getPosition()].lessonDone(new Lesson(data.getStringExtra("name"), data.getStringExtra("time"), data.getStringExtra("length")));
    }

    @Override
    public void toggleEditMode(boolean isEditMode) {

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        String[] days_names;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            days_names = getResources().getStringArray(R.array.days_names);
        }

        @Override
        public Fragment getItem(int position) {
            return daysFragments[position];
        }

        @Override
        public int getCount() {
            return DAYS_NUM;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (rtl) return days_names[DAYS_NUM - position - 1];
            return days_names[position];
        }
    }
}
