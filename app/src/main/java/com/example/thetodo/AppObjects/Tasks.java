package com.example.thetodo.AppObjects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks_table")
public class Tasks {

    @PrimaryKey(autoGenerate = true)
    private int t_id;
    private String title;
    private boolean completed;
    private String type;
//    private String next;

    public Tasks(String title, boolean completed, String type) {
        this.title = title;
        this.completed = completed;
        this.type= type;

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
