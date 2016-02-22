package com.example.user.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.LessonsNamesAdapter;
import Interfaces.Communicator;
import Interfaces.LessonsNameListener;

/**
 * Created by user on 15/01/16.
 */
public class LessonActivity extends Activity implements View.OnClickListener, Communicator, LessonsNameListener {

    EditText name_input;
    TextView setTime_text;
    NumberPicker numberPicker;
    RecyclerView recyclerView;
    LessonsNamesAdapter adapter;
    Button addButton;
    ImageButton done_button;
    ImageButton cancel_button;
    boolean isNew;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setContentView(R.layout.lesson_activity);

        name_input = (EditText) findViewById(R.id.name_input);
        name_input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        setTime_text = (TextView) findViewById(R.id.setTime_text);
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(180);
        numberPicker.setValue(45);

        setTime_text.setOnClickListener(this);

        adapter = new LessonsNamesAdapter(this, this);

        recyclerView = (RecyclerView) findViewById(R.id.classesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        addButton = (Button) findViewById(R.id.lessonsList_button);
        addButton.setOnClickListener(this);

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

        if (v.getId() == R.id.lessonsList_button) {
            View view = getLayoutInflater().inflate(R.layout.name_dialog_layout, null);
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            final EditText editText = (EditText) view.findViewById(R.id.name_input2);
            editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            editText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (keyCode == EditorInfo.IME_ACTION_DONE) {

                        String name = editText.getText().toString();

                        if (!name.matches("")){
                            adapter.addLesson(name);
                            adapter.notifyDataSetChanged();
                        }

                        return true;
                    }

                    return false;
                }
            });

            dialog.setView(view);
            dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String name = editText.getText().toString();

                    if (!name.matches("")){
                        adapter.addLesson(name);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            Dialog d = dialog.create();
            d.show();
        }

        if (v.getId() == R.id.cancel_button) {
            finish();
            overridePendingTransition(R.anim.stay_in_place, R.anim.out_to_bottom);
        }

        if (v.getId() == R.id.done_button) {
            if (name_input.getText().toString().isEmpty()) {
                Toast.makeText(this, "Enter the class name!", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent();
                intent.putExtra("name", name_input.getText().toString());
                intent.putExtra("time", setTime_text.getText().toString());
                intent.putExtra("length", numberPicker.getValue());
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

    public void lessonDone(Lesson lesson) {
    }

    public void newLesson(Lesson lesson) {
    }

    public void updateLesson(int position, Lesson lesson) {

    }

    public ArrayList<Lesson> getList(int p) {
        return null;
    }

    public int getPagerPosition() {
        return 0;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay_in_place, R.anim.out_to_bottom);
    }

    public void recyclerViewListClicked(String name) {
        name_input.setText(name);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
