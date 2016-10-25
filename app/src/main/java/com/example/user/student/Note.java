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

    public Note(String title, String description, String xPos, String yPos) {
        this.title = title;
        this.description = description;
        this.xPos = xPos;
        this.yPos = yPos;
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

}