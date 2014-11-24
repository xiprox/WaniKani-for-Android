package tr.xip.wanikani.api.response;

import java.io.Serializable;

public class CriticalItem implements Serializable {
    private String type;
    private String character;
    private String kana;
    private String meaning;
    private String onyomi;
    private String kunyomi;
    private String important_reading;
    private String image;
    private int level;
    private int percentage;

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

    public int getPercentage() {
        return percentage;
    }
}