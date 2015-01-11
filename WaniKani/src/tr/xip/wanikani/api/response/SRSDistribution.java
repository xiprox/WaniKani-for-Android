package tr.xip.wanikani.api.response;

import java.io.Serializable;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class SRSDistribution implements Serializable {

    private int id;

    private UserInfo user_information;
    private RequestedInformation requested_information;

    public SRSDistribution(int id,
                           UserInfo info,
                           int apprenticeRadicals,
                           int apprenticeKanji,
                           int apprenticeVocabulary,
                           int guruRadicals,
                           int guruKanji,
                           int guruVocabulary,
                           int masterRadicals,
                           int masterKanji,
                           int masterVocabulary,
                           int enlightenedRadicals,
                           int enlightenedKanji,
                           int enlightenedVocabulary,
                           int burnedRadicals,
                           int burnedKanji,
                           int burnedVocabulary) {
        this.id = id;
        this.user_information = info;
        this.requested_information = new RequestedInformation(
                apprenticeRadicals,
                apprenticeKanji,
                apprenticeVocabulary,
                guruRadicals,
                guruKanji,
                guruVocabulary,
                masterRadicals,
                masterKanji,
                masterVocabulary,
                enlightenedRadicals,
                enlightenedKanji,
                enlightenedVocabulary,
                burnedRadicals,
                burnedKanji,
                burnedVocabulary
        );
    }

    public UserInfo getUserInfo() {
        return user_information;
    }

    public RequestedInformation.Level getAprentice() {
        return requested_information.apprentice;
    }

    public RequestedInformation.Level getGuru() {
        return requested_information.guru;
    }

    public RequestedInformation.Level getMaster() {
        return requested_information.master;
    }

    public RequestedInformation.Level getEnlighten() {
        return requested_information.enlighten;
    }

    public RequestedInformation.Level getBurned() {
        return requested_information.burned;
    }

    public int getTotal() {
        return getAprentice().getTotalCount()
                + getGuru().getTotalCount()
                + getMaster().getTotalCount()
                + getEnlighten().getTotalCount()
                + getBurned().getTotalCount();
    }

    private class RequestedInformation implements Serializable {
        private Level apprentice;
        private Level guru;
        private Level master;
        private Level enlighten;
        private Level burned;

        public RequestedInformation(int apprenticeRadicals,
                                    int apprenticeKanji,
                                    int apprenticeVocabulary,
                                    int guruRadicals,
                                    int guruKanji,
                                    int guruVocabulary,
                                    int masterRadicals,
                                    int masterKanji,
                                    int masterVocabulary,
                                    int enlightenedRadicals,
                                    int enlightenedKanji,
                                    int enlightenedVocabulary,
                                    int burnedRadicals,
                                    int burnedKanji,
                                    int burnedVocabulary) {
            this.apprentice = new Level(apprenticeRadicals, apprenticeKanji, apprenticeVocabulary);
            this.guru = new Level(guruRadicals, guruKanji, guruVocabulary);
            this.master = new Level(masterRadicals, masterKanji, masterVocabulary);
            this.enlighten = new Level(enlightenedRadicals, enlightenedKanji, enlightenedVocabulary);
            this.burned = new Level(burnedRadicals, burnedKanji, burnedVocabulary);
        }

        public class Level implements Serializable {
            private int radicals;
            private int kanji;
            private int vocabulary;
            private int total;

            public Level(int radicals, int kanji, int vocabulary) {
                this.radicals = radicals;
                this.kanji = kanji;
                this.vocabulary = vocabulary;
                this.total = radicals + kanji + vocabulary;
            }

            public int getRadicalsCount() {
                return radicals;
            }

            public int getKanjiCount() {
                return kanji;
            }

            public int getVocabularyCount() {
                return vocabulary;
            }

            public int getTotalCount() {
                return total;
            }
        }
    }
}
