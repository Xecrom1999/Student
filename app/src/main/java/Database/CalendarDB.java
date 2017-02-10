package Database;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.service.notification.StatusBarNotification;

import app.ariel.student.CalendarActivity;
import app.ariel.student.Event;

import java.util.Date;

/**
 * Created by gamrian on 09/08/2016.
 */
public class CalendarDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Calendar.db";

    public static final String TABLE_NAME = "calendar_table";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "TITLE";
    public static final String COL_3 = "DATE";
    public static final String COL_4 = "TIME";
    public static final String COL_5 = "COMMENT";
    public static final String COL_6 = "REMINDER";

    Context ctx;

    public CalendarDB(Context context) {
        super(context, DATABASE_NAME, null, 10);

        this.ctx = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,DATE TEXT,TIME TEXT,COMMENT TEXT, REMINDER TEXT)");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public long insertData(Event event) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, event.getTitle());
        contentValues.put(COL_3, event.getDate());
        contentValues.put(COL_4, event.getTime());
        contentValues.put(COL_5, event.getComment());
        contentValues.put(COL_6, event.getReminder());

        long id =  db.insert(TABLE_NAME, null, contentValues);

        CalendarActivity.updateFragments(new Date(Long.valueOf(event.getDate())));

        return id;
    }

    public void deleteData(String id, String date) {

        SQLiteDatabase db = this.getWritableDatabase();

        CalendarActivity.updateFragments(new Date(Long.valueOf(date)));

        db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Event getEventById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME + " where " + COL_1 + "='" + id + "'" , null);
        res.moveToNext();
        Event event = new Event(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5));

        return event;
    }

    public void updateData(String id, final Event newEvent) {

        Event event = getEventById(id);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, newEvent.getTitle());
        contentValues.put(COL_3, newEvent.getDate());
        contentValues.put(COL_4, newEvent.getTime());
        contentValues.put(COL_5, newEvent.getComment());
        contentValues.put(COL_6, newEvent.getReminder());

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});

        CalendarActivity.updateFragments(new Date(Long.valueOf(event.getDate())));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CalendarActivity.updateFragments(new Date(Long.valueOf(newEvent.getDate())));
            }
        }, 400);

    }

    public Cursor getAllEventsAtDate(String date) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME + " where " + COL_3 + "='" + date + "'" , null);
        return res;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        for (StatusBarNotification s : notificationManager.getActiveNotifications()) {
            if (s.getId() < 100000)
                notificationManager.cancel(s.getId());
        }

        db.delete(TABLE_NAME, null, null);
    }
}
