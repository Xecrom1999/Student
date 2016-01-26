package Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.student.R;
import com.example.user.student.Task;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeworkFragment extends Fragment {

    TextView title_text;
    TextView description_text;
    View layout;
    Task task;
    boolean isEmpty;

    public HomeworkFragment(){
        isEmpty = true;
    }

    public HomeworkFragment(Task task) {
        isEmpty = false;
        this.task = task;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.homework_fragment_layout, container, false);

        title_text = (TextView) layout.findViewById(R.id.title_text);

        description_text = (TextView) layout.findViewById(R.id.description_text);

        if (isEmpty) {
            title_text.setVisibility(View.INVISIBLE);

            description_text.setText("Choose a task to display.");
            description_text.setGravity(Gravity.RIGHT);
        }
        else {
            title_text.setText(task.getTitle());
            description_text.setText(task.getDescription());
        }


        return layout;
    }
}
