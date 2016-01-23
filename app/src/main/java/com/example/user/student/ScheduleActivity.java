package com.example.user.student;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import Fragments.DayFragment;
import Interfaces.Communicator;
import Interfaces.LessonsListener;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class ScheduleActivity extends ActionBarActivity implements MaterialTabListener, View.OnClickListener, Communicator, LessonsListener {

    String[] daysNames;
    FragmentManager fm;
    MaterialTabHost tabHost;
    ViewPager pager;
    DayFragment[] daysFragments;
    FloatingActionButton fab;
    Toolbar toolbar;
    DataBaseHelper dataBase;
    int position;
    int itemPosition;
    ArrayList<Integer> list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        list = new ArrayList<>();

        position = 0;

        dataBase = new DataBaseHelper(this);

        toolbar = (Toolbar) findViewById(R.id.toolBar2);
        toolbar.setTitle("Schedule");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button3);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton3);
        fab.setOnClickListener(this);

        fm = getSupportFragmentManager();

        daysNames = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        daysFragments = new DayFragment[7];
        for (int i = 0; i < daysFragments.length; i++) daysFragments[i] = new DayFragment(i, this);

        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        pager = (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fm, daysFragments);
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
                setPosition(position);
            }
        });

        for (int i = 0; i < daysNames.length; i++) {
            tabHost.addTab(tabHost.newTab().setText(daysNames[i]).setTabListener(this));
        }
    }

    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
        setPosition(tab.getPosition());
    }

    public void onTabReselected(MaterialTab tab) {
    }

    public void onTabUnselected(MaterialTab tab) {
    }

    public void onClick(View v) {
        newLesson(new Lesson("", "", ""));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
                boolean isNew = data.getBooleanExtra("isNew", false);

                String name = data.getStringExtra("name");
                String time = data.getStringExtra("time");
                int length = data.getIntExtra("length", 45);
                Lesson lesson = new Lesson(name, time, String.valueOf(length));

                if (isNew) {
                    dataBase.insertData(getPosition(), lesson);
                    daysFragments[getPosition()].lessonDone(lesson);
                }
            else {
                    dataBase.updateData(getPosition(), daysFragments[getPosition()].getAdapter().getItemAtPosition(itemPosition), lesson);
                    daysFragments[getPosition()].updateLesson(itemPosition, lesson);
                }
        }
    }

    public void lessonDone(Lesson lesson) {
    }

    public void updateLesson(int position, Lesson lesson) {
    }

    public void newLesson(Lesson lesson) {
        Intent intent = new Intent(this, LessonActivity.class);

        if (!lesson.getName().equals("")) {
            intent.putExtra("isNew", false);
            intent.putExtra("name", lesson.getName());
            intent.putExtra("time", lesson.getTime());
            intent.putExtra("length", lesson.getLength());
            intent.putExtra("itemPosition", itemPosition);
        }
        else intent.putExtra("isNew", true);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.in_from_bottom, R.anim.stay_in_place);
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

    public int getPagerPosition() {
        return pager.getCurrentItem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.schedule_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
}

    public void showMenu(final int position, final Lesson lesson) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    newLesson(lesson);
                    setCurrentItemPosition(position);
                }

                if (which == 1) {
                    deleteLesson(position, lesson);
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    public void setCurrentItemPosition(int p) {
        this.itemPosition = p;
    }

    private void deleteLesson(int position, Lesson lesson) {
        daysFragments[getPosition()].deleteLesson(position);
        dataBase.deleteData(getPosition(), lesson);
    }

    public int getPosition() {
        return this.position;
    }
}

class ViewPagerAdapter extends FragmentStatePagerAdapter {

    DayFragment[] fragments;

    public ViewPagerAdapter(FragmentManager fm, DayFragment[] daysFragments) {
        super(fm);

        this.fragments = daysFragments;
    }

    public android.support.v4.app.Fragment getItem(int position) {
        return fragments[position];
    }

    public int getCount() {
        return fragments.length;
    }
}