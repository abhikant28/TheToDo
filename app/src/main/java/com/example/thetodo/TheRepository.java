package com.example.thetodo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.Daos.GroupsDao;
import com.example.thetodo.Daos.NotesDao;

import java.util.ArrayList;

public class TheRepository {
    private NotesDao notesDao;
    private GroupsDao groupsDao;

    private LiveData<ArrayList<Notes>> allNotes;
    private LiveData<ArrayList<Groups>> allGroups;

    public TheRepository(Application application){
        TheDatabase database = TheDatabase.getInstance(application);
        notesDao= database.notesDao();
        groupsDao= database.groupsDao();
        allNotes= notesDao.getAllNotes();
        allGroups=groupsDao.getAllGroups();
    }

    public void insertNote(Notes note){
        new InsertNotesAsyncTask(notesDao).execute(note);
    }

    public void updateNote(Notes note){
        new UpdateNotesAsyncTask(notesDao).execute(note);
    }

    public void deleteNote(Notes note){
        new DeleteNotesAsyncTask(notesDao).execute(note);
    }

    public LiveData<ArrayList<Notes>> getAllNotes(){
        return allNotes;
    }

    public void insertGroup(Groups group){
        new InsertGroupsAsyncTask(groupsDao).execute(group);
    }
    public void updateGroup(Groups group){
        new UpdateGroupsAsyncTask(groupsDao).execute(group);
    }
    public void deleteGroup(Groups group){
        new DeleteGroupsAsyncTask(groupsDao).execute(group);
    }

    public LiveData<ArrayList<Groups>> getAllGroups(){
        return allGroups;
    }
    private static class InsertNotesAsyncTask extends AsyncTask<Notes,Void,Void>{
        private NotesDao notesDao;

        public InsertNotesAsyncTask(NotesDao notesDao) {
            this.notesDao = notesDao;
        }

        @Override
        protected Void doInBackground(Notes... notes) {
            notesDao.insert(notes[0]);
            return null;
        }
    }
    private static class UpdateNotesAsyncTask extends AsyncTask<Notes,Void,Void>{
        private NotesDao notesDao;

        public UpdateNotesAsyncTask(NotesDao notesDao) {
            this.notesDao = notesDao;
        }

        @Override
        protected Void doInBackground(Notes... notes) {
            notesDao.update(notes[0]);
            return null;
        }
    }
    private static class DeleteNotesAsyncTask extends AsyncTask<Notes,Void,Void>{
        private NotesDao notesDao;

        public DeleteNotesAsyncTask(NotesDao notesDao) {
            this.notesDao = notesDao;
        }

        @Override
        protected Void doInBackground(Notes... notes) {
            notesDao.delete(notes[0]);
            return null;
        }
    }

    private static class InsertGroupsAsyncTask extends AsyncTask<Groups,Void,Void>{
        private GroupsDao groupsDao;

        public InsertGroupsAsyncTask(GroupsDao groupsDao) {
            this.groupsDao = groupsDao;
        }

        @Override
        protected Void doInBackground(Groups... group) {
            groupsDao.insert(group[0]);
            return null;
        }
    }
    private static class UpdateGroupsAsyncTask extends AsyncTask<Groups,Void,Void>{
        private GroupsDao groupsDao;

        public UpdateGroupsAsyncTask(GroupsDao groupsDao) {
            this.groupsDao = groupsDao;
        }

        @Override
        protected Void doInBackground(Groups... group) {
            groupsDao.update(group[0]);
            return null;
        }
    }
    private static class DeleteGroupsAsyncTask extends AsyncTask<Groups,Void,Void>{
        private GroupsDao groupsDao;

        public DeleteGroupsAsyncTask(GroupsDao groupsDao) {
            this.groupsDao = groupsDao;
        }

        @Override
        protected Void doInBackground(Groups... group) {
            groupsDao.delete(group[0]);
            return null;
        }
    }
}
