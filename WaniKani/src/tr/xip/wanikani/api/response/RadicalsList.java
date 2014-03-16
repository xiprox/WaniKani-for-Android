package tr.xip.wanikani.api.response;

import java.util.List;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class RadicalsList {
    public UserInfo user_information;
    public List<RadicalItem> requested_information;
    public Error error;

    public class RadicalItem {
        public String character;
        public String meaning;
        public String image;
        public int level;
        public List<UserSpecific> user_specific;

        public class UserSpecific {
            public String srs;
            public long unlocked_date;
            public long available_date;
            public boolean burned;
            public long burned_date;
            public int meaning_correct;
            public int meaning_incorrect;
            public int meaning_max_streak;
            public int meaning_current_streak;
            public int reading_correct;
            public int reading_incorrect;
            public int reading_max_streak;
            public int reading_current_streak;
            public String meaning_note;
            public String user_synonyms;
        }
    }
}
