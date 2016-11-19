package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.student.Lesson;

/**
 * Created by gamrian on 17/09/2016.
 */
public class DefaultLessonsDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Default.db";

    public static final String TABLE_NAME = "default_table";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "TIME";
    public static final String COL_3 = "LENGTH";

    public DefaultLessonsDB(Context context) {
        super(context, DATABASE_NAME, null, 12);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TIME TEXT,LENGTH TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public void updateData(String id, String time, String length) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, time);
        contentValues.put(COL_3, length);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
    }

    public long insertData(String time, String length) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, time);
        contentValues.put(COL_3, length);

        return db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
