package akw.android.thetodo.DataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import akw.android.thetodo.AppObjects.Groups;
import akw.android.thetodo.AppObjects.Notes;
import akw.android.thetodo.AppObjects.Tasks;

public class TheViewModel extends AndroidViewModel {

    private final TheRepository repository;
    private final LiveData<List<Notes>> allNotes;
    private final LiveData<List<Groups>> allGroups;


    public TheViewModel(@NonNull Application application) {
        super(application);
        repository = new TheRepository(application);
        allGroups = repository.getAllGroups();
        allNotes = repository.getAllNotes();
    }

    public Long insert(Tasks task) {
        return repository.insertTask(task);
    }

    public void update(Tasks task) {
        repository.updateTask(task);
    }

    public void delete(Tasks task) {
        repository.deleteTask(task);
    }

    public Tasks getTask(Integer t_id) {
        return repository.getTask(t_id);
    }

    public LiveData<List<Tasks>> getFutureTasks() {
        return repository.getFutureTask();
    }

    public LiveData<List<Tasks>> getAllTasks(boolean desc) {
        return repository.getAllTasks(desc);
    }


    public void insert(Notes note) {
        repository.insertNote(note);
    }

    public void update(Notes note) {
        repository.updateNote(note);
    }

    public void delete(Notes note) {
        repository.deleteNote(note);
    }

    public Notes getNote(Integer n_id) {
        return repository.getNote(n_id);
    }

    public List<Notes> getNotesSearch(String query) {
        return repository.searchNotes(query);
    }

    public LiveData<List<Notes>> getAllNotes() {
        return allNotes;
    }

    public List<Notes> getGroupNotes(Integer g_id) {
        return repository.getGroupNotes(g_id);
    }


    public void insert(Groups group) {
        repository.insertGroup(group);
    }

    public void update(Groups group) {
        repository.updateGroup(group);
    }

    public void delete(Groups group) {
        repository.deleteGroup(group);
    }

    public LiveData<List<Groups>> getAllGroups() {
        return allGroups;
    }
}
