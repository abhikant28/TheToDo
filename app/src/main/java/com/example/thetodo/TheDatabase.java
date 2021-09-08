package com.example.thetodo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
                    .build();
        }
        return instance;
    }
}
