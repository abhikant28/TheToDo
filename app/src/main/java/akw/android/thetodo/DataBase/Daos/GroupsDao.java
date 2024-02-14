package akw.android.thetodo.DataBase.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import akw.android.thetodo.AppObjects.Groups;

@Dao
public interface GroupsDao {

    @Insert
    void insert(Groups group);

    @Update
    void update(Groups group);

    @Delete
    void delete(Groups group);

    @Query("SELECT * FROM notesGroups_table")
    LiveData<List<Groups>> getAllGroups();

}
