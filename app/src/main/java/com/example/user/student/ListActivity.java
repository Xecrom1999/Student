package com.example.user.student;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import Adapters.ListAdapter;

public class ListActivity extends ActionBarActivity {

    ListAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        toolbar = (Toolbar) findViewById(R.id.toolBar2);
        setSupportActionBar(toolbar);

        adapter = new ListAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
