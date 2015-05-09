package tr.xip.wanikani.database.table;

import android.provider.BaseColumns;

/**
 * Created by Hikari on 1/10/15.
 */
public class SRSDistributionTable implements BaseColumns {
    public static final String TABLE_NAME = "srs_distribution";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_APPRENTICE_RADICALS = "apprentice_radicals";
    public static final String COLUMN_NAME_APPRENTICE_KANJI = "apprentice_kanji";
    public static final String COLUMN_NAME_APPRENTICE_VOCABULARY = "apprentice_vocabulary";
    public static final String COLUMN_NAME_GURU_RADICALS = "guru_radicals";
    public static final String COLUMN_NAME_GURU_KANJI = "guru_kanji";
    public static final String COLUMN_NAME_GURU_VOCABULARY = "guru_vocabulary";
    public static final String COLUMN_NAME_MASTER_RADICALS = "master_radicals";
    public static final String COLUMN_NAME_MASTER_KANJI = "master_kanji";
    public static final String COLUMN_NAME_MASTER_VOCABULARY = "master_vocabulary";
    public static final String COLUMN_NAME_ENLIGHTENED_RADICALS = "enlightened_radicals";
    public static final String COLUMN_NAME_ENLIGHTENED_KANJI = "enlightened_kanji";
    public static final String COLUMN_NAME_ENLIGHTENED_VOCABULARY = "enlightened_vocabulary";
    public static final String COLUMN_NAME_BURNED_RADICALS = "burned_radicals";
    public static final String COLUMN_NAME_BURNED_KANJI = "burned_kanji";
    public static final String COLUMN_NAME_BURNED_VOCABULARY = "burned_vocabulary";
    public static final String COLUMN_NAME_NULLABLE = "nullable";

    public static final String[] COLUMNS = {
            COLUMN_NAME_ID,
            COLUMN_NAME_APPRENTICE_RADICALS,
            COLUMN_NAME_APPRENTICE_KANJI,
            COLUMN_NAME_APPRENTICE_VOCABULARY,
            COLUMN_NAME_GURU_RADICALS,
            COLUMN_NAME_GURU_KANJI,
            COLUMN_NAME_GURU_VOCABULARY,
            COLUMN_NAME_MASTER_RADICALS,
            COLUMN_NAME_MASTER_KANJI,
            COLUMN_NAME_MASTER_VOCABULARY,
            COLUMN_NAME_ENLIGHTENED_RADICALS,
            COLUMN_NAME_ENLIGHTENED_KANJI,
            COLUMN_NAME_ENLIGHTENED_VOCABULARY,
            COLUMN_NAME_BURNED_RADICALS,
            COLUMN_NAME_BURNED_KANJI,
            COLUMN_NAME_BURNED_VOCABULARY,
            COLUMN_NAME_NULLABLE
    };
}