package Interfaces;

/**
 * Created by gamrian on 27/08/2016.
 */
public interface CalendarListener {

    void newEvent(int day, int month, int year, boolean b);

    void openDay(int day, int month, int year);
}
