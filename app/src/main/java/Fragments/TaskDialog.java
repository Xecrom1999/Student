package Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.student.R;

import java.util.List;

/**
 * Created by user on 20/02/16.
 */
public class TaskDialog extends DialogFragment{

    String title;
    String dis;
    String date;
    List<ImageView> images;

    public TaskDialog(String title, String dis, String date, List<ImageView> images) {
        this.title = title;
        this.dis = dis;
        this.date = date;
        this.images = images;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.task_dialog, null);

        TextView title = (TextView) view.findViewById(R.id.task_title2);
        TextView  dis = (TextView) view.findViewById(R.id.task_dis);
        TextView  date = (TextView) view.findViewById(R.id.task_date2);

        builder.setView(view);

        return builder.create();
    }

}
