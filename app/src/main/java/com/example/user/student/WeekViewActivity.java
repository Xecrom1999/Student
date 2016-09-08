package com.example.user.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

    private void setupToolbar() {
        config = getResources().getConfiguration();
        toolbar = (Toolbar) findViewById(R.id.week_view_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(R.string.schedule_string);
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
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        return true;
    }
}
