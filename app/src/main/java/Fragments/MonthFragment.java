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
import android.widget.TextView;

import com.example.user.student.CalendarDayActivity;
import com.example.user.student.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Adapters.CalendarAdapter;
import Interfaces.CalendarListener;

/**
 * Created by gamrian on 19/08/2016.
 */
public class MonthFragment extends Fragment implements CalendarListener{

    RecyclerView recyclerView;
    CalendarAdapter adapter;
    TextView month_text;
    int position;
    SimpleDateFormat format;
    Calendar calendar;
    String month;

    public MonthFragment(int position) {
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.month_layout, container, false);

        format = new SimpleDateFormat("MMMM    MM/yy");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, position);
        month = format.format(calendar.getTime());

        adapter = new CalendarAdapter(getContext(), position, this);

        recyclerView = (RecyclerView) view.findViewById(R.id.calendar_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void openDay(int day, int month, int year) {
        Intent intent = new Intent(getActivity(), CalendarDayActivity.class);
        
        intent.putExtra("day", day);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        startActivityForResult(intent, 1);
    }

    public String getMonth() {
        return month;
    }
}
