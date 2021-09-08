package com.example.thetodo.AppObjects;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notesGroups_table")
public class Groups {

    @PrimaryKey(autoGenerate = true)
    private String g_id;
    private String title,date;
    private boolean hidden=false;

    public Groups(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getG_id() {
        return g_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}

