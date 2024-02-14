package akw.android.thetodo.DataBase.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import akw.android.thetodo.AppObjects.Tasks;

@Dao
public interface TasksDao {

    @Insert
    Long insert(Tasks task);

    @Delete
    void delete(Tasks task);

    @Update
    void update(Tasks task);

    @Query("SELECT * FROM tasks_table WHERE t_id= :t_id LIMIT 1")
    Tasks getTask(int t_id);

    @Query("SELECT * FROM tasks_table WHERE show is 1 ORDER BY completed ASC")
    LiveData<List<Tasks>> getAllTasks();

    @Query("SELECT * FROM tasks_table WHERE nextDate is not null ORDER BY completed ASC")
    LiveData<List<Tasks>> getFutureTasks();

    @Query("SELECT * FROM tasks_table WHERE show is 1 ORDER BY completed ASC, createdOn DESC")
    LiveData<List<Tasks>> getAllTasksDesc();


}
