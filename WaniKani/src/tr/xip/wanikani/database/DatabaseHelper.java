package tr.xip.wanikani.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hikari on 1/5/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "database.db";

    public static DatabaseHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

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
        db.execSQL(SQL.CREATE_TABLE_NOTIFICATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(SQL.CREATE_TABLE_NOTIFICATIONS);
                break;
        }
    }
}