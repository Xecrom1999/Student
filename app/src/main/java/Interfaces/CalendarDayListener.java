package Interfaces;

import android.view.View;

/**
 * Created by gamrian on 17/09/2016.
 */
public interface CalendarDayListener {

    void openEvent(String position);

    void deleteEvent(String position, View v);
}
