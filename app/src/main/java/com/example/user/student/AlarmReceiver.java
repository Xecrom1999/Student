package com.example.user.student;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Calendar calendar = (Calendar) intent.getExtras().get("calendar");

        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myIntent.putExtra("calendar", calendar);
        myIntent.putExtra("fromNoti", true);

        Log.d("MYLOG", calendar.getTime().toString());

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("תזכורת")
                .setContentText(intent.getStringExtra("title"))
                .setSmallIcon(R.drawable.ic_calendar)
                .setContentIntent(PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setVibrate(new long[] {500, 500})
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setColor(context.getColor(R.color.primary_calendar))
                .setLights(context.getColor(R.color.primary_calendar), 1000, 1000)
                .build();

        StatusBarNotification [] not = notificationManager.getActiveNotifications();
        notificationManager.notify(not.length, notification);
    }
}
