package com.example.user.student;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import Adapters.ListAdapter;
import Fragments.HomeworkFragment;
import Interfaces.TaskListener;

public class ListActivity extends ActionBarActivity implements TaskListener {

    ListAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    FragmentManager fm;
    FragmentTransaction ft;
    HomeworkFragment fragment;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        toolbar = (Toolbar) findViewById(R.id.toolBar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button3);
        toolbar.setTitle("To Do List");

        adapter = new ListAdapter(this, this);
        recyclerView = (RecyclerView) findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.addTask("HW Math", "page 110 - 112", null);
        adapter.addTask("HW Hebrew", "page 110 - 112", null);
        adapter.addTask("HW English", "page 110 - 112", null);

        fragment = new HomeworkFragment();

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.container, new HomeworkFragment());
        ft.commit();
    }

    public void changeTask(Task task) {

        ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.container, new HomeworkFragment(task)).commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
