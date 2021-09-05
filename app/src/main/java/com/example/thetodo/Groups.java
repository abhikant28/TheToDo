package com.example.thetodo;

import java.util.ArrayList;

public class Groups {
    private String g_id,title;
    private boolean hidden;
    private ArrayList<Notes> notes;

    public Groups(String g_id, String title, ArrayList<Notes> notes) {
        this.g_id = g_id;
        this.title = title;
        this.hidden = hidden;
        this.notes = notes;
    }
}

