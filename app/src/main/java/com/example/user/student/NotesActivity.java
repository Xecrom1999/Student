package com.example.user.student;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Database.NotesDB;

public class NotesActivity extends ActionBarActivity {

    Toolbar toolbar;
    RelativeLayout theLayout;
    ImageButton notesPack;
    boolean isNew;
    NotesDB dataBase;
    int width;
    int height;
    View chosen;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity);

        dataBase = new NotesDB(this);

        theLayout = (RelativeLayout) findViewById(R.id.theLayout);

        notesPack = (ImageButton) findViewById(R.id.notes_img);

        toolbar = (Toolbar) findViewById(R.id.notes_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Configuration config = getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            toolbar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);
        }
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(getString(R.string.my_notes_string));

        width = (int) convertDpToPixel(105);
        height = (int) convertDpToPixel(125);

        showNotes();

        addNote();
    }

    private String getTime(String dateString) {

        if (dateString.equals("")) return "";

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

        Date date;

        String timeToDate;

        Calendar cal = Calendar.getInstance();

        try {
            date = format.parse(dateString);

            cal.setTime(date);
            cal.add(Calendar.DATE, -1);

            int diff = (int)((date.getTime() - System.currentTimeMillis()) / (24 * 60 * 60 * 1000) + 1);

            if (diff > 365) timeToDate = getString(R.string.year_string);

            else if (diff > 29) {
                diff /= 30;
                if (diff == 1) timeToDate = getString(R.string.month_string);
                else timeToDate =  "  " + diff + "  " + getString(R.string.months_string);
            }

            else if (diff > 6) {
                diff /= 7;
                if (diff == 1) timeToDate = getString(R.string.week_string);
                else timeToDate = "  " + diff + "  " + getString(R.string.weeks_string);
            }

            else if (DateUtils.isToday(cal.getTime().getTime())) timeToDate = getString(R.string.tomorrow_string);

            else if (DateUtils.isToday(date.getTime())) timeToDate = getString(R.string.today_string);

            else timeToDate = sdf.format(date);
        } catch (ParseException e) {
            timeToDate = getString(R.string.passed_string);
        }
    return timeToDate;
    }


    private void showNotes() {

        Cursor res = dataBase.getAllData();

        while (res.moveToNext()) {
            dataBase.updateData(res.getString(0), getTime(res.getString(5)));
            addNote(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
        }
    }

    private void addNote(String id, String title, String description, String xPos, String yPos, String date, String time) {

        final View note = getLayoutInflater().inflate(R.layout.note_item_layout, null, false);

        TextView title_text = (TextView) note.findViewById(R.id.note_title);
        TextView date_text = (TextView) note.findViewById(R.id.note_date);
        TextView time_text = (TextView) note.findViewById(R.id.note_time);

        note.setOnTouchListener(new NoteListener());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);

        params.leftMargin = Integer.parseInt(xPos);
        params.topMargin = Integer.parseInt(yPos);

        title_text.setText(title);
        date_text.setText(date);
        time_text.setText(time);

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
            addNote(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
    }

    public void noteClicked(View v) {
        Intent intent = new Intent(this, NewNoteActivity.class);

        String id = v.getTag().toString();

        if (!id.equals("")) {
            Note note = getNoteById(id);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("description", note.getDescription());
            intent.putExtra("date", note.getDate());
        }

        chosen = v;
        
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.in_from_bottom, R.anim.stay_in_place);
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

                    notesPack.setImageResource(R.drawable.garbage_can);

                    note = new Note("", "", String.valueOf(x - _xDelta), String.valueOf(y - _yDelta), "", "");

                    id = v.getTag().toString();
                    break;

                case MotionEvent.ACTION_UP:

                    if (isNew) {
                        addNote();

                        if (inGarbageRange(x, y))
                            theLayout.removeView(v);
                        else {
                            note.setxPos(String.valueOf(x - _xDelta));
                            note.setyPos(String.valueOf(y - _yDelta));

                            v.setTag(String.valueOf(dataBase.insertData(note)));

                            noteClicked(v);
                        }
                    }
                    else if (inGarbageRange(x, y)) {
                        dataBase.deleteData(id);
                        theLayout.removeView(v);
                        addNote(id);
                    } else if (Math.abs((int) event.getRawX() - fx)  < 4 && Math.abs((int) event.getRawY() - fy)  < 4) {
                        noteClicked(v);
                    } else dataBase.updateData(id, String.valueOf(x - _xDelta), String.valueOf(y - _yDelta));

                    notesPack.setImageResource(R.drawable.new_note);
                    break;

                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v
                            .getLayoutParams();
                    layoutParams.leftMargin = x - _xDelta;
                    layoutParams.topMargin = y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    v.setLayoutParams(layoutParams);

                    break;
                default:
                    break;
            }

            theLayout.invalidate();
            return true;
        }

        private boolean inGarbageRange(int x, int y) {
            return (x < 430 && y > 1360);
        }
    }

    public float convertDpToPixel(float dp){
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            String date = data.getStringExtra("date");
            String time = data.getStringExtra("time");

            TextView title_text = (TextView) chosen.findViewById(R.id.note_title);
            TextView date_text = (TextView) chosen.findViewById(R.id.note_date);
            TextView time_text = (TextView) chosen.findViewById(R.id.note_time);

            dataBase.updateData(chosen.getTag().toString(),title, description, date, time);

            title_text.setText(title);
            date_text.setText(date);
            time_text.setText(time);
        }
    }

    private Note getNoteById(String id) {
        Cursor res = dataBase.getAllData();
        Note note = null;
        while (res.moveToNext())
            if (res.getString(0).equals(id))  {
               note = new Note(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
                break;
            }
        return note;
    }
}

