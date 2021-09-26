package com.example.thetodo.AppObjects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.thetodo.Daos.DataConverter;

import java.util.Calendar;

@Entity(tableName = "tasks_table")
@TypeConverters(DataConverter.class)
public class Tasks {

    @PrimaryKey(autoGenerate = true)
    private int t_id;
    private String title;
    private boolean completed;
    private String type;
    private Calendar date;
    private boolean show=true;

    public Tasks(String title, boolean completed, String type) {
        this.title = title;
        this.completed = completed;
        this.type= type;

    }

    public Calendar getDate() {
        return date;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }
}
