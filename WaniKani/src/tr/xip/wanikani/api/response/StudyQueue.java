package tr.xip.wanikani.api.response;

import android.content.Context;

import tr.xip.wanikani.managers.OfflineDataManager;

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

    public int getLessonsAvailable(Context context) {
        new OfflineDataManager(context).setLessonsAvailable(requested_information.lessons_available);
        return requested_information.lessons_available;
    }

    public int getReviewsAvailable(Context context) {
        new OfflineDataManager(context).setReviewsAvailable(requested_information.reviews_available);
        return requested_information.reviews_available;
    }

    public long getNextReviewDate(Context context) {
        new OfflineDataManager(context).setNextReviewDate(requested_information.next_review_date * 1000);
        return requested_information.next_review_date * 1000;
    }

    public int getReviewsAvailableNextHour(Context context) {
        new OfflineDataManager(context).setReviewsAvailableNextHour(requested_information.reviews_available_next_hour);
        return requested_information.reviews_available_next_hour;
    }

    public int getReviewsAvailableNextDay(Context context) {
        new OfflineDataManager(context).setReviewsAvailableNextDay(requested_information.reviews_available_next_hour);
        return requested_information.reviews_available_next_day;
    }
}
