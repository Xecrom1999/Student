package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.user.student.R;

import java.util.ArrayList;

/**
 * Created by user on 24/01/16.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>  {

    Context ctx;
    ArrayList<String> list;

    public ListAdapter(Context ctx) {
        this.ctx = ctx;
        list = new ArrayList<>();
    }

    public void addTask() {
        list.add("");
        notifyItemInserted(list.size() - 1);
    }

    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;

    }

    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {

    }

    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.taskText);
            checkBox = (CheckBox) itemView.findViewById(R.id.taskCheckBox);
        }
    }
}
