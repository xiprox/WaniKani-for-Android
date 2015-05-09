package tr.xip.wanikani.database.table;

import android.provider.BaseColumns;

/**
 * Created by Hikari on 1/7/15.
 */
public class UsersTable implements BaseColumns {
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_USERNAME = "username";
    public static final String COLUMN_NAME_GRAVATAR = "gravater";
    public static final String COLUMN_NAME_LEVEL = "level";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_ABOUT = "about";
    public static final String COLUMN_NAME_WEBSITE = "website";
    public static final String COLUMN_NAME_TWITTER = "twitter";
    public static final String COLUMN_NAME_TOPICS_COUNT = "topics_count";
    public static final String COLUMN_NAME_POSTS_COUNT = "posts_count";
    public static final String COLUMN_NAME_CREATION_DATE = "creation_date";
    public static final String COLUMN_NAME_VACATION_DATE = "vacation_date";
    public static final String COLUMN_NAME_NULLABLE = "nullable";

    public static final String[] COLUMNS = {
            COLUMN_NAME_ID,
            COLUMN_NAME_USERNAME,
            COLUMN_NAME_GRAVATAR,
            COLUMN_NAME_LEVEL,
            COLUMN_NAME_TITLE,
            COLUMN_NAME_ABOUT,
            COLUMN_NAME_WEBSITE,
            COLUMN_NAME_TWITTER,
            COLUMN_NAME_TOPICS_COUNT,
            COLUMN_NAME_POSTS_COUNT,
            COLUMN_NAME_CREATION_DATE,
            COLUMN_NAME_VACATION_DATE,
            COLUMN_NAME_NULLABLE
    };
}
