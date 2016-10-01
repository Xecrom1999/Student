package com.example.user.student;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String title = intent.getStringExtra("title");

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText("Random text")
                .setSmallIcon(R.drawable.ic_calendar)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, CalendarActivity.class), 0))
                .setVibrate(new long[] {500, 500})
                .setAutoCancel(true)
                .setSound(alarmSound)
                .build();

        StatusBarNotification [] not = notificationManager.getActiveNotifications();
        Log.d("MYLOG", String.valueOf(not.length));
        notificationManager.notify(not.length, notification);
    }
}
