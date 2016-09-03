package Fragments;

import android.app.Activity;
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
import Interfaces.Communicator;
import Interfaces.LessonsListener;

/**
 * Created by user on 15/01/16.
 */
public class DayFragment2 extends Fragment implements View.OnClickListener {

    View layout;
    RecyclerView recyclerView;
    ScheduleAdapter adapter;
    TextView addLesson_text;
    int position;
    ArrayList<Lesson> list;
    ScheduleDB database;
    Context ctx;
    LessonsListener listener;

    public DayFragment2(int position, LessonsListener listener, ScheduleDB database) {
        this.position = position;
        ctx = getActivity();
        this.database = database;
        this.listener = listener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.day_fragment_layout, container, false);

        addLesson_text = (TextView) layout.findViewById(R.id.addLesson_text);

        list = getList();

        if (list.size() != 0) addLesson_text.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) layout.findViewById(R.id.lessonsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter = new ScheduleAdapter(getActivity(), list, listener);
        recyclerView.setAdapter(adapter);

        addLesson_text.setOnClickListener(this);

        return layout;
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
        Intent intent = new Intent(getActivity(), NewLessonActivity.class);

        if (!lesson.getName().equals("")) {
            intent.putExtra("isNew", false);
            intent.putExtra("name", lesson.getName());
            intent.putExtra("time", lesson.getTime());
            intent.putExtra("length", lesson.getLength());
            intent.putExtra("itemPosition", position);
        }
        else intent.putExtra("isNew", true);
        startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.stay_in_place);
    }

    public void onClick(View v) {
        newLesson(new Lesson("","",""));
    }

    public void lessonDone(Lesson lesson) {
        adapter.addLesson(lesson);
        addLesson_text.setVisibility(View.INVISIBLE);
    }

    public void updateLesson(int position, Lesson lesson) {
        adapter.updateLesson(position, lesson);
    }

    public void deleteLesson(int position) {
        adapter.deleteLesson(position);
        if (adapter.getItemCount() == 0) addLesson_text.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public ScheduleAdapter getAdapter() {
        return adapter;
    }
}
