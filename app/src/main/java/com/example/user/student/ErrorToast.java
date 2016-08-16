package com.example.user.student;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by gamrian on 14/08/2016.
 */
public class ErrorToast extends Toast {

    TextView textView;

    public ErrorToast(Context context, String text) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);

        textView = (TextView) view.findViewById(R.id.toast_text);

        textView.setText(text);

        setGravity(Gravity.CENTER, 0, 0);

        this.setView(view);

        show();
    }
}
