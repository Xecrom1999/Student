package Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.student.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Database.CalendarDB;
import Interfaces.CalendarListener;

/**
 * Created by gamrian on 19/08/2016.
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private final Context ctx;
    Calendar calendar;
    final int HEADER_TYPE = 0;
    final int ITEM_TYPE = 1;
    int position;
    int month;
    CalendarListener listener;
    CalendarDB database;
    ArrayList<String> datesList;
    ArrayList<String> titlesList;

    public CalendarAdapter(Context ctx, int position, CalendarListener listener, CalendarDB database) {
        this.listener = listener;
        this.ctx = ctx;
        this.position = position;
        this.database = database;

        datesList = getDatesList();
        titlesList = getTitlesList();

        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, position);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.getTime();
        month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.add(Calendar.DATE, -7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == HEADER_TYPE)
            view = LayoutInflater.from(ctx).inflate(R.layout.calendar_header, parent, false);
        else {
            view = LayoutInflater.from(ctx).inflate(R.layout.calendar_item, parent, false);
        }
        CalendarAdapter.ViewHolder viewHolder = new CalendarAdapter.ViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Calendar cal = Calendar.getInstance();

        holder.setMonth(calendar.get(Calendar.MONTH));

        if (holder.getItemViewType() == HEADER_TYPE) {
            holder.day_text.setText(new SimpleDateFormat("EE").format(calendar.getTime()));
            if (position + 1 == cal.get(Calendar.DAY_OF_WEEK) && calendar.get(Calendar.MONTH) == cal.get(Calendar.MONTH)-1)
                holder.day_text.setTextColor(ctx.getColor(R.color.primary_new_item));
        }

        else {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            holder.day_text.setText(day + "");
            if (day == cal.get(Calendar.DAY_OF_MONTH) && cal.get(Calendar.MONTH) == holder.month) {
                holder.day_text.setTextColor(ctx.getColor(R.color.primary_new_item));
                holder.day_text.setTypeface(null, Typeface.BOLD);
                holder.day_text.setTextSize(11);

            }
            if (month != calendar.get(Calendar.MONTH))
                holder.day_text.setTextColor(Color.parseColor("#BDBDBD"));
            for (int i = 0; i < datesList.size(); i++) { 
                    if (datesList.get(i).equals(String.valueOf(calendar.getTime()))) {
                        if (holder.num == 0) {
                            holder.title_text.setText(titlesList.get(i));
                        }
                        holder.num++;
                        if (holder.num > 1) {
                            holder.number_text.setVisibility(View.VISIBLE);
                            holder.number_text.setText((holder.num-1) + "+");
                        }
                    }
            }
        }

        calendar.add(Calendar.DATE, 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 7) return HEADER_TYPE;
        else return ITEM_TYPE;
    }

    @Override
    public int getItemCount() {
        return 49;
    }

    public ArrayList<String> getDatesList() {
        Cursor res = database.getAllData();
        ArrayList<String> datesList = new ArrayList<>();

        while (res.moveToNext()) {
            datesList.add(res.getString(2));
        }

        return datesList;
    }

    public ArrayList<String> getTitlesList() {
        Cursor res = database.getAllData();
        ArrayList<String> titlesList = new ArrayList<>();

        while (res.moveToNext()) {
            titlesList.add(res.getString(1));
        }

        return titlesList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView day_text;
        TextView title_text;
        TextView number_text;

        RelativeLayout root;
        int month;

        int num;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == HEADER_TYPE)
                day_text = (TextView) itemView.findViewById(R.id.calendar_item_text2);
            else {
                day_text = (TextView) itemView.findViewById(R.id.calendar_item_text);
                title_text = (TextView) itemView.findViewById(R.id.calendar_item_title);
                number_text = (TextView) itemView.findViewById(R.id.calendar_item_number);
                number_text.setVisibility(View.INVISIBLE);
                root = (RelativeLayout) itemView.findViewById(R.id.item_root);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);

                num = 0;
            }
        }

        public void setMonth(int month) {
            this.month = month;
        }

        @Override
        public void onClick(View v) {
            listener.newEvent(Integer.valueOf(day_text.getText().toString()), month, calendar.get(Calendar.YEAR), !title_text.getText().toString().isEmpty());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.openDay(Integer.valueOf(day_text.getText().toString()), month, calendar.get(Calendar.YEAR));
            return true;
        }
    }
}
