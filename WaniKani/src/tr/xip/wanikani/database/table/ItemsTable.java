package tr.xip.wanikani.database.table;

import android.provider.BaseColumns;

/**
 * Created by Hikari on 1/5/15.
 */
public class ItemsTable implements BaseColumns {
    public static final String TABLE_NAME = "items";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_CHARACTER = "character";
    public static final String COLUMN_NAME_KANA = "kana";
    public static final String COLUMN_NAME_MEANING = "meaning";
    public static final String COLUMN_NAME_IMAGE = "image";
    public static final String COLUMN_NAME_ONYOMI = "onyomi";
    public static final String COLUMN_NAME_KUNYOMI = "kunyomi";
    public static final String COLUMN_NAME_IMPORTANT_READING = "important_reading";
    public static final String COLUMN_NAME_LEVEL = "level";
    public static final String COLUMN_NAME_ITEM_TYPE = "item_type";
    public static final String COLUMN_NAME_SRS = "srs";
    public static final String COLUMN_NAME_UNLOCKED_DATE = "unlocked_date";
    public static final String COLUMN_NAME_AVAILABLE_DATE = "available_date";
    public static final String COLUMN_NAME_BURNED = "burned";
    public static final String COLUMN_NAME_BURNED_DATE = "burned_date";
    public static final String COLUMN_NAME_MEANING_CORRECT = "meaning_correct";
    public static final String COLUMN_NAME_MEANING_INCORRECT = "meaning_incorrect";
    public static final String COLUMN_NAME_MEANING_MAX_STREAK = "meaning_max_streak";
    public static final String COLUMN_NAME_MEANING_CURRENT_STREAK = "meaning_current_streak";
    public static final String COLUMN_NAME_READING_CORRECT = "reading_correct";
    public static final String COLUMN_NAME_READING_INCORRECT = "reading_incorrect";
    public static final String COLUMN_NAME_READING_MAX_STREAK = "reading_max_streak";
    public static final String COLUMN_NAME_READING_CURRENT_STREAK = "reading_current_streak";
    public static final String COLUMN_NAME_MEANING_NOTE = "meaning_note";
    public static final String COLUMN_NAME_USER_SYNONYMS = "user_synonyms";
    public static final String COLUMN_NAME_READING_NOTE = "reading_note";
    public static final String COLUMN_NAME_NULLABLE = "nullable";

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
            COLUMN_NAME_NULLABLE
    };
}