package tr.xip.wanikani.database.table;

import android.provider.BaseColumns;

/**
 * Created by Hikari on 1/10/15.
 */
public class CriticalItemsTable extends ItemsTable implements BaseColumns {
    public static final String TABLE_NAME = "critical_items";
    public static final String COLUMN_NAME_PERCENTAGE = "percentage";

    public static final String[] COLUMNS = {
            COLUMN_NAME_ID,
            COLUMN_NAME_CHARACTER,
            COLUMN_NAME_KANA,
            COLUMN_NAME_MEANING,
            COLUMN_NAME_IMAGE,
            COLUMN_NAME_ONYOMI,
            COLUMN_NAME_KUNYOMI,
            COLUMN_NAME_IMPORTANT_READING,
            COLUMN_NAME_LEVEL,
            COLUMN_NAME_ITEM_TYPE,
            COLUMN_NAME_SRS,
            COLUMN_NAME_UNLOCKED_DATE,
            COLUMN_NAME_AVAILABLE_DATE,
            COLUMN_NAME_BURNED,
            COLUMN_NAME_BURNED_DATE,
            COLUMN_NAME_MEANING_CORRECT,
            COLUMN_NAME_MEANING_INCORRECT,
            COLUMN_NAME_MEANING_MAX_STREAK,
            COLUMN_NAME_MEANING_CURRENT_STREAK,
            COLUMN_NAME_READING_CORRECT,
            COLUMN_NAME_READING_INCORRECT,
            COLUMN_NAME_READING_MAX_STREAK,
            COLUMN_NAME_READING_CURRENT_STREAK,
            COLUMN_NAME_MEANING_NOTE,
            COLUMN_NAME_USER_SYNONYMS,
            COLUMN_NAME_READING_NOTE,
            COLUMN_NAME_PERCENTAGE,
            COLUMN_NAME_NULLABLE
    };
}
