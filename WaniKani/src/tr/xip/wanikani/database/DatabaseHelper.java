package tr.xip.wanikani.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hikari on 1/5/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL.CREATE_TABLE_ITEMS);
        db.execSQL(SQL.CREATE_TABLE_USERS);
        db.execSQL(SQL.CREATE_TABLE_RECENT_UNLOCKS);
        db.execSQL(SQL.CREATE_TABLE_CRITICAL_ITEMS);
        db.execSQL(SQL.CREATE_TABLE_STUDY_QUEUE);
        db.execSQL(SQL.CREATE_TABLE_LEVEL_PROGRESSION);
        db.execSQL(SQL.CREATE_TABLE_SRS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL.DELETE_TABLE_ITEMS);
        db.execSQL(SQL.DELETE_TABLE_USERS);
        db.execSQL(SQL.DELETE_TABLE_RECENT_UNLOCKS);
        db.execSQL(SQL.DELETE_TABLE_CRITICAL_ITEMS);
        db.execSQL(SQL.DELETE_TABLE_STUDY_QUEUE);
        db.execSQL(SQL.DELETE_TABLE_LEVEL_PROGRESSION);
        db.execSQL(SQL.DELETE_TABLE_SRS);
        onCreate(db);
    }
}