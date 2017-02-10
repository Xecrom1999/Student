package Database;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.service.notification.StatusBarNotification;

import app.ariel.student.Note;

/**
 * Created by gamrian on 09/08/2016.
 */
public class NotesDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Notes.db";

    public static final String TABLE_NAME = "notes_table";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "TITLE";
    public static final String COL_3 = "DATE";
    public static final String COL_4 = "X_POS";
    public static final String COL_5 = "Y_POS";
    public static final String COL_6 = "ISTOP";

    Context ctx;

    public NotesDB(Context context) {
        super(context, DATABASE_NAME, null, 25);
        this.ctx = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,DATE TEXT,X_POS TEXT,Y_POS TEXT,ISTOP INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public long insertData(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, note.getTitle());
        contentValues.put(COL_3, note.getDate());
        contentValues.put(COL_4, note.getxPos());
        contentValues.put(COL_5, note.getyPos());
        contentValues.put(COL_6, note.isTop());

        return db.insert(TABLE_NAME, null, contentValues);
    }

    public void deleteData(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor getRowById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME + " where " + COL_1 + "='" + id + "'" , null);
        return res;
    }

    public void updateData(String id, String xPos, String yPos) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_4, xPos);
        contentValues.put(COL_5, yPos);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
    }

    public void updateData2(String id, String title, String date, int isTop) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, title);
        contentValues.put(COL_3, date);
        contentValues.put(COL_6, isTop);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        for (StatusBarNotification s : notificationManager.getActiveNotifications()) {
            if (s.getId() > 100000)
                notificationManager.cancel(s.getId());
        }

        db.delete(TABLE_NAME, null, null);
    }

    public Note getNoteById(String id) {
        Cursor res = getAllData();
        Note note = null;
        while (res.moveToNext())
            if (res.getString(0).equals(id))  {
                note = new Note(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getInt(5));
                break;
            }
        return note;
    }

}
