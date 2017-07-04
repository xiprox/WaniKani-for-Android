package tr.xip.wanikani.database.table;

import android.provider.BaseColumns;

public class NotificationsTable implements BaseColumns {
    public static final String TABLE_NAME = "notifications";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_SHORT_TEXT = "short_text";
    public static final String COLUMN_NAME_TEXT = "text";
    public static final String COLUMN_NAME_IMAGE = "image";
    public static final String COLUMN_NAME_ACTION_URL = "action_url";
    public static final String COLUMN_NAME_ACTION_TEXT = "action_text";
    public static final String COLUMN_NAME_READ = "read";
    public static final String COLUMN_NAME_NULLABLE = "nullable";

    public static final String[] COLUMNS = {
            COLUMN_NAME_ID,
            COLUMN_NAME_TITLE,
            COLUMN_NAME_SHORT_TEXT,
            COLUMN_NAME_TEXT,
            COLUMN_NAME_IMAGE,
            COLUMN_NAME_ACTION_URL,
            COLUMN_NAME_ACTION_TEXT,
            COLUMN_NAME_READ,
            COLUMN_NAME_NULLABLE
    };
}
