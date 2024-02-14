package akw.android.thetodo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import akw.android.thetodo.AppObjects.Tasks;
import akw.android.thetodo.DataBase.TheViewModel;

public class ReminderWorker extends Worker{

    TheViewModel viewModel;


    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        viewModel= new TheViewModel((Application) getApplicationContext());
        new NotificationHelper(getApplicationContext())
                .makeChannel1Notification(getInputData().getString(Keys.INTENT_TITLE),getInputData().getString(Keys.INTENT_DESC));

        Log.i("ReminderWorker.doWork",getInputData().getInt(Keys.INTENT_TID,0)+"_"+getInputData().getString(Keys.INTENT_TITLE)+"_"+getInputData().getString(Keys.INTENT_DESC));
        Tasks task= viewModel.getTask(getInputData().getInt(Keys.INTENT_TID,0));

        Log.i("ReminderWorker.doWork",task.getT_id()+"_"+task.getType());
        setNextAlert(task);

        return Result.success();
    }

    public void setNextAlert(Tasks task) {
        Calendar date = task.getNextDate();

        switch (task.getType()) {
            case Keys.REPEAT_TYPE_MONTHLY:
                date= getDate(date,Calendar.MONTH);
                break;
            case Keys.REPEAT_TYPE_DAILY:
                date.add(Calendar.DATE, 1);
                break;
            case Keys.REPEAT_TYPE_YEARLY:
                date= getDate(date,Calendar.YEAR);
                break;
            case Keys.REPEAT_TYPE_WEEKLY:
                date.add(Calendar.DATE, 7);
        }

        task.setNextDate(date);
        viewModel.update(task);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:SS, dd/MM/yy");
        String currentDate = sdf.format(date.getTime());
        Log.i("ReminderWorker.setNextAlert::::", currentDate);
        long initialDelay = date.getTime().getTime() -  Calendar.getInstance().getTimeInMillis();
        Log.i("worker.seNextAlert::::", date.getTime().getTime()+"_"+initialDelay+"_"+Calendar.getInstance().getTimeInMillis());
        OneTimeWorkRequest work = new
                OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setInputData(getInputData())
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork(task.getWorkerID(), ExistingWorkPolicy.KEEP,work);
        task.setWorkerID(work.getId().toString());

    }

    private Calendar getDate(Calendar date, int type) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:SS, dd/MM/yy");
        String currentDate = sdf.format(cal.getTime());
        Log.i("ReminderWorker.getDate::::", currentDate);
        Calendar next;

        for(int i =1; i<100;i++){
            next= date;
            next.add(type,i);
            if(next.getTimeInMillis()>cal.getTimeInMillis()){
                return next;
            }
        }
        return null;

    }

}
