package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.student.Note;

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

    Context ctx;

    public CalendarDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.ctx = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,DATE TEXT,TIME TEXT)");

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
        contentValues.put(COL_4, note.getTime());

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

    public void updateData(String id, String title, String date, String time) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, title);
        contentValues.put(COL_3, date);
        contentValues.put(COL_4, time);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
    }
}
