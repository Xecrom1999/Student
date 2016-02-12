package Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.student.Lesson;
import com.example.user.student.R;

import java.util.ArrayList;

import Adapters.ScheduleAdapter;
import Interfaces.Communicator;
import Interfaces.LessonsListener;

/**
 * Created by user on 15/01/16.
 */
public class DayFragment extends Fragment implements View.OnClickListener, Communicator {

    View layout;
    RecyclerView recyclerView;
    ScheduleAdapter adapter;
    TextView addLesson_text;
    Communicator communicator;
    int position;
    LessonsListener listener;
    ArrayList<Lesson> list;

    public DayFragment() {
    }

    public DayFragment(int position, LessonsListener listener) {
        this.position = position;
        this.listener = listener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.day_fragment_layout, container, false);

        addLesson_text = (TextView) layout.findViewById(R.id.addLesson_text);

        this.position = communicator.getPagerPosition();

        list = new ArrayList<>();

        list = communicator.getList(position);

        if (list.size() != 0) addLesson_text.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) layout.findViewById(R.id.lessonsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter = new ScheduleAdapter(getActivity(), list, listener);
        recyclerView.setAdapter(adapter);

        addLesson_text.setOnClickListener(this);

        return layout;
    }


    public void onClick(View v) {
        communicator.newLesson(new Lesson("","",""));
    }

    public void lessonDone(Lesson lesson) {
        adapter.addLesson(lesson);
        /*RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getActivity()).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm((Iterable<RealmObject>) lesson);
        realm.commitTransaction();
        realm.close();*/
        addLesson_text.setVisibility(View.INVISIBLE);


    }

    public void updateLesson(int position, Lesson lesson) {
        adapter.updateLesson(position, lesson);
    }

    public void newLesson(Lesson lesson) {
    }

    public ArrayList<Lesson> getList(int p) {
        return null;
    }

    public int getPagerPosition() {
        return 0;
    }

    public void deleteLesson(int position) {
        adapter.deleteLesson(position);
        if (adapter.getItemCount() == 0) addLesson_text.setVisibility(View.VISIBLE);
    }

    public ScheduleAdapter getAdapter() {
        return adapter;
    }
}
