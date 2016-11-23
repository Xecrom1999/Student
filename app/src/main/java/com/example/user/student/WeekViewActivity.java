package com.example.user.student;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import Database.ScheduleDB;

import java.util.ArrayList;

import Adapters.WeekAdapter;

public class WeekViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView weekGrid;
    ScheduleDB dataBase;
    Configuration config;
    final int DAYS_NUM = 6;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_view_schedule);

        setupToolbar();

        dataBase = new ScheduleDB(this);

        weekGrid = (RecyclerView) findViewById(R.id.gridRecyclerView);
        weekGrid.setLayoutManager(new GridLayoutManager(this, DAYS_NUM));
        ArrayList <Lesson>[] lessons = new ArrayList[DAYS_NUM];
        for (int i = 0; i < lessons.length; i++) lessons[i] = getList(i);
        weekGrid.setAdapter(new WeekAdapter(this, lessons));
        weekGrid.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Helper.setupAd(this);
    }

    private void setupToolbar() {
        config = getResources().getConfiguration();
        toolbar = (Toolbar) findViewById(R.id.week_view_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(R.string.schedule_string);
        toolbar.setBackgroundResource(R.color.primary_color);
        toolbar.setAlpha(0.6f);
    }

    public ArrayList<Lesson> getList(int p) {
        ArrayList<Lesson> list = new ArrayList<>();

        Cursor res = dataBase.getData(p);

        while (res.moveToNext()) {
            String name = res.getString(1);
            String time = res.getString(2);
            String length = res.getString(3);
            Lesson lesson = new Lesson(name, time, length);
            list.add(lesson);
        }
        return list;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.week_view_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        else if (id == R.id.view_id2) {
            onBackPressed();
        }
        return true;
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
            window.setStatusBarColor(getColor(isStarted ? R.color.dark_schedule : R.color.status_bar_color));
        }
    }
}
