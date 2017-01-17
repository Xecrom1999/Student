package Interfaces;

import app.ariel.student.Lesson;

/**
 * Created by gamrian on 09/09/2016.
 */
public interface ScheduleListener {

    void deleteLesson(int position, Lesson lesson);

    void openLesson(int position, Lesson lesson);

    void newLesson();
}
