package tr.xip.wanikani.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.CriticalItem;
import tr.xip.wanikani.models.LevelProgression;
import tr.xip.wanikani.models.SRSDistribution;
import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.models.UnlockItem;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.database.table.CriticalItemsTable;
import tr.xip.wanikani.database.table.ItemsTable;
import tr.xip.wanikani.database.table.LevelProgressionTable;
import tr.xip.wanikani.database.table.RecentUnlocksTable;
import tr.xip.wanikani.database.table.SRSDistributionTable;
import tr.xip.wanikani.database.table.StudyQueueTable;
import tr.xip.wanikani.database.table.UsersTable;

/**
 * Created by Hikari on 1/5/15.
 */
public class DatabaseManager {
    private static final String TAG = "Database Manager";

    private Context context;

    private SQLiteDatabase db;

    public DatabaseManager(Context context) {
        this.context = context;
        db = new DatabaseHelper(context).getWritableDatabase();
    }

    private void addItem(BaseItem item) {
        ContentValues values = new ContentValues();
        values.put(ItemsTable.COLUMN_NAME_CHARACTER, item.getCharacter());
        values.put(ItemsTable.COLUMN_NAME_KANA, item.getKana());
        values.put(ItemsTable.COLUMN_NAME_MEANING, item.getMeaning());
        values.put(ItemsTable.COLUMN_NAME_IMAGE, item.getImage());
        values.put(ItemsTable.COLUMN_NAME_ONYOMI, item.getOnyomi());
        values.put(ItemsTable.COLUMN_NAME_KUNYOMI, item.getKunyomi());
        values.put(ItemsTable.COLUMN_NAME_IMPORTANT_READING, item.getImportantReading());
        values.put(ItemsTable.COLUMN_NAME_LEVEL, item.getLevel());
        values.put(ItemsTable.COLUMN_NAME_ITEM_TYPE, item.getType().toString());
        values.put(ItemsTable.COLUMN_NAME_SRS, item.getSrsLevel());
        values.put(ItemsTable.COLUMN_NAME_UNLOCKED_DATE, item.getUnlockDateInSeconds());
        values.put(ItemsTable.COLUMN_NAME_AVAILABLE_DATE, item.getAvailableDateInSeconds());
        values.put(ItemsTable.COLUMN_NAME_BURNED, item.isBurned() ? 1 : 0);
        values.put(ItemsTable.COLUMN_NAME_BURNED_DATE, item.getBurnedDateInSeconds());
        values.put(ItemsTable.COLUMN_NAME_MEANING_CORRECT, item.getMeaningCorrect());
        values.put(ItemsTable.COLUMN_NAME_MEANING_INCORRECT, item.getMeaningIncorrect());
        values.put(ItemsTable.COLUMN_NAME_MEANING_MAX_STREAK, item.getMeaningMaxStreak());
        values.put(ItemsTable.COLUMN_NAME_MEANING_CURRENT_STREAK, item.getMeaningCurrentStreak());
        values.put(ItemsTable.COLUMN_NAME_READING_CORRECT, item.getReadingCorrect());
        values.put(ItemsTable.COLUMN_NAME_READING_INCORRECT, item.getReadingIncorrect());
        values.put(ItemsTable.COLUMN_NAME_READING_MAX_STREAK, item.getReadingMaxStreak());
        values.put(ItemsTable.COLUMN_NAME_READING_CURRENT_STREAK, item.getReadingCurrentStreak());
        values.put(ItemsTable.COLUMN_NAME_MEANING_NOTE, item.getMeaningNote());
        values.put(ItemsTable.COLUMN_NAME_USER_SYNONYMS, item.getUserSynonymsAsString());
        values.put(ItemsTable.COLUMN_NAME_READING_NOTE, item.getReadingNote());

        db.insert(ItemsTable.TABLE_NAME, ItemsTable.COLUMN_NAME_NULLABLE, values);
    }

    public void saveItems(List<BaseItem> list, BaseItem.ItemType type) {
        deleteAllItemsFromSameLevelAndType(list, type);

        for (BaseItem item : list)
            addItem(item);
    }

    public List<BaseItem> getItems(BaseItem.ItemType itemType, int[] levels) {
        List<BaseItem> list = new ArrayList<>();

        for (int level : levels) {
            String whereClause = ItemsTable.COLUMN_NAME_ITEM_TYPE + " = ?" + " AND "
                    + ItemsTable.COLUMN_NAME_LEVEL + " = ?";
            String[] whereArgs = {
                    itemType.toString(),
                    String.valueOf(level)
            };

            Cursor c = db.query(
                    ItemsTable.TABLE_NAME,
                    ItemsTable.COLUMNS,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null
            );

            if (c != null && c.moveToFirst()) {
                do {
                    BaseItem item = new BaseItem(
                            c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_ID)),
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_CHARACTER)),
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_KANA)),
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING)),
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_IMAGE)),
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_ONYOMI)),
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_KUNYOMI)),
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_IMPORTANT_READING)),
                            c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_LEVEL)),
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_ITEM_TYPE)),
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_SRS)),
                            c.getLong(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_UNLOCKED_DATE)),
                            c.getLong(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_AVAILABLE_DATE)),
                            c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_BURNED)) == 1,
                            c.getLong(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_BURNED_DATE)),
                            c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING_CORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING_INCORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING_MAX_STREAK)),
                            c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING_CURRENT_STREAK)),
                            c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_READING_CORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_READING_INCORRECT)),
                            c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_READING_MAX_STREAK)),
                            c.getInt(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_READING_CURRENT_STREAK)),
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_MEANING_NOTE)),
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_USER_SYNONYMS)) != null
                                    ? c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_USER_SYNONYMS)).split(",")
                                    : null,
                            c.getString(c.getColumnIndexOrThrow(ItemsTable.COLUMN_NAME_READING_NOTE))
                    );
                    list.add(item);
                } while (c.moveToNext());
            }
        }

        return list.size() != 0 ? list : null;
    }

    private void deleteAllItemsFromSameLevelAndType(List<BaseItem> list, BaseItem.ItemType type) {
        HashSet<Integer> levels = new HashSet();

        for (BaseItem item : list)
            levels.add(item.getLevel());

        for (Integer level : levels) {
            String whereClause = ItemsTable.COLUMN_NAME_LEVEL + " = ?" + " AND "
                    + ItemsTable.COLUMN_NAME_ITEM_TYPE + " = ?";
            String[] whereArgs = {
                    String.valueOf(level),
                    type.toString()
            };

            db.delete(ItemsTable.TABLE_NAME, whereClause, whereArgs);
        }
    }

    private void addRecentUnlock(UnlockItem item) {
        ContentValues values = new ContentValues();
        values.put(RecentUnlocksTable.COLUMN_NAME_CHARACTER, item.getCharacter());
        values.put(RecentUnlocksTable.COLUMN_NAME_KANA, item.getKana());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING, item.getMeaning());
        values.put(RecentUnlocksTable.COLUMN_NAME_IMAGE, item.getImage());
        values.put(RecentUnlocksTable.COLUMN_NAME_ONYOMI, item.getOnyomi());
        values.put(RecentUnlocksTable.COLUMN_NAME_KUNYOMI, item.getKunyomi());
        values.put(RecentUnlocksTable.COLUMN_NAME_IMPORTANT_READING, item.getImportantReading());
        values.put(RecentUnlocksTable.COLUMN_NAME_LEVEL, item.getLevel());
        values.put(RecentUnlocksTable.COLUMN_NAME_ITEM_TYPE, item.getType().toString());
        values.put(RecentUnlocksTable.COLUMN_NAME_SRS, item.getSrsLevel());
        values.put(RecentUnlocksTable.COLUMN_NAME_UNLOCKED_DATE, item.getUnlockDateInSeconds());
        values.put(RecentUnlocksTable.COLUMN_NAME_AVAILABLE_DATE, item.getAvailableDateInSeconds());
        values.put(RecentUnlocksTable.COLUMN_NAME_BURNED, item.isBurned() ? 1 : 0);
        values.put(RecentUnlocksTable.COLUMN_NAME_BURNED_DATE, item.getBurnedDateInSeconds());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING_CORRECT, item.getMeaningCorrect());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING_INCORRECT, item.getMeaningIncorrect());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING_MAX_STREAK, item.getMeaningMaxStreak());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING_CURRENT_STREAK, item.getMeaningCurrentStreak());
        values.put(RecentUnlocksTable.COLUMN_NAME_READING_CORRECT, item.getReadingCorrect());
        values.put(RecentUnlocksTable.COLUMN_NAME_READING_INCORRECT, item.getReadingIncorrect());
        values.put(RecentUnlocksTable.COLUMN_NAME_READING_MAX_STREAK, item.getReadingMaxStreak());
        values.put(RecentUnlocksTable.COLUMN_NAME_READING_CURRENT_STREAK, item.getReadingCurrentStreak());
        values.put(RecentUnlocksTable.COLUMN_NAME_MEANING_NOTE, item.getMeaningNote());
        values.put(RecentUnlocksTable.COLUMN_NAME_USER_SYNONYMS, item.getUserSynonymsAsString());
        values.put(RecentUnlocksTable.COLUMN_NAME_READING_NOTE, item.getReadingNote());

        db.insert(RecentUnlocksTable.TABLE_NAME, RecentUnlocksTable.COLUMN_NAME_NULLABLE, values);
    }

    public void saveRecentUnlocks(List<UnlockItem> list) {
        deleteSameRecentUnlocks(list);

        for (UnlockItem item : list)
            addRecentUnlock(item);
    }

    public List<UnlockItem> getRecentUnlocks(int limit) {
        List<UnlockItem> list = new ArrayList<>();

        Cursor c = db.query(
                RecentUnlocksTable.TABLE_NAME,
                RecentUnlocksTable.COLUMNS,
                null,
                null,
                null,
                null,
                null,
                String.valueOf(limit)
        );

        if (c != null && c.moveToFirst()) {
            do {
                UnlockItem item = new UnlockItem(
                        c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_ID)),
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_CHARACTER)),
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_KANA)),
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING)),
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_IMAGE)),
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_ONYOMI)),
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_KUNYOMI)),
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_IMPORTANT_READING)),
                        c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_LEVEL)),
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_ITEM_TYPE)),
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_SRS)),
                        c.getLong(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_UNLOCKED_DATE)),
                        c.getLong(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_AVAILABLE_DATE)),
                        c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_BURNED)) == 1,
                        c.getLong(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_BURNED_DATE)),
                        c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING_CORRECT)),
                        c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING_INCORRECT)),
                        c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING_MAX_STREAK)),
                        c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING_CURRENT_STREAK)),
                        c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_READING_CORRECT)),
                        c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_READING_INCORRECT)),
                        c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_READING_MAX_STREAK)),
                        c.getInt(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_READING_CURRENT_STREAK)),
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_MEANING_NOTE)),
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_USER_SYNONYMS)) != null
                                ? c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_USER_SYNONYMS)).split(",")
                                : null,
                        c.getString(c.getColumnIndexOrThrow(RecentUnlocksTable.COLUMN_NAME_READING_NOTE))
                );
                list.add(item);
            } while (c.moveToNext());
        }

        return list.size() != 0 ? list : null;
    }

    public void deleteSameRecentUnlocks(List<UnlockItem> list) {
        for (UnlockItem item : list) {
            String whereClause = item.getImage() == null
                    ? RecentUnlocksTable.COLUMN_NAME_CHARACTER + " = ?"
                    : RecentUnlocksTable.COLUMN_NAME_IMAGE + " = ?";
            String[] whereArgs = {
                    item.getImage() == null ? String.valueOf(item.getCharacter()) : item.getImage()
            };

            db.delete(RecentUnlocksTable.TABLE_NAME, whereClause, whereArgs);
        }
    }

    private void addCriticalItem(CriticalItem item) {
        ContentValues values = new ContentValues();
        values.put(CriticalItemsTable.COLUMN_NAME_CHARACTER, item.getCharacter());
        values.put(CriticalItemsTable.COLUMN_NAME_KANA, item.getKana());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING, item.getMeaning());
        values.put(CriticalItemsTable.COLUMN_NAME_IMAGE, item.getImage());
        values.put(CriticalItemsTable.COLUMN_NAME_ONYOMI, item.getOnyomi());
        values.put(CriticalItemsTable.COLUMN_NAME_KUNYOMI, item.getKunyomi());
        values.put(CriticalItemsTable.COLUMN_NAME_IMPORTANT_READING, item.getImportantReading());
        values.put(CriticalItemsTable.COLUMN_NAME_LEVEL, item.getLevel());
        values.put(CriticalItemsTable.COLUMN_NAME_ITEM_TYPE, item.getType().toString());
        values.put(CriticalItemsTable.COLUMN_NAME_SRS, item.getSrsLevel());
        values.put(CriticalItemsTable.COLUMN_NAME_UNLOCKED_DATE, item.getUnlockDateInSeconds());
        values.put(CriticalItemsTable.COLUMN_NAME_AVAILABLE_DATE, item.getAvailableDateInSeconds());
        values.put(CriticalItemsTable.COLUMN_NAME_BURNED, item.isBurned() ? 1 : 0);
        values.put(CriticalItemsTable.COLUMN_NAME_BURNED_DATE, item.getBurnedDateInSeconds());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING_CORRECT, item.getMeaningCorrect());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING_INCORRECT, item.getMeaningIncorrect());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING_MAX_STREAK, item.getMeaningMaxStreak());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING_CURRENT_STREAK, item.getMeaningCurrentStreak());
        values.put(CriticalItemsTable.COLUMN_NAME_READING_CORRECT, item.getReadingCorrect());
        values.put(CriticalItemsTable.COLUMN_NAME_READING_INCORRECT, item.getReadingIncorrect());
        values.put(CriticalItemsTable.COLUMN_NAME_READING_MAX_STREAK, item.getReadingMaxStreak());
        values.put(CriticalItemsTable.COLUMN_NAME_READING_CURRENT_STREAK, item.getReadingCurrentStreak());
        values.put(CriticalItemsTable.COLUMN_NAME_MEANING_NOTE, item.getMeaningNote());
        values.put(CriticalItemsTable.COLUMN_NAME_USER_SYNONYMS, item.getUserSynonymsAsString());
        values.put(CriticalItemsTable.COLUMN_NAME_READING_NOTE, item.getReadingNote());
        values.put(CriticalItemsTable.COLUMN_NAME_PERCENTAGE, item.getPercentage());

        db.insert(CriticalItemsTable.TABLE_NAME, CriticalItemsTable.COLUMN_NAME_NULLABLE, values);
    }

    public void saveCriticalItems(List<CriticalItem> list) {
        deleteSameCriticalItems(list);

        for (CriticalItem item : list)
            addCriticalItem(item);
    }

    public List<CriticalItem> getCriticalItems(int percentage) {
        List<CriticalItem> list = new ArrayList<>();

        String whereClause = CriticalItemsTable.COLUMN_NAME_PERCENTAGE + " < ?";
        String[] whereArgs = {
                String.valueOf(percentage)
        };

        Cursor c = db.query(
                CriticalItemsTable.TABLE_NAME,
                CriticalItemsTable.COLUMNS,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        if (c != null && c.moveToFirst()) {
            do {
                CriticalItem item = new CriticalItem(
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_ID)),
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_CHARACTER)),
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_KANA)),
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING)),
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_IMAGE)),
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_ONYOMI)),
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_KUNYOMI)),
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_IMPORTANT_READING)),
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_LEVEL)),
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_ITEM_TYPE)),
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_SRS)),
                        c.getLong(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_UNLOCKED_DATE)),
                        c.getLong(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_AVAILABLE_DATE)),
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_BURNED)) == 1,
                        c.getLong(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_BURNED_DATE)),
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING_CORRECT)),
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING_INCORRECT)),
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING_MAX_STREAK)),
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING_CURRENT_STREAK)),
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_READING_CORRECT)),
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_READING_INCORRECT)),
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_READING_MAX_STREAK)),
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_READING_CURRENT_STREAK)),
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_MEANING_NOTE)),
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_USER_SYNONYMS)) != null
                                ? c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_USER_SYNONYMS)).split(",")
                                : null,
                        c.getString(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_READING_NOTE)),
                        c.getInt(c.getColumnIndexOrThrow(CriticalItemsTable.COLUMN_NAME_PERCENTAGE))
                );
                list.add(item);
            } while (c.moveToNext());
        }

        return list;
    }

    private void deleteSameCriticalItems(List<CriticalItem> list) {
        for (CriticalItem item : list) {
            String whereClause = item.getImage() == null
                    ? CriticalItemsTable.COLUMN_NAME_CHARACTER + " = ?"
                    : CriticalItemsTable.COLUMN_NAME_IMAGE + " = ?";
            String[] whereArgs = {
                    item.getImage() == null ? String.valueOf(item.getCharacter()) : item.getImage()
            };

            db.delete(CriticalItemsTable.TABLE_NAME, whereClause, whereArgs);
        }
    }

    public void saveStudyQueue(StudyQueue queue) {
        deleteStudyQueue();

        ContentValues values = new ContentValues();
        values.put(StudyQueueTable.COLUMN_NAME_LESSONS_AVAILABLE, queue.getAvailableLesonsCount());
        values.put(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE, queue.getAvailableReviewsCount());
        values.put(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_HOUR, queue.getAvailableReviewsNextHourCount());
        values.put(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_DAY, queue.getAvailableReviewsNextDayCount());
        values.put(StudyQueueTable.COLUMN_NAME_NEXT_REVIEW_DATE, queue.getNextReviewDateInSeconds());

        db.insert(StudyQueueTable.TABLE_NAME, StudyQueueTable.COLUMN_NAME_NULLABLE, values);
    }

    public StudyQueue getStudyQueue() {
        Cursor c = db.query(
                StudyQueueTable.TABLE_NAME,
                StudyQueueTable.COLUMNS,
                null,
                null,
                null,
                null,
                null
        );

        if (c != null && c.moveToFirst()) {
            return new StudyQueue(
                    c.getInt(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_ID)),
                    c.getInt(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_LESSONS_AVAILABLE)),
                    c.getInt(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE)),
                    c.getInt(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_HOUR)),
                    c.getInt(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_REVIEWS_AVAILABLE_NEXT_DAY)),
                    c.getLong(c.getColumnIndexOrThrow(StudyQueueTable.COLUMN_NAME_NEXT_REVIEW_DATE)),
                    getUser().getUserInformation()
            );
        } else {
            Log.e(TAG, "No study queue found; returning null");
            return null;
        }
    }

    public void deleteStudyQueue() {
        db.delete(StudyQueueTable.TABLE_NAME, null, null);
    }

    public void saveLevelProgression(LevelProgression progression) {
        deleteLevelProgression();

        ContentValues values = new ContentValues();
        values.put(LevelProgressionTable.COLUMN_NAME_RADICALS_PROGRESS, progression.getRadicalsProgress());
        values.put(LevelProgressionTable.COLUMN_NAME_RADICALS_TOTAL, progression.getRadicalsTotal());
        values.put(LevelProgressionTable.COLUMN_NAME_REVIEWS_KANJI_PROGRESS, progression.getKanjiProgress());
        values.put(LevelProgressionTable.COLUMN_NAME_REVIEWS_KANJI_TOTAL, progression.getKanjiTotal());

        db.insert(LevelProgressionTable.TABLE_NAME, LevelProgressionTable.COLUMN_NAME_NULLABLE, values);
    }

    public LevelProgression getLevelProgression() {
        Cursor c = db.query(
                LevelProgressionTable.TABLE_NAME,
                LevelProgressionTable.COLUMNS,
                null,
                null,
                null,
                null,
                null
        );

        if (c != null && c.moveToFirst()) {
            return new LevelProgression(
                    c.getInt(c.getColumnIndexOrThrow(LevelProgressionTable.COLUMN_NAME_ID)),
                    getUser().getUserInformation(),
                    c.getInt(c.getColumnIndexOrThrow(LevelProgressionTable.COLUMN_NAME_RADICALS_PROGRESS)),
                    c.getInt(c.getColumnIndexOrThrow(LevelProgressionTable.COLUMN_NAME_RADICALS_TOTAL)),
                    c.getInt(c.getColumnIndexOrThrow(LevelProgressionTable.COLUMN_NAME_REVIEWS_KANJI_PROGRESS)),
                    c.getInt(c.getColumnIndexOrThrow(LevelProgressionTable.COLUMN_NAME_REVIEWS_KANJI_TOTAL))
            );
        } else {
            Log.e(TAG, "No study queue found; returning null");
            return null;
        }
    }

    public void deleteLevelProgression() {
        db.delete(SRSDistributionTable.TABLE_NAME, null, null);
    }

    public void saveSrsDistribution(SRSDistribution distribution) {
        deleteSrsDistribution();

        ContentValues values = new ContentValues();
        values.put(SRSDistributionTable.COLUMN_NAME_APPRENTICE_RADICALS, distribution.getAprentice().getRadicalsCount());
        values.put(SRSDistributionTable.COLUMN_NAME_APPRENTICE_KANJI, distribution.getAprentice().getKanjiCount());
        values.put(SRSDistributionTable.COLUMN_NAME_APPRENTICE_VOCABULARY, distribution.getAprentice().getVocabularyCount());
        values.put(SRSDistributionTable.COLUMN_NAME_GURU_RADICALS, distribution.getGuru().getRadicalsCount());
        values.put(SRSDistributionTable.COLUMN_NAME_GURU_KANJI, distribution.getGuru().getKanjiCount());
        values.put(SRSDistributionTable.COLUMN_NAME_GURU_VOCABULARY, distribution.getGuru().getVocabularyCount());
        values.put(SRSDistributionTable.COLUMN_NAME_MASTER_RADICALS, distribution.getMaster().getRadicalsCount());
        values.put(SRSDistributionTable.COLUMN_NAME_MASTER_KANJI, distribution.getMaster().getKanjiCount());
        values.put(SRSDistributionTable.COLUMN_NAME_MASTER_VOCABULARY, distribution.getMaster().getVocabularyCount());
        values.put(SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_RADICALS, distribution.getEnlighten().getRadicalsCount());
        values.put(SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_KANJI, distribution.getEnlighten().getKanjiCount());
        values.put(SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_VOCABULARY, distribution.getEnlighten().getVocabularyCount());
        values.put(SRSDistributionTable.COLUMN_NAME_BURNED_RADICALS, distribution.getBurned().getRadicalsCount());
        values.put(SRSDistributionTable.COLUMN_NAME_BURNED_KANJI, distribution.getBurned().getKanjiCount());
        values.put(SRSDistributionTable.COLUMN_NAME_BURNED_VOCABULARY, distribution.getBurned().getVocabularyCount());

        db.insert(SRSDistributionTable.TABLE_NAME, SRSDistributionTable.COLUMN_NAME_NULLABLE, values);
    }

    public SRSDistribution getSrsDistribution() {
        Cursor c = db.query(
                SRSDistributionTable.TABLE_NAME,
                SRSDistributionTable.COLUMNS,
                null,
                null,
                null,
                null,
                null
        );

        if (c != null && c.moveToFirst()) {
            return new SRSDistribution(
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_ID)),
                    getUser().getUserInformation(),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_APPRENTICE_RADICALS)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_APPRENTICE_KANJI)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_APPRENTICE_VOCABULARY)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_GURU_RADICALS)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_GURU_KANJI)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_GURU_VOCABULARY)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_MASTER_RADICALS)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_MASTER_KANJI)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_MASTER_VOCABULARY)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_RADICALS)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_KANJI)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_ENLIGHTENED_VOCABULARY)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_BURNED_RADICALS)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_BURNED_KANJI)),
                    c.getInt(c.getColumnIndexOrThrow(SRSDistributionTable.COLUMN_NAME_BURNED_VOCABULARY))
            );
        } else {
            Log.e(TAG, "No srs distribution found; returning null");
            return null;
        }
    }

    public void deleteSrsDistribution() {
        db.delete(SRSDistributionTable.TABLE_NAME, null, null);
    }

    private void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(UsersTable.COLUMN_NAME_USERNAME, user.getUsername());
        values.put(UsersTable.COLUMN_NAME_GRAVATAR, user.getGravatar());
        values.put(UsersTable.COLUMN_NAME_LEVEL, user.getLevel());
        values.put(UsersTable.COLUMN_NAME_TITLE, user.getTitle());
        values.put(UsersTable.COLUMN_NAME_ABOUT, user.getAbout());
        values.put(UsersTable.COLUMN_NAME_WEBSITE, user.getWebsite());
        values.put(UsersTable.COLUMN_NAME_TWITTER, user.getTwitter());
        values.put(UsersTable.COLUMN_NAME_TOPICS_COUNT, user.getTopicsCount());
        values.put(UsersTable.COLUMN_NAME_POSTS_COUNT, user.getPostsCount());
        values.put(UsersTable.COLUMN_NAME_CREATION_DATE, user.getCreationDateInSeconds());
        values.put(UsersTable.COLUMN_NAME_VACATION_DATE, user.getVacationDateInSeconds());
        db.insert(UsersTable.TABLE_NAME, UsersTable.COLUMN_NAME_NULLABLE, values);
    }

    public void saveUser(User user) {
        deleteUsers();

        addUser(user);
    }

    public User getUser() {
        Cursor c = db.query(
                UsersTable.TABLE_NAME,
                UsersTable.COLUMNS,
                null,
                null,
                null,
                null,
                null
        );

        if (c != null && c.moveToFirst()) {
            return new User(
                    c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_USERNAME)),
                    c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_GRAVATAR)),
                    c.getInt(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_LEVEL)),
                    c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_TITLE)),
                    c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_ABOUT)),
                    c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_WEBSITE)),
                    c.getString(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_TWITTER)),
                    c.getInt(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_TOPICS_COUNT)),
                    c.getInt(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_POSTS_COUNT)),
                    c.getLong(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_CREATION_DATE)),
                    c.getLong(c.getColumnIndexOrThrow(UsersTable.COLUMN_NAME_VACATION_DATE))
            );
        } else {
            Log.e(TAG, "No users found; returning null");
            return null;
        }
    }

    public void deleteUsers() {
        db.delete(UsersTable.TABLE_NAME, null, null);
    }
}