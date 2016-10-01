package com.example.user.student;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    TextView title_text;
    TextView description_text;
    TextView date_text;
    TextView time_text;

    Configuration config;

    String id;
    final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

    Toolbar toolbar;
    LinearLayout layout;

    String title;
    String description;
    String date;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);

        initializeViews();

        setupToolbar();

        format.setLenient(false);

        intent = getIntent();

        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        date = intent.getStringExtra("date");

        id = intent.getStringExtra("id");

        title_text.setText(title);
        description_text.setText(description);
        date_text.setText(date);
        setTime();

    }

    private void setupToolbar() {
        config = getResources().getConfiguration();
        toolbar = (Toolbar) findViewById(R.id.note_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        if (title_text.getText().toString().isEmpty())
            getSupportActionBar().setTitle(R.string.new_note_string);
        else getSupportActionBar().setTitle("");
    }


    private void initializeViews() {
        title_text = (TextView) findViewById(R.id.note_title);
        description_text = (TextView) findViewById(R.id.note_description);
        date_text = (TextView) findViewById(R.id.note_date);
        time_text = (TextView) findViewById(R.id.note_time);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.edit_note_id) {
            Intent intent;

            intent = new Intent(this, NewNoteActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("description", description);
            intent.putExtra("date", date);
            intent.putExtra("id", this.id);

            startActivity(intent);
            finish();
        }

        if (id == android.R.id.home) onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay_in_place, R.anim.out_to_bottom);
    }

    public void setTime() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

        String dateString = date_text.getText().toString();

        Date date;

        String timeToDate;

        Calendar cal = Calendar.getInstance();

        try {
            date = format.parse(dateString);

            cal.setTime(date);
            cal.add(Calendar.DATE, -1);

            int diff = (int)((date.getTime() - System.currentTimeMillis()) / (24 * 60 * 60 * 1000) + 1);

            if (diff > 365) timeToDate = getString(R.string.year_string);

            else if (diff > 29) {
                diff /= 30;
                if (diff == 1) timeToDate = getString(R.string.month_string);
                else timeToDate =  "  " + diff + "  " + getString(R.string.months_string);
            }

            else if (diff > 6) {
                diff /= 7;
                if (diff == 1) timeToDate = getString(R.string.week_string);
                else timeToDate = "  " + diff + "  " + getString(R.string.weeks_string);
            }

            else if (DateUtils.isToday(cal.getTime().getTime())) timeToDate = getString(R.string.tomorrow_string);

            else if (DateUtils.isToday(date.getTime())) timeToDate = getString(R.string.today_string);

            else if (date.before(Calendar.getInstance().getTime())) timeToDate = getString(R.string.passed_string);

            else timeToDate = sdf.format(date);
            time_text.setText(timeToDate);
        } catch (ParseException e) {}
    }
}
