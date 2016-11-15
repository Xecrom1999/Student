package com.example.user.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
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

    boolean rtl;

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
        getSupportActionBar().setTitle(getString(R.string.my_notes_string));
        toolbar.setBackgroundResource(R.color.notes_primary);
    }

    private void showNotes() {

        Cursor res = dataBase.getAllData();

        while (res.moveToNext()) {
            addNote(res.getString(0), res.getString(1), res.getString(2), res.getString(3));
        }
        theLayout.invalidate();
    }

    private void addNote(String id, String title, String xPos, String yPos) {

        final View note = getLayoutInflater().inflate(R.layout.note_item_layout, null, false);

        TextView title_text = (TextView) note.findViewById(R.id.note_title);

        note.setOnTouchListener(new NoteListener());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);

        params.leftMargin = Integer.parseInt(xPos);
        params.topMargin = Integer.parseInt(yPos);

        title_text.setText(title);

        note.setTag(id);

        theLayout.addView(note, params);

        isNew = false;
    }

    private void addNote() {
        final View note = getLayoutInflater().inflate(R.layout.note_item_layout, null, false);

        note.setOnTouchListener(new NoteListener());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);

        params.leftMargin = 15;
        params.topMargin = 1370;

        note.setAlpha(0);

        note.setTag("");

        theLayout.addView(note, params);

        isNew = false;
    }

    private void addNote(String id) {
        Cursor res = dataBase.getRowById(id);
        while (res.moveToNext())
            addNote(res.getString(0), res.getString(1), res.getString(2), res.getString(3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        else if (id == R.id.notes_delete_id)
            userDialog();

        else if (id == R.id.arragne_all)
            arrangeAll();

        return true;
    }

    private void arrangeAll() {
        
        int firstX = (int) convertDpToPixel(8);
        int y = (int) convertDpToPixel(60);

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
                v.setX(x);
                v.setY(y);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_all_string) + "?").setPositiveButton(getString(R.string.delete_string), dialogClickListener)
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
        Intent intent;

        String id = v.getTag().toString();
        Note note = getNoteById(id);

        String title = note.getTitle();
        if (title.isEmpty()) {
            intent = new Intent(this, NewNoteActivity.class);
            intent.putExtra("isNew", true);
        }
        else {
            intent = new Intent(this, NoteActivity.class);
            intent.putExtra("title", title);
        }

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

                    note = new Note("", String.valueOf(x - _xDelta), String.valueOf(y - _yDelta));

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
            return (v.getBottom() > 1550);
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
                note = new Note(res.getString(1), res.getString(2), res.getString(3));
                break;
            }
        return note;
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
            window.setStatusBarColor(getColor(isStarted ? R.color.dark_notes : R.color.primary_dark));
        }
    }
}

