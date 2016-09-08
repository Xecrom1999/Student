package com.example.user.student;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by user on 15/01/16.
 */
public class NewLessonActivity extends Activity implements View.OnClickListener {

    EditText name_input;
    TextView setTime_text;
    NumberPicker numberPicker;
    ImageButton done_button;
    ImageButton cancel_button;
    boolean isNew;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setContentView(R.layout.new_lesson_activity);

        name_input = (EditText) findViewById(R.id.name_input);
        name_input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        setTime_text = (TextView) findViewById(R.id.setTime_text);
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(180);
        numberPicker.setValue(45);

        setTime_text.setOnClickListener(this);

        done_button = (ImageButton) findViewById(R.id.done_button);
        done_button.setOnClickListener(this);

        cancel_button = (ImageButton) findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(this);

        Intent intent = getIntent();
        isNew = intent.getBooleanExtra("isNew", false);
        if (!isNew) {
            String name = intent.getStringExtra("name");
            String time = intent.getStringExtra("time");
            String length = intent.getStringExtra("length");

            name_input.setText(name);
            setTime_text.setText(time.substring(time.indexOf('0')));
            numberPicker.setValue(Integer.valueOf(length));
        }
    }

    public void onClick(View v) {

        if (v.getId() == R.id.setTime_text) {
            String str = setTime_text.getText().toString();
            new TimePickerDialog(this, onTimeSetListener, Integer.valueOf(str.substring(0, str.indexOf(':'))), Integer.valueOf(str.substring(str.indexOf(':') + 1, str.length())), true).show();
        }

        if (v.getId() == R.id.cancel_button) {
            finish();
            overridePendingTransition(R.anim.stay_in_place, R.anim.out_to_bottom);
        }

        if (v.getId() == R.id.done_button) {
            if (name_input.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter the class name!", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent();
                intent.putExtra("name", name_input.getText().toString());
                intent.putExtra("time", setTime_text.getText().toString());
                intent.putExtra("length", String.valueOf(numberPicker.getValue()));
                intent.putExtra("isNew", isNew);
                setResult(1, intent);
                finish();
                overridePendingTransition(R.anim.stay_in_place, R.anim.out_to_bottom);
            }
        }
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hour = String.valueOf(hourOfDay);
            String min = String.valueOf(minute);


            if (hourOfDay < 10)
                hour = "0" + hourOfDay;

            if (minute < 10)
                min = "0" + minute;

            setTime_text.setText(hour + ":" + min);
        }
    };

    public void deleteInput(View view) {
        name_input.setText("");
    }
}
