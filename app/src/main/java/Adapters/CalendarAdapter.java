package Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.student.CalendarActivity;
import com.example.user.student.Event;
import com.example.user.student.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    static CalendarListener listener;
    static CalendarDB database;
    Calendar mCalendar;
    long t;


    final int NUM_OF_ITEMS = 49;

    public CalendarAdapter(Context ctx, int position, CalendarListener listener, CalendarDB database) {
        this.listener = listener;
        this.ctx = ctx;
        this.position = position;
        if (this.database == null) this.database = database;

        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, this.position);
        calendar.getTime();

        month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.getTime();

        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.getTime();

        calendar.add(Calendar.DATE, -7);
        calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        mCalendar = Calendar.getInstance();
        mCalendar.setTime(calendar.getTime());
    }
    
    public void updateSelf(Date d) {

        final int p = (int) ((d.getTime() - mCalendar.getTimeInMillis()) / 86400000);

        if (p < 6 || p > (NUM_OF_ITEMS - 1)) return;

        calendar.setTime(mCalendar.getTime());
        calendar.add(Calendar.DATE, p);

        notifyItemChanged(p);
    }

    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == HEADER_TYPE)
            view = LayoutInflater.from(ctx).inflate(R.layout.calendar_header, parent, false);
        else {
            view = LayoutInflater.from(ctx).inflate(R.layout.calendar_item, parent, false);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) convertDpToPixel(79));
            view.setLayoutParams(params);
        }
        CalendarAdapter.ViewHolder viewHolder = new CalendarAdapter.ViewHolder(view, viewType);
        return viewHolder;
    }

    public float convertDpToPixel(float dp){
        Resources resources = ctx.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == 0) t = System.currentTimeMillis();

        holder.num = 0;

        Typeface font = Typeface.createFromAsset(ctx.getAssets(), "fonts/font1.ttf");

        Calendar cal = Calendar.getInstance();

        holder.month = calendar.get(Calendar.MONTH);
        holder.year = calendar.get(Calendar.YEAR);

        if (holder.getItemViewType() == HEADER_TYPE) {
            holder.day_text.setTypeface(font);
            holder.day_text.setText(new SimpleDateFormat("EE").format(calendar.getTime()));
            if (position + 1 == cal.get(Calendar.DAY_OF_WEEK) && this.position == 0)
                holder.day_text.setTextColor(ctx.getResources().getColor(R.color.calendar_accent));
        }

        else {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            holder.day_text.setText(day + "");
            if (day == cal.get(Calendar.DAY_OF_MONTH) && this.position == 0) {
                holder.day_text.setTextColor(ctx.getResources().getColor(R.color.calendar_accent));
                holder.day_text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }

            if (month != holder.month) {
                holder.root.setAlpha(0.5f);
            }

            boolean has = false;

            ArrayList<Event> list1 = new ArrayList<>();

            Cursor res = database.getAllEventsAtDate(String.valueOf(calendar.getTime().getTime()));

            while (res.moveToNext()) {
                list1.add(new Event(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5)));
            }

            res.close();

            for (int i = 0; i < list1.size(); i++) {
                    if (list1.get(i).getDate().equals(String.valueOf(calendar.getTimeInMillis()))) {
                        has = true;
                        if (holder.num == 0) {
                            holder.title_text.setText(list1.get(i).getTitle());
                            holder.number_text.setText("");
                        }
                        holder.num++;
                        if (holder.num > 1) {
                            holder.number_text.setVisibility(View.VISIBLE);
                            holder.number_text.setText((holder.num - 1) + "+");
                        } else holder.number_text.setVisibility(View.INVISIBLE);

                    }
            }
            if (!has) {
                holder.title_text.setText("");
                holder.number_text.setText("");
            }
        }
        calendar.add(Calendar.DATE, 1);

        if (position == getItemCount() - 1) {
            //Log.d("MYLOG", String.valueOf(System.currentTimeMillis() - t));
        }

        //All code ~ 304
        //No events ~
        //No code ~ 139
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 7) return HEADER_TYPE;
        else return ITEM_TYPE;
    }

    @Override
    public int getItemCount() {
        return NUM_OF_ITEMS;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView day_text;
        TextView title_text;
        TextView number_text;

        RelativeLayout root;
        int month;
        int year;
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

        @Override
        public void onClick(View v) {
            listener.itemClicked(Integer.valueOf(day_text.getText().toString()), month, year, !title_text.getText().toString().isEmpty());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.itemClicked(Integer.valueOf(day_text.getText().toString()), month, year, true);
            return true;
        }
    }
}
