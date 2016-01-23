package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.student.R;

import java.util.ArrayList;
import java.util.Collections;

import Interfaces.LessonsNameListener;

/**
 * Created by user on 16/01/16.
 */
public class LessonsNamesAdapter extends RecyclerView.Adapter<LessonsNamesAdapter.ViewHolder> {

    ArrayList<String> list;
    String[] names;
    Context ctx;
    static LessonsNameListener listener;

    public LessonsNamesAdapter(Context ctx, LessonsNameListener listener) {
        list = new ArrayList<>();
        names = new String[]{"Math", "Physics", "Sport", "Computer Science", "English", "Hebrew", "History", "Bible", "Citizenship", "Education", "Geography", "Biology", "Art", "Chemistry", "Theater", "Music", };
        Collections.addAll(list, names);
        this.ctx = ctx;
        this.listener = listener;
    }

    public void addLesson(String name) {
        list.add(name);
    }

    public LessonsNamesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView;

        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_name_layout, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.name_text.setText(list.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name_text;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name_text = (TextView) itemLayoutView.findViewById(R.id.name_text);

            itemLayoutView.setOnClickListener(this);
        }


        public void onClick(View v) {
            listener.recyclerViewListClicked(name_text.getText().toString());
        }
    }

    public int getItemCount() {
        return list.size();
    }

}
