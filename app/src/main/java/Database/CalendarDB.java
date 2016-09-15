package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.student.Event;

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

    public CalendarDB(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,DATE TEXT,TIME TEXT,COMMENT TEXT)");

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

    public void updateData(String id, Event event) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, event.getTitle());
        contentValues.put(COL_3, event.getDate());
        contentValues.put(COL_4, event.getTime());
        contentValues.put(COL_5, event.getComment());

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
    }

    public Cursor getAllTitles(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME + " where " + COL_3 + "='" + date + "'" , null);
        return res;
    }
}
