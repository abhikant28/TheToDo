package com.example.thetodo;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.example.thetodo.DataBase.TheViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static TheViewModel viewModel;

    private RecyclerView task_RecyclerView;
    public final Task_RecyclerView_Adapter task_adapter= new Task_RecyclerView_Adapter();
    private RecyclerView notes_group_RecyclerView;
    private RecyclerView notes_all_RecyclerView;
    public final Notes_RecyclerView_Adapter allNotes_adapter = new Notes_RecyclerView_Adapter();
    public final Notes_Groups_RecyclerView_Adapter notes_groups_adapter = new Notes_Groups_RecyclerView_Adapter();


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

        viewModel= ViewModelProviders.of(this).get(TheViewModel.class);
        viewModel.getAllNotes().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                    allNotes_adapter.setNotes(notes);
            }
        });
        viewModel.getAllTasks().observe(this, new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                task_adapter.setTasks(tasks);
            }
        });
        viewModel.getAllGroups().observe(this, new Observer<List<Groups>>() {
            @Override
            public void onChanged(List<Groups> groups) {
                notes_groups_adapter.setGroups(groups);
            }
        });

        task_RecyclerView=findViewById(R.id.Main_Super_Task_RecyclerView);
        notes_group_RecyclerView=findViewById(R.id.Main_Notes_Super_RecyclerView);
        notes_all_RecyclerView=findViewById(R.id.Main_Super_RecyclerView_AllNotes_List);

        ev_task_tapToAdd=findViewById(R.id.Main_Task_TapToAdd);
        ib_add_task=findViewById(R.id.Main_Task_ImageButton_AddButton);
        tv_task_remind=findViewById(R.id.Main_Task_TextView_Remind);
        tv_task_repeat=findViewById(R.id.Main_Task_TextView_Repeat);
        tv_notes_newGroup=findViewById(R.id.Main_Notes_TextView_NewNoteGroup);
        tv_notes_newNote=findViewById(R.id.Main_Notes_TextView_NewNote);

        setTaskAdapter();
        setAllNotesAdapter();
       setNotesGroupsAdapter();

        ev_task_tapToAdd.addTextChangedListener(checkText);
        tv_notes_newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EditNote.class));
            }
        });
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        notes_group_RecyclerView.setLayoutManager(layoutManager);
        notes_group_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        notes_group_RecyclerView.setAdapter(notes_groups_adapter);
        notes_groups_adapter.setOnItemClickListener(new Notes_Groups_RecyclerView_Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Groups group) {
                Intent intent= new Intent(MainActivity.this,EditNote.class);
                intent.putExtra("isNew", true);
                intent.putExtra("g_id", group.getG_id());
                startActivity(intent);
            }
        });
    }

    private void setTaskAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        task_RecyclerView.setLayoutManager(layoutManager);
        task_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        task_RecyclerView.setAdapter(task_adapter);
    }

    private void setAllNotesAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        notes_all_RecyclerView.setLayoutManager(layoutManager);
        notes_all_RecyclerView.setHasFixedSize(true);
        notes_all_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        notes_all_RecyclerView.setAdapter(allNotes_adapter);
        allNotes_adapter.setOnItemClickListener(new Notes_RecyclerView_Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Notes note) {
                Intent intent = new Intent(MainActivity.this,EditNote.class);
                intent.putExtra("isNew",false );
                intent.putExtra("note_id",note.getN_id());
                startActivity(intent);
            }
        });
    }

//    private void demoTaskData() {
//        myTasks.add(new Tasks("Daily Task 1",false,"Weekly"));
//        myTasks.add(new Tasks("Daily Task 2",false,"Weekly"));
//        myTasks.add(new Tasks("Daily Task 3",false,"Weekly"));
//        myTasks.add(new Tasks("Daily Task 4",true,"Weekly"));
//        myTasks.add(new Tasks("Daily Task 5",false,"Weekly"));
//    }

    public void addTask(View view) {
        if(!ev_task_tapToAdd.getText().toString().isEmpty()){
            viewModel.insert( new Tasks(ev_task_tapToAdd.getText().toString(), false, "Once"));
            ev_task_tapToAdd.setText("");
            task_adapter.notifyDataSetChanged();
        }
    }

}