package tr.xip.wanikani.api.response;

import java.io.Serializable;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class SRSDistribution implements Serializable {
    private UserInfo user_information;
    private RequestedInformation requested_information;

    public UserInfo getUserInfo() {
        return user_information;
    }

    private class RequestedInformation implements Serializable {
        private Level apprentice;
        private Level guru;
        private Level master;
        private Level enlighten;
        private Level burned;

        public class Level implements Serializable {
            private int radicals;
            private int kanji;
            private int vocabulary;
            private int total;

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
}
