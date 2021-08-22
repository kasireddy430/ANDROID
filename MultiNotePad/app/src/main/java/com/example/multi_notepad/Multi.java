package com.example.multi_notepad;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Multi implements Serializable {
    String title;
    String date;
    String description;


    public String getTitle(){ return title; }

    public void setTitle(String title){ this.title = title; }

    public String getDate(){ return date; }

    public void setDate(String date){ this.date = date; }

    public String getDescription(){ return description; }

    public void setDescription(String description){ this.description = description; }
    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}

