package app.ariel.student;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ariel.student.student.R;

import java.util.Calendar;

import Database.DefaultLessonsDB;
import Database.ScheduleDB;
import Fragments.ScheduleDayFragment;
import Interfaces.EditModeListener;
import Interfaces.ScheduleListener;

/**
 * Created by gamrian on 03/09/2016.
 */
public class ScheduleActivity extends AppCompatActivity implements EditModeListener, ScheduleListener {

    PagerAdapter adapter;
    static ViewPager viewPager;
    ScheduleDayFragment[] daysFragments;
    ScheduleDB dataBase;
    TabLayout tabLayout;
    Toolbar toolbar;
    boolean rtl;
    Configuration config;
    final int DAYS_NUM = 6;
    boolean editMode;
    Menu menu;
    InputMethodManager imm;
    DefaultLessonsDB db;

    boolean h_hour;
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        editMode = false;

        setupToolbar();
        
        dataBase = new ScheduleDB(this);

        db = new DefaultLessonsDB(this);

        boolean isNew = getSharedPreferences("data", MODE_PRIVATE).getBoolean("isNew", true);
        getSharedPreferences("data", MODE_PRIVATE).edit().putBoolean("isNew", false).commit();
        if (isNew) {
            db.insertData("8:00", "45");
            db.insertData("8:45", "45");
            db.insertData("9:30", "45");
            db.insertData("10:15", "45");
            db.insertData("12:00", "45");
            db.insertData("12:45", "45");
            db.insertData("13:30", "45");
            db.insertData("14:15", "45");
            db.insertData("15:00", "45");
            db.insertData("15:45", "45");
            db.insertData("16:30", "45");
            db.insertData("17:15", "45");
        }

        adapter = new PagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.schedule_pager);

        daysFragments = new ScheduleDayFragment[DAYS_NUM];
        rtl = (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL);
        if (rtl) for (int i = 0; i < DAYS_NUM; i++) daysFragments[i] = new ScheduleDayFragment(i, dataBase, this, this, this);
        else for (int i = 0; i < DAYS_NUM; i++) daysFragments[i] = new ScheduleDayFragment(DAYS_NUM - 1 - i, dataBase, this, this, this);

        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Calendar calendar = Calendar.getInstance();
        boolean rtl = (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL);
        if (rtl)
            viewPager.setCurrentItem(DAYS_NUM - (calendar.get(Calendar.DAY_OF_WEEK)));
        else viewPager.setCurrentItem(calendar.get(Calendar.DAY_OF_WEEK) - 1);

        if (viewPager.getCurrentItem() == 0 && rtl) viewPager.setCurrentItem(7);
        else if (viewPager.getCurrentItem() == 7 && !rtl) viewPager.setCurrentItem(0);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Helper.setupAd(this);

        preferences = getSharedPreferences("data", MODE_PRIVATE);
        h_hour = preferences.getBoolean("h_hour", false);
    }

    private void updateMenu() {
        preferences.edit().putBoolean("h_hour", h_hour).commit();
        menu.getItem(3).setChecked(h_hour);
        for (int i = 0; i < DAYS_NUM; i++) daysFragments[i].update();
    }

    private void setupToolbar() {
        config = getResources().getConfiguration();
        toolbar = (Toolbar) findViewById(R.id.schedule_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(R.string.schedule_string);
        toolbar.setBackgroundResource(R.color.primary_color);
        toolbar.setAlpha(0.6f);
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
            case R.id.delete_all_id:
                userDialog();
                break;
            case R.id.edit_id:
                toggleEditMode();
                break;
            case R.id.h_hour_id:
                h_hour = !h_hour;
                updateMenu();
                break;
        }
        return true;
    }

    private void toggleEditMode() {
        editMode = !editMode;

        menu.getItem(0).setIcon(editMode ? R.mipmap.ic_done : R.drawable.ic_edit);

        for (int i = 0; i < DAYS_NUM; i++) daysFragments[i].toggleEditMode();
    }

    private void userDialog() {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        for (int i = 0; i < DAYS_NUM; i++)
                            try {
                                daysFragments[i].deleteDay();
                            } catch (NullPointerException e) {}
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogStyle);
        builder.setMessage(getString(R.string.are_you_sure_string)).setPositiveButton(getString(R.string.delete_string), dialogClickListener)
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
        this.menu = menu;
        updateMenu();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public void startEditMode() {
        if (editMode) return;

        editMode = !editMode;

        menu.getItem(0).setIcon(editMode ? R.mipmap.ic_done : R.drawable.ic_edit);

        for (int i = 0; i < DAYS_NUM; i++) daysFragments[i].toggleEditMode();
    }

    @Override
    public void deleteLesson(int position, Lesson lesson) {
        daysFragments[getPosition()].deleteLesson(position, lesson);
    }

    @Override
    public void openLesson(int position, Lesson lesson) {
        daysFragments[getPosition()].openLesson(position, lesson);
    }

    @Override
    public void newLesson() {
        daysFragments[getPosition()].newLesson();
    }

    public void newLesson(View view) {
        newLesson();
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

    @Override
    public void onPause() {
        super.onPause();
        Helper.hideKeyboard(this);

    }
}
