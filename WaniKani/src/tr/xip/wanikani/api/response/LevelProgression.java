package tr.xip.wanikani.api.response;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class LevelProgression {
    public UserInfo user_information;
    public RequestedInformation requested_information;

    public class RequestedInformation {
        public int radicals_progress;
        public int radicals_total;
        public int kanji_progress;
        public int kanji_total;
    }
}
