package akw.android.thetodo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class NotificationHelper extends ContextWrapper {
    public static final String CHANNEL_1_ID = "task_reminder";
    public static final String CHANNEL_1_NAME = "Reminder Channel";
    public static final String CHANNEL_2_NAME = "TimeTable";
    public static final String CHANNEL_3_NAME = "Backup and Sync";
    public static final String CHANNEL_2_ID = "timetable_reminder";
    public static final String CHANNEL_3_ID = "backup_sync";
    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel1();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel1() {
        NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, CHANNEL_1_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.design_default_color_primary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1);
    }

//    @TargetApi(Build.VERSION_CODES.O)
//    public void createNotificationChannel2() {
//
//        NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, CHANNEL_2_NAME, NotificationManager.IMPORTANCE_HIGH);
//        channel1.enableLights(true);
//        channel1.enableVibration(true);
//        channel1.setLightColor(R.color.design_default_color_primary);
//        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//
//        getManager().createNotificationChannel(channel2);
//    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public void makeChannel1Notification(String title, String message) {

        createNotificationChannel1();

        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_MUTABLE);


        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_playlist_add_check_24_white)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManagerCompat.from(this).notify(1, notification);
    }

    public NotificationCompat.Builder getChannel2Notification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_2_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_table_view_24);
    }
}
