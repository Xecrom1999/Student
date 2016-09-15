package Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.user.student.Lesson;
import com.example.user.student.NewLessonActivity;
import com.example.user.student.R;

import java.util.ArrayList;

import Adapters.ScheduleAdapter;
import Database.ScheduleDB;
import Interfaces.EditModeListener;
import Interfaces.ScheduleListener;

/**
 * Created by user on 15/01/16.
 */
public class ScheduleDayFragment extends Fragment implements View.OnClickListener {

    View layout;
    RecyclerView recyclerView;
    ScheduleAdapter adapter;
    TextView addLesson_text;
    int position;
    ArrayList<Lesson> list;
    ScheduleDB database;
    Context ctx;
    boolean editMode;
    EditModeListener listener;

    public ScheduleDayFragment(int position, ScheduleDB database, Context ctx, EditModeListener listener, ScheduleListener scheduleListener) {
        this.position = position;
        this.database = database;
        this.ctx = ctx;
        list = getList();
        this.listener = listener;
        adapter = new ScheduleAdapter(ctx, list, scheduleListener);
        editMode = false;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.day_fragment_layout, container, false);

        addLesson_text = (TextView) layout.findViewById(R.id.addLesson_text);

        updateText();
        
        recyclerView = (RecyclerView) layout.findViewById(R.id.lessonsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        addLesson_text.setOnClickListener(this);

        return layout;
    }

    private void updateText() {
        try {
            addLesson_text.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
        }catch (NullPointerException e){}
    }

    public ArrayList<Lesson> getList() {
        ArrayList<Lesson> list = new ArrayList<>();

        Cursor res = database.getData(position);
        
        while (res.moveToNext()) {
            String name = res.getString(1);
            String time = res.getString(2);
            String length = res.getString(3);
            Lesson lesson = new Lesson(name, time, length);
            list.add(lesson);
        }
        return list;
    }

    public void newLesson(Lesson lesson) {
        Intent intent = new Intent(ctx, NewLessonActivity.class);

        intent.putExtra("isNew", false);
        intent.putExtra("name", lesson.getName());
        intent.putExtra("time", lesson.getTime());
        intent.putExtra("length", lesson.getLength());
        intent.putExtra("itemPosition", position);

        startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.stay_in_place);
    }

    public void newLesson() {
        /*Intent intent = new Intent(ctx, NewLessonActivity.class);
        
        intent.putExtra("isNew", true);
       
        startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.stay_in_place);*/

        int number = adapter.getItemCount();
        if (editMode) number--;

        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        new NewLessonFragment(getActivity(), editMode ? adapter.getItemCount() : adapter.getItemCount() + 1).show(getFragmentManager(), "TAG");

    }

    public void onClick(View v) {
        newLesson();
    }

    public void lessonDone(Lesson lesson) {
        adapter.addLesson(lesson);
        database.insertData(position, lesson);
        updateText();
        listener.startEditMode();
    }

    public void updateLesson(int position,  Lesson lesson) {
        adapter.updateLesson(position, lesson);
        database.updateData(position, adapter.getItemAtPosition(position), lesson);
    }

    public void deleteLesson(int position, Lesson lesson) {
        database.deleteData(this.position, lesson);
        adapter.deleteLesson(position);
        updateText();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Lesson lesson = new Lesson(data.getStringExtra("name"), data.getStringExtra("time"), data.getStringExtra("length"));
            if (data.getBooleanExtra("isNew", true))
                lessonDone(lesson);
            else updateLesson(data.getIntExtra("position", 0), lesson);
        }
    }

    public void deleteDay() {
        database.deleteDay(position);
        adapter.deleteAll();
        updateText();
    }

    public void toggleEditMode() {
        editMode = !editMode;
        adapter.toggleEditMode();
        updateText();
    }
}
