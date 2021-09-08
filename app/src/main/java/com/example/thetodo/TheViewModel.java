package com.example.thetodo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.thetodo.AppObjects.Groups;
import com.example.thetodo.AppObjects.Notes;

import java.util.ArrayList;

public class TheViewModel extends AndroidViewModel {

    private TheRepository repository;
    private LiveData<ArrayList<Notes>> allNotes;
    private LiveData<ArrayList<Groups>> allGroups;


    public TheViewModel(@NonNull Application application) {
        super(application);
    }
}
