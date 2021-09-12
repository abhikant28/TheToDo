package com.example.thetodo.DataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.AppObjects.Tasks;

import java.util.List;

public class TheViewModel extends AndroidViewModel {

    private TheRepository repository;
    private LiveData<List<Tasks>> allTasks;
    private LiveData<List<Notes>> allNotes;
    private LiveData<List<Groups>> allGroups;


    public TheViewModel(@NonNull Application application) {
        super(application);
        repository= new TheRepository(application);
        allGroups=repository.getAllGroups();
        allTasks=repository.getAllTasks();
        allNotes=repository.getAllNotes();
    }

    public void insert(Tasks task){
        repository.insertTask(task);
    }
    public void update(Tasks task){
        repository.updateTask(task);
    }
    public void delete(Tasks task){
        repository.deleteTask(task);
    }
    public LiveData<List<Tasks>> getAllTasks(){
        return allTasks;
    }


    public void insert(Notes note){
        repository.insertNote(note);
    }
    public void update(Notes note){
        repository.updateNote(note);
    }
    public void delete(Notes note){
        repository.deleteNote(note);
    }
    public Notes getNote(Integer n_id){
        return repository.getNote(n_id);
    }
    public LiveData<List<Notes>> getAllNotes(){
        return allNotes;
    }


    public void insert(Groups group){
        repository.insertGroup(group);
    }
    public void update(Groups group){
        repository.updateGroup(group);
    }
    public void delete(Groups group){
        repository.deleteGroup(group);
    }
    public LiveData<List<Groups>> getAllGroups(){
    return allGroups;
    }}
