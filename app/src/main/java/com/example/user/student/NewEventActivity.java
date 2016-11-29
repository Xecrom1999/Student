package com.example.user.student;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Database.CalendarDB;
import Fragments.ReminderFragment;
import Interfaces.ReminderListener;

public class NewEventActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, ReminderListener {

    TextView time_text;
    Toolbar toolbar;
    TextView date_text;
    DatePickerDialog datePicker;
    Calendar calendar;
    int mDay;
    int mMonth;
    int mYear;
    LinearLayout date_layout;
    LinearLayout time_layout;
    LinearLayout reminder_layout;
    LinearLayout comment_layout;
    EditText comment_edit;
    ImageView remove_time;
    ImageView remove_reminder;
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

    int reminder_hour;
    int reminder_minute;
    int reminder_count;
    String reminder_units;

    int hour;
    int minute;

    AlarmManager alarmManager;

    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_activity);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

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

        remove_reminder = (ImageView) findViewById(R.id.remove_reminder);
        remove_reminder.setOnClickListener(this);

        time_text = (TextView) findViewById(R.id.event_time_text);
        toolbar = (Toolbar) findViewById(R.id.event_toolbar);

        date_text = (TextView) findViewById(R.id.event_date_text);

        reminder_text = (TextView) findViewById(R.id.reminder_text);

        Intent intent = getIntent();

        calendar = (Calendar) intent.getExtras().get("calendar");

        setDate(calendar);

        isOld = intent.getBooleanExtra("isOld", false);

        num = intent.getIntExtra("position", 0);

        if (isOld) {
            Cursor res = database.getRowByDate(String.valueOf(calendar.getTime()));
            res.moveToNext();
            int i = num;
            while (i != 0 && res.moveToNext()) i--;

            id = res.getString(0);
            title = res.getString(1);
            time = res.getString(3);
            comment = res.getString(4);
            reminder = res.getString(5);

            title_edit.setText(title);
            title_edit.setSelection(title_edit.length());
            time_text.setText(time);
            comment_edit.setText(comment);

            if (!reminder.isEmpty()) {
                int reminder_count = Integer.parseInt(reminder.substring(reminder.indexOf("count") + 5, reminder.indexOf("units")));
                String reminder_units = reminder.substring(reminder.indexOf("units") + 5, reminder.indexOf("hour"));
                int reminder_hour = Integer.parseInt(reminder.substring(reminder.indexOf("hour") + 4, reminder.indexOf("minute")));
                int reminder_minute = Integer.parseInt(reminder.substring(reminder.indexOf("minute") + 6));
                setReminderText(reminder_count, reminder_units, reminder_hour, reminder_minute);
            }
        }

        remove_time.setVisibility(time_text.getText().toString().isEmpty() ? View.INVISIBLE : View.VISIBLE);
        remove_reminder.setVisibility(reminder_text.getText().toString().isEmpty() ? View.INVISIBLE : View.VISIBLE);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        getSupportActionBar().setTitle("");
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary_new_event));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Helper.setupAd(this);
    }

    private void setDate(Calendar calendar) {
        String str = format.format(calendar.getTime());

        date_text.setText(str);

        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMonth = calendar.get(Calendar.MONTH);
        mYear = calendar.get(Calendar.YEAR);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour = String.valueOf(hourOfDay);
        String min = String.valueOf(minute);

        if (minute < 10)
            min = "0" + minute;

        if (time_text.getText().toString().isEmpty()) {
            reminder_text.setText("");
            remove_reminder.setVisibility(View.INVISIBLE);
        }

        time_text.setText(hour + ":" + min);

        remove_time.setVisibility(View.VISIBLE);

        this.hour = hourOfDay;
        this.minute = minute;
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
            saveEvent();
        }

        if (id == android.R.id.home) {
            Helper.hideKeyboard(this);
            finish();
        }

        return true;
    }

    private void saveEvent() {

        if (title_edit.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.enter_title_string), Toast.LENGTH_SHORT).show();
            Helper.showKeyboard(this, title_edit);
            return;
        }

        Helper.hideKeyboard(this);

        String title = title_edit.getText().toString();
        String date = String.valueOf(calendar.getTime());
        String time = time_text.getText().toString();
        String comment = comment_edit.getText().toString();

        String reminder;

        if (!time_text.getText().toString().isEmpty()) reminder_hour = -1;

        if (reminder_text.getText().toString().isEmpty())
            reminder = "";
        else
            reminder = "count" + reminder_count + "units" + reminder_units + "hour" + reminder_hour +  "minute" + reminder_minute;

        Event event = new Event(title, date, time, comment, reminder);

        finish();

        if (isOld) {
            database.updateData(this.id, event, calendar);
            Toast.makeText(getApplicationContext(), getString(R.string.event_saved_string), Toast.LENGTH_SHORT).show();
        }
        else {
            this.id = String.valueOf(database.insertData(event, calendar));
            Toast.makeText(getApplicationContext(), getString(R.string.event_created_string), Toast.LENGTH_SHORT).show();
        }

        if (reminder_text.getText().toString().isEmpty())
            alarmManager.cancel(PendingIntent.getBroadcast(this, Integer.parseInt(this.id), new Intent(this, AlarmReceiver.class), 0));
        else setReminder(event);
    }

    private void setReminder(Event event) {

        String[] arr = getResources().getStringArray(R.array.reminder_spinner2);
        int units;
        if (reminder_units.equals(arr[0])) units = Calendar.MINUTE;
        else if (reminder_units.equals(arr[1])) units = Calendar.HOUR;
        else if (reminder_units.equals(arr[2])) units = Calendar.DATE;
        else units = Calendar.WEEK_OF_YEAR;

        boolean hasTime = !time_text.getText().toString().isEmpty();

        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar.getTime());

        if (!hasTime) {
            cal.set(Calendar.HOUR_OF_DAY, reminder_hour);
            cal.set(Calendar.MINUTE, reminder_minute);
        } else {
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
        }
        cal.add(units, -reminder_count);

        if (cal.getTime().getTime() < System.currentTimeMillis() - 60000) return;

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("title", event.getTitle());
        alarmIntent.putExtra("calendar", calendar);
        alarmIntent.putExtra("comment", event.getComment());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(id), alarmIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.event_date_layout:
                datePicker = new DatePickerDialog(this, R.style.NewEventDialog, new DatePickerListener(), mYear, mMonth, mDay);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.show();
                break;

            case R.id.event_time_layout:
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
                new TimePickerDialog(this, R.style.NewEventDialog, this, hour, min, true).show();
                break;

            case R.id.remove_time:
                time_text.setText("");
                remove_time.setVisibility(View.INVISIBLE);
                reminder_text.setText("");
                remove_reminder.setVisibility(View.INVISIBLE);
                break;

            case R.id.event_comment_layout:
                Helper.showKeyboard(this, comment_edit);
                break;

            case R.id.event_reminder_layout:
                new ReminderFragment(this, this.reminder_count, this.reminder_units, this.reminder_hour, this.reminder_minute, this, time_text.getText().toString().isEmpty() ? false : true).show(getSupportFragmentManager(), "");
                break;

            case R.id.remove_reminder:
                reminder_text.setText("");
                remove_reminder.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void setReminderText(int count, String unitsBefore, int hour, int minute) {

        boolean hasTime = !time_text.getText().toString().isEmpty();

        String m = String.valueOf(minute);

        if (minute < 10)
            m = "0" + minute;

        String str = count + " " + unitsBefore;
        boolean f = unitsBefore.equals(getResources().getStringArray(R.array.reminder_spinner)[0]);

        if (count < 3)
        if (hasTime) {
            String[] arr = getResources().getStringArray(R.array.reminder_spinner2);
            if (count == 0) str = getString(R.string.on_event_time_string);

            else if (count == 1) {
                if (unitsBefore.equals(arr[0])) str = getString(R.string.minute_before_string);
                else if (unitsBefore.equals(arr[1])) str = getString(R.string.hour_before_string);
                else if (unitsBefore.equals(arr[2])) str = getString(R.string.day_before_string);
                else if (unitsBefore.equals(arr[3])) str = getString(R.string.week_before_string);
            }
            else if (count == 2) {
                if (unitsBefore.equals(arr[0])) str = getString(R.string.two_minutes_before_string);
                else if (unitsBefore.equals(arr[1])) str = getString(R.string.two_hours_before_string);
                else if (unitsBefore.equals(arr[2])) str = getString(R.string.two_days_before_string);
                else if (unitsBefore.equals(arr[3])) str = getString(R.string.two_weeks_before_string);
            }

        } else {
            if (count == 0) str = getString(R.string.on_the_day_string);
            else if (count == 1) str = getString(f ? R.string.day_before_string : R.string.week_before_string);
            else if (count == 2) str = getString(f ? R.string.two_days_before_string : R.string.two_weeks_before_string);
        }

        if (hasTime) reminder_text.setText(str);
        else {
            reminder_text.setText(str + " " +  getString(R.string.at_string) + " " + hour + ":" + m);
            this.reminder_hour = hour;
            this.reminder_minute = minute;
        }

        this.reminder_count = count;
        this.reminder_units = unitsBefore;

        remove_reminder.setVisibility(View.VISIBLE);
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
        Helper.hideKeyboard(this);
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
            window.setStatusBarColor(getResources().getColor(isStarted ? R.color.dark_new_event : R.color.dark_color));
        }
    }
}