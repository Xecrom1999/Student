package com.example.user.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Database.NotesDB;

public class NotesActivity extends ActionBarActivity {

    Toolbar toolbar;
    RelativeLayout theLayout;
    boolean isNew;
    NotesDB dataBase;
    int width;
    int height;
    View chosen;
    ImageView garbage_img;
    ImageView notes_img;
    View line;
    Menu menu;
    boolean rtl;

    boolean autoSave;
    boolean autoArrange;

    SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity);

        rtl = false;

        dataBase = new NotesDB(this);

        theLayout = (RelativeLayout) findViewById(R.id.theLayout);

        notes_img = (ImageView) findViewById(R.id.notes_img);

        garbage_img = (ImageView) findViewById(R.id.garbage_img);
        garbage_img.setVisibility(View.INVISIBLE);

        line = findViewById(R.id.garbage_line);
        line.setVisibility(View.INVISIBLE);

        setToolbar();

        width = (int) convertDpToPixel(85);
        height = (int) convertDpToPixel(95);


    }

    @Override
    protected void onStart() {
        super.onStart();

        Helper.setupAd(this);
        removeAllNotes();
        showNotes();

        preferences = getSharedPreferences("data", MODE_PRIVATE);
        autoSave = preferences.getBoolean("autoSave", true);
        autoArrange = preferences.getBoolean("autoArrange", true);

        if (autoArrange) arrangeNotes();
    }

    @Override
    protected void onPause() {
        super.onPause();

        preferences.edit().putBoolean("autoSave", autoSave).commit();
        preferences.edit().putBoolean("autoArrange", autoArrange).commit();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.notes_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Configuration config = getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            rtl = true;
            toolbar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        }
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(getString(R.string.notes_string));
        toolbar.setBackgroundResource(R.color.primary_color);
        toolbar.setAlpha(0.6f);
    }

    private void showNotes() {

        Cursor res = dataBase.getAllData();

        while (res.moveToNext()) {
            addNote(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4));
        }
        theLayout.invalidate();
    }

    private void addNote(String id, String title, String date, String xPos, String yPos) {

        final View note = getLayoutInflater().inflate(R.layout.note_item_layout, null, false);

        TextView title_text = (TextView) note.findViewById(R.id.note_edit);
        TextView date_text = (TextView) note.findViewById(R.id.note_date);

        note.setOnTouchListener(new NoteListener());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);

        params.leftMargin = Integer.parseInt(xPos);
        params.topMargin = Integer.parseInt(yPos);

        title_text.setText(title);
        date_text.setText(date);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf");
        date_text.setTypeface(typeface);

        note.setTag(id);

        theLayout.addView(note, params);

        isNew = false;
    }

    private void addNote() {
        final View note = getLayoutInflater().inflate(R.layout.note_item_layout, null, false);

        note.setOnTouchListener(new NoteListener());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);

        params.leftMargin = (int) convertDpToPixel(0);
        params.topMargin = (int) convertDpToPixel(495);

        note.setAlpha(0);

        note.setTag("");

        theLayout.addView(note, params);

        isNew = false;
    }

    private void addNote(String id) {
        Cursor res = dataBase.getRowById(id);
        while (res.moveToNext())
            addNote(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        this.menu = menu;
        updateMenu();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.notes_delete_id:
                userDialog();
                break;
            case R.id.arrange_notes:
                arrangeNotes();
                break;
            case R.id.auto_save_id:
                autoSave = !autoSave;
                updateMenu();
                break;
            case R.id.auto_arrange_id:
                autoArrange = !autoArrange;
                updateMenu();
                break;
        }


        return true;
    }

    private void updateMenu() {

        menu.getItem(2).setChecked(autoSave);
        menu.getItem(3).setChecked(autoArrange);
    }


    private void arrangeNotes() {
        
        int firstX = (int) convertDpToPixel(8);
        int y = (int) convertDpToPixel(70);

        int spaceX = (int) convertDpToPixel(89);
        int spaceY = (int) convertDpToPixel(110);

        if (rtl) {
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics ();
            display.getMetrics(outMetrics);

            float density  = getResources().getDisplayMetrics().density;
            float dpWidth  = outMetrics.widthPixels / density;

            firstX = (int) ((convertDpToPixel(dpWidth) - firstX) - convertDpToPixel(81));
            spaceX *= -1;
        }

        int count = 1;

        int x = firstX;

        for (int i = 0; i < theLayout.getChildCount(); i++) {
            View v = theLayout.getChildAt(i);
            if (v.getTag() != null && !v.getTag().toString().isEmpty()) {


                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

                layoutParams.leftMargin = x;
                layoutParams.topMargin = y;
                layoutParams.rightMargin = (int) convertDpToPixel(-250);
                layoutParams.bottomMargin = (int) convertDpToPixel(-250);
                v.setLayoutParams(layoutParams);

                dataBase.updateData(v.getTag().toString(), String.valueOf(x), String.valueOf(y));

                x += spaceX;

                if (count % 4 == 0) {
                    x = firstX;
                    y += spaceY;
                }
                count++;
                if (count > 16) break;
            }
        }
    }

    private void userDialog() {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dataBase.deleteAll();
                        removeAllNotes();
                        addNote();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogStyle);
        builder.setMessage(getString(R.string.are_you_sure_string)).setPositiveButton(getString(R.string.delete_string), dialogClickListener)
                .setNegativeButton(getString(R.string.cancel_string), dialogClickListener).show();
    }

    private void removeAllNotes() {
        boolean doBreak = false;
        while (!doBreak) {
            int childCount = theLayout.getChildCount();
            int i;
            for(i=0; i<childCount; i++) {
                View currentChild = theLayout.getChildAt(i);
                if (currentChild.getId() != toolbar.getId() && currentChild.getId() != R.id.notes_img && currentChild.getId() != garbage_img.getId() && currentChild.getId() != line.getId()) {
                    theLayout.removeView(currentChild);
                    break;
                }
            }

            if (i == childCount) {
                doBreak = true;
            }
        }
        addNote();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void noteClicked(View v) {

        String id = v.getTag().toString();
        Note note = getNoteById(id);

        String title = note.getTitle();
        String date = note.getDate();
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("date", date);
        intent.putExtra("id", v.getTag().toString());

        chosen = v;
        
        startActivityForResult(intent, 0);
    }

    public class NoteListener implements View.OnTouchListener {

        private int _xDelta;
        private int _yDelta;

        int fx=0, fy=0;

        Note note;

        String id;

        public boolean onTouch(View v, MotionEvent event) {
            final int x = (int) event.getRawX();
            final int y = (int) event.getRawY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    fx = (int) event.getRawX();
                    fy = (int) event.getRawY();

                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    _xDelta = x - lParams.leftMargin;
                    _yDelta = y - lParams.topMargin;

                    if (v.getTag().toString().equals("")) isNew = true;

                    v.setAlpha(1);

                    note = new Note("", "", String.valueOf(x - _xDelta), String.valueOf(y - _yDelta));

                    id = v.getTag().toString();

                    garbage_img.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    notes_img.setVisibility(View.INVISIBLE);

                    break;

                case MotionEvent.ACTION_UP:

                    garbage_img.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.INVISIBLE);
                    notes_img.setVisibility(View.VISIBLE);

                    if (isNew) {
                        addNote();

                        if (inGarbageRange(v))
                            theLayout.removeView(v);
                        else {
                            note.setxPos(String.valueOf(x - _xDelta));
                            note.setyPos(String.valueOf(y - _yDelta));

                            v.setTag(String.valueOf(dataBase.insertData(note)));

                            noteClicked(v);
                        }
                    }
                    else if (inGarbageRange(v)) {
                        dataBase.deleteData(id);
                        theLayout.removeView(v);
                        addNote(id);
                    } else if (Math.abs((int) event.getRawX() - fx)  < 3 && Math.abs((int) event.getRawY() - fy)  < 3) {
                        noteClicked(v);
                    } else dataBase.updateData(id, String.valueOf(x - _xDelta), String.valueOf(y - _yDelta));

                    break;

                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v
                            .getLayoutParams();
                    layoutParams.leftMargin = x - _xDelta;
                    layoutParams.topMargin = y - _yDelta;
                    layoutParams.rightMargin = (int) getResources().getDimension(R.dimen.note_margin);
                    layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.note_margin);
                    v.setLayoutParams(layoutParams);

                    if (inGarbageRange(v)) {
                        v.setAlpha((float) 0.4);
                        garbage_img.setAlpha((float) 1);
                    }
                    else  {
                        v.setAlpha((float) 1);
                        garbage_img.setAlpha((float) 0.5);
                    }
                    break;
                default:
                    break;
            }

            theLayout.invalidate();
            return true;
        }

        private boolean inGarbageRange(View v) {
            return (v.getBottom() >= line.getY());
        }
    }

    public float convertDpToPixel(float dp){
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private Note getNoteById(String id) {
        Cursor res = dataBase.getAllData();
        Note note = null;
        while (res.moveToNext())
            if (res.getString(0).equals(id))  {
                note = new Note(res.getString(1), res.getString(2), res.getString(3), res.getString(4));
                break;
            }
        return note;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_color));
        }
    }
}