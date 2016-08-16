package com.example.user.student;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

        toolbar = (Toolbar) findViewById(R.id.toolBar2);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button3);
        toolbar.setTitle("To Do List");

        width = (int) convertDpToPixel(105);
        height = (int) convertDpToPixel(125);

        showNotes();

        addNote();
    }

    private void showNotes() {

        Cursor res = dataBase.getAllData();

        while (res.moveToNext()) {
            addNote(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5));
        }

    }

    private void addNote(String id, String title, String description, String xPos, String yPos, String date) {

        final View note = getLayoutInflater().inflate(R.layout.note_item_layout, null, false);

        TextView title_text = (TextView) note.findViewById(R.id.note_title);
        TextView date_text = (TextView) note.findViewById(R.id.note_date);

        note.setOnTouchListener(new NoteListener());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);

        params.leftMargin = Integer.parseInt(xPos);
        params.topMargin = Integer.parseInt(yPos);

        title_text.setText(title);
        date_text.setText(date);

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

                    note = new Note("", "", String.valueOf(x - _xDelta), String.valueOf(y - _yDelta), "");

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
                        theLayout.removeView(v);
                        dataBase.deleteData(id);
                    } else if (Math.abs((int) event.getRawX() - fx)  < 4 && Math.abs((int) event.getRawY() - fy)  < 4) {
                        noteClicked(v);
                    } else dataBase.updateData(id, String.valueOf(x - _xDelta), String.valueOf(y - _yDelta));

                    notesPack.setImageResource(R.drawable.pack_of_notes);
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

            TextView title_text = (TextView) chosen.findViewById(R.id.note_title);
            TextView date_text = (TextView) chosen.findViewById(R.id.note_date);

            dataBase.updateData(chosen.getTag().toString(),title, description, date);

            title_text.setText(title);
            date_text.setText(date);
        }
    }

    private Note getNoteById(String id) {
        Cursor res = dataBase.getAllData();
        Note note = null;
        while (res.moveToNext())
            if (res.getString(0).equals(id))  {
               note = new Note(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5));
                break;
            }
        return note;
    }
}

