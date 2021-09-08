package com.example.thetodo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.Daos.GroupsDao;
import com.example.thetodo.Daos.NotesDao;

@Database(entities = {Notes.class, Groups.class}, version = 1)
public abstract class TheDatabase extends RoomDatabase {

    private static TheDatabase instance;

    public abstract NotesDao notesDao();
    public abstract GroupsDao groupsDao();

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

        private PopulateDbAsyncTask(TheDatabase db){
            notesDao=db.notesDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            notesDao.insert(new Notes("Note 1", "DescriptionABC",null));
            notesDao.insert(new Notes("Note 2", "DescriptionDBC",null));
            notesDao.insert(new Notes("Note 3", "DescriptionEBC",null));
            notesDao.insert(new Notes("Note 4", "DescriptionFBC",null));
            notesDao.insert(new Notes("Note 5", "DescriptionGBC",null));
            return null;
        }
    }
}
