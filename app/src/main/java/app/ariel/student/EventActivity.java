package app.ariel.student;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ariel.student.student.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Database.CalendarDB;

public class EventActivity extends AppCompatActivity {

    TextView time_text;
    Toolbar toolbar;
    TextView date_text;
    Calendar calendar;
    LinearLayout date_layout;
    LinearLayout time_layout;
    LinearLayout reminder_layout;
    LinearLayout comment_layout;
    TextView comment_edit;
    TextView title_edit;
    CalendarDB database;
    TextView reminder_text;

    String id;
    String title;
    String time;
    String comment;
    String reminder;

    Intent intent;

    final SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);

        database = new CalendarDB(this);

        date_layout = (LinearLayout) findViewById(R.id.event_date_layout);
        time_layout = (LinearLayout) findViewById(R.id.event_time_layout);
        reminder_layout = (LinearLayout) findViewById(R.id.event_reminder_layout);
        comment_layout = (LinearLayout) findViewById(R.id.event_comment_layout);

        title_edit = (TextView) findViewById(R.id.event_title_edit);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf");
        title_edit.setTypeface(font);
        comment_edit = (TextView) findViewById(R.id.comment_edit);

        time_text = (TextView) findViewById(R.id.event_time_text);
        toolbar = (Toolbar) findViewById(R.id.event_toolbar);

        date_text = (TextView) findViewById(R.id.event_date_text);
        reminder_text = (TextView) findViewById(R.id.reminder_text);

        intent = getIntent();

        calendar = (Calendar) intent.getExtras().get("calendar");

        setAll();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        getSupportActionBar().setTitle("");
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary_new_event));

        AdView mAdView = (AdView) findViewById(R.id.event_adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Helper.setupAd(this);
    }

    private void setAll() {
        setDate(calendar);

        id = intent.getStringExtra("id");

        Event event = database.getEventById(id);

        title = event.getTitle();
        time = event.getTime();
        comment = event.getComment();
        reminder = event.getReminder();

        title_edit.setText(title);

        if (time.isEmpty()) time_layout.setVisibility(View.GONE);
        else time_text.setText(time);

        if (comment.isEmpty()) comment_layout.setVisibility(View.GONE);
        else comment_edit.setText(comment);

        if (reminder.isEmpty()) reminder_layout.setVisibility(View.GONE);

        else {
            int reminder_count = Integer.parseInt(reminder.substring(reminder.indexOf("count") + 5, reminder.indexOf("units")));
            String reminder_units = reminder.substring(reminder.indexOf("units") + 5, reminder.indexOf("hour"));
            int reminder_hour = Integer.parseInt(reminder.substring(reminder.indexOf("hour") + 4, reminder.lastIndexOf("minute")));
            int reminder_minute = Integer.parseInt(reminder.substring(reminder.lastIndexOf("minute") + 6));
            setReminderText(reminder_count, reminder_units, reminder_hour, reminder_minute);
        }
    }

    private void setDate(Calendar calendar) {
        String str = format.format(calendar.getTime());

        date_text.setText(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.event_edit) {
            Intent intent = new Intent(this, NewEventActivity.class);
            intent.putExtra("isOld", true);
            intent.putExtra("calendar", calendar);
            intent.putExtra("id", this.id);
            startActivity(intent);
            finish();
        }

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        changeColor(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        changeColor(true);
    }

    private void changeColor(boolean isStarted) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(isStarted ? R.color.dark_new_event : R.color.dark_color));
        }
    }

    public void setReminderText(int count, String unitsBefore, int hour, int minute) {
        String m = String.valueOf(minute);

        if (minute < 10)
            m = "0" + minute;

        String str = count + " " + unitsBefore;

        boolean f = unitsBefore.equals(getResources().getStringArray(R.array.reminder_spinner)[0]);

        boolean hasTime = !time_text.getText().toString().isEmpty();

        if (count < 3)
            if (hasTime) {
                String[] arr = getResources().getStringArray(R.array.reminder_spinner2);
                if (count == 0) str = getString(R.string.on_event_time_string);

                else if (count == 1) {
                    if (unitsBefore.equals(arr[0])) str = getString(R.string.minute_before_string);
                    else if (unitsBefore.equals(arr[1])) str = getString(R.string.hour_before_string);
                    else if (unitsBefore.equals(arr[2])) str = getString(R.string.day_before_string);
                    else if (unitsBefore.equals(arr[3])) str = getString(R.string.week_before_string);
                }
                else if (count == 2) {
                    if (unitsBefore.equals(arr[0])) str = getString(R.string.two_minutes_before_string);
                    else if (unitsBefore.equals(arr[1])) str = getString(R.string.two_hours_before_string);
                    else if (unitsBefore.equals(arr[2])) str = getString(R.string.two_days_before_string);
                    else if (unitsBefore.equals(arr[3])) str = getString(R.string.two_weeks_before_string);
                }

            } else {
                if (count == 0) str = getString(R.string.on_the_day_string);
                else if (count == 1) str = getString(f ? R.string.day_before_string : R.string.week_before_string);
                else if (count == 2) str = getString(f ? R.string.two_days_before_string : R.string.two_weeks_before_string);
            }


        if (hasTime) reminder_text.setText(str);
        else reminder_text.setText(str + " " +  getString(R.string.at_string) + " " + hour + ":" + m);
    }
}