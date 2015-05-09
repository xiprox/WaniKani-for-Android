package tr.xip.wanikani.database;

import tr.xip.wanikani.database.table.CriticalItemsTable;
import tr.xip.wanikani.database.table.ItemsTable;
import tr.xip.wanikani.database.table.LevelProgressionTable;
import tr.xip.wanikani.database.table.RecentUnlocksTable;
import tr.xip.wanikani.database.table.SRSDistributionTable;
import tr.xip.wanikani.database.table.StudyQueueTable;
import tr.xip.wanikani.database.table.UsersTable;

public class SQL {

    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String INTEGER_PRIMARY_KEY_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String COMMA_SEP = ", ";

    public static final String CREATE_TABLE_ITEMS = "CREATE TABLE "
            + ItemsTable.TABLE_NAME + " ("
            + ItemsTable.COLUMN_NAME_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + ItemsTable.COLUMN_NAME_CHARACTER + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_KANA + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_MEANING + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_IMAGE + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_ONYOMI + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_KUNYOMI + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_IMPORTANT_READING + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_LEVEL + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_ITEM_TYPE + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_SRS + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_UNLOCKED_DATE + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_AVAILABLE_DATE + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_BURNED + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_BURNED_DATE + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_MEANING_CORRECT + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_MEANING_INCORRECT + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_MEANING_MAX_STREAK + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_MEANING_CURRENT_STREAK + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_READING_CORRECT + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_READING_INCORRECT + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_READING_MAX_STREAK + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_READING_CURRENT_STREAK + INTEGER_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_MEANING_NOTE + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_USER_SYNONYMS + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_READING_NOTE + TEXT_TYPE + COMMA_SEP
            + ItemsTable.COLUMN_NAME_NULLABLE + TEXT_TYPE + ")";

    public static final String DELETE_TABLE_ITEMS = "DROP TABLE IF EXISTS "
            + ItemsTable.TABLE_NAME;

    public static final String CREATE_TABLE_RECENT_UNLOCKS = "CREATE TABLE "
            + RecentUnlocksTable.TABLE_NAME + " ("
            + RecentUnlocksTable.COLUMN_NAME_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_CHARACTER + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_KANA + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_MEANING + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_IMAGE + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_ONYOMI + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_KUNYOMI + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_IMPORTANT_READING + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_LEVEL + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_ITEM_TYPE + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_SRS + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_UNLOCKED_DATE + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_AVAILABLE_DATE + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_BURNED + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_BURNED_DATE + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_MEANING_CORRECT + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_MEANING_INCORRECT + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_MEANING_MAX_STREAK + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_MEANING_CURRENT_STREAK + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_READING_CORRECT + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_READING_INCORRECT + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_READING_MAX_STREAK + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_READING_CURRENT_STREAK + INTEGER_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_MEANING_NOTE + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_USER_SYNONYMS + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_READING_NOTE + TEXT_TYPE + COMMA_SEP
            + RecentUnlocksTable.COLUMN_NAME_NULLABLE + TEXT_TYPE  + ")";

    public static final String DELETE_TABLE_RECENT_UNLOCKS = "DROP TABLE IF EXISTS "
            + RecentUnlocksTable.TABLE_NAME;

    public static final String CREATE_TABLE_CRITICAL_ITEMS = "CREATE TABLE "
            + CriticalItemsTable.TABLE_NAME + " ("
            + CriticalItemsTable.COLUMN_NAME_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_CHARACTER + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_KANA + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_MEANING + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_IMAGE + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_ONYOMI + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_KUNYOMI + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_IMPORTANT_READING + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_LEVEL + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_ITEM_TYPE + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_SRS + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_UNLOCKED_DATE + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_AVAILABLE_DATE + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_BURNED + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_BURNED_DATE + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_MEANING_CORRECT + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_MEANING_INCORRECT + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_MEANING_MAX_STREAK + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_MEANING_CURRENT_STREAK + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_READING_CORRECT + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_READING_INCORRECT + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_READING_MAX_STREAK + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_READING_CURRENT_STREAK + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_MEANING_NOTE + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_USER_SYNONYMS + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_READING_NOTE + TEXT_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_PERCENTAGE + INTEGER_TYPE + COMMA_SEP
            + CriticalItemsTable.COLUMN_NAME_NULLABLE + TEXT_TYPE  + ")";

    public static final String DELETE_TABLE_CRITICAL_ITEMS = "DROP TABLE IF EXISTS "
            + CriticalItemsTable.TABLE_NAME;

    public static final String CREATE_TABLE_STUDY_QUEUE = "CREATE TABLE "
            + StudyQueueTable.TABLE_NAME + " ("
            + StudyQueueTable.COLUMN_NAME_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + StudyQueueTable.COLUMN_NAME_LESSONS_AVAILABLE + INTEGER_TYPE + COMMA_SEP
            + StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE + INTEGER_TYPE + COMMA_SEP
            + StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_HOUR + INTEGER_TYPE + COMMA_SEP
            + StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_DAY + INTEGER_TYPE + COMMA_SEP
            + StudyQueueTable.COLUMN_NAME_NEXT_REVIEW_DATE + INTEGER_TYPE + COMMA_SEP
            + StudyQueueTable.COLUMN_NAME_NULLABLE + TEXT_TYPE  + ")";

    public static final String DELETE_TABLE_STUDY_QUEUE = "DROP TABLE IF EXISTS "
            + StudyQueueTable.TABLE_NAME;

    public static final String CREATE_TABLE_LEVEL_PROGRESSION = "CREATE TABLE "
            + LevelProgressionTable.TABLE_NAME + " ("
            + LevelProgressionTable.COLUMN_NAME_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + LevelProgressionTable.COLUMN_NAME_RADICALS_PROGRESS + INTEGER_TYPE + COMMA_SEP
            + LevelProgressionTable.COLUMN_NAME_RADICALS_TOTAL + INTEGER_TYPE + COMMA_SEP
            + LevelProgressionTable.COLUMN_NAME_REVIEWS_KANJI_PROGRESS + INTEGER_TYPE + COMMA_SEP
            + LevelProgressionTable.COLUMN_NAME_REVIEWS_KANJI_TOTAL + INTEGER_TYPE + COMMA_SEP
            + LevelProgressionTable.COLUMN_NAME_NULLABLE + TEXT_TYPE  + ")";

    public static final String DELETE_TABLE_LEVEL_PROGRESSION = "DROP TABLE IF EXISTS "
            + LevelProgressionTable.TABLE_NAME;

    public static final String CREATE_TABLE_SRS = "CREATE TABLE "
            + SRSDistributionTable.TABLE_NAME + " ("
            + SRSDistributionTable.COLUMN_NAME_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_APPRENTICE_RADICALS + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_APPRENTICE_KANJI + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_APPRENTICE_VOCABULARY + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_GURU_RADICALS + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_GURU_KANJI + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_GURU_VOCABULARY + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_MASTER_RADICALS + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_MASTER_KANJI + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_MASTER_VOCABULARY + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_RADICALS + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_KANJI + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_VOCABULARY + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_BURNED_RADICALS + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_BURNED_KANJI + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_BURNED_VOCABULARY + INTEGER_TYPE + COMMA_SEP
            + SRSDistributionTable.COLUMN_NAME_NULLABLE + TEXT_TYPE + ")";

    public static final String DELETE_TABLE_SRS = "DROP TABLE IF EXISTS "
            + SRSDistributionTable.TABLE_NAME;

    public static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + UsersTable.TABLE_NAME + " ("
            + UsersTable.COLUMN_NAME_ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + UsersTable.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP
            + UsersTable.COLUMN_NAME_GRAVATAR + TEXT_TYPE + COMMA_SEP
            + UsersTable.COLUMN_NAME_LEVEL + INTEGER_TYPE + COMMA_SEP
            + UsersTable.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP
            + UsersTable.COLUMN_NAME_ABOUT + TEXT_TYPE + COMMA_SEP
            + UsersTable.COLUMN_NAME_WEBSITE + TEXT_TYPE + COMMA_SEP
            + UsersTable.COLUMN_NAME_TWITTER + TEXT_TYPE + COMMA_SEP
            + UsersTable.COLUMN_NAME_TOPICS_COUNT + INTEGER_TYPE + COMMA_SEP
            + UsersTable.COLUMN_NAME_POSTS_COUNT + INTEGER_TYPE + COMMA_SEP
            + UsersTable.COLUMN_NAME_CREATION_DATE + INTEGER_TYPE + COMMA_SEP
            + UsersTable.COLUMN_NAME_VACATION_DATE + INTEGER_TYPE + COMMA_SEP
            + UsersTable.COLUMN_NAME_NULLABLE + TEXT_TYPE  + ")";

    public static final String DELETE_TABLE_USERS = "DROP TABLE IF EXISTS "
            + UsersTable.TABLE_NAME;
}