package com.example.user.student;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import Database.ScheduleDB;
import Fragments.DayFragment;
import Fragments.DayFragment2;
import Interfaces.LessonsListener;

/**
 * Created by gamrian on 03/09/2016.
 */
public class ScheduleActivity2 extends AppCompatActivity implements LessonsListener {

    PagerAdapter adapter;
    ViewPager mViewPager;
    DayFragment2[] daysFragments;
    ScheduleDB dataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        dataBase = new ScheduleDB(this);

        adapter = new PagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.schedule_pager);
        mViewPager.setAdapter(adapter);

        daysFragments = new DayFragment2[7];
        for (int i = 0; i < daysFragments.length; i++) daysFragments[i] = new DayFragment2(i, this, dataBase);
    }

    @Override
    public void showMenu(final int position, final Lesson lesson) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    newLesson(lesson);
                }

                if (which == 1) {
                    deleteLesson(position, lesson);
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    public void newLesson(Lesson lesson) {
        Intent intent = new Intent(this, NewLessonActivity.class);

        if (!lesson.getName().equals("")) {
            intent.putExtra("isNew", false);
            intent.putExtra("name", lesson.getName());
            intent.putExtra("time", lesson.getTime());
            intent.putExtra("length", lesson.getLength());
            intent.putExtra("itemPosition", getPosition());
        }
        else intent.putExtra("isNew", true);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.in_from_bottom, R.anim.stay_in_place);
    }

    private void deleteLesson(int position, Lesson lesson) {
        daysFragments[getPosition()].deleteLesson(position);
        dataBase.deleteData(getPosition(), lesson);
    }

    private int getPosition() {
        return mViewPager.getCurrentItem();
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
            return days_names.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return days_names[position];
        }
    }
}
