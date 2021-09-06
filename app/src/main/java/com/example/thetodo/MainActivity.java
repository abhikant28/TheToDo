package com.example.thetodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static public ArrayList<Tasks> myTasks = new ArrayList<>();
    static public ArrayList<Groups> myGroups = new ArrayList<>();
    static public ArrayList<Notes> allNotes = new ArrayList<>();

    private RecyclerView task_RecyclerView;
    public Task_RecyclerView_Adapter.RecyclerViewClickListener task_listener;
    private RecyclerView notes_group_RecyclerView;
    public Task_RecyclerView_Adapter.RecyclerViewClickListener groups_listener;
    private RecyclerView notes_all_RecyclerView;
    public Notes_RecyclerView_Adapter.RecyclerViewClickListener allNotes_listener;

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

        setTaskAdapter();
        setAllNotesAdapter();

        ev_task_tapToAdd.addTextChangedListener(checkText);

    }

    private TextWatcher checkText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                ib_add_task.setClickable((charSequence.length()>0));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void setTaskAdapter() {
        Task_RecyclerView_Adapter adapter = new Task_RecyclerView_Adapter(myTasks, task_listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        task_RecyclerView.setLayoutManager(layoutManager);
        task_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        task_RecyclerView.setAdapter(adapter);
    }

    private void setAllNotesAdapter() {
        Notes_RecyclerView_Adapter adapter = new Notes_RecyclerView_Adapter(allNotes, allNotes_listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        notes_all_RecyclerView.setLayoutManager(layoutManager);
        notes_all_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        notes_all_RecyclerView.setAdapter(adapter);
    }


    private void demoTaskData() {
        myTasks.add(new Tasks("Daily Task 1",false,"Weekly"));
        myTasks.add(new Tasks("Daily Task 2",false,"Weekly"));
        myTasks.add(new Tasks("Daily Task 3",false,"Weekly"));
        myTasks.add(new Tasks("Daily Task 4",true,"Weekly"));
        myTasks.add(new Tasks("Daily Task 5",false,"Weekly"));
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
        myGroups.add(new Groups("awsdw","First Group"));
        myGroups.add(new Groups("awsdw","Second Group"));
        myGroups.add(new Groups("awsdw","Third Group"));
        myGroups.add(new Groups("awsdw","Fourth Group"));
    }

    public void addTask(View view) {
        myTasks.add(0,new Tasks(ev_task_tapToAdd.getText().toString(),false,"Daily"));
        ev_task_tapToAdd.setText("");
    }
}