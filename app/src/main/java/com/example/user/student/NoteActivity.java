package com.example.user.student;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;

import Database.NotesDB;

public class NoteActivity extends AppCompatActivity {

    EditText title_edit;
    TextView date_text;

    Configuration config;

    String id;

    Toolbar toolbar;
    LinearLayout layout;

    String date;

    Intent intent;

    NotesDB database;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);

        database = new NotesDB(this);

        title_edit = (EditText) findViewById(R.id.note_edit);
        date_text = (TextView) findViewById(R.id.note_date);

        setTexts();

        setupToolbar();

        AdView mAdView = (AdView) findViewById(R.id.note_adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Helper.setupAd(this);
    }

    private void setupToolbar() {
        config = getResources().getConfiguration();
        toolbar = (Toolbar) findViewById(R.id.note_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("");
        toolbar.setBackgroundResource(R.color.notes_primary);
    }


    private void setTexts() {

        intent = getIntent();

        String title = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        date = intent.getStringExtra("date");

        if (date == null || date.equals(""))
            date = simpleDateFormat.format(System.currentTimeMillis());

        title_edit.setText(title);
        date_text.setText(date);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf");
        date_text.setTypeface(font);

        Helper.showKeyboard(this, title_edit);
        title_edit.setSelection(title.length());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.done_id)
            finishNote();

        if (id == android.R.id.home) onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Helper.hideKeyboard(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.dark_notes));
        }
    }

    public void finishNote() {

        String title = title_edit.getText().toString();

        database.updateData2(id, title, date);

        finish();
    }
}
