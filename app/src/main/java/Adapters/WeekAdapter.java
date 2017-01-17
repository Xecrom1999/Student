package Adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.ariel.student.Lesson;
import com.ariel.student.student.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {

    ArrayList<Lesson>[] list;
    static Context ctx;
    final static int HEADER_TYPE = 0;
    final static int ITEM_TYPE = 1;
    Calendar calendar;
    SimpleDateFormat format;
    final int DAYS_NUM = 6;

    public WeekAdapter(Context ctx, ArrayList<Lesson>[] list) {
        this.ctx = ctx;
        this.list = list;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        format = new SimpleDateFormat("EE");
    }

    public WeekAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView;

        if (viewType == HEADER_TYPE) {
            itemLayoutView = LayoutInflater.from(ctx).inflate(R.layout.week_view_header, parent, false);
        }

        else itemLayoutView = LayoutInflater.from(ctx).inflate(R.layout.week_view_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView, viewType);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        if (position < DAYS_NUM) {
            viewHolder.name_text.setText(format.format(calendar.getTime()));
            Typeface font = Typeface.createFromAsset(ctx.getAssets(), "fonts/font1.ttf");
            viewHolder.name_text.setTypeface(font);
            calendar.add(Calendar.DATE, 1);
        }

        else {

            Typeface font = Typeface.createFromAsset(ctx.getAssets(), "fonts/font2.ttf");
            viewHolder.name_text.setTypeface(font);

            position -= DAYS_NUM;

            int day = 5 - (position % DAYS_NUM);

            if (position / DAYS_NUM < list[day].size()) {
                String name = list[day].get(position / DAYS_NUM).getName();
                if (name.length() > 9) name = name.substring(0, 9);
                viewHolder.name_text.setText(name);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < DAYS_NUM) return HEADER_TYPE;
        return ITEM_TYPE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name_text;
        LinearLayout layout;

        public ViewHolder(View itemLayoutView, int viewType) {
            super(itemLayoutView);

            if (viewType == HEADER_TYPE)
                name_text = (TextView) itemLayoutView.findViewById(R.id.gridName_text2);
            else {
                name_text = (TextView) itemLayoutView.findViewById(R.id.gridName_text);
                layout = (LinearLayout) itemLayoutView.findViewById(R.id.week_layout);
            }
        }
    }

    public int getItemCount() {
        int max = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i].size() > max) max = list[i].size();
        }
        return DAYS_NUM * (max + 1);
    }
}