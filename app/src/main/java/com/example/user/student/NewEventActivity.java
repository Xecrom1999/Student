package com.example.user.student;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
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
    boolean hasTime;
    EditText title_edit;

    CalendarDB database;

    final static SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_activity);

        database = new CalendarDB(this);

        hasTime = false;

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
        remove_time.setVisibility(View.INVISIBLE);
        remove_time.setOnClickListener(this);

        time_text = (TextView) findViewById(R.id.event_time_text);
        time_text.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.event_toolbar);

        date_text = (TextView) findViewById(R.id.event_date_text);
        date_text.setOnClickListener(this);

        calendar = Calendar.getInstance();

        mDay = getIntent().getIntExtra("day", 0);
        mMonth = getIntent().getIntExtra("month", 0);
        mYear = getIntent().getIntExtra("year", 0);

        setSupportActionBar(toolbar);

        toolbar.setOnClickListener(this);

        if (mYear == 0)
            setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        else setDate(mYear, mMonth, mDay);

        calendar.set(mYear, mMonth, mDay);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear);
        getSupportActionBar().setTitle("");

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour = String.valueOf(hourOfDay);
        String min = String.valueOf(minute);

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;

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
                Toast.makeText(getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                title_edit.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                return true;
            }

            hideKeyboard();
            String title = title_edit.getText().toString();
            String date = "error";
            try {
                date = String.valueOf(format.parse(date_text.getText().toString()));
            } catch (ParseException e) {}
            String time = time_text.getText().toString();
            String comment = comment_edit.getText().toString();
            database.insertData(new Event(title, date, time, comment));
            finish();
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
                datePicker = new DatePickerDialog(this, new DatePickerListener(), mYear, mMonth, mDay);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                showDialog(0);
                break;

            case R.id.event_time_layout:
            case R.id.event_time_text:
                String str = time_text.getText().toString();
                new TimePickerDialog(this, this, 8, 0, true).show();
                break;


            case R.id.remove_time:
                time_text.setText("");
                remove_time.setVisibility(View.INVISIBLE);
                break;

            case R.id.event_comment_layout:
                comment_edit.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                break;

            case R.id.event_reminder_layout:

                break;

            case R.id.event_toolbar:
                hideKeyboard();
                finish();
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

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);

        String str = format.format(cal.getTime());

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
}