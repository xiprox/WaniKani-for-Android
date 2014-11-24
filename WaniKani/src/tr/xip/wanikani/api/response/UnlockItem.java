package tr.xip.wanikani.api.response;

import java.io.Serializable;

public class UnlockItem implements Serializable {
    private String type;
    private String character;
    private String kana;
    private String meaning;
    private String onyomi;
    private String kunyomi;
    private String important_reading;
    private String image;
    private int level;
    private long unlocked_date;

    public String getType() {
        return type;
    }

    public String getCharacter() {
        return character;
    }

    public String getKana() {
        return kana;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getOnyomi() {
        return onyomi;
    }

    public String getKunyomi() {
        return kunyomi;
    }

    public String getImportantReading() {
        return important_reading;
    }

    public String getImage() {
        return image;
    }

    public int getLevel() {
        return level;
    }

    public long getUnlockDate() {
        return unlocked_date * 1000;
    }
}