package com.example.thetodo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
String title="Text";
String body="Notification";
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper appNotificationHelp = new NotificationHelper(context);
        if(intent.getExtras()!=null){
                title=intent.getStringExtra("title");
                body=intent.getStringExtra("body");
        }

        NotificationCompat.Builder notificationBuilder = appNotificationHelp.getChannel1Notification(title, body);
        appNotificationHelp.getManager().notify(1, notificationBuilder.build());

    }
}
