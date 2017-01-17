package Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.ariel.student.CalendarDayActivity;
import app.ariel.student.NewEventActivity;
import com.ariel.student.student.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Adapters.CalendarAdapter;
import Database.CalendarDB;
import Interfaces.CalendarListener;

/**
 * Created by gamrian on 19/08/2016.
 */
public class MonthFragment extends Fragment implements CalendarListener {

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
    Context ctx;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.calendar_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setItemAnimator(null);

        adapter = new CalendarAdapter(getContext(), position, this, database);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.ctx = context;
    }

    @Override
    public void itemClicked(int day, int month, int year, boolean hasEvent) {
        Intent intent = new Intent(ctx, hasEvent ? CalendarDayActivity.class : NewEventActivity.class);

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

       intent.putExtra("calendar", calendar);

        try {
            startActivity(intent);
            if (hasEvent)
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (IllegalStateException e){}
    }

    public String getMonth() {
        return month;
    }

    public String getMonth2() {
        return month2;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        active = false;
    }

    public void update(Date d) {
        if (adapter != null)
        adapter.updateSelf(d);
    }

}
