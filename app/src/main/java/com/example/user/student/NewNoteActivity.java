package com.example.user.student;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class NewNoteActivity extends AppCompatActivity implements TextWatcher, TextView.OnEditorActionListener {

    EditText title_edit;
    EditText description_edit;
    EditText date_edit;
    TextView day_edit;
    View note_item;
    TextView title_text;
    TextView date_text;
    TextView day_text;
    int day;
    int month;
    int year;
    Calendar calendar;
    String [] days;

    DatePickerListener dateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note_activity);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        days = getResources().getStringArray(R.array.daysOfWeek);

        day_edit = (TextView) findViewById(R.id.day_text);

        note_item = findViewById(R.id.note_item);
        title_text = (TextView) note_item.findViewById(R.id.note_title);
        date_text = (TextView) note_item.findViewById(R.id.note_date);
        day_text = (TextView) note_item.findViewById(R.id.note_day);

        title_edit = (EditText) findViewById(R.id.title_edit);
        description_edit = (EditText) findViewById(R.id.description_edit);
        date_edit = (EditText) findViewById(R.id.date_edit);

        title_edit.addTextChangedListener(this);
        date_edit.addTextChangedListener(this);
        date_edit.setOnEditorActionListener(this);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");

        title_edit.setText(title);
        title_text.setText(title);

        if (date.isEmpty()) setTomorrowDate();
        else {
            date_edit.setText(date.substring(0, date.indexOf("\n")));
            day_edit.setText(date.substring(date.indexOf("\n")+1));
            date_text.setText(date_edit.getText().toString() + "\n" + day_edit.getText().toString());
        }

        description_edit.setText(description);


        title_edit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        dateListener = new DatePickerListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay_in_place, R.anim.out_to_bottom);
    }

    public void finishNote(View view) {

        if (isDateValid() == null) {
            ErrorToast toast = new ErrorToast(this, "Date is not valid");
            date_edit.requestFocus();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("title", title_edit.getText().toString());
        intent.putExtra("description", description_edit.getText().toString());
        intent.putExtra("date", date_text.getText().toString());

        setResult(0, intent);

        finish();
        overridePendingTransition(R.anim.stay_in_place, R.anim.out_to_bottom);
    }

    private Date isDateValid() {

        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

        String date = date_edit.getText().toString();

        if (date.equals("")) return new Date();

        Date d;

        try {
            format.setLenient(false);
            d = format.parse(date);
        } catch (ParseException e) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        if (d.before(cal.getTime())) return null;

        return d;
    }

    public void openCalendar(View view) {
        title_edit.requestFocus();
        showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == 0) {
            DatePickerDialog dialog = new DatePickerDialog(this, dateListener, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return dialog;
        }
        return null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        char c;

        if (title_edit.isFocused()) {
            title_text.setText(s);
            return;
        }

        else if (!date_edit.isFocused()) return;
        else if (s.length() == 0) {
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
        date_text.setText(date_edit.getText().toString() + "\n" + day_edit.getText().toString());
        
        Date date = isDateValid();
        if (date != null) {
            //day_edit.setText(getDay(date.getDay(), date.getMonth() + 1, date.getYear()));
            //date_text.setText(date_edit.getText().toString() + "\n" + day_edit.getText().toString());
        } else {
            //day_edit.setText("");
            date_text.setText(date_edit.getText().toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        finishNote(v);
        return true;
    }

    public class DatePickerListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String str;

            if (monthOfYear < 9)
                str = dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + (year - 2000);
            else str = dayOfMonth + "/" + (monthOfYear + 1) + "/" + (year - 2000);

            if (dayOfMonth < 10) str = "0" + str;

            String day = getDay(dayOfMonth, monthOfYear, year);
            day_edit.setText(day);

            date_edit.setText(str);
            date_text.setText(str + "\n" + day);

            date_edit.setSelection(8);
        }
    }

    private void setTomorrowDate() {

        String str = (day) + "/" + (month + 1) + "/" + (year - 2000);
        day_edit.setText(getDay(day, month, year));
        date_edit.setText(str);
        date_text.setText(str);
        date_text.setText(str + "\n" + day_edit.getText().toString());
    }

    private String getDay(int day, int month, int year) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);

        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, 7);

        if(c.getTime().compareTo(cal.getTime()) > 0){
            return days[cal.get(Calendar.DAY_OF_WEEK) - 1];
        }
        return days[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }
}
