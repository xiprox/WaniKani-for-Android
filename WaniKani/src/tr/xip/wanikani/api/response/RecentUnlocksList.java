package tr.xip.wanikani.api.response;

import java.util.List;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class RecentUnlocksList {
    public UserInfo user_information;
    public List<UnlockItem> requested_information;

    public class UnlockItem {
        public String type;
        public String character;
        public String kana;
        public String meaning;
        public String onyomi;
        public String kunyomi;
        public String important_reading;
        public String image;
        public int level;
        public long unlocked_date;
    }
}
