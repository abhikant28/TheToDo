package com.example.thetodo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.AppObjects.Tasks;
import com.example.thetodo.DataBase.TheViewModel;
import com.example.thetodo.dialogBoxes.NewGroup_Dialog;
import com.example.thetodo.dialogBoxes.TaskRepeat_Dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static TheViewModel viewModel;
    public static Tasks theTask = new Tasks("", false, "");
    private Calendar calen;
    private boolean setReminder=false;

    private RecyclerView task_RecyclerView;
    public final Task_RecyclerView_Adapter task_adapter = new Task_RecyclerView_Adapter();
    private RecyclerView notes_group_RecyclerView;
    private RecyclerView notes_all_RecyclerView;
    public final Notes_RecyclerView_Adapter allNotes_adapter = new Notes_RecyclerView_Adapter();
    public final Notes_Groups_RecyclerView_Adapter notes_groups_adapter = new Notes_Groups_RecyclerView_Adapter();


    private EditText ev_task_tapToAdd;
    private TextView tv_task_remind;
    private TextView tv_task_repeat;
    private TextView tv_notes_newNote;
    private TextView tv_notes_newGroup;
    private Button b_add_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(TheViewModel.class);
        viewModel.getAllNotes().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                allNotes_adapter.submitList(notes);
            }
        });
        viewModel.getAllTasks().observe(this, new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                task_adapter.submitList(tasks);
            }
        });
        viewModel.getAllGroups().observe(this, new Observer<List<Groups>>() {
            @Override
            public void onChanged(List<Groups> groups) {
                notes_groups_adapter.submitList(groups);
            }
        });

        task_RecyclerView = findViewById(R.id.Main_Super_Task_RecyclerView);
        notes_group_RecyclerView = findViewById(R.id.Main_Notes_Super_RecyclerView);
        notes_all_RecyclerView = findViewById(R.id.Main_Super_RecyclerView_AllNotes_List);
        ev_task_tapToAdd = findViewById(R.id.Main_Task_TapToAdd);
        b_add_task = findViewById(R.id.Main_Task_ImageButton_AddButton);
        tv_task_remind = findViewById(R.id.Main_Task_TextView_Remind);
        tv_task_repeat = findViewById(R.id.Main_Task_TextView_Repeat);
        tv_notes_newGroup = findViewById(R.id.Main_Notes_TextView_NewNoteGroup);
        tv_notes_newNote = findViewById(R.id.Main_Notes_TextView_NewNote);

        setTaskAdapter();
        setAllNotesAdapter();
        setNotesGroupsAdapter();

        tv_task_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ev_task_tapToAdd.getText().toString().isEmpty()) {
                    calen = setTaskTimeDate();
                }
            }
        });
        tv_task_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ev_task_tapToAdd.getText().toString().isEmpty()) {
                    openRepeatTaskDialog();
                }
            }
        });
        ev_task_tapToAdd.addTextChangedListener(checkText);
        tv_notes_newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditNote.class));
            }
        });
        tv_notes_newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewGroupDialog();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Tasks task = task_adapter.getTask(viewHolder.getAdapterPosition());
                if (task.isCompleted()) {
                    cancelAlarm(task.getT_id());
                    viewModel.delete(task);
                    Toast.makeText(MainActivity.this, "Task removed", Toast.LENGTH_SHORT).show();
                } else {
                    task_adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Complete Task to remove", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(task_RecyclerView);
    }

    private void openRepeatTaskDialog() {
        TaskRepeat_Dialog taskRepeat_dialog = new TaskRepeat_Dialog();
        taskRepeat_dialog.show(getSupportFragmentManager(), "Repeat");
    }

    private Calendar setTaskTimeDate() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
                        tv_task_remind.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(MainActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(MainActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        setReminder=true;
        return calendar;
    }

    private void openNewGroupDialog() {
        NewGroup_Dialog newGroup_dialog = new NewGroup_Dialog();
        newGroup_dialog.show(getSupportFragmentManager(), "New Group");
    }

    private TextWatcher checkText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (charSequence.length() > 0) {
                b_add_task.setClickable(true);
                tv_task_remind.setClickable(true);
                tv_task_repeat.setClickable(true);
                b_add_task.setBackgroundColor(Color.parseColor("#FF6DC530"));
            } else {
                theTask = null;
                b_add_task.setClickable(false);
                tv_task_remind.setClickable(false);
                tv_task_repeat.setClickable(false);
                b_add_task.setBackgroundColor(Color.parseColor("#817E7E"));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() > 0) {
                b_add_task.setClickable(true);
                tv_task_remind.setClickable(true);
                tv_task_repeat.setClickable(true);
                b_add_task.setBackgroundColor(Color.parseColor("#FF6DC530"));
            } else {
                theTask=new Tasks("", false, "Once");
                b_add_task.setClickable(false);
                tv_task_remind.setClickable(false);
                tv_task_repeat.setClickable(false);
                b_add_task.setBackgroundColor(Color.parseColor("#817E7E"));
            }
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
                Intent intent = new Intent(MainActivity.this, EditNote.class);
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
                Intent intent = new Intent(MainActivity.this, EditNote.class);
                intent.putExtra("isNew", false);
                intent.putExtra("note_id", note.getN_id());
                startActivity(intent);
            }
        });
    }

    public void addTask(View view) {
        if (!ev_task_tapToAdd.getText().toString().isEmpty()) {
            theTask.getType();
            viewModel.insert(theTask);

            if(setReminder){
                Intent intent = new Intent(this, AlertReceiver.class);
                intent.putExtra("title", "Reminder : ");
                intent.putExtra("body", theTask.getTitle());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, theTask.getT_id(), intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (calen.before(Calendar.getInstance())) {
                    calen.add(Calendar.DATE, 1);
                }
                theTask.setDate(calen);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calen.getTimeInMillis(), pendingIntent);
                setReminder=false;
            }
            theTask=new Tasks("", false, "Once");
            ev_task_tapToAdd.setText("");
            tv_task_remind.setText("Remind");
        }
    }

    public void cancelAlarm(int t_id) {
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, t_id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

    }
}