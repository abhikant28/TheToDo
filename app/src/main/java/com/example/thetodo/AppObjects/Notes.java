package com.example.thetodo.AppObjects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notes_table")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    private String n_id;

    private String title,body,date,g_id;
    private boolean show;

    public Notes(String title, String body) {
        this.title = title;
        this.body = body;
        Date getDate=java.util.Calendar.getInstance().getTime();
        this.date=String.valueOf(getDate).substring(0, 10)+","+String.valueOf(getDate).substring(20);

    }

    public String getN_id() {
        return n_id;
    }

    public void setN_id(String n_id) {
        this.n_id = n_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
