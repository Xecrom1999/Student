package com.example.user.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    TextView calender_button;
    TextView schedule_button;
    TextView notes_button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        calender_button = (TextView) findViewById(R.id.calendar_button);
        calender_button.setOnClickListener(this);

        notes_button = (TextView) findViewById(R.id.notes_button);
        notes_button.setOnClickListener(this);

        schedule_button = (TextView) findViewById(R.id.schedule_button);
        schedule_button.setOnClickListener(this);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return true;
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
            }
    }

    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Helper.hideKeyboard(this);
    }
}
