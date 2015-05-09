package tr.xip.wanikani.models;

import java.io.Serializable;

/**
 * Created by Hikari on 9/2/14.
 */
public class BaseItem implements Serializable {
    public static final String TYPE_RADICAL = "radical";
    public static final String TYPE_KANJI = "kanji";
    public static final String TYPE_VOCABULARY = "vocabulary";

    int id;

    String character;
    String kana;
    String meaning;
    String image;
    String onyomi;
    String kunyomi;
    String important_reading;
    int level;
    UserSpecific user_specific;

    ItemType typeEnum;

    public BaseItem() {
    }

    public BaseItem(int id, String character, String kana, String meaning, String image, String onyomi,
                    String kunyomi, String importantReading, int level, String type, String srs,
                    long unlockDate, long availableDate, boolean burned, long burnedDate,
                    int meaningCorrect, int meaningIncorrect, int meaningMaxStreak,
                    int meaningCurrentStreak, int readingCorrect, int readingIncorrect,
                    int readingMaxStreak, int readingCurrentStreak, String meaningNote,
                    String[] userSynonyms, String readingNote) {
        this.id = id;
        this.character = character;
        this.kana = kana;
        this.meaning = meaning;
        this.image = image;
        this.onyomi = onyomi;
        this.kunyomi = kunyomi;
        this.important_reading = importantReading;
        this.level = level;
        this.typeEnum = ItemType.fromString(type);

        if (unlockDate != 0)
            this.user_specific = new UserSpecific(
                    srs,
                    unlockDate,
                    availableDate,
                    burned,
                    burnedDate,
                    meaningCorrect,
                    meaningIncorrect,
                    meaningMaxStreak,
                    meaningCurrentStreak,
                    readingCorrect,
                    readingIncorrect,
                    readingMaxStreak,
                    readingCurrentStreak,
                    meaningNote,
                    userSynonyms,
                    readingNote
            );
    }

    public int getId() {
        return id;
    }

    public ItemType getType() {
        return typeEnum;
    }

    public void setType(ItemType type) {
        this.typeEnum = type;
    }

    public String getCharacter() {
        return character;
    }

    public String getKana() {
        return getType() == ItemType.VOCABULARY ? kana : null;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getOnyomi() {
        return getType() == ItemType.KANJI ? onyomi : null;
    }

    public String getKunyomi() {
        return getType() == ItemType.KANJI ? kunyomi : null;
    }

    public String getImportantReading() {
        return getType() == ItemType.KANJI ? important_reading : null;
    }

    public String getImage() {
        return image;
    }

    public int getLevel() {
        return level;
    }

    public boolean isUnlocked() {
        return user_specific != null;
    }

    public String getSrsLevel() {
        return isUnlocked() ? user_specific.srs : null;
    }

    public long getUnlockDate() {
        return isUnlocked() ? user_specific.unlocked_date * 1000 : 0;
    }

    public long getUnlockDateInSeconds() {
        return isUnlocked() ? user_specific.unlocked_date : 0;
    }

    public long getAvailableDate() {
        return isUnlocked() ? user_specific.available_date * 1000 : 0;
    }

    public long getAvailableDateInSeconds() {
        return isUnlocked() ? user_specific.available_date : 0;
    }

    public boolean isBurned() {
        return isUnlocked() ? user_specific.burned : false;
    }

    public long getBurnedDate() {
        return isUnlocked() ? user_specific.burned_date * 1000 : 0;
    }

    public long getBurnedDateInSeconds() {
        return isUnlocked() ? user_specific.burned_date : 0;
    }

    public int getMeaningCorrect() {
        return isUnlocked() ? user_specific.meaning_correct : 0;
    }

    public int getMeaningIncorrect() {
        return isUnlocked() ? user_specific.meaning_incorrect : 0;
    }

    public int getMeaningMaxStreak() {
        return isUnlocked() ? user_specific.meaning_max_streak : 0;
    }

    public int getMeaningCurrentStreak() {
        return isUnlocked() ? user_specific.meaning_current_streak : 0;
    }

    public int getMeaningAnswersCount() {
        return isUnlocked() ? user_specific.meaning_correct + user_specific.meaning_incorrect : 0;
    }

    public int getMeaningCorrectPercentage() {
        return isUnlocked() ? (int) ((double) user_specific.meaning_correct / getMeaningAnswersCount() * 100) : 0;
    }

    public int getMeaningIncorrectPercentage() {
        return isUnlocked() ? (int) ((double) user_specific.meaning_incorrect / getMeaningAnswersCount() * 100) : 0;
    }

    public int getReadingCorrect() {
        return isUnlocked() ? getType() == ItemType.KANJI ? user_specific.reading_correct : 0 : 0;
    }

    public int getReadingIncorrect() {
        return isUnlocked() ? getType() == ItemType.KANJI ? user_specific.reading_incorrect : 0 : 0;
    }

    public int getReadingMaxStreak() {
        return isUnlocked() ? getType() == ItemType.KANJI ? user_specific.reading_max_streak : 0 : 0;
    }

    public int getReadingCurrentStreak() {
        return isUnlocked() ? getType() == ItemType.KANJI ? user_specific.reading_current_streak : 0 : 0;
    }

    public int getReadingAnswersCount() {
        return isUnlocked() ? getType() == ItemType.KANJI ?
                user_specific.reading_correct + user_specific.reading_incorrect : 0 : 0;
    }

    public int getReadingCorrectPercentage() {
        return isUnlocked() ? getType() == ItemType.KANJI ?
                (int) ((double) user_specific.reading_correct / getReadingAnswersCount() * 100) : 0 : 0;
    }

    public int getReadingIncorrectPercentage() {
        return isUnlocked() ? getType() == ItemType.KANJI ?
                (int) ((double) user_specific.reading_incorrect / getReadingAnswersCount() * 100) : 0 : 0;
    }

    public String getMeaningNote() {
        return isUnlocked() ? user_specific.meaning_note : null;
    }

    public String[] getUserSynonyms() {
        return isUnlocked() ? user_specific.user_synonyms : null;
    }

    public String getUserSynonymsAsString() {
        if (getUserSynonyms() == null)
            return null;

        String synonyms = "";
        for (int i = 0; i < getUserSynonyms().length; i++)
            if (i == 0)
                synonyms = getUserSynonyms()[i];
            else
                synonyms += ", " + getUserSynonyms()[i];
        return synonyms;
    }

    public String getReadingNote() {
        return isUnlocked() ? getType() == ItemType.KANJI ? user_specific.reading_note : null : null;
    }

    public enum ItemType implements Serializable {
        RADICAL, KANJI, VOCABULARY;

        public static ItemType fromString(String type) {
            if (type.equals(TYPE_RADICAL))
                return ItemType.RADICAL;
            if (type.equals(TYPE_KANJI))
                return ItemType.KANJI;
            if (type.equals(TYPE_VOCABULARY))
                return ItemType.VOCABULARY;
            else return null;
        }

        public String toString() {
            switch (this) {
                case RADICAL:
                    return TYPE_RADICAL;
                case KANJI:
                    return TYPE_KANJI;
                case VOCABULARY:
                    return TYPE_VOCABULARY;
                default:
                    return null;
            }
        }
    }

    class UserSpecific implements Serializable {
        private String srs;
        private long unlocked_date;
        private long available_date;
        private boolean burned;
        private long burned_date;
        private int meaning_correct;
        private int meaning_incorrect;
        private int meaning_max_streak;
        private int meaning_current_streak;
        private int reading_correct;
        private int reading_incorrect;
        private int reading_max_streak;
        private int reading_current_streak;
        private String meaning_note;
        private String[] user_synonyms;
        private String reading_note;

        public UserSpecific(String srs, long unlockDate, long availableDate, boolean burned, long burnedDate,
                            int meaningCorrect, int meaningIncorrect, int meaningMaxStreak,
                            int meaningCurrentStreak, int readingCorrect, int readingIncorrect,
                            int readingMaxStreak, int readingCurrentStreak, String meaningNote,
                            String[] userSynonyms, String readingNote) {
            this.srs = srs;
            this.unlocked_date = unlockDate;
            this.available_date = availableDate;
            this.burned = burned;
            this.burned_date = burnedDate;
            this.meaning_correct = meaningCorrect;
            this.meaning_incorrect = meaningIncorrect;
            this.meaning_max_streak = meaningMaxStreak;
            this.meaning_current_streak = meaningCurrentStreak;
            this.reading_correct = readingCorrect;
            this.reading_incorrect = readingIncorrect;
            this.reading_max_streak = readingMaxStreak;
            this.reading_current_streak = readingCurrentStreak;
            this.meaning_note = meaningNote;
            this.user_synonyms = userSynonyms;
            this.reading_note = readingNote;
        }
    }
}
