package Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.student.AlarmReceiver;
import com.example.user.student.EventActivity;
import com.example.user.student.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Adapters.CalendarDayAdapter;
import Database.CalendarDB;
import Interfaces.CalendarDayListener;
import Interfaces.EventDateListener;

/**
 * Created by gamrian on 31/08/2016.
 */
public class CalendarDayFragment extends Fragment implements CalendarDayListener {

    private Calendar calendar;
    private Context ctx;
    private CalendarDB database;
    private EventDateListener listener;
    RecyclerView recyclerView;
    CalendarDayAdapter adapter;
    TextView no_events_text;
    TextView date_text;
    String str;
    final SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    AlarmManager alarmManager;

    public CalendarDayFragment() {
    }

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

        Calendar now = Calendar.getInstance();

        if (DateUtils.isToday(calendar.getTimeInMillis())) str += " (" + ctx.getString(R.string.today_string) + ")";

        else {
            now.add(Calendar.DATE, -1);

            if (calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                if (calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH))
                    if (calendar.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH))
                        str += " (" + ctx.getString(R.string.yesterday_string) + ")";
            }
        }

        now.add(Calendar.DATE, 2);

        if (calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            if (calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH))
                if (calendar.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH))
                    str += " (" + ctx.getString(R.string.tomorrow_string) + ")";
        }

        date_text.setText(str);

        alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void openEvent(String id) {

        Intent intent = new Intent(ctx, EventActivity.class);

        intent.putExtra("calendar", listener.getCurrentDate());
        intent.putExtra("id", id);

        startActivity(intent);
    }

    public void deleteEvent(final String id, View v) {

        final PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(R.menu.delete_event_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                Cursor res = database.getEventById(id);
                res.moveToNext();

                database.deleteData(id, res.getString(2));

                alarmManager.cancel(PendingIntent.getBroadcast(ctx, Integer.valueOf(id), new Intent(ctx, AlarmReceiver.class), 0));

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
        no_events_text.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
    }
}
