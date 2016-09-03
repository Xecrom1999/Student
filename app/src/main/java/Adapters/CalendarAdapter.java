package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.student.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public CalendarAdapter(Context ctx, int position, CalendarListener listener) {
        this.listener = listener;
        this.ctx = ctx;
        this.position = position;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, position);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.getTime();
        month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.add(Calendar.DATE, -7);
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

        holder.setMonth(calendar.get(Calendar.MONTH));

        if (holder.getItemViewType() == HEADER_TYPE)
            holder.day_text.setText(new SimpleDateFormat("EE").format(calendar.getTime()));
        else {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            holder.day_text.setText(day + "");
            Calendar cal = Calendar.getInstance();
            if (day == cal.get(Calendar.DAY_OF_MONTH) && cal.get(Calendar.MONTH) == holder.month)
                holder.root.setBackground(ctx.getResources().getDrawable(R.drawable.rectangle_drawable2));
            if (month != calendar.get(Calendar.MONTH)) holder.day_text.setTextColor(Color.parseColor("#BDBDBD"));
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView day_text;
        LinearLayout root;
        int month;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == HEADER_TYPE)
                day_text = (TextView) itemView.findViewById(R.id.calendar_item_text2);
            else {
                day_text = (TextView) itemView.findViewById(R.id.calendar_item_text);
                root = (LinearLayout) itemView.findViewById(R.id.item_root);
                itemView.setOnClickListener(this);
            }
        }

        public void setMonth(int month) {
            this.month = month;
        }

        @Override
        public void onClick(View v) {
            listener.openDay(Integer.valueOf(day_text.getText().toString()), month, calendar.get(Calendar.YEAR));
        }
    }
}
