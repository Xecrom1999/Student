package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.student.R;

import java.util.Date;

import Adapters.CalendarDayAdapter;
import Database.CalendarDB;

/**
 * Created by gamrian on 31/08/2016.
 */
public class CalendarDayFragment extends Fragment {

    private final Date date;
    private final Context ctx;
    private final CalendarDB database;
    RecyclerView recyclerView;
    CalendarDayAdapter adapter;
    TextView no_events_text;

    public CalendarDayFragment(Context ctx, CalendarDB database, Date time) {
        this.date = time;
        this.ctx = ctx;
        this.database = database;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calendar_day_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        no_events_text = (TextView) view.findViewById(R.id.no_events_text);

        adapter = new CalendarDayAdapter(ctx, database, date);

        no_events_text.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.calendar_day_list);

        recyclerView.setAdapter(adapter);
    }
}
