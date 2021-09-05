package com.example.thetodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static public ArrayList<Tasks> myTasks = new ArrayList<>();
    static public ArrayList<Groups> myGroups = new ArrayList<>();
    static public ArrayList<Notes> allNotes = new ArrayList<>();

    private RecyclerView task_RecyclerView;
    private RecyclerView notes_group_RecyclerView;
    private RecyclerView notes_all_RecyclerView;

    private EditText ev_task_tapToAdd;
    private TextView tv_task_remind;
    private TextView tv_task_repeat;
    private TextView tv_notes_newNote;
    private TextView tv_notes_newGroup;
    private ImageButton ib_add_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        task_RecyclerView=findViewById(R.id.Main_Super_Task_RecyclerView);
        notes_group_RecyclerView=findViewById(R.id.Main_Notes_Super_RecyclerView);
        notes_all_RecyclerView=findViewById(R.id.Main_Super_RecyclerView_AllNotes_List);

        ev_task_tapToAdd=findViewById(R.id.Main_Task_TapToAdd);
        ib_add_task=findViewById(R.id.Main_Task_ImageButton_AddButton);
        tv_task_remind=findViewById(R.id.Main_Task_TextView_Remind);
        tv_task_repeat=findViewById(R.id.Main_Task_TextView_Repeat);
        tv_notes_newGroup=findViewById(R.id.Main_Notes_TextView_NewNoteGroup);
        tv_notes_newNote=findViewById(R.id.Main_Notes_TextView_NewNote);

        demoGroupsData();
        demoNotesData();
        demoTaskData();



    }



    private void demoTaskData() {
        myTasks.add(new Tasks("Daily Task 1",true,"Weekly"));
        myTasks.add(new Tasks("Daily Task 2",true,"Weekly"));
        myTasks.add(new Tasks("Daily Task 3",true,"Weekly"));
        myTasks.add(new Tasks("Daily Task 4",false,"Weekly"));
        myTasks.add(new Tasks("Daily Task 5",true,"Weekly"));
    }
    private void demoNotesData(){
        allNotes.add(new Notes("First Note","Content of the note"));
        allNotes.add(new Notes("Second Note","Content of the note"));
        allNotes.add(new Notes("Third Note","Content of the note"));
        allNotes.add(new Notes("Fourth Note","Content of the note"));
        allNotes.add(new Notes("Fifth Note","Content of the note"));
        allNotes.add(new Notes("Sixth Note","Content of the note"));
    }
    private void demoGroupsData(){
        myGroups.add(new Groups("awsdw","First Group",allNotes));
        myGroups.add(new Groups("awsdw","Second Group",allNotes));
        myGroups.add(new Groups("awsdw","Third Group",allNotes));
        myGroups.add(new Groups("awsdw","Fourth Group",allNotes));
    }
}