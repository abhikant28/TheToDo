package akw.android.thetodo.AppObjects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Calendar;

import akw.android.thetodo.DataBase.Daos.DataConverter;

@Entity(tableName = "tasks_table")
@TypeConverters(DataConverter.class)
public class Tasks {

    @PrimaryKey(autoGenerate = true)
    private int t_id;
    private String title;
    private boolean completed;
    private String type,createdOn, workerID;
    private Calendar nextDate;
    private boolean show = true;

    public Tasks(String title, boolean completed, String type) {
        this.title = title;
        this.completed = completed;
        this.type = type;
        this.createdOn= String.valueOf(System.currentTimeMillis());
    }

    public Calendar getNextDate() {
        return nextDate;
    }

    public void setNextDate(Calendar nextDate) {
        this.nextDate = nextDate;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getWorkerID() {
        return workerID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }
}
