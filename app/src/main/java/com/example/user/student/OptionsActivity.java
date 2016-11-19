package com.example.user.student;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import Database.CalendarDB;
import Database.DefaultLessonsDB;
import Database.NotesDB;
import Database.ScheduleDB;

public class OptionsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Configuration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_activity);

        setupToolbar();
        AdView mAdView = (AdView) findViewById(R.id.options_adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void setupToolbar() {
        config = getResources().getConfiguration();
        toolbar = (Toolbar) findViewById(R.id.options_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(R.string.options_string);
        toolbar.setBackgroundResource(R.color.primary_new_item);
    }

    public void clearDataRequest(View view) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        clearData();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogStyle);
        builder.setMessage(getString(R.string.are_you_sure_string)).setPositiveButton(getString(R.string.clear_string), dialogClickListener)
                .setNegativeButton(getString(R.string.cancel_string), dialogClickListener).show();

    }

    private void clearData() {
        CalendarDB db1 = new CalendarDB(this);
        db1.deleteAll();

        DefaultLessonsDB db2 = new DefaultLessonsDB(this);
        db2.deleteAll();

        NotesDB db3 = new NotesDB(this);
        db3.deleteAll();

        ScheduleDB db4 = new ScheduleDB(this);
        db4.deleteAll();

        SharedPreferences sp = getSharedPreferences("", MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    public void contactUs(View view) {

        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, new String[] {"arielg1000@gmail.com"});
        Email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name_string));
        Email.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(Email, getString(R.string.contact_us_string)));
    }

    public void rateApp(View view) {

        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
