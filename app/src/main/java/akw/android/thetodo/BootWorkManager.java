package akw.android.thetodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import akw.android.thetodo.AppObjects.Tasks;
import akw.android.thetodo.DataBase.TheViewModel;

public class BootWorkManager extends Worker {
    public BootWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
//
//        TheViewModel viewModel = new TheViewModel();
//        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            allTasks = viewModel.getAllTasks(false).getValue();
//            for (Tasks i : allTasks) {
//                if (i.getNextDate() != null) {
//                    Intent in = new Intent(null, AlertReceiver.class);
//                    intent.putExtra("title", "Reminder :");
//                    intent.putExtra("body", i.getTitle());
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(cxt, i.getT_id(), in, PendingIntent.FLAG_IMMUTABLE);
//                    AlarmManager alarmManager = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, i.getNextDate().getTimeInMillis(), pendingIntent);
//                }
//            }
//        }
    return null;
    }

}
