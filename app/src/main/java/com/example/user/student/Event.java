package com.example.user.student;

/**
 * Created by gamrian on 14/09/2016.
 */
public class Event {

    private String title;
    private String date;
    private String time;
    private String comment;
    private String reminder;

    public Event(String title, String date, String time, String comment, String reminder) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.reminder = reminder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }
}
