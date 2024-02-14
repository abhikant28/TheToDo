package akw.android.thetodo.DataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import akw.android.thetodo.AppObjects.Groups;
import akw.android.thetodo.AppObjects.Notes;
import akw.android.thetodo.AppObjects.Tasks;
import akw.android.thetodo.DataBase.Daos.GroupsDao;
import akw.android.thetodo.DataBase.Daos.NotesDao;
import akw.android.thetodo.DataBase.Daos.TasksDao;

@Database(entities = {Notes.class, Groups.class, Tasks.class}, version = 1)
public abstract class TheDatabase extends RoomDatabase {

    private static TheDatabase instance;
    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    public static synchronized TheDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TheDatabase.class
                            , "AppDatabase").fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    public abstract NotesDao notesDao();

    public abstract GroupsDao groupsDao();

    public abstract TasksDao tasksDao();

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NotesDao notesDao;
        private GroupsDao groupsDao;
        private TasksDao taskDao;

        private PopulateDbAsyncTask(TheDatabase db) {
            notesDao = db.notesDao();
            groupsDao = db.groupsDao();
            taskDao = db.tasksDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            groupsDao.insert(new Groups("Notes Group"));


            taskDao.insert(new Tasks("Your to do list", false, "Daily"));
            taskDao.insert(new Tasks("You can set reminders", false, "Weekly"));
            taskDao.insert(new Tasks("Swipe to delete tasks once completed", true, "Annual"));

            notesDao.insert(new Notes("Note Title", "Description", 0));

            return null;
        }
    }

}
