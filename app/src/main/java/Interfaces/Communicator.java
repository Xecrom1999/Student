package Interfaces;

import com.example.user.student.Lesson;

import java.util.ArrayList;

/**
 * Created by user on 02/01/16.
 */
public interface Communicator {

    void lessonDone(Lesson lesson);

    void updateLesson(int position, Lesson lesson);

    void newLesson(Lesson lesson);

    ArrayList<Lesson> getList(int p);
}
