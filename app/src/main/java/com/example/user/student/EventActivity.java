package com.example.user.student;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Database.CalendarDB;

public class EventActivity extends AppCompatActivity {

    TextView time_text;
    Toolbar toolbar;
    TextView date_text;
    Calendar calendar;
    LinearLayout date_layout;
    LinearLayout time_layout;
    LinearLayout reminder_layout;
    LinearLayout comment_layout;
    TextView comment_edit;
    TextView title_edit;
    CalendarDB database;
    TextView reminder_text;

    String id;
    String title;
    String time;
    String comment;
    String reminder;

    final SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);

        database = new CalendarDB(this);

        date_layout = (LinearLayout) findViewById(R.id.event_date_layout);
        time_layout = (LinearLayout) findViewById(R.id.event_time_layout);
        reminder_layout = (LinearLayout) findViewById(R.id.event_reminder_layout);
        comment_layout = (LinearLayout) findViewById(R.id.event_comment_layout);

        title_edit = (TextView) findViewById(R.id.event_title_edit);
        comment_edit = (TextView) findViewById(R.id.comment_edit);

        time_text = (TextView) findViewById(R.id.event_time_text);
        toolbar = (Toolbar) findViewById(R.id.event_toolbar);

        date_text = (TextView) findViewById(R.id.event_date_text);
        reminder_text = (TextView) findViewById(R.id.reminder_text);


        Intent intent = getIntent();

        calendar = (Calendar) intent.getExtras().get("calendar");

        setDate(calendar);

        Cursor res = database.getRowByDate(String.valueOf(calendar.getTime()));
        res.moveToNext();
        int num = intent.getIntExtra("position", 0);
        while (num != 0 && res.moveToNext()) num--;

        id = res.getString(0);
        title = res.getString(1);
        time = res.getString(3);
        comment = res.getString(4);
        reminder = res.getString(5);

        title_edit.setText(title);

        if (time.isEmpty()) time_layout.setVisibility(View.GONE);
        else time_text.setText(time);

        if (comment.isEmpty()) comment_layout.setVisibility(View.GONE);
        else comment_edit.setText(comment);

        if (reminder.isEmpty()) reminder_layout.setVisibility(View.GONE);
        else reminder_text.setText(reminder);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        getSupportActionBar().setTitle("");
        toolbar.setBackgroundColor(getColor(R.color.primary_second));

    }

    private void setDate(Calendar calendar) {
        String str = format.format(calendar.getTime());

        date_text.setText(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.event_edit) {
            Intent intent = new Intent(this, NewEventActivity.class);
            intent.putExtra("isOld", true);
            intent.putExtra("title", title);
            intent.putExtra("time", time);
            intent.putExtra("comment", comment);
            intent.putExtra("reminder", reminder);
            intent.putExtra("id", this.id);
            intent.putExtra("calendar", calendar);
            startActivity(intent);
            finish();
        }

        if (id == android.R.id.home) {
            finish();
        }

        return true;
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