package com.example.user.student;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by user on 25/01/16.
 */
public class Task {

    private String title;
    private String description;
    private ArrayList<ImageView> imagesList;
    private String when;

    public Task(String task, String description, ArrayList<ImageView> imagesList, String when) {
        this.title = task;
        this.description = description;
        this.imagesList = imagesList;
        this.when = when;
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

    public ArrayList<ImageView> getImagesList() {
        return imagesList;
    }

    public void setImagesList(ArrayList<ImageView> imagesList) {
        this.imagesList = imagesList;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }
}
