package tr.xip.wanikani.models;

import java.io.Serializable;

public class CriticalItem extends BaseItem implements Serializable {
    private String type;
    private int percentage;

    public CriticalItem(int id, String character, String kana, String meaning, String image, String onyomi,
                      String kunyomi, String importantReading, int level, String type, String srs,
                      long unlockDate, long availableDate, boolean burned, long burnedDate,
                      int meaningCorrect, int meaningIncorrect, int meaningMaxStreak,
                      int meaningCurrentStreak, int readingCorrect, int readingIncorrect,
                      int readingMaxStreak, int readingCurrentStreak, String meaningNote,
                      String[] userSynonyms, String readingNote, int percentage) {
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
        this.type = type;
        this.percentage = percentage;

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

    @Override
    public ItemType getType() {
        return ItemType.fromString(type);
    }

    public int getPercentage() {
        return percentage;
    }
}