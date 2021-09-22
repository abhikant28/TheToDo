package com.example.thetodo.AppObjects;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notesGroups_table")
public class Groups {

    @PrimaryKey(autoGenerate = true)
    private int g_id;
    private String title,date;
    private boolean show =false;

    public Groups(String title) {
        this.title = title;
        Date getDate=java.util.Calendar.getInstance().getTime();
        this.date=String.valueOf(getDate).substring(0, 10);
    }

    public int getG_id() {
        return g_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public boolean isShow() {
        return show;
    }

    public void setG_id(int g_id) {
        this.g_id = g_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}

