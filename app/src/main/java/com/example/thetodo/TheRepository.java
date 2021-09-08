package com.example.thetodo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.Daos.GroupsDao;
import com.example.thetodo.Daos.NotesDao;

import java.util.ArrayList;

public class TheRepository {
    private NotesDao notesDao;
    private GroupsDao groupsDao;

    private LiveData<ArrayList<Notes>> allNotes;

    public TheRepository(Application application){
        TheDatabase database = TheDatabase.getInstance(application);
        notesDao= database.notesDao();
        groupsDao= database.groupsDao();
        allNotes= notesDao.getAllNotes();
    }

    public void insert(Notes note){

    }

    public void update(Notes note){

    }

    public void delete(Notes note){

    }

    public LiveData<ArrayList<Notes>> getAllNotes(){
        return allNotes;
    }

    private static class  InsertNotesAsyncTask extends AsyncTask<Notes,Void,Void>{
        private NotesDao notesDao;

        @Override
        protected Void doInBackground(Notes... notes) {
            notesDao.insert(notes[0]);
            return null;
        }
    }
}
