package Interfaces;

import android.view.View;

/**
 * Created by gamrian on 17/09/2016.
 */
public interface CalendarDayListener {

    void openEvent(int position);

    void deleteEvent(int position, View v);
}
