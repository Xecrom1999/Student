package widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.user.student.R;

/**
 * Created by user on 12/02/16.
 */
public class ScheduleTabHost extends LinearLayout {

    RecyclerView recyclerView;
    ViewPager viewPager;

    public ScheduleTabHost(Context context) {
        super(context);
        initialize(context);
    }

    public ScheduleTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ScheduleTabHost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.schedule_tab_host, this);
    }
}
