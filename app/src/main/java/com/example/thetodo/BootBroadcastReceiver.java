package com.example.thetodo;

import static com.example.thetodo.MainActivity.viewModel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.thetodo.AppObjects.Tasks;

import java.util.ArrayList;
import java.util.List;

public class BootBroadcastReceiver extends BroadcastReceiver {
    Context cxt;
    List<Tasks> allTasks= new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            allTasks=viewModel.getAllTasks().getValue();
            for(Tasks i :allTasks){
                if(i.getDate()!=null){
                    Intent in = new Intent(null, AlertReceiver.class);
                    intent.putExtra("title", "Reminder :");
                    intent.putExtra("body", i.getTitle());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(cxt, i.getT_id(), in, 0);
                    AlarmManager alarmManager = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, i.getDate().getTimeInMillis(), pendingIntent);
                }
            }
        }
    }
}
