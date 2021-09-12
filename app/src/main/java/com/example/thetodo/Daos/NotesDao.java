package com.example.thetodo.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.thetodo.AppObjects.Notes;

import java.util.List;

@Dao
public interface NotesDao {
    @Insert
    void insert(Notes note);

    @Update
    void update(Notes note);

    @Delete
    void delete(Notes note);

    @Query("SELECT * FROM notes_table WHERE g_id= :g_id")
    List<Notes> getGroupNotes(int g_id);

    @Query("SELECT * FROM notes_table WHERE n_id= :n_id LIMIT 1")
    Notes getNote(int n_id);

    @Query("SELECT * FROM notes_table WHERE show is 1 ORDER BY date")
    LiveData<List<Notes>> getAllNotes();
}
