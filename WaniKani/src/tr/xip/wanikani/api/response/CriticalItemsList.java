package tr.xip.wanikani.api.response;

import java.util.List;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class CriticalItemsList {
    public UserInfo user_information;
    public List<CriticalItem> requested_information;

    public class CriticalItem {
        public String type;
        public String character;
        public String kana;
        public String meaning;
        public String onyomi;
        public String kunyomi;
        public String important_reading;
        public String image;
        public int level;
        public int percentage;
    }
}
