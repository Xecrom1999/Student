package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.student.CalendarDayActivity;
import com.example.user.student.NewEventActivity;
import com.example.user.student.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Adapters.CalendarAdapter;
import Database.CalendarDB;
import Interfaces.CalendarListener;

/**
 * Created by gamrian on 19/08/2016.
 */
public class MonthFragment extends Fragment implements CalendarListener{

    RecyclerView recyclerView;
    CalendarAdapter adapter;
    int position;
    SimpleDateFormat format;
    Calendar calendar;
    String month;
    CalendarDB database;

    public MonthFragment() {
    }

    public MonthFragment(int position, CalendarDB database) {
        this.position = position;
        this.position--;
        this.database = database;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.month_layout, container, false);

        format = new SimpleDateFormat("MMMM    MM/yy");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, position);
        month = format.format(calendar.getTime());

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CalendarAdapter(getContext(), position, this, database);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.calendar_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void newEvent(int day, int month, int year, boolean hasEvent) {
        Intent intent = new Intent(getActivity(), hasEvent ? CalendarDayActivity.class : NewEventActivity.class);

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

       intent.putExtra("calendar", calendar);

        startActivity(intent);
        if (hasEvent)
            getActivity().overridePendingTransition(R.anim.stay_in_place, R.anim.stay_in_place);
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new CalendarAdapter(getContext(), position, this, database);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void openDay(int day, int month, int year) {
        Intent intent = new Intent(getActivity(), CalendarDayActivity.class);

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        intent.putExtra("calendar", calendar);

        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.stay_in_place, R.anim.stay_in_place);
    }

    public String getMonth() {
        return month;
    }
}
