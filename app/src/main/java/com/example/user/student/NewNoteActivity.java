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

public class NewNoteActivity extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener {

    EditText title_edit;
    EditText description_edit;
    TextView date_edit;
    TextView time_edit;

    TextView title_text;
    TextView date_text;
    TextView time_text;

    LinearLayout date_layout;
    CheckBox checkBox;
    Configuration config;

    boolean editMode;

    View note_item;
    DatePickerDialog datePicker;
    final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
    InputMethodManager imm;

    Toolbar toolbar;
    LinearLayout box_layout;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note_activity);

        initializeViews();
        setListeners();
        setTexts();

        setupToolbar();

        format.setLenient(false);

        title_edit.requestFocus();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboardVisible(true);

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
        else getSupportActionBar().setTitle("");
    }

    private void setTexts() {
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");

        title_edit.setText(title);
        description_edit.setText(description);

        if (title.isEmpty()) {
            setTomorrowDate();
            editMode = true;
        }

        else if (date.isEmpty()) checkBox.setChecked(false);

        else {
            date_edit.setText(date);
            date_text.setText(date);
            setTime();
        }

        if (!title.isEmpty()) cantEdit();

        String date2 = date_edit.getText().toString();
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(format.parse(date2));
            datePicker = new DatePickerDialog(this, new DatePickerListener(), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {}
    }

    private void cantEdit() {
        editMode = false;

        title_edit.setEnabled(false);
        description_edit.setEnabled(false);
        date_layout.setEnabled(false);
        box_layout.setEnabled(false);
        checkBox.setEnabled(false);
    }

    private void startEditing() {
        editMode = true;

        title_edit.setEnabled(true);
        description_edit.setEnabled(true);
        date_layout.setEnabled(true);
        box_layout.setEnabled(true);
        checkBox.setEnabled(true);
    }

    private void setListeners() {
        title_edit.addTextChangedListener(this);
        description_edit.setOnFocusChangeListener(this);
        checkBox.setOnCheckedChangeListener(this);
    }

    private void initializeViews() {

        layout = (LinearLayout) findViewById(R.id.linearLayout);

        box_layout = (LinearLayout) findViewById(R.id.box_layout);

        date_layout = (LinearLayout) findViewById(R.id.date_layout);

        checkBox = (CheckBox) findViewById(R.id.date_check_box);

        time_edit = (TextView) findViewById(R.id.time_text);

        note_item = findViewById(R.id.note_item);
        title_text = (TextView) note_item.findViewById(R.id.note_title);
        date_text = (TextView) note_item.findViewById(R.id.note_date);
        time_text = (TextView) note_item.findViewById(R.id.note_time);

        title_edit = (EditText) findViewById(R.id.title_edit);
        description_edit = (EditText) findViewById(R.id.description_edit);
        date_edit = (TextView) findViewById(R.id.date_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_note_menu, menu);
        if (!getIntent().getStringExtra("title").isEmpty()) menu.getItem(0).setIcon(R.drawable.ic_edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.done_id) {
            if (!editMode) {
                item.setIcon(R.mipmap.ic_done);
                startEditing();
            }
            else finishNote();
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

    public void finishNote() {
        
        if (title_edit.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.enter_title_string, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("title", title_edit.getText().toString());
        intent.putExtra("description", description_edit.getText().toString());
        if (checkBox.isChecked()) {
            intent.putExtra("date", date_edit.getText().toString());
            intent.putExtra("time", time_edit.getText().toString());
        } else {
            intent.putExtra("date", "");
            intent.putExtra("time", "");
        }

        setResult(0, intent);

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

    private boolean isDateValid() {

        String date = date_edit.getText().toString();

        if (date.equals("")) return true;

        Date d;

        try {
            d = format.parse(date);
        } catch (ParseException e) {
            return false;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, - 1);

        if (d.before(cal.getTime())) return false;

        return true;
    }

    public void openCalendar(View view) {
        showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePicker;
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        date_layout.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);

        date_text.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        time_text.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);

        if (date_edit.getText().toString().trim().isEmpty()) setTomorrowDate();
    }

    public void toggleBox(View view) {
        checkBox.toggle();
    }

    public class DatePickerListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            setDate(dayOfMonth, monthOfYear + 1, year - 2000);
        }
    }

    private void setTomorrowDate() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        setDate(day, month + 1, year - 2000);
    }

    private void setDate(int day, int month, int year) {

        String str = month + "/" + year;
        if (month < 10) str = "0" + str;
        str = day + "/" + str;
        if (day < 10) str = "0" + str;

        date_edit.setText(str);
        date_text.setText(str);

        setTime();
    }

    public void setTime() {

        if (!isDateValid()) {
            time_edit.setText("-");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

        String dateString = date_edit.getText().toString();

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

            else timeToDate = sdf.format(date);
            time_edit.setText(timeToDate);
            time_text.setText(timeToDate);
        } catch (ParseException e) {
            timeToDate = getString(R.string.passed_string);
        }
    }
}
