package com.example.user.student;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Toast;

import Database.NotesDB;

public class NewNoteActivity extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener {

    EditText title_edit;
    EditText description_edit;

    TextView title_text;

    Configuration config;

    NotesDB database;

    boolean isNew;

    View note_item;
    InputMethodManager imm;

    Toolbar toolbar;
    LinearLayout layout;

    Intent intent;

    String id;

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
        String description = intent.getStringExtra("description");

        title_edit.setText(title);
        description_edit.setText(description);
    }

    private void setListeners() {
        title_edit.addTextChangedListener(this);
        description_edit.setOnFocusChangeListener(this);
    }

    private void initializeViews() {

        layout = (LinearLayout) findViewById(R.id.linearLayout);

        note_item = findViewById(R.id.note_item);
        title_text = (TextView) note_item.findViewById(R.id.note_title);

        title_edit = (EditText) findViewById(R.id.title_edit);
        description_edit = (EditText) findViewById(R.id.description_edit);
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
        overridePendingTransition(R.anim.stay_in_place, R.anim.out_to_bottom);
    }

    public void finishNote() {

        String title = title_edit.getText().toString();

        if (title.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.enter_title_string, Toast.LENGTH_SHORT).show();
            return;
        }

        String description = description_edit.getText().toString();

        database.updateData2(id, title, description);

        finish();
        overridePendingTransition(R.anim.stay_in_place, R.anim.out_to_bottom);
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
    public void onFocusChange(View v, boolean hasFocus) {
        description_edit.setSelection(description_edit.length());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.note_status));
        }
    }
}
