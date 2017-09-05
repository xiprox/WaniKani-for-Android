package tr.xip.wanikani.models;

public class WaniKaniItem {

    public enum Type {

        RADICAL,
        KANJI,
        VOCABULARY;

        public static Type fromString(String type) {
            if (type.equals("radical"))
                return RADICAL;
            else if (type.equals("kanji"))
                return KANJI;
            else if (type.equals("vocabulary"))
                return VOCABULARY;

            return null;
        }
    }
}
