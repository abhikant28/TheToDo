package com.example.thetodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.AppObjects.Tasks;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static public ArrayList<Tasks> myTasks = new ArrayList<>();
    static public ArrayList<Groups> myGroups = new ArrayList<>();
    static public ArrayList<Notes> allNotes = new ArrayList<>();
    static boolean DATABASE=false;

    SharedPreferences sharedPreferences;
    //public SQLiteDatabase sqlDB = this.openOrCreateDatabase(String.valueOf(R.string.app_name), MODE_PRIVATE, null);

    private RecyclerView task_RecyclerView;
    Task_RecyclerView_Adapter task_adapter;
    public Task_RecyclerView_Adapter.RecyclerViewClickListener task_listener;
    private RecyclerView notes_group_RecyclerView;
    public Notes_Groups_RecyclerView_Adapter.RecyclerViewClickListener groups_listener;
    private RecyclerView notes_all_RecyclerView;
    public Notes_RecyclerView_Adapter.RecyclerViewClickListener allNotes_listener;

    private EditText ev_task_tapToAdd;
    private TextView tv_task_remind;
    private TextView tv_task_repeat;
    private TextView tv_notes_newNote;
    private TextView tv_notes_newGroup;
    private ImageButton ib_add_task;

    public static ArrayList<Notes> getGroupNotes(String g_id) {
        ArrayList<Notes> notes= new ArrayList<>();



        return notes;
    }

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

        if(DATABASE){
            fetchAllData();
        }else{

        }

//        demoGroupsData();
//        demoNotesData();
//        demoTaskData();

        setTaskAdapter();
        setAllNotesAdapter();
        setNotesGroupsAdapter();

        ev_task_tapToAdd.addTextChangedListener(checkText);

    }

    private void fetchAllData() {
       // sqlDB.query();
    }

    private TextWatcher checkText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if(charSequence.length()>0){
                ib_add_task.setClickable(true);
           //     ib_add_task.setBackgroundColor(Color.parseColor("#FF6DC530"));
            }else{
                ib_add_task.setClickable(false);
             //   ib_add_task.setBackgroundColor(Color.parseColor("#817E7E"));

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void setNotesGroupsAdapter() {
        Notes_Groups_RecyclerView_Adapter adapter = new Notes_Groups_RecyclerView_Adapter(myGroups, groups_listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        notes_group_RecyclerView.setLayoutManager(layoutManager);
        notes_group_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        notes_group_RecyclerView.setAdapter(adapter);
    }

    private void setTaskAdapter() {
        task_adapter = new Task_RecyclerView_Adapter(myTasks, task_listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        task_RecyclerView.setLayoutManager(layoutManager);
        task_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        task_RecyclerView.setAdapter(task_adapter);
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
        myGroups.add(new Groups("First Group","1 Aug,2021"));
        myGroups.add(new Groups("Second Group","2 Aug,2022"));
        myGroups.add(new Groups("Third Group","4 Aug,2023"));
        myGroups.add(new Groups("Fourth Group","5 Aug,2024"));
        myGroups.add(new Groups("Fifth Group","6 Aug,2025"));

    }

    public void addTask(View view) {
        if(!ev_task_tapToAdd.getText().toString().isEmpty()){
            myTasks.add(0, new Tasks(ev_task_tapToAdd.getText().toString(), false, "Daily"));
            ev_task_tapToAdd.setText("");
            task_adapter.notifyItemInserted(0);
        }
    }
}