package com.example.user.student;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import Database.NotesDB;

public class NewNoteActivity extends AppCompatActivity implements TextWatcher {

    EditText title_edit;

    TextView title_text;
    TextView date_text;

    Configuration config;

    NotesDB database;

    boolean isNew;

    View note_item;
    InputMethodManager imm;

    Toolbar toolbar;
    LinearLayout layout;

    Intent intent;

    String id;
    String date;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note_activity);

        database = new NotesDB(this);

        intent = getIntent();

        id = intent.getStringExtra("id");

        isNew = intent.getBooleanExtra("isNew", false);

        initializeViews();
        setListeners();
        setTexts();

        setupToolbar();

        title_edit.requestFocus();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Helper.setupAd(this);
    }

    private void setupToolbar() {
        config = getResources().getConfiguration();
        toolbar = (Toolbar) findViewById(R.id.new_note_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        if (title_text.getText().toString().isEmpty())
            getSupportActionBar().setTitle(R.string.new_note_string);
        else getSupportActionBar().setTitle(R.string.edit_note_string);
        toolbar.setBackgroundResource(R.color.notes_primary);
    }

    private void setTexts() {

        String title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");

        if (date == null)
             date = simpleDateFormat.format(System.currentTimeMillis());

        title_edit.setText(title);
        date_text.setText(date);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf");
        date_text.setTypeface(font);
    }

    private void setListeners() {
        title_edit.addTextChangedListener(this);
    }

    private void initializeViews() {

        note_item = findViewById(R.id.note_item);
        title_text = (TextView) note_item.findViewById(R.id.note_title);
        title_edit = (EditText) findViewById(R.id.title_edit);
        date_text = (TextView) note_item.findViewById(R.id.note_date);
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

    public void finishNote() {

        String title = title_edit.getText().toString();


        database.updateData2(id, title, date);

        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        keyboardVisible(false);
    }

    private void keyboardVisible(boolean visible) {

        if (visible) imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        else {

            View view = this.getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        title_text.setText(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
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
}
