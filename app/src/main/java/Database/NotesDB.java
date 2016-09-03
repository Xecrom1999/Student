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
public class NotesDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Notes.db";

    public static final String TABLE_NAME = "notes_table";


    public static final String COL_1 = "ID";
    public static final String COL_2 = "TITLE";
    public static final String COL_3 = "DESCRIPTION";
    public static final String COL_4 = "X_POS";
    public static final String COL_5 = "Y_POS";
    public static final String COL_6 = "DATE";
    public static final String COL_7 = "TIME";

    Context ctx;

    public NotesDB(Context context) {
        super(context, DATABASE_NAME, null, 20);
        this.ctx = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,DESCRIPTION TEXT,X_POS TEXT,Y_POS TEXT,DATE TEXT,TIME TEXT)");

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public long insertData(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, note.getTitle());
        contentValues.put(COL_3, note.getDescription());
        contentValues.put(COL_4, note.getxPos());
        contentValues.put(COL_5, note.getyPos());
        contentValues.put(COL_6, note.getDate());
        contentValues.put(COL_7, note.getTime());

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

    public void updateData(String id, String title, String description, String date, String time) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, title);
        contentValues.put(COL_3, description);
        contentValues.put(COL_6, date);
        contentValues.put(COL_7, time);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
    }

    public void updateData(String id, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_7, time);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
    }
}
