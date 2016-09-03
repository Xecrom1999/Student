package com.example.user.student;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    TextView time_text;
    Toolbar toolbar;
    TextView date_text;
    DatePickerDialog datePicker;
    Calendar calendar;
    int mDay;
    int mMonth;
    int mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_activity);

        time_text = (TextView) findViewById(R.id.event_time_text);
        time_text.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.event_toolbar);

        date_text = (TextView) findViewById(R.id.event_date_text);
        date_text.setOnClickListener(this);

        mDay = getIntent().getIntExtra("day", 0);
        mMonth = getIntent().getIntExtra("month", 0);
        mYear = getIntent().getIntExtra("year", 0);

        setDate(mYear, mMonth, mDay);

        calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth, mDay);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear);

        getSupportActionBar().setTitle(getString(R.string.new_event_string));
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_id) finish();

        if (id == android.R.id.home) finish();

        return true;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.event_time_text:
                String str = time_text.getText().toString();
                new TimePickerDialog(this, this, Integer.valueOf(str.substring(0, str.indexOf(':'))), Integer.valueOf(str.substring(str.indexOf(':') + 1, str.length())), true).show();
                break;
            case R.id.event_date_text:
                datePicker = new DatePickerDialog(this, new DatePickerListener(), mYear, mMonth, mDay);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                showDialog(0);
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

        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy");

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);

        String str = format.format(cal.getTime());

        date_text.setText(str);
    }
}