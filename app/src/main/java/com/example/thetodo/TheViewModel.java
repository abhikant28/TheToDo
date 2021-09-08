package com.example.thetodo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Notes;

import java.util.ArrayList;
import java.util.List;

public class TheViewModel extends AndroidViewModel {

    private TheRepository repository;
    private LiveData<List<Notes>> allNotes;
    private LiveData<List<Groups>> allGroups;


    public TheViewModel(@NonNull Application application) {
        super(application);
        repository= new TheRepository(application);
        allGroups=repository.getAllGroups();
        allNotes=repository.getAllNotes();
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
