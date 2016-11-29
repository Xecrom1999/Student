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
import com.example.user.student.Helper;
import com.example.user.student.NewEventActivity;
import com.example.user.student.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    SimpleDateFormat format = new SimpleDateFormat("MMMM");;
    SimpleDateFormat format2 = new SimpleDateFormat("MM/yy");
    Calendar calendar;
    String month;
    String month2;
    CalendarDB database;
    boolean active;

    public MonthFragment() {
    }

    public MonthFragment(int position, CalendarDB database) {
        this.position = position - 1;
        this.database = database;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.month_layout, container, false);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, position);
        month = format.format(calendar.getTime());
        month2 = format2.format(calendar.getTime());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ArrayList <Calendar> list = Helper.changes;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, position);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.getTime();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.add(Calendar.DATE, -7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        for (int i = 0; i < list.size(); i++) {
            int position = (int) ((list.get(i).getTimeInMillis() - calendar.getTimeInMillis()) / 86400000);
            if (position > 6 && position < 49) {
                adapter.update(position);
                adapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Helper.changes.clear();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.calendar_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));

        adapter = new CalendarAdapter(getContext(), position, this, database);

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
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public String getMonth() {
        return month;
    }

    public String getMonth2() {
        return month2;
    }

    @Override
    public void onDestroy() {
        active = false;
        super.onDestroy();
    }


}
