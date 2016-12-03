package Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.student.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Database.CalendarDB;
import Interfaces.CalendarDayListener;

/**
 * Created by gamrian on 14/09/2016.
 */
public class CalendarDayAdapter extends RecyclerView.Adapter<CalendarDayAdapter.ViewHolder> {

    private final CalendarDB database;
    private final Calendar calendar;
    Context ctx;
    ArrayList<String> ids;
    ArrayList<String> titles;
    ArrayList<String> times;
    static CalendarDayListener listener;

    public CalendarDayAdapter(Context ctx, CalendarDB database, Calendar calendar, CalendarDayListener listener) {
        this.ctx = ctx;
        this.database = database;
        this.calendar = calendar;
        this.listener = listener;
        titles = new ArrayList<>();
        times = new ArrayList<>();
        ids = new ArrayList<>();
    }

    private void setLists() {

        titles.clear();
        times.clear();
        ids.clear();

        Cursor res = database.getAllEventsAtDate(calendar);

        while (res.moveToNext()) {
            ids.add(res.getString(0));
            titles.add(res.getString(1));
            times.add(res.getString(3));
        }

        ArrayList <Integer> list = new ArrayList<>();

        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).isEmpty()) list.add(2400 + i);
            else list.add(Integer.valueOf(times.get(i).replace(":", "")));
        }

        for (int i = 0; i < times.size() - 1; i++) {
            int min = list.get(i);
            int p = i;
            for (int j = i + 1; j < times.size(); j++) {
                if (list.get(j) < min) {
                    min = list.get(j);
                    p = j;
                }
            }
            int x = list.get(i);
            String id = ids.get(i);
            String m = titles.get(i);
            String t = times.get(i);
            list.set(i, min);
            list.set(p, x);
            ids.set(i, ids.get(p));
            ids.set(p, id);
            titles.set(i, titles.get(p));
            titles.set(p, m);
            times.set(i, times.get(p));
            times.set(p, t);
        }
    }

    public void update() {
        setLists();
        notifyDataSetChanged();
    }

    @Override
    public CalendarDayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(ctx).inflate(R.layout.calendar_day_item, parent, false);

        CalendarDayAdapter.ViewHolder viewHolder = new CalendarDayAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.position = position;
        holder.title_text.setText(titles.get(position));
        holder.time_text.setText(times.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        int position;
        TextView title_text;
        TextView time_text;

        public ViewHolder(View itemView) {
            super(itemView);

            title_text = (TextView) itemView.findViewById(R.id.day_item_title);
            time_text = (TextView) itemView.findViewById(R.id.day_item_time);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.openEvent(ids.get(position));
        }

        @Override
        public boolean onLongClick(View v) {
            listener.deleteEvent(ids.get(position), v);
            return true;
        }
    }
}
