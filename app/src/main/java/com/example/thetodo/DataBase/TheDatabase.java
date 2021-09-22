package com.example.thetodo.DataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.AppObjects.Tasks;
import com.example.thetodo.Daos.DataConverter;
import com.example.thetodo.Daos.GroupsDao;
import com.example.thetodo.Daos.NotesDao;
import com.example.thetodo.Daos.TasksDao;

@Database(entities = {Notes.class, Groups.class, Tasks.class}, version = 1)
public abstract class TheDatabase extends RoomDatabase {

    private static TheDatabase instance;

    public abstract NotesDao notesDao();
    public abstract GroupsDao groupsDao();
    public abstract TasksDao tasksDao();

    public static synchronized  TheDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),TheDatabase.class
                    ,"AppDatabase").fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private  static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private NotesDao notesDao;
        private GroupsDao groupsDao;
        private TasksDao taskDao;

        private PopulateDbAsyncTask(TheDatabase db){
            notesDao=db.notesDao();
            groupsDao=db.groupsDao();
            taskDao= db.tasksDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            groupsDao.insert(new Groups("This is a Group"));


            taskDao.insert(new Tasks("1st Task", false, "Daily"));
            taskDao.insert(new Tasks("2nd Task", false, "Weekly"));
            taskDao.insert(new Tasks("3rd Task", true, "Daily"));
            taskDao.insert(new Tasks("4th Task", true, "Annual"));
//            taskDao.insert(new Tasks("5th Task", false, "Annual"));
//            taskDao.insert(new Tasks("6th Task", true, "Annual"));
//            taskDao.insert(new Tasks("7th Task", false, "Annual"));
//            taskDao.insert(new Tasks("8th Task", false, "Annual"));

            notesDao.insert(new Notes("Note 1", "DescriptionABC",0));
            notesDao.insert(new Notes("Note 2", "DescriptionDBC",0));
            notesDao.insert(new Notes("Note 3", "DescriptionEBC",0));
            notesDao.insert(new Notes("Note 4", "DescriptionFBC",0));
            notesDao.insert(new Notes("Note 5", "DescriptionGBC",0));
            return null;
        }
    }
}
