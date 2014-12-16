package tr.xip.wanikani.api.response;

import java.io.Serializable;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class StudyQueue implements Serializable {
    private UserInfo user_information;
    private RequestedInformation requested_information;

    public UserInfo getUserInfo() {
        return user_information;
    }

    private class RequestedInformation {
        private int lessons_available;
        private int reviews_available;
        private int reviews_available_next_hour;
        private int reviews_available_next_day;
        private long next_review_date;
    }

    public int getAvailableLesonsCount() {
        return requested_information != null ? requested_information.lessons_available : 0;
    }

    public int getAvailableReviewsCount() {
        return requested_information != null ? requested_information.reviews_available : 0;
    }

    public int getAvailableReviewsNextHourCount() {
        return requested_information != null ? requested_information.reviews_available_next_hour : 0;
    }

    public int getAvailableReviewsNextDayCount() {
        return requested_information != null ? requested_information.reviews_available_next_day : 0;
    }

    public long getNextReviewDate() {
        return requested_information != null ? requested_information.next_review_date * 1000 : 0;
    }
}
