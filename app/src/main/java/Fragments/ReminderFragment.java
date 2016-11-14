package Fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.user.student.Helper;
import com.example.user.student.R;

import Interfaces.ReminderListener;

/**
 * Created by gamrian on 01/10/2016.
 */

public class ReminderFragment extends DialogFragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    boolean hasTime;
    ReminderListener listener;
    Context ctx;
    Spinner spinner;
    TextView time_text;
    String[] choices;
    int hour;
    int minute;
    TextView done_text;
    TextView at_text;
    String units;
    EditText count_edit;
    int count;

    public ReminderFragment(){
    }

    public ReminderFragment(Context ctx, int reminder_count, String reminder_units, int reminder_hour, int reminder_minute, ReminderListener listener, boolean hasTime){
        this.ctx = ctx;
        this.listener = listener;
        this.count = reminder_count;
        this.units = reminder_units;
        this.hasTime = hasTime;
        this.hour = reminder_hour;
        this.minute = reminder_minute;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reminder_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        choices = ctx.getResources().getStringArray(hasTime ? R.array.reminder_spinner2 : R.array.reminder_spinner);

        spinner = (Spinner) view.findViewById(R.id.spinner);

        count_edit = (EditText) view.findViewById(R.id.count_edit);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_dropdown_item_1line, choices);
        spinner.setAdapter(adapter);

        time_text = (TextView) view.findViewById(R.id.reminder_time_text);

        time_text.setOnClickListener(this);

        at_text = (TextView) view.findViewById(R.id.at_string);
        at_text.setText(ctx.getString(R.string.at_string) + " ");

        if (hasTime)  {
            time_text.setVisibility(View.GONE);
            at_text.setVisibility(View.GONE);
        }

        if (units == null) {
            hour = 9;
            minute = 0;
            count = 1;
            units = choices[0];
        }

        setAll();

        done_text = (TextView) view.findViewById(R.id.reminder_done);
        done_text.setOnClickListener(this);

        Helper.showKeyboard(ctx, count_edit);
        count_edit.selectAll();
    }

    private int getCurrentSpinnerPosition(String str) {
        for (int i = 0; i < choices.length; i++) {
            if (str.equals(choices[i])) return i;
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.reminder_time_text) {

            String str = time_text.getText().toString();

            int hour = Integer.parseInt(str.substring(0, str.indexOf(':')));
            int min = Integer.parseInt(str.substring(str.indexOf(':') + 1));

            new TimePickerDialog(ctx, R.style.PickersStyle, this, hour, min, true).show();
        }

        else if (id == R.id.reminder_done) {
            listener.setReminderText(Integer.parseInt(count_edit.getText().toString()), spinner.getSelectedItem().toString(), hour, minute);
            dismiss();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        listener.setReminderText(Integer.parseInt(count_edit.getText().toString()), spinner.getSelectedItem().toString(), hourOfDay, minute);

        dismiss();
    }

    private void setAll() {

        String hour = String.valueOf(this.hour);
        String min = String.valueOf(this.minute);

        if (minute < 10)
            min = "0" + minute;

        time_text.setText(hour + ":" + min);

        count_edit.setText(String.valueOf(count));

        spinner.setSelection(getCurrentSpinnerPosition(units));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        Helper.hideKeyboard((AppCompatActivity) ctx);
    }
}
