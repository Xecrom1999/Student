package Adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.student.Lesson;
import com.example.user.student.R;

import java.util.ArrayList;
import java.util.Arrays;

import Interfaces.ScheduleListener;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    ArrayList<Lesson> list;
    static Context ctx;
    final static int ITEM_TYPE = 1;
    final static int TAIL_TYPE = 2;
    boolean editMode;
    static ScheduleListener listener;

    public ScheduleAdapter(Context ctx, ArrayList<Lesson> list, ScheduleListener listener) {

        if (list == null)
            this.list = new ArrayList<>();
        else {
            sortList(list);
        }
        this.ctx = ctx;
        editMode = false;
        this.listener = listener;
    }

    public void addLesson(Lesson lesson) {
        list.add(lesson);
        notifyItemInserted(list.size() - 1);
        sortList(list);
    }

    private void sortList(ArrayList<Lesson> list2) {

        ArrayList<Lesson> list = new ArrayList<>();

        for (int i = 0; i < list2.size(); i++) list.add(list2.get(i));

        ArrayList<Lesson> newList = new ArrayList<>();

        if (list == null) {
            this.list = newList;
            return;
        }

        int[] arr = new int[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Integer.parseInt(list.get(i).getTime().replace(":", ""));
        }
        Arrays.sort(arr);

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                if (arr[i] == Integer.parseInt(list.get(j).getTime().replace(":", ""))) {
                    newList.add(list.get(j));
                    list.remove(j);
                }
            }
        }
        this.list = newList;
        notifyDataSetChanged();
    }

    public void toggleEditMode() {
        this.editMode = !this.editMode;
        notifyDataSetChanged();
    }

    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView;

        if (viewType == ITEM_TYPE || !editMode)
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item_layout, parent, false);
        else
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_lesson_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView, viewType);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        if (position == list.size() && editMode) return;

        Typeface font = Typeface.createFromAsset(ctx.getAssets(), "fonts/font2.ttf");
        viewHolder.name_text.setTypeface(font);
        viewHolder.number_text.setTypeface(font);

        if (editMode) {
            viewHolder.layout.setBackground(ctx.getResources().getDrawable(R.drawable.lesson_item));
            viewHolder.layout.setAlpha(0.5f);
        }
        else {
            viewHolder.layout.setBackgroundColor(ctx.getResources().getColor(R.color.dark_color));
            viewHolder.layout.setAlpha(1f);
        }

        Lesson lesson = list.get(position);

        viewHolder.name_text.setText(lesson.getName());
        viewHolder.time_text.setText(ctx.getString(R.string.starts_at_string) + " " + lesson.getTime());
        viewHolder.length_text.setText(lesson.getLength() + " " + ctx.getString(R.string.minutes_string));
        viewHolder.number_text.setText(position + 1 + "");
        viewHolder.delete_button.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);

        viewHolder.lesson = lesson;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) return TAIL_TYPE;
        return ITEM_TYPE;
    }

    public void deleteLesson(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void updateLesson(int position, Lesson lesson) {
        list.get(position).setLesson(lesson);
        notifyItemChanged(position);
        sortList(list);
    }

    public Lesson getItemAtPosition(int itemPosition) {
        return list.get(itemPosition);
    }

    public void deleteAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return (editMode ? list.size() + 1 : list.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView time_text, length_text, name_text, number_text;
        ImageView delete_button;
        RelativeLayout layout;
        Lesson lesson;

        public ViewHolder(View itemLayoutView, int viewType) {
            super(itemLayoutView);

            if (viewType == TAIL_TYPE) return;

            time_text = (TextView) itemLayoutView.findViewById(R.id.startsAt_text);
            length_text = (TextView) itemLayoutView.findViewById(R.id.length_text);
            name_text = (TextView) itemLayoutView.findViewById(R.id.lesson_text);
            number_text = (TextView) itemLayoutView.findViewById(R.id.lesson_number);
            layout = (RelativeLayout) itemLayoutView.findViewById(R.id.lesson_layout);
            delete_button = (ImageView) itemLayoutView.findViewById(R.id.lesson_delete);

            delete_button.setOnClickListener(this);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (getPosition() < 0) return;

            if (delete_button.getVisibility() == View.INVISIBLE) return;

            switch (v.getId()) {
                case R.id.lesson_delete:
                    listener.deleteLesson(getPosition(), lesson);
                    break;
                case R.id.lesson_layout:
                    listener.openLesson(getPosition(), lesson);
                    break;
            }
        }
    }
}

