package tr.xip.wanikani.models;

import java.io.Serializable;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class StudyQueue implements Serializable {

    private int id;

    private UserInfo user_information;
    private RequestedInformation requested_information;

    public StudyQueue(int id,
                      int lessons,
                      int reviews,
                      int reviewsNextHour,
                      int reviewsNextDay,
                      long nextReviewDate,
                      UserInfo userInfo) {
        this.id = id;
        this.requested_information = new RequestedInformation(
                lessons,
                reviews,
                reviewsNextHour,
                reviewsNextDay,
                nextReviewDate
        );
        this.user_information = userInfo;
    }

    public UserInfo getUserInfo() {
        return user_information;
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

    public long getNextReviewDateInSeconds() {
        return requested_information != null ? requested_information.next_review_date : 0;
    }

    private class RequestedInformation {
        private int lessons_available;
        private int reviews_available;
        private int reviews_available_next_hour;
        private int reviews_available_next_day;
        private long next_review_date;

        public RequestedInformation(int lessons, int reviews, int reviewsNextHour, int reviewsNextDay, long nextReviewDate) {
            this.lessons_available = lessons;
            this.reviews_available = reviews;
            this.reviews_available_next_hour = reviewsNextHour;
            this.reviews_available_next_day = reviewsNextDay;
            this.next_review_date = nextReviewDate;
        }
    }
}
