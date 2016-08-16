package com.example.user.student;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewNoteActivity extends AppCompatActivity implements TextWatcher, TextView.OnEditorActionListener, View.OnFocusChangeListener {

    EditText title_edit;
    EditText description_edit;
    EditText date_edit;
    TextView time_edit;

    TextView title_text;
    TextView date_text;
    TextView time_text;

    View note_item;
    DatePickerDialog datePicker;
    final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note_activity);

        //TODO set time text to weeks and months.
        //TODO hebrew

        initializeViews();
        setListeners();
        setTexts();

        format.setLenient(false);

        title_edit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void setTexts() {
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");

        title_edit.setText(title);
        description_edit.setText(description);

        if (date.isEmpty()) setTomorrowDate();
        else {
            date_edit.setText(date);
            setDay();
        }

        String date2 = date_edit.getText().toString();
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(format.parse(date2));
            datePicker = new DatePickerDialog(this, new DatePickerListener(), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {}
    }


    private void setListeners() {
        title_edit.addTextChangedListener(new TextWatcher() {
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
        });
        date_edit.addTextChangedListener(this);
        date_edit.setOnFocusChangeListener(this);
        date_edit.setOnEditorActionListener(this);
        time_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                time_text.setText(s);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        description_edit.setOnFocusChangeListener(this);
    }

    private void initializeViews() {
        time_edit = (TextView) findViewById(R.id.time_text);

        note_item = findViewById(R.id.note_item);
        title_text = (TextView) note_item.findViewById(R.id.note_title);
        date_text = (TextView) note_item.findViewById(R.id.note_date);
        time_text = (TextView) note_item.findViewById(R.id.note_time);

        title_edit = (EditText) findViewById(R.id.title_edit);
        description_edit = (EditText) findViewById(R.id.description_edit);
        date_edit = (EditText) findViewById(R.id.date_edit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay_in_place, R.anim.out_to_bottom);
    }

    public void finishNote(View view) {

        if (!isDateValid()) {
            new ErrorToast(this, "Date is not valid");
            date_edit.requestFocus();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("title", title_edit.getText().toString());
        intent.putExtra("description", description_edit.getText().toString());
        intent.putExtra("date", date_edit.getText().toString());
        intent.putExtra("time", time_edit.getText().toString());

        setResult(0, intent);

        finish();
        overridePendingTransition(R.anim.stay_in_place, R.anim.out_to_bottom);
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

        char c;

        if (s.length() == 0) {
            date_text.setText("");
            return;
        }
        else if (count != 0)
        if ((start == 1 || start == 4)) {
            date_edit.append("/");
        }

        else if ((start == 2 || start == 5) && s.toString().charAt(s.length()-1) != '/') {
            String str = s.toString();
            c = s.toString().charAt(s.length()-1);
            str = str.substring(0, str.length()-1);
            str = str + "/" + c;
            date_edit.setText(str);
            date_edit.setSelection(str.length()-1);
        }
        date_text.setText(date_edit.getText().toString());
        setDay();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        finishNote(v);
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v.getId() == R.id.description_edit)
                description_edit.setSelection(description_edit.length());
            if (v.getId() == R.id.date_edit)
                date_edit.setSelection(date_edit.length());
        }
    }

    public class DatePickerListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


            setDate(dayOfMonth, monthOfYear + 1, year - 2000);
            date_edit.setSelection(8);
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
        if (day < 10) str += "0";

        date_edit.setText(str);
    }

    public void setDay() {

        if (!isDateValid()) {
            time_edit.setText("-");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

        String dateString = date_edit.getText().toString();

        Date date;

        String day;

        try {
            date = format.parse(dateString);

            if (DateUtils.isToday(date.getTime())) day = getResources().getString(R.string.today_string);

            else day = sdf.format(date);
            time_edit.setText(day);
        } catch (ParseException e) {
            time_edit.setText("-");
        }
    }
}
