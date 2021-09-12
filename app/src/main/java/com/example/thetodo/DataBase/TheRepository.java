package com.example.thetodo.DataBase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.AppObjects.Tasks;
import com.example.thetodo.Daos.GroupsDao;
import com.example.thetodo.Daos.NotesDao;
import com.example.thetodo.Daos.TasksDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TheRepository {

    private NotesDao notesDao;
    private GroupsDao groupsDao;
    private TasksDao tasksDao;

    private LiveData<List<Notes>> allNotes;
    private LiveData<List<Groups>> allGroups;
    private LiveData<List<Tasks>> allTasks;

    public TheRepository(Application application){
        TheDatabase database = TheDatabase.getInstance(application);
        notesDao= database.notesDao();
        groupsDao= database.groupsDao();
        tasksDao=database.tasksDao();
        allNotes= notesDao.getAllNotes();
        allGroups=groupsDao.getAllGroups();
        allTasks=tasksDao.getAllTasks();
    }

    public void insertTask(Tasks task){
        new InsertTasksAsyncTask(tasksDao).execute(task);
    }
    public void updateTask(Tasks task){
        new UpdateTasksAsyncTask(tasksDao).execute(task);
    }
    public void deleteTask(Tasks task){
        new DeleteTasksAsyncTask(tasksDao).execute(task);
    }
    public LiveData<List<Tasks>> getAllTasks(){
        return allTasks;
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
    public Notes getNote(Integer n_id){
        try {
            return new GetNoteAsyncTask(notesDao).execute(n_id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Notes("Wrong Result", "Go back and select the Note again", null);
    }
    public LiveData<List<Notes>> getAllNotes(){
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
    public LiveData<List<Groups>> getAllGroups(){
        return allGroups;
    }


    private static class InsertTasksAsyncTask extends AsyncTask<Tasks,Void,Void>{
        private TasksDao tasksDao;

        public InsertTasksAsyncTask(TasksDao tasksDao) {
            this.tasksDao = tasksDao;
        }

        @Override
        protected Void doInBackground(Tasks... task) {
            tasksDao.insert(task[0]);
            return null;
        }
    }
    private static class UpdateTasksAsyncTask extends AsyncTask<Tasks,Void,Void>{
        private TasksDao tasksDao;

        public UpdateTasksAsyncTask(TasksDao tasksDao) {
            this.tasksDao = tasksDao;
        }

        @Override
        protected Void doInBackground(Tasks... task) {
            tasksDao.update(task[0]);
            return null;
        }
    }
    private static class DeleteTasksAsyncTask extends AsyncTask<Tasks,Void,Void>{
        private TasksDao tasksDao;

        public DeleteTasksAsyncTask(TasksDao tasksDao) {
            this.tasksDao = tasksDao;
        }

        @Override
        protected Void doInBackground(Tasks... task) {
            tasksDao.delete(task[0]);
            return null;
        }
    }


    private static class GetNoteAsyncTask extends AsyncTask<Integer,Void,Notes>{
        private NotesDao notesDao;

        public GetNoteAsyncTask(NotesDao notesDao) {
            this.notesDao = notesDao;
        }

        @Override
        protected Notes doInBackground(Integer... n_id) {
            Notes notes= notesDao.getNote(n_id[0]);
            return notes;
        }

        @Override
        protected void onPostExecute(Notes notes) {
            super.onPostExecute(notes);
        }
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