package com.example.user.student;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import Database.DefaultLessonsDB;

/**
 * Created by user on 15/01/16.
 */
public class NewLessonActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    EditText subject_edit;
    TextView time_text;
    Button done_button;
    Button cancel_button;
    EditText length_edit;
    boolean isNew;
    Toolbar toolbar;
    CheckBox checkBox;
    DefaultLessonsDB database;
    int lessonPosition;
    Configuration config;
    String id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_lesson_activity);

        database = new DefaultLessonsDB(this);

        checkBox = (CheckBox) findViewById(R.id.default_checkBox);

        subject_edit = (EditText) findViewById(R.id.subject_edit);

        time_text = (TextView) findViewById(R.id.setTime_text);

        done_button = (Button) findViewById(R.id.done_button);
        done_button.setOnClickListener(this);

        cancel_button = (Button) findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(this);

        length_edit = (EditText) findViewById(R.id.length_edit);
        length_edit.setOnClickListener(this);

        Intent intent = getIntent();
        lessonPosition = intent.getIntExtra("itemPosition", 99);

        checkBox.setText(getString(R.string.setDefaultLesson_string) + " " + (lessonPosition + 1));
        setToolbar();
        getSupportActionBar().setTitle(getString(R.string.lesson_string) + " " + (lessonPosition + 1));

        isNew = intent.getBooleanExtra("isNew", false);
        if (!isNew) {
            String name = intent.getStringExtra("name");
            String time = intent.getStringExtra("time");
            String length = intent.getStringExtra("length");

            subject_edit.setText(name);
            time_text.setText(time);
            length_edit.setText(length);
            subject_edit.setSelection(name.length());
        }

        int pos = lessonPosition;

        if (pos < 12){
            Cursor res = database.getAllData();
            res.moveToNext();
            while (pos > 0 && res.moveToNext()) pos--;

            time_text.setText(res.getString(1));
            length_edit.setText(res.getString(2));
            id = res.getString(0);
        }
    }

    private void setToolbar() {
        config = getResources().getConfiguration();
        toolbar = (Toolbar) findViewById(R.id.new_lesson_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        toolbar.setBackgroundColor(getColor(R.color.new_lesson_primary));
        toolbar.setAlpha(0.6f);
    }

    public void onClick(View v) {

        if (v.getId() == R.id.length_edit)
            length_edit.selectAll();

        if (v.getId() == R.id.cancel_button)
            onBackPressed();
        
        if (v.getId() == R.id.done_button)
            lessonDone();
    }

    private void lessonDone() {
        if (subject_edit.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_name_string), Toast.LENGTH_SHORT).show();
            return;
        }

        if (length_edit.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_length_string), Toast.LENGTH_SHORT).show();
            return;
        }

        String time = time_text.getText().toString();
        String length = length_edit.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("name", subject_edit.getText().toString());
        intent.putExtra("time", time);
        intent.putExtra("length", length);
        intent.putExtra("isNew", isNew);
        intent.putExtra("position", lessonPosition);
        setResult(1, intent);

        if (checkBox.isChecked())
            database.updateData(id, time, length);

        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        changeColor(false);
    }

    private void changeColor(boolean isStarted) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(isStarted ? R.color.new_lesson_dark : R.color.dark_schedule));
        }
    }

    public void openTimeDialog(View view) {

        String time = time_text.getText().toString();

        int hour = Integer.parseInt(time.substring(0, time.indexOf(':')));
        int minute = Integer.parseInt(time.substring(time.indexOf(':') + 1));

        new TimePickerDialog(this, R.style.NewLessonDialog, this, hour, minute, true).show();
    }

    public void lengthClicked(View view) {
        length_edit.requestFocus();
        length_edit.selectAll();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour = String.valueOf(hourOfDay);
        String min = String.valueOf(minute);

        if (minute < 10)
            min = "0" + minute;

        time_text.setText(hour + ":" + min);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        changeColor(true);
    }
}
