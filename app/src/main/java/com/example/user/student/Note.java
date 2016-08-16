package com.example.user.student;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by user on 25/01/16.
 */
public class Note {

    private String title;
    private String description;
    private String xPos;
    private String yPos;
    private String date;
    private String time;

    public Note(String title, String description, String xPos, String yPos, String date, String time) {
        this.title = title;
        this.description = description;
        this.xPos = xPos;
        this.yPos = yPos;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getxPos() {
        return xPos;
    }

    public void setxPos(String xPos) {
        this.xPos = xPos;
    }

    public String getyPos() {
        return yPos;
    }


    public void setyPos(String yPos) {
        this.yPos = yPos;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
