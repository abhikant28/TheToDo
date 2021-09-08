package com.example.thetodo.AppObjects;

public class Tasks {
    private int t_id;
    private String title;
    private boolean completed;
    private String type;

    public Tasks(String title, boolean completed, String type) {
        this.title = title;
        this.completed = completed;
        this.type= type;
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
