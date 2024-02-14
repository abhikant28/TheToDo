package akw.android.thetodo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import akw.android.thetodo.AppObjects.Tasks;

public class BootBroadcastReceiver extends BroadcastReceiver {
    Context cxt;
    List<Tasks> allTasks = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
//        TheViewModel viewModel= new TheViewModel((Application) context.getApplicationContext());
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
    }
}
