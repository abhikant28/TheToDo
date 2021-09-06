package com.example.thetodo;

public class Notes {
    private String title,body,date,g_id;

    public Notes(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }
}
