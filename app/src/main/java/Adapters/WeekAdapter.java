package Adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.student.Lesson;
import com.example.user.student.R;

import java.util.ArrayList;


public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {

    ArrayList<Lesson>[] list;
    static Context ctx;

    public WeekAdapter(Context ctx, ArrayList<Lesson>[] list) {
        this.ctx = ctx;
        this.list = list;
    }

    public WeekAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_grid, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        int day = position % 7;

        if (position / 7 < list[day].size()) {
            String name = list[day].get(position / 7).getName();
            if (name.length() > 4) name = name.substring(0, 4);
            viewHolder.name_text.setText(name);
        }
        else {
            viewHolder.card.setVisibility(View.INVISIBLE);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name_text;
        CardView card;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name_text = (TextView) itemLayoutView.findViewById(R.id.gridName_text);
            card = (CardView) itemLayoutView.findViewById(R.id.card);
        }
    }

    public int getItemCount() {
        int max = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i].size() > max) max = list[i].size();
        }
        return list.length * max;
    }
}