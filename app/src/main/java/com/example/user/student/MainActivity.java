package com.example.user.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    TextView schedule_button;
    TextView notes_button;
    TextView calender_button;
    TextView options_button;
    TextView title_text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6617091054237983/2023963555");

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf");

        title_text = (TextView) findViewById(R.id.main_title_text);
        title_text.setTypeface(font);

        schedule_button = (TextView) findViewById(R.id.schedule_button);
        schedule_button.setOnClickListener(this);
        schedule_button.setTypeface(font);

        notes_button = (TextView) findViewById(R.id.notes_button);
        notes_button.setOnClickListener(this);
        notes_button.setTypeface(font);

        calender_button = (TextView) findViewById(R.id.calendar_button);
        calender_button.setOnClickListener(this);
        calender_button.setTypeface(font);

        options_button = (TextView) findViewById(R.id.options_button);
        options_button.setOnClickListener(this);
        options_button.setTypeface(font);


        boolean fromNoti = getIntent().getBooleanExtra("fromNoti", false);

        if (fromNoti) {
            Intent intent = new Intent(this, CalendarActivity.class);
            intent.putExtra("fromNoti", true);
            Calendar calendar = (Calendar) getIntent().getExtras().get("calendar");
            intent.putExtra("calendar", calendar);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Helper.setupAd(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        boolean fromNoti = intent.getBooleanExtra("fromNoti", false);

        if (fromNoti) {
            Intent intent1 = new Intent(this, CalendarActivity.class);
            Calendar calendar = (Calendar) intent.getExtras().get("calendar");
            intent1.putExtra("calendar", calendar);
            intent1.putExtra("fromNoti", true);
            startActivity(intent1);
        }
    }

        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.calendar_button:
                    startActivity(new Intent(this, CalendarActivity.class));
                    break;
                case R.id.schedule_button:
                    startActivity(new Intent(this, ScheduleActivity.class));
                    break;
                case R.id.notes_button:
                    startActivity(new Intent(this, NotesActivity.class));
                    break;
                case R.id.options_button:
                    startActivity(new Intent(this, OptionsActivity.class));
                    break;
            }
    }

    public void onBackPressed() {
        finish();
    }
}
