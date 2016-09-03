package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.student.R;

import java.util.Date;

/**
 * Created by gamrian on 31/08/2016.
 */
public class CalendarDayFragment extends Fragment {

    public CalendarDayFragment(Date time) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calendar_day_fragment, container, false);

        return view;
    }
}
