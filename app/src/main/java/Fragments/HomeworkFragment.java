package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.student.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeworkFragment extends Fragment {

    TextView title_text;
    TextView description_text;
    View layout;

    public HomeworkFragment() {
    }

    public void setTask(String title, String description) {
        this.title_text.setText("Title: " + title);
        this.description_text.setText("Description: " + description);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.homework_fragment_layout, container, false);

        title_text = (TextView) layout.findViewById(R.id.title_text);

        description_text = (TextView) layout.findViewById(R.id.description_text);

        return layout;
    }
}
