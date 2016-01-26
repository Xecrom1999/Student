package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.student.R;
import com.example.user.student.Task;

import java.util.ArrayList;

import Interfaces.TaskListener;

/**
 * Created by user on 24/01/16.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>  {

    Context ctx;
    ArrayList<Task> list;
    static TaskListener listener;

    public ListAdapter(Context ctx, TaskListener listener) {
        this.ctx = ctx;
        list = new ArrayList<>();
        this.listener = listener;
    }

    public void addTask(String title, String description, ArrayList<ImageView> imagesList) {
        list.add(new Task(title, description, imagesList, ""));
        notifyItemInserted(list.size() - 1);
    }

    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;

    }

    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {

        holder.title_text.setText(list.get(position).getTitle());
        holder.description += list.get(position).getDescription();
    }

    public int getItemCount() {
        return list.size();
    }

    public void moveItemToEndOfList(int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_text;
        String description = "Description: ";
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            title_text = (TextView) itemView.findViewById(R.id.taskText);
            checkBox = (CheckBox) itemView.findViewById(R.id.taskCheckBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.changeTask(list.get(getPosition()));
                }
            });
        }
    }
}
