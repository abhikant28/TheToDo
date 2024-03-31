package akw.android.thetodo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import akw.android.thetodo.Adapter.Notes_RecyclerView_Adapter;
import akw.android.thetodo.Adapter.Task_RecyclerView_Adapter;
import akw.android.thetodo.AppObjects.Tasks;
import akw.android.thetodo.DataBase.TheViewModel;
import akw.android.thetodo.Preferences.Preferences_Main;

public class MainActivity extends AppCompatActivity {

    public static TheViewModel viewModel;
    private SharedPreferences sharedPreferences;
    public static Tasks theTask = new Tasks("", false, "");
    private boolean taskDesc;
    private Observer taskObserver;
    public static ConstraintLayout MAIN_ACTIVITY_LAYOUT = null;
    public Task_RecyclerView_Adapter task_adapter = new Task_RecyclerView_Adapter();
    public final Notes_RecyclerView_Adapter allNotes_adapter = new Notes_RecyclerView_Adapter();
    private Calendar calen;
    private boolean setReminder = false, taskToBottom = false;
    private RecyclerView task_RecyclerView, notes_all_RecyclerView;
    private Toolbar toolbar;
    private EditText ev_task_tapToAdd;
    private TextView tv_task_remind, tv_notes_newNote;
    private Button b_add_task;


    private TextWatcher checkText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (charSequence.toString().trim().length() > 0) {
                theTask.setTitle(charSequence.toString());
                b_add_task.setEnabled(true);
                tv_task_remind.setClickable(true);
            } else {
                theTask = new Tasks("", false, "");
                b_add_task.setEnabled(false);
                tv_task_remind.setClickable(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    private LiveData<List<Tasks>> getAllTasks;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        notes_groups_adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.MainMenu_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Type to search");
            searchView.setOnSearchClickListener(view -> {

                findViewById(R.id.Main_main_nestedScrollView).setVisibility(View.GONE);

                findViewById(R.id.MainActivity_search_Scrollview).setVisibility(View.VISIBLE);
            });

            Notes_RecyclerView_Adapter searchResultAdapter = new Notes_RecyclerView_Adapter();
            RecyclerView rv_searchResults = findViewById(R.id.MainActivity_search_recyclerView);


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newQuery) {
                    searchResultAdapter.submitList(null);
                    if (newQuery.trim().isEmpty()) {
                        findViewById(R.id.MainActivity_search_ImageView).setVisibility(View.VISIBLE);
                        findViewById(R.id.MainActivity_search_textView).setVisibility(View.VISIBLE);
                    } else {

                        findViewById(R.id.MainActivity_search_ImageView).setVisibility(View.GONE);
                        findViewById(R.id.MainActivity_search_textView).setVisibility(View.GONE);

                        searchResultAdapter.submitList(viewModel.getNotesSearch(newQuery));
                    }
                    return false;
                }
            });

            searchView.setOnCloseListener(() -> {
                findViewById(R.id.MainActivity_search_ImageView).setVisibility(View.VISIBLE);
                findViewById(R.id.MainActivity_search_textView).setVisibility(View.VISIBLE);
                findViewById(R.id.MainActivity_search_Scrollview).setVisibility(View.GONE);
                findViewById(R.id.Main_main_nestedScrollView).setVisibility(View.VISIBLE);

                return false;
            });
            searchView.setOrientation(SearchView.VERTICAL);

            setNotesAdapter(rv_searchResults, searchResultAdapter);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.MainMenu_Settings) {
            startActivity(new Intent(this, Preferences_Main.class));
        } else if (item.getItemId() == R.id.MainMenu_futureTasks) {
            startActivity(new Intent(this, FutureTasksActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        task_adapter = new Task_RecyclerView_Adapter();
        taskToBottom = getPreferences();
        setTaskAdapter(task_adapter);

        getAllTasks.removeObserver(taskObserver);

        taskDesc = sharedPreferences.getBoolean(Keys.SHAREDPREF_TASKS_ADD_NEW_BOTTOM, true);
        taskObserver = o -> {
            List<Tasks> tasks = (List<Tasks>) o;
            if (tasks.isEmpty()) {
                findViewById(R.id.Main_Super_Task_Dialog).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.Main_Super_Task_Dialog).setVisibility(View.GONE);
                task_adapter.submitList(tasks);
            }
        };
        getAllTasks=viewModel.getAllTasks(taskDesc);
        getAllTasks.observe(this, taskObserver);
        task_RecyclerView.swapAdapter(task_adapter,false);


        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adMob();
        initialize();
        //Log.i("DATE::::::::", String.valueOf(java.util.Calendar.getInstance().getTime()));

        setNotesAdapter(notes_all_RecyclerView, allNotes_adapter);
        connectToDB();


        tv_task_remind.setOnClickListener(view -> {
            if (!ev_task_tapToAdd.getText().toString().trim().isEmpty()) {
                calen = setTaskTimeDate();
            }
        });

        ev_task_tapToAdd.addTextChangedListener(checkText);

        tv_notes_newNote.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), EditNote.class)));


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Tasks task = task_adapter.getTask(viewHolder.getAdapterPosition());
                if (task.isCompleted()) {
                    if (task.getNextDate() == null || task.getType().equals(Keys.REPEAT_TYPE_ONCE)) {
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


    private void connectToDB() {
        viewModel = ViewModelProviders.of(this).get(TheViewModel.class);

        sharedPreferences = getSharedPreferences(Keys.SHARED_PREFS_NAME, MODE_PRIVATE);
        viewModel.getAllNotes().observe(this, allNotes_adapter::submitList);
        taskDesc = sharedPreferences.getBoolean(Keys.SHAREDPREF_TASKS_ADD_NEW_BOTTOM, true);
        taskObserver = o -> {
            List<Tasks> tasks = (List<Tasks>) o;
            if (tasks.isEmpty()) {
                findViewById(R.id.Main_Super_Task_Dialog).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.Main_Super_Task_Dialog).setVisibility(View.GONE);
                task_adapter.submitList(tasks);
            }
        };
        getAllTasks=viewModel.getAllTasks(taskDesc);
        getAllTasks.observe(this, taskObserver);

    }


    private void repeatPopup() {
        PopupMenu popupMenu = new PopupMenu(this, tv_task_remind);
        popupMenu.inflate(R.menu.long_press_menu);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.Repeat_Option_once:
                    theTask.setType(Keys.REPEAT_TYPE_ONCE);
                    tv_task_remind.setText(Keys.REPEAT_TYPE_ONCE);
                    return true;
                case R.id.Repeat_Option_daily:
                    theTask.setType(Keys.REPEAT_TYPE_DAILY);
                    tv_task_remind.setText(Keys.REPEAT_TYPE_DAILY);
                    return true;
                case R.id.Repeat_Option_weekly:
                    theTask.setType(Keys.REPEAT_TYPE_WEEKLY);
                    tv_task_remind.setText(Keys.REPEAT_TYPE_WEEKLY);
                    return true;
                case R.id.Repeat_Option_monthly:
                    theTask.setType(Keys.REPEAT_TYPE_MONTHLY);
                    tv_task_remind.setText(Keys.REPEAT_TYPE_MONTHLY);
                    return true;
                case R.id.Repeat_Option_yearly:
                    theTask.setType(Keys.REPEAT_TYPE_YEARLY);
                    tv_task_remind.setText(Keys.REPEAT_TYPE_YEARLY);
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }


    private Calendar setTaskTimeDate() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog.OnTimeSetListener timeSetListener = (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
                tv_task_remind.setText(simpleDateFormat.format(calendar.getTime()));
                repeatPopup();
            };

            new TimePickerDialog(MainActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        };

        new DatePickerDialog(MainActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        setReminder = true;
        return calendar;
    }


    private void setTaskAdapter(Task_RecyclerView_Adapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        task_RecyclerView.setLayoutManager(layoutManager);
        task_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        task_RecyclerView.setAdapter(this.task_adapter);
        task_RecyclerView.setOnLongClickListener(view -> false);
    }


    private void setNotesAdapter(RecyclerView notesRecyclerView, Notes_RecyclerView_Adapter allNotesAdapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        notesRecyclerView.setLayoutManager(layoutManager);
        notesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        notesRecyclerView.setAdapter(allNotesAdapter);
        allNotesAdapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(MainActivity.this, EditNote.class);
            intent.putExtra("isNew", false);
            intent.putExtra("note_id", note.getN_id());
            startActivity(intent);
            overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
        });
    }


    public void addTask(View view) {
        if (!ev_task_tapToAdd.getText().toString().trim().isEmpty()) {
            long t_id= viewModel.insert(theTask);

            if (setReminder) {
                String taskWorkerID=createReminder(calen.getTime().getTime(), Math.toIntExact(t_id), ev_task_tapToAdd.getText().toString().trim());
//                Intent intent = new Intent(this, AlertReceiver.class);
//                intent.putExtra("title", "Reminder : ");
//                intent.putExtra("body", theTask.getTitle());
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, theTask.getT_id(), intent, PendingIntent.FLAG_IMMUTABLE);
//                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                if (calen.before(Calendar.getInstance())) {
//                    calen.add(Calendar.DATE, 1);
//                }
                theTask= viewModel.getTask((int)t_id);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:SS, dd/MM/yy");
                String currentDate = sdf.format(calen.getTime());
                Log.i("MainActivity.addTask::::", currentDate);
                theTask.setNextDate(calen);
                theTask.setWorkerID(taskWorkerID);
                setReminder = false;
                viewModel.update(theTask);
            }
            theTask = new Tasks("", false, "");
            ev_task_tapToAdd.setText("");
            tv_task_remind.setText("Remind");
        } else {
            Toast.makeText(this, "Type task to add", Toast.LENGTH_SHORT).show();
        }
    }


    public String createReminder (Long time, int t_id, String title) {
        Long initialDelay = time - Calendar.getInstance().getTimeInMillis();
        Data data = new Data.Builder()
                .putInt(Keys.INTENT_TID, t_id)
                .putString(Keys.INTENT_DESC, title)
                .putString(Keys.INTENT_TITLE, "Reminder : ")
                .build();
        OneTimeWorkRequest work = new
                OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork(work.getStringId(), ExistingWorkPolicy.KEEP, work);
        return work.getStringId();
    }


    public boolean getPreferences() {
        if (sharedPreferences.getBoolean(Keys.SHAREDPREF_IS_BG_PREF_SET, false)) {

            if (sharedPreferences.getBoolean(Keys.SHAREDPREF_IS_BG_IMG, false)) {
                String imgUri = sharedPreferences.getString(Keys.SHAREDPREF_BG_IMG, "");

                try {
                    // Open an input stream from the Uri
                    InputStream inputStream = new FileInputStream(imgUri);

                    // Decode the InputStream into a Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

                    ImageView iv = findViewById(R.id.MAIN_IMG_BG);
                    iv.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(bitmapDrawable.getBitmap());
                    // Close the InputStream
                    inputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("FileNotFoundException", "File not found");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IOException", "Error reading image from URI");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ImageView iv = findViewById(R.id.MAIN_IMG_BG);
                iv.setVisibility(View.GONE);
                String colorHex = sharedPreferences.getString(Keys.SHAREDPREF_BG_COLOR, "FFDAD5");
                MAIN_ACTIVITY_LAYOUT.setBackgroundColor(Color.parseColor("#" + colorHex));

            }
        }
        return sharedPreferences.getBoolean(Keys.SHAREDPREF_TASKS_ADD_NEW_BOTTOM, false);
    }


    public void goToSetting(MenuItem item) {
        startActivity(new Intent(this, Preferences_Main.class));
    }

    private void initialize() {
        toolbar = findViewById(R.id.MainActivity_Toolbar);
        MAIN_ACTIVITY_LAYOUT = findViewById(R.id.MAIN_RELATIVELAYOUT);
        task_RecyclerView = findViewById(R.id.Main_Super_Task_RecyclerView);
        notes_all_RecyclerView = findViewById(R.id.Main_Super_RecyclerView_AllNotes_List);
        ev_task_tapToAdd = findViewById(R.id.Main_Task_TapToAdd);
        b_add_task = findViewById(R.id.Main_Task_ImageButton_AddButton);
        tv_task_remind = findViewById(R.id.Main_Task_TextView_Remind);
        tv_notes_newNote = findViewById(R.id.Main_Notes_TextView_NewNote);
        String date = String.valueOf(Calendar.getInstance().getTime());
        toolbar.setTitle(date.substring(4, 10) + ", " + date.substring(0, 4));
        setSupportActionBar(toolbar);
    }

    private void adMob() {
        MobileAds.initialize(this);

        AdView adView = findViewById(R.id.AdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("BA11D423CFBC4A36B03D49C0FC28141E"));
        adView.loadAd(adRequest);

    }

}