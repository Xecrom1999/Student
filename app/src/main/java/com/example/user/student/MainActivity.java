package com.example.user.student;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Toolbar toolbar;
    ImageView calender_img;
    ImageView schedule_img;
    ImageView list_img;
    TextView title_text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        calender_img = (ImageView) findViewById(R.id.calendar_img);
        calender_img.setOnClickListener(this);

        schedule_img = (ImageView) findViewById(R.id.schedule_img);
        schedule_img.setOnClickListener(this);

        list_img = (ImageView) findViewById(R.id.list_img);
        list_img.setOnClickListener(this);

        title_text = (TextView) findViewById(R.id.main_title);

        boolean fromNoti = getIntent().getBooleanExtra("fromNoti", false);

        if (fromNoti) {
            Intent intent = new Intent(this, CalendarActivity.class);
            intent.putExtra("fromNoti", true);
            Calendar calendar = (Calendar) intent.getExtras().get("calendar");
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
                case R.id.calendar_img:
                    startActivity(new Intent(this, CalendarActivity.class));
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                    break;
                case R.id.schedule_img:
                    startActivity(new Intent(this, ScheduleActivity.class));
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    break;
                case R.id.list_img:
                    startActivity(new Intent(this, NotesActivity.class));
                    overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
                    break;
                default:
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
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
