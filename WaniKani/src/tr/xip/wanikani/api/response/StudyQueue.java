package tr.xip.wanikani.api.response;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class StudyQueue {
    public UserInfo user_information;
    public RequestedInformation requested_information;

    public class RequestedInformation {
        public int lessons_available;
        public int reviews_available;
        public long next_review_date;
        public int reviews_available_next_hour;
        public int reviews_available_next_day;
    }
}
