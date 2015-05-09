package tr.xip.wanikani.models;

/**
 * Created by Hikari on 8/26/14.
 */
public class WaniKaniItem {

    public static enum Type {

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
