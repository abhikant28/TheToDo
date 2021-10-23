package com.example.thetodo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.AppObjects.Tasks;
import com.example.thetodo.DataBase.TheViewModel;
import com.example.thetodo.Prefereances.Preferences_Main;
import com.example.thetodo.dialogBoxes.NewGroup_Dialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static TheViewModel viewModel;
    public static final String SHARED_PREFS_NAME = "NotesPreferences";
    public static Tasks theTask = new Tasks("", false, "");
    private Calendar calen;
    private boolean setReminder = false;
    public static String REPEAT_TYPE_ONCE = "Once";
    public static String REPEAT_TYPE_DAILY = "Daily";
    public static String REPEAT_TYPE_WEEKLY = "Weekly";
    public static String REPEAT_TYPE_MONTHLY = "Monthly";
    public static String REPEAT_TYPE_YEARLY = "Yearly";
    public static RelativeLayout MAIN_ACTIVITY_LAYOUT = null;

    private RecyclerView task_RecyclerView;
    public final Task_RecyclerView_Adapter task_adapter = new Task_RecyclerView_Adapter();
    private ExpandableListView notes_group_View;
    private RecyclerView notes_all_RecyclerView;
    public final Notes_RecyclerView_Adapter allNotes_adapter = new Notes_RecyclerView_Adapter();
    public final Notes_Groups_ExpandableList_Adapter notes_groups_adapter = new Notes_Groups_ExpandableList_Adapter(this);


    private Toolbar toolbar;
    private EditText ev_task_tapToAdd;
    private TextView tv_task_remind;
    private TextView tv_task_repeat;
    private TextView tv_notes_newNote;
    private TextView tv_notes_newGroup;
    private Button b_add_task;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        notes_groups_adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.MainMenu_Settings) {
            startActivity(new Intent(this, Preferences_Main.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this);

        AdView adView = findViewById(R.id.AdView);
         AdRequest adRequest = new AdRequest.Builder().build();

         adView.loadAd(adRequest);

        toolbar = findViewById(R.id.MainActivity_Toolbar);
        Log.i("DATE::::::::", String.valueOf(java.util.Calendar.getInstance().getTime()));
        String date = String.valueOf(Calendar.getInstance().getTime());
        toolbar.setTitle(date.substring(4, 10) + ", " + date.substring(0, 4));
        setSupportActionBar(toolbar);

        MAIN_ACTIVITY_LAYOUT = findViewById(R.id.MAIN_RELATIVELAYOUT);
        task_RecyclerView = findViewById(R.id.Main_Super_Task_RecyclerView);
//        notes_group_View = findViewById(R.id.Main_Notes_Super_GroupedNotes_RecyclerView);
        notes_all_RecyclerView = findViewById(R.id.Main_Super_RecyclerView_AllNotes_List);
        ev_task_tapToAdd = findViewById(R.id.Main_Task_TapToAdd);
        b_add_task = findViewById(R.id.Main_Task_ImageButton_AddButton);
//        tv_task_remind = findViewById(R.id.Main_Task_TextView_Remind);
//        tv_task_repeat = findViewById(R.id.Main_Task_TextView_Repeat);
//        tv_notes_newGroup = findViewById(R.id.Main_Notes_TextView_NewNoteGroup);
        tv_notes_newNote = findViewById(R.id.Main_Notes_TextView_NewNote);
        MAIN_ACTIVITY_LAYOUT.setBackgroundColor(Color.parseColor("#EFD75C"));

        getPreferences();

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
//        viewModel.getAllGroups().observe(this, new Observer<List<Groups>>() {
//            @Override
//            public void onChanged(List<Groups> groups) {
//                notes_groups_adapter.setList(groups);
//            }
//        });

        setTaskAdapter();
        setAllNotesAdapter();
//        setNotesGroupsAdapter();

//        tv_task_remind.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!ev_task_tapToAdd.getText().toString().trim().isEmpty()) {
//                    calen = setTaskTimeDate();
//                }
//            }
//        });

        ev_task_tapToAdd.addTextChangedListener(checkText);

        tv_notes_newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditNote.class));
            }
        });
//        tv_notes_newGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openNewGroupDialog();
//            }
//        });
//        tv_task_repeat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!ev_task_tapToAdd.getText().toString().trim().isEmpty()){
//                    repeatPopup();
//                }
//            }
//        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Tasks task = task_adapter.getTask(viewHolder.getAdapterPosition());
                if (task.isCompleted()) {
                    if (task.getDate() == null || task.getType().equals(REPEAT_TYPE_ONCE)) {
                        viewModel.delete(task);
                    } else {
                        task.setShow(false);
                        viewModel.update(task);
                    }
                    Toast.makeText(MainActivity.this, "Task removed", Toast.LENGTH_SHORT).show();
                } else {
                    task_adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Complete Task to remove", Toast.LENGTH_SHORT).show();
                    task_adapter.notifyDataSetChanged();
                }
            }
        }).attachToRecyclerView(task_RecyclerView);


    }


    private void repeatPopup() {
        PopupMenu popupMenu = new PopupMenu(this, tv_task_repeat);
        popupMenu.inflate(R.menu.long_press_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Repeat_Option_once:
                        theTask.setType(REPEAT_TYPE_ONCE);
                        tv_task_repeat.setText(REPEAT_TYPE_ONCE);
                        return true;
                    case R.id.Repeat_Option_daily:
                        theTask.setType(REPEAT_TYPE_DAILY);
                        tv_task_repeat.setText(REPEAT_TYPE_DAILY);
                        return true;
                    case R.id.Repeat_Option_weekly:
                        theTask.setType(REPEAT_TYPE_WEEKLY);
                        tv_task_repeat.setText(REPEAT_TYPE_WEEKLY);
                        return true;
                    case R.id.Repeat_Option_monthly:
                        theTask.setType(REPEAT_TYPE_MONTHLY);
                        tv_task_repeat.setText(REPEAT_TYPE_MONTHLY);
                        return true;
                    case R.id.Repeat_Option_yearly:
                        theTask.setType(REPEAT_TYPE_YEARLY);
                        tv_task_repeat.setText(REPEAT_TYPE_YEARLY);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
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
        setReminder = true;
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
            if (charSequence.toString().trim().length() > 0) {
                theTask.setTitle(charSequence.toString());
                b_add_task.setEnabled(true);
//                tv_task_remind.setClickable(true);
//                tv_task_repeat.setClickable(true);
            } else {
                theTask = new Tasks("", false, "");
                b_add_task.setEnabled(false);
//                tv_task_remind.setClickable(false);
//                tv_task_repeat.setClickable(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private void setNotesGroupsAdapter() {
        notes_group_View.setAdapter(notes_groups_adapter);
    }

    private void setTaskAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        task_RecyclerView.setLayoutManager(layoutManager);
        task_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        task_RecyclerView.setAdapter(task_adapter);
        task_RecyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
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
                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });
    }

    public void addTask(View view) {
        if (!ev_task_tapToAdd.getText().toString().trim().isEmpty()) {

            if (setReminder) {
                Intent intent = new Intent(this, AlertReceiver.class);
                intent.putExtra("title", "Reminder : ");
                intent.putExtra("body", theTask.getTitle());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, theTask.getT_id(), intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (calen.before(Calendar.getInstance())) {
                    calen.add(Calendar.DATE, 1);
                }
                theTask.setDate(calen);
                viewModel.insert(theTask);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calen.getTimeInMillis(), pendingIntent);
                setReminder = false;
            } else {
                viewModel.insert(theTask);
            }
            theTask = new Tasks("", false, "");
            ev_task_tapToAdd.setText("");
//            tv_task_remind.setText("Remind");
//            tv_task_repeat.setText("Repeat");
        } else {
            Toast.makeText(this, "Type task to add", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAlarm(int t_id) {
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, t_id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public void goToSetting(MenuItem item) {
        startActivity(new Intent(this, Preferences_Main.class));
    }

    public void getPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean("IS_BG_PREF_SET", false)) {
            String colorHex = sharedPreferences.getString("BG_COLOR", "FFDAD5");
            MAIN_ACTIVITY_LAYOUT.setBackgroundColor(Color.parseColor("#" + colorHex));
        }
    }
}