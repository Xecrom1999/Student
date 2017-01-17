package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.ariel.student.Lesson;

/**
 * Created by user on 17/01/16.
 */
public class ScheduleDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Schedule.db";

    public static final String SUNDAY_TABLE_NAME = "sunday_table";
    public static final String MONDAY_TABLE_NAME = "monday_table";
    public static final String TUESDAY_TABLE_NAME = "tuesday_table";
    public static final String WEDNESDAY_TABLE_NAME = "wednesday_table";
    public static final String THURSDAY_TABLE_NAME = "thursday_table";
    public static final String FRIDAY_TABLE_NAME = "friday_table";
    public static final String LESSONS_TIME = "time_table";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "TIME";
    public static final String COL_4 = "LENGTH";

    public ScheduleDB(Context context) {
        super(context, DATABASE_NAME, null, 15);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + SUNDAY_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,TIME TEXT,LENGTH TEXT)");
        db.execSQL("create table " + MONDAY_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,TIME TEXT,LENGTH TEXT)");
        db.execSQL("create table " + TUESDAY_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,TIME TEXT,LENGTH TEXT)");
        db.execSQL("create table " + WEDNESDAY_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,TIME TEXT,LENGTH TEXT)");
        db.execSQL("create table " + THURSDAY_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,TIME TEXT,LENGTH TEXT)");
        db.execSQL("create table " + FRIDAY_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,TIME TEXT,LENGTH TEXT)");
        db.execSQL("create table " + LESSONS_TIME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TIME TEXT)");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SUNDAY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MONDAY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TUESDAY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WEDNESDAY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + THURSDAY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FRIDAY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LESSONS_TIME);

        onCreate(db);
    }

    public void insertData(String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, time);

        db.insert(LESSONS_TIME, null, contentValues);
    }

    public void insertData(int day, Lesson lesson) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, lesson.getName());
        contentValues.put(COL_3, lesson.getTime());
        contentValues.put(COL_4, lesson.getLength());

        String tableName = getTableName(day);

        db.insert(tableName, null, contentValues);
    }

    public void deleteData(int day, Lesson lesson) {

        SQLiteDatabase db = this.getWritableDatabase();

        String tableName = getTableName(day);

        db.delete(tableName, "NAME = ? AND TIME = ? AND LENGTH = ?",new String[]{lesson.getName(), lesson.getTime(), lesson.getLength()});
    }

    public Cursor getData(int day) {
        SQLiteDatabase db = this.getWritableDatabase();

        String tableName = getTableName(day);

            Cursor res = db.rawQuery("select * from " + tableName, null);
            return res;
    }

    public void updateData(int day, Lesson oldLesson, Lesson lesson) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        String name = oldLesson.getName();
        String time = oldLesson.getTime();
        String length = oldLesson.getLength();

        String tableName = getTableName(day);

        contentValues.put(COL_2, lesson.getName());
        contentValues.put(COL_3, lesson.getTime());
        contentValues.put(COL_4, lesson.getLength());

        db.update(tableName, contentValues, "NAME = ? AND TIME = ? AND LENGTH = ?", new String[]{name, time, length});
    }

    public void deleteDay(int day) {

        SQLiteDatabase db = this.getWritableDatabase();

        String tableName = getTableName(day);

        db.delete(tableName, null, null);
    }

    public void deleteAll() {

        for (int i = 0; i < 7; i++)
            deleteDay(i);
    }

    private String getTableName(int day) {
        String tableName = MONDAY_TABLE_NAME;

        switch (day) {
            case 0:
                tableName = SUNDAY_TABLE_NAME;
                break;
            case 1:
                tableName = MONDAY_TABLE_NAME;
                break;
            case 2:
                tableName = TUESDAY_TABLE_NAME;
                break;
            case 3:
                tableName = WEDNESDAY_TABLE_NAME;
                break;
            case 4:
                tableName = THURSDAY_TABLE_NAME;
                break;
            case 5:
                tableName = FRIDAY_TABLE_NAME;
                break;
        }

        return tableName;
    }
}
