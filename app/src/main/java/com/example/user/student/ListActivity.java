package com.example.user.student;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import Adapters.ListAdapter;
import Fragments.TaskDialog;
import Interfaces.TaskListener;

public class ListActivity extends ActionBarActivity implements TaskListener {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        toolbar = (Toolbar) findViewById(R.id.toolBar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button3);
        toolbar.setTitle("To Do List");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_task);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ListAdapter(this, this);

        recyclerView.setAdapter(adapter);
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

    public void showFullTask(final View v) {

        TaskDialog dialog = new TaskDialog("", "", "", null);

        dialog.show(getSupportFragmentManager(), "");
    }
    }

