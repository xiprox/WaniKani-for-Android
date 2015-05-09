package tr.xip.wanikani.database.table;

import android.provider.BaseColumns;

/**
 * Created by Hikari on 1/10/15.
 */
public class LevelProgressionTable implements BaseColumns {
    public static final String TABLE_NAME = "level_progression";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_RADICALS_PROGRESS = "radicals_progress";
    public static final String COLUMN_NAME_RADICALS_TOTAL = "radicals_total";
    public static final String COLUMN_NAME_REVIEWS_KANJI_PROGRESS = "reviews_kanji_progress";
    public static final String COLUMN_NAME_REVIEWS_KANJI_TOTAL = "reviews_kanji_total";
    public static final String COLUMN_NAME_NULLABLE = "nullable";

    public static final String[] COLUMNS = {
            COLUMN_NAME_ID,
            COLUMN_NAME_RADICALS_PROGRESS,
            COLUMN_NAME_RADICALS_TOTAL,
            COLUMN_NAME_REVIEWS_KANJI_PROGRESS,
            COLUMN_NAME_REVIEWS_KANJI_TOTAL,
            COLUMN_NAME_NULLABLE
    };
}