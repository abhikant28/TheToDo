package com.example.thetodo.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.thetodo.AppObjects.Groups;

@Dao
public interface GroupsDao {

    @Insert
    void insert(Groups group);

    @Update
    void update(Groups group);

    @Delete
    void delete(Groups group);

    @Query("DELETE FROM notes_table")
    void deleteAll();
}
