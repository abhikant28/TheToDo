package akw.android.thetodo;

import static akw.android.thetodo.MainActivity.viewModel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import akw.android.thetodo.AppObjects.Tasks;

public class AlertReceiver extends BroadcastReceiver {
    String title = "Text";
    String body = "Notification";
    Context cxt;
    int t_id;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper appNotificationHelp = new NotificationHelper(context);
        if (intent.getExtras() != null) {
            t_id = intent.getIntExtra("t_id", 0);
            title = intent.getStringExtra("title");
            body = intent.getStringExtra("body");
        }

//        NotificationCompat.Builder notificationBuilder = appNotificationHelp.getChannel1Notification(title, body);
//        appNotificationHelp.getManager().notify(1, notificationBuilder.build());

    }


    public void setNextAlert(int t_id) {
        Tasks task = viewModel.getTask(t_id);
        task.setShow(true);
        Calendar date = task.getNextDate();
        switch (task.getType()) {
            case "monthly":
                date.add(Calendar.MONTH, 1);
                break;
            case "daily":
                date.add(Calendar.DATE, 1);
                break;
            case "yearly":
                date.add(Calendar.YEAR, 1);
                break;
            case "weekly":
                date.add(Calendar.DATE, 7);
        }
        task.setNextDate(date);
        Intent intent = new Intent(cxt, AlertReceiver.class);
        intent.putExtra("title", "Reminder : ");
        intent.putExtra("body", task.getTitle());
        viewModel.update(task);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(cxt, task.getT_id(), intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) cxt.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);

    }
}
