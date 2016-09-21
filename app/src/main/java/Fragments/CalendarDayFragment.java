package Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.student.EventActivity;
import com.example.user.student.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Adapters.CalendarDayAdapter;
import Database.CalendarDB;
import Interfaces.CalendarDayListener;
import Interfaces.EventDateListener;

import static android.R.attr.id;

/**
 * Created by gamrian on 31/08/2016.
 */
public class CalendarDayFragment extends Fragment implements CalendarDayListener {

    private final Calendar calendar;
    private final Context ctx;
    private final CalendarDB database;
    private final EventDateListener listener;
    RecyclerView recyclerView;
    CalendarDayAdapter adapter;
    TextView no_events_text;
    TextView date_text;
    String str;
    final SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");


    public CalendarDayFragment(Context ctx, CalendarDB database, Calendar calendar, EventDateListener listener) {
        this.calendar = calendar;
        this.ctx = ctx;
        this.database = database;
        this.listener = listener;
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

        adapter = new CalendarDayAdapter(ctx, database, calendar, this);

        no_events_text.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.calendar_day_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));

        recyclerView.setAdapter(adapter);

        date_text = (TextView) view.findViewById(R.id.day_date_text);

        str = format.format(calendar.getTime());

        date_text.setText(str);

    }

    @Override
    public void openEvent(int position) {

        Intent intent = new Intent(ctx, EventActivity.class);

        intent.putExtra("calendar", listener.getCurrentDate());
        intent.putExtra("position", position);

        startActivityForResult(intent, 2);
    }

    @Override
    public void deleteEvent(final int position, View v) {
        final PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.getMenuInflater().inflate(R.menu.delete_event_menu, popup.getMenu());

        final int[] pos = {position};

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                Cursor res = database.getRowByDate(String.valueOf(listener.getCurrentDate().getTime()));
                res.moveToNext();
                while (pos[0] != 0 && res.moveToNext()) pos[0]--;

                String id = res.getString(0);

                database.deleteData(id);

                listener.update();

                return true;
            }
        });

        popup.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.update();
    }
}
