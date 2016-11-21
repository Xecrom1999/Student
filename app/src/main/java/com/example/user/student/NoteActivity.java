package com.example.user.student;

import android.content.Intent;
import android.content.res.Configuration;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class NoteActivity extends AppCompatActivity {

    TextView title_text;
    TextView description_text;

    Configuration config;

    String id;

    Toolbar toolbar;
    LinearLayout layout;

    String title;
    String description;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);

        initializeViews();

        setupToolbar();

        intent = getIntent();

        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");

        id = intent.getStringExtra("id");

        title_text.setText(title);

        description_text.setText(description);

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


    private void initializeViews() {
        title_text = (TextView) findViewById(R.id.note_title);
        description_text = (TextView) findViewById(R.id.note_description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.edit_note_id) {
            Intent intent;

            intent = new Intent(this, NewNoteActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("description", description);
            intent.putExtra("id", this.id);

            startActivity(intent);
            finish();
        }

        if (id == android.R.id.home) onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.dark_notes));
        }
    }
}
