package app.ariel.student;

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

import com.ariel.student.student.R;

import java.util.Calendar;
import java.util.Date;

import Database.CalendarDB;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {
    }

    @Override
        public void onReceive(Context context, Intent intent) {

        CalendarDB database = new CalendarDB(context);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Calendar calendar = Calendar.getInstance();

        String id = intent.getStringExtra("id");

        Event event = database.getEventById(id);

        calendar.setTime(new Date(Long.valueOf(event.getDate())));

        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myIntent.putExtra("calendar", calendar);
        myIntent.putExtra("fromNoti", true);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.reminder_string))
                .setContentText(event.getTitle())
                .setSmallIcon(R.mipmap.ic_calendar5)
                .setContentIntent(PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setVibrate(new long[] {500, 500})
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setColor(context.getResources().getColor(R.color.notification_color))
                .setLights(context.getResources().getColor(R.color.primary_color), 1000, 1000)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        StatusBarNotification [] not;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            not = notificationManager.getActiveNotifications();
        }
        else not = new StatusBarNotification[1];
        notificationManager.notify(not.length, notification);
    }
}