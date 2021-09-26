package com.example.thetodo.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.thetodo.AppObjects.Notes;
import com.example.thetodo.AppObjects.Tasks;

import java.util.List;

@Dao
public interface TasksDao {

    @Insert
    void insert(Tasks task);

    @Delete
    void delete(Tasks task);

    @Update
    void update(Tasks task);

    @Query("SELECT * FROM tasks_table WHERE t_id= :t_id LIMIT 1")
    Tasks getTask(int t_id);

    @Query("SELECT * FROM tasks_table WHERE show is 1 ORDER BY completed ASC")
    LiveData<List<Tasks>> getAllTasks();

}
