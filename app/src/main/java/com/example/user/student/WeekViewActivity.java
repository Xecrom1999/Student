package com.example.user.student;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import Adapters.WeekAdapter;

public class WeekViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView weekGrid;
    DataBaseHelper dataBase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_view_schedule);

        dataBase = new DataBaseHelper(this);

        toolbar = (Toolbar) findViewById(R.id.toolBar3);
        setSupportActionBar(toolbar);

        weekGrid = (RecyclerView) findViewById(R.id.gridRecyclerView);
        weekGrid.setLayoutManager(new GridLayoutManager(this, 7));
        ArrayList <Lesson>[] lessons = new ArrayList[7];
        for (int i = 0; i < lessons.length; i++) lessons[i] = getList(i);
        weekGrid.setAdapter(new WeekAdapter(this, lessons));
        weekGrid.setHasFixedSize(true);
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





    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.changeView) {

           finish();
        }

        return false;
    }
}
