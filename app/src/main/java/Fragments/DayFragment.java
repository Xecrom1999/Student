package Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.student.Lesson;
import com.example.user.student.NewLessonActivity;
import com.example.user.student.R;

import java.util.ArrayList;

import Adapters.ScheduleAdapter;
import Database.ScheduleDB;
import Interfaces.EditModeListener;

/**
 * Created by user on 15/01/16.
 */
public class DayFragment extends Fragment implements View.OnClickListener {

    View layout;
    RecyclerView recyclerView;
    ScheduleAdapter adapter;
    TextView addLesson_text;
    int position;
    ArrayList<Lesson> list;
    ScheduleDB database;
    Context ctx;

    public DayFragment(int position, ScheduleDB database, Context ctx) {
        this.position = position;
        this.database = database;
        this.ctx = ctx;
        list = getList();
        adapter = new ScheduleAdapter(ctx, list);
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
        if (adapter.getItemCount() > 2) addLesson_text.setVisibility(View.VISIBLE);
        else addLesson_text.setVisibility(View.INVISIBLE);
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
        Intent intent = new Intent(ctx, NewLessonActivity.class);
        
        intent.putExtra("isNew", true);
       
        startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.stay_in_place);
    }

    public void onClick(View v) {
        newLesson();
    }

    public void lessonDone(Lesson lesson) {
        adapter.addLesson(lesson);
        database.insertData(position, lesson);
        addLesson_text.setVisibility(View.INVISIBLE);
    }

    public void updateLesson(int position,  Lesson lesson) {
        adapter.updateLesson(position, lesson);
        database.updateData(position, adapter.getItemAtPosition(position), lesson);
    }

    public void deleteLesson(int position) {
        database.deleteData(this.position, adapter.getItemAtPosition(position));
        adapter.deleteLesson(position);
        if (adapter.getItemCount() == 0) addLesson_text.setVisibility(View.VISIBLE);
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
        adapter.toggleEditMode();
    }
}
