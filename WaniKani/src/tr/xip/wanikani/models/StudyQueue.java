package tr.xip.wanikani.models;

import java.io.Serializable;

import tr.xip.wanikani.database.DatabaseManager;

public class StudyQueue implements Serializable, Storable {
    public int id;
    public int lessons_available;
    public int reviews_available;
    public int reviews_available_next_hour;
    public int reviews_available_next_day;
    public long next_review_date;

    public StudyQueue(int id, int lessons, int reviews, int reviewsNextHour, int reviewsNextDay, long nextReviewDate) {
        this.id = id;
        this.lessons_available = lessons;
        this.reviews_available = reviews;
        this.reviews_available_next_hour = reviewsNextHour;
        this.reviews_available_next_day = reviewsNextDay;
        this.next_review_date = nextReviewDate;
    }

    @Override
    public void save() {
        DatabaseManager.saveStudyQueue(this);
    }
}
