package app.ariel.student;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ariel.student.student.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;

import Database.NotesDB;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener {

    EditText title_edit;
    TextView date_text;
    Configuration config;
    String id;
    Toolbar toolbar;
    LinearLayout layout;
    String date;
    Intent intent;
    NotesDB database;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM");
    ImageView star_img;
    int isTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);

        database = new NotesDB(this);

        title_edit = (EditText) findViewById(R.id.note_edit);
        title_edit.setSingleLine(false);
        date_text = (TextView) findViewById(R.id.note_date);

        star_img = (ImageView) findViewById(R.id.star_img);

        setAll();

        star_img.setOnClickListener(this);

        setupToolbar();

        AdView mAdView = (AdView) findViewById(R.id.note_adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Helper.setupAd(this);
    }

    private void setupToolbar() {
        config = getResources().getConfiguration();
        toolbar = (Toolbar) findViewById(R.id.note_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("");
        toolbar.setBackgroundResource(R.color.notes_primary);
    }


    private void setAll() {

        intent = getIntent();


        id = intent.getStringExtra("id");

        Note note = database.getNoteById(id);

        String title = note.getTitle();
        isTop = note.isTop();

        if (isTop == 1)
            star_img.setImageResource(R.mipmap.ic_star_full);
        else
            star_img.setImageResource(R.mipmap.ic_star_empty);

        if (date == null || date.equals(""))
            date = simpleDateFormat.format(System.currentTimeMillis());

        title_edit.setText(title);
        date_text.setText(date);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf");
        date_text.setTypeface(font);

        Helper.showKeyboard(this, title_edit);
        title_edit.setSelection(title.length());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.done_id)
            finish();

        if (id == android.R.id.home) onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Helper.hideKeyboard(this);

        changeColor(false);

        finishNote();
    }

    @Override
    protected void onResume() {
        super.onResume();

        changeColor(true);
    }

    private void changeColor(boolean f) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_notes));
        }
    }

    public void finishNote() {

        String title = title_edit.getText().toString();

        database.updateData2(id, title, date, isTop);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (isTop == 1) {

            Intent myIntent = new Intent(this, MainActivity.class);
            myIntent.putExtra("fromNoti", 2);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(this.getString(R.string.to_do_string))
                    .setContentText(title)
                    .setSmallIcon(R.mipmap.notes_img2)
                    .setContentIntent(PendingIntent.getActivity(this, 2, myIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setAutoCancel(false)
                    .setSound(null)
                    .setColor(getResources().getColor(R.color.notes_primary))
                    .setPriority(Notification.PRIORITY_LOW)
                    .setOngoing(true)
                    .build();

            notificationManager.notify(100000 + Integer.valueOf(id), notification);
        }
        else
            notificationManager.cancel(100000 + Integer.valueOf(id));
    }


    @Override
    public void onClick(View v) {

        if (isTop == 0) {
            star_img.setImageResource(R.mipmap.ic_star_full);
            isTop = 1;
        }
        else {
            star_img.setImageResource(R.mipmap.ic_star_empty);
            isTop = 0;
        }
    }
}
