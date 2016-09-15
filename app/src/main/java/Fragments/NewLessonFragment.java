package Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.user.student.R;

/**
 * Created by gamrian on 07/09/2016.
 */
public class NewLessonFragment extends DialogFragment {

    private final Context ctx;
    private final int position;
    EditText editText;
    NumberPicker numberPicker;
    TextView number_text;

    public NewLessonFragment(Context ctx, int position) {
        this.ctx = ctx;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_lesson_fragment, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = (EditText) view.findViewById(R.id.lesson_edit);
        number_text = (TextView) view.findViewById(R.id.new_lesson_title);

        number_text.setText(getContext().getString(R.string.lesson_string) + " " + position);

        numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);

        String[] numbers = new String[24];
        for (int i = 0; i < numbers.length; i++)
            numbers[i] = Integer.toString(i*5+5);
        numberPicker.setDisplayedValues(numbers);
        numberPicker.setMaxValue(numbers.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setValue(8);
        numberPicker.setWrapSelectorWheel(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(995, 1500);
    }


}
