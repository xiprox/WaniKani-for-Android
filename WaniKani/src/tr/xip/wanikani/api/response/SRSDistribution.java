package tr.xip.wanikani.api.response;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class SRSDistribution {
    public UserInfo user_information;
    public RequestedInformation requested_information;

    public class RequestedInformation {
        public Level apprentice;
        public Level guru;
        public Level master;
        public Level enlighten;
        public Level burned;

        public class Level {
            public int radicals;
            public int kanji;
            public int vocabulary;
            public int total;
        }
    }
}
