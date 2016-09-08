package com.example.user.student;

/**
 * Created by user on 15/01/16.
 */
public class Lesson {

    private String name;
    private String time;
    private String length;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Lesson(String lesson, String time, String length) {
        this.name = lesson;
        this.time = time;
        this.length = length;
    }

    public void setLesson(Lesson lesson) {
        this.name = lesson.getName();
        this.time = lesson.getTime();
        this.length = lesson.length;
    }

    public String toString() {
        return "Lesson{" +
                "name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", length='" + length + '\'' +
                '}';
    }


}


