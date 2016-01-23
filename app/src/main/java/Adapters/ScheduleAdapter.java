package Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.student.R;
import com.example.user.student.Lesson;

import java.util.ArrayList;

import Interfaces.LessonsListener;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    ArrayList<Lesson> list;
    static LessonsListener listener;
    static Context ctx;

    public void addLesson(Lesson lesson) {
        list.add(lesson);
        notifyItemInserted(list.size()-1);
    }

    public ScheduleAdapter(Context ctx, ArrayList<Lesson> list, LessonsListener listener) {
        if (list == null)
            this.list = new ArrayList<>();
        else
            this.list = list;

        this.listener = listener;
        this.ctx = ctx;
    }

    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        if (position != list.size()) {
            viewHolder.name_text.setText(list.get(position).getName());
            viewHolder.time_text.setText("starts at " + list.get(position).getTime());
            viewHolder.length_text.setText(list.get(position).getLength() + " minutes");
        }
    }

    public void deleteLesson(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void updateLesson(int position, Lesson lesson) {
        list.get(position).setLesson(lesson);
        notifyItemChanged(position);
    }

    public Lesson getItemAtPosition(int itemPosition) {
        return list.get(itemPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView time_text, length_text, name_text;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

                time_text = (TextView) itemLayoutView.findViewById(R.id.startsAt_text);
                length_text = (TextView) itemLayoutView.findViewById(R.id.length_text);
                name_text = (TextView) itemLayoutView.findViewById(R.id.lesson_text);

            itemLayoutView.setOnLongClickListener(this);
        }

        public boolean onLongClick(View v) {
            String name = name_text.getText().toString();

            String time = time_text.getText().toString();
            time = time.substring(time.lastIndexOf(' ') + 1);

            String length = length_text.getText().toString();
            length = length.substring(0, length.indexOf(' '));
            listener.showMenu(getPosition(), new Lesson(name, time, length));
            return true;
        }
    }

    public int getItemCount() {
        return list.size();
    }
}