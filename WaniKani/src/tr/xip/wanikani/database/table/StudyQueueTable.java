package tr.xip.wanikani.database.table;

import android.provider.BaseColumns;

/**
 * Created by Hikari on 1/10/15.
 */
public class StudyQueueTable implements BaseColumns {
    public static final String TABLE_NAME = "study_queue";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_LESSONS_AVAILABLE = "lessons_available";
    public static final String COLUMN_NAME_REVIEWS_AVAILABLE = "reviews_available";
    public static final String COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_HOUR = "reviews_available_next_hour";
    public static final String COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_DAY = "reviews_available_next_day";
    public static final String COLUMN_NAME_NEXT_REVIEW_DATE = "next_review_date";
    public static final String COLUMN_NAME_NULLABLE = "nullable";

    public static final String[] COLUMNS = {
            COLUMN_NAME_ID,
            COLUMN_NAME_LESSONS_AVAILABLE,
            COLUMN_NAME_REVIEWS_AVAILABLE,
            COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_HOUR,
            COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_DAY,
            COLUMN_NAME_NEXT_REVIEW_DATE,
            COLUMN_NAME_NULLABLE
    };
}
