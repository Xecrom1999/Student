package com.example.user.student;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Database.CalendarDB;

public class NewEventActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    TextView time_text;
    Toolbar toolbar;
    TextView date_text;
    DatePickerDialog datePicker;
    Calendar calendar;
    int mDay;
    int mMonth;
    int mYear;
    InputMethodManager imm;
    LinearLayout date_layout;
    LinearLayout time_layout;
    LinearLayout reminder_layout;
    LinearLayout comment_layout;
    EditText comment_edit;
    ImageView remove_time;
    EditText title_edit;
    CalendarDB database;
    TextView reminder_text;

    boolean isOld;
    final static SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");

    String id;
    String title;
    String time;
    String comment;
    String reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_activity);

        database = new CalendarDB(this);

        date_layout = (LinearLayout) findViewById(R.id.event_date_layout);
        time_layout = (LinearLayout) findViewById(R.id.event_time_layout);
        reminder_layout = (LinearLayout) findViewById(R.id.event_reminder_layout);
        comment_layout = (LinearLayout) findViewById(R.id.event_comment_layout);

        date_layout.setOnClickListener(this);
        time_layout.setOnClickListener(this);
        reminder_layout.setOnClickListener(this);
        comment_layout.setOnClickListener(this);

        title_edit = (EditText) findViewById(R.id.event_title_edit);
        comment_edit = (EditText) findViewById(R.id.comment_edit);
        remove_time = (ImageView) findViewById(R.id.remove_time);
        remove_time.setOnClickListener(this);

        time_text = (TextView) findViewById(R.id.event_time_text);
        time_text.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.event_toolbar);

        date_text = (TextView) findViewById(R.id.event_date_text);
        date_text.setOnClickListener(this);

        reminder_text = (TextView) findViewById(R.id.reminder_text);

        Intent intent = getIntent();

        calendar = (Calendar) intent.getExtras().get("calendar");

        setDate(calendar);

        isOld = intent.getBooleanExtra("isOld", false);

        if (isOld) {
            id = intent.getStringExtra("id");
            title = intent.getStringExtra("title");
            time = intent.getStringExtra("time");
            comment = intent.getStringExtra("comment");
            reminder = intent.getStringExtra("reminder");

            title_edit.setText(title);
            title_edit.setSelection(title_edit.length());
            time_text.setText(time);
            comment_edit.setText(comment);
            reminder_text.setText(reminder);
        }

        remove_time.setVisibility(time_text.getText().toString().isEmpty() ? View.INVISIBLE : View.VISIBLE);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        getSupportActionBar().setTitle("");
        toolbar.setBackgroundColor(getColor(R.color.primary_second));

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void setDate(Calendar calendar) {
        String str = format.format(calendar.getTime());

        date_text.setText(str);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour = String.valueOf(hourOfDay);
        String min = String.valueOf(minute);

        if (minute < 10)
            min = "0" + minute;

        time_text.setText(hour + ":" + min);

        remove_time.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_id) {

            if (title_edit.getText().toString().trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.enter_title_string), Toast.LENGTH_SHORT).show();
                title_edit.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                return true;
            }

            hideKeyboard();
            String title = title_edit.getText().toString();
            String date = String.valueOf(calendar.getTime());
            String time = time_text.getText().toString();
            String comment = comment_edit.getText().toString();
            String reminder = reminder_text.getText().toString();

            finish();

            if (isOld) {
                database.updateData(this.id, new Event(title, date, time, comment, reminder));
                Toast.makeText(getApplicationContext(), getString(R.string.event_saved_string), Toast.LENGTH_SHORT).show();
            }
            else {
                database.insertData(new Event(title, date, time, comment, reminder));
                Toast.makeText(getApplicationContext(), getString(R.string.event_created_string), Toast.LENGTH_SHORT).show();
            }
        }

        if (id == android.R.id.home) {
            hideKeyboard();
            finish();
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.event_date_layout:
            case R.id.event_date_text:
                datePicker = new DatePickerDialog(this, R.style.PickersStyle, new DatePickerListener(), mYear, mMonth, mDay);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                showDialog(0);
                break;

            case R.id.event_time_layout:
            case R.id.event_time_text:
                String str = time_text.getText().toString();
                int hour;
                int min;
                if (str.isEmpty()) {
                    hour = 8;
                    min = 0;
                }
                else {
                    hour = Integer.parseInt(str.substring(0, str.indexOf(':')));
                    min = Integer.parseInt(str.substring(str.indexOf(':') + 1));
                }
                new TimePickerDialog(this, R.style.PickersStyle, this, hour, min, true).show();
                break;

            case R.id.remove_time:
                time_text.setText("");
                remove_time.setVisibility(View.INVISIBLE);
                break;

            case R.id.event_comment_layout:
                comment_edit.requestFocus();
                comment_edit.setSelection(comment_edit.length());
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                break;

            case R.id.event_reminder_layout:

                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return datePicker;
    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setDate(year, monthOfYear, dayOfMonth);
        }
    }

    private void setDate(int year, int month, int day) {

        calendar.set(year, month, day);

        String str = format.format(calendar.getTime());

        date_text.setText(str);

    }
    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        changeColor(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        changeColor(true);
    }

    private void changeColor(boolean isStarted) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(isStarted ? R.color.dark_second : R.color.primary_dark));
        }
    }
}