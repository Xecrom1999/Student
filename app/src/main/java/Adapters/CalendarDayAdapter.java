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

import java.util.ArrayList;
import java.util.Date;

import Database.CalendarDB;

/**
 * Created by gamrian on 14/09/2016.
 */
public class CalendarDayAdapter extends RecyclerView.Adapter<CalendarDayAdapter.ViewHolder> {

    private final CalendarDB database;
    private final String date;
    Context ctx;
    ArrayList<String> titles;

    public CalendarDayAdapter(Context ctx, CalendarDB database, Date date) {
        this.ctx = ctx;
        this.database = database;
        this.date = String.valueOf(date);

        titles = getTitles();
    }

    @Override
    public CalendarDayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(ctx).inflate(R.layout.calendar_day_item, parent);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CalendarDayAdapter.ViewHolder holder, int position) {
        holder.title_text.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public ArrayList<String> getTitles() {
        Cursor res = database.getAllTitles(date);
        ArrayList<String> titlesList = new ArrayList<>();

        while (res.moveToNext()) {
            titlesList.add(res.getString(1));
        }

        return titlesList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_text;

        public ViewHolder(View itemView) {
            super(itemView);

            title_text = (TextView) itemView.findViewById(R.id.day_item_title);
        }
    }
}
