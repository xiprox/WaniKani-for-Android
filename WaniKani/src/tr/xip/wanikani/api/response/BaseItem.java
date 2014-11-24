package tr.xip.wanikani.api.response;

import java.io.Serializable;

/**
 * Created by Hikari on 9/2/14.
 */
public class BaseItem implements Serializable {
    private String character;
    private String kana;
    private String meaning;
    private String image;
    private String onyomi;
    private String kunyomi;
    private String important_reading;
    private int level;
    private UserSpecific user_specific;
    private ItemType type;

    public BaseItem() {

    }

    public BaseItem(ItemType type) {
        switch (type) {
            case RADICAL:
                setType(ItemType.RADICAL);
                break;
            case KANJI:
                setType(ItemType.KANJI);
                break;
            case VOCABULARY:
                setType(ItemType.VOCABULARY);
                break;
        }
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
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
        return user_specific.srs;
    }

    public long getUnlockDate() {
        return user_specific.unlocked_date * 1000;
    }

    public long getAvailableDate() {
        return user_specific.available_date * 1000;
    }

    public boolean isBurned() {
        return user_specific.burned;
    }

    public long getBurnedDate() {
        return user_specific.burned_date * 1000;
    }

    public int getMeaningCorrect() {
        return user_specific.meaning_correct;
    }

    public int getMeaningIncorrect() {
        return user_specific.meaning_incorrect;
    }

    public int getMeaningMaxStreak() {
        return user_specific.meaning_max_streak;
    }

    public int getMeaningCurrentStreak() {
        return user_specific.meaning_current_streak;
    }

    public int getMeaningAnswersCount() {
        return user_specific.meaning_correct + user_specific.meaning_incorrect;
    }

    public int getMeaningCorrectPercentage() {
        return (int) ((double) user_specific.meaning_correct / getMeaningAnswersCount() * 100);
    }

    public int getMeaningIncorrectPercentage() {
        return (int) ((double) user_specific.meaning_incorrect / getMeaningAnswersCount() * 100);
    }

    public int getReadingCorrect() {
        return getType() == ItemType.KANJI ? user_specific.reading_correct : 0;
    }

    public int getReadingIncorrect() {
        return getType() == ItemType.KANJI ? user_specific.reading_incorrect : 0;
    }

    public int getReadingMaxStreak() {
        return getType() == ItemType.KANJI ? user_specific.reading_max_streak : 0;
    }

    public int getReadingCurrentStreak() {
        return getType() == ItemType.KANJI ? user_specific.reading_current_streak : 0;
    }

    public int getReadingAnswersCount() {
        return getType() == ItemType.KANJI ?
                user_specific.reading_correct + user_specific.reading_incorrect : 0;
    }

    public int getReadingCorrectPercentage() {
        return getType() == ItemType.KANJI ?
                (int) ((double) user_specific.reading_correct / getReadingAnswersCount() * 100) : 0;
    }

    public int getReadingIncorrectPercentage() {
        return getType() == ItemType.KANJI ?
                (int) ((double) user_specific.reading_incorrect / getReadingAnswersCount() * 100) : 0;
    }

    public String getMeaningNote() {
        return user_specific.meaning_note;
    }

    public String[] getUserSynonyms() {
        return user_specific.user_synonyms;
    }

    public String getReadingNote() {
        return getType() == ItemType.KANJI ? user_specific.reading_note : null;
    }

    public enum ItemType {
        RADICAL, KANJI, VOCABULARY
    }

    private class UserSpecific {
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
    }
}
