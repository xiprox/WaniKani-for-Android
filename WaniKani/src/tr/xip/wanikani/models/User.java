package tr.xip.wanikani.models;

import java.io.Serializable;

import tr.xip.wanikani.database.DatabaseManager;

public class User implements Serializable, Storable {
    public String username;
    public String gravatar;
    public int level;
    public String title;
    public String about;
    public String website;
    public String twitter;
    public int topics_count;
    public int posts_count;
    public long creation_date;
    public long vacation_date;

    public User(String username, String gravatar, int level, String title, String about,
                String website, String twitter, int topicsCount, int postsCount, long creationDate,
                long vacationDate) {
        this.username = username;
        this.gravatar = gravatar;
        this.level = level;
        this.title = title;
        this.about = about;
        this.website = website;
        this.twitter = twitter;
        this.topics_count = topicsCount;
        this.posts_count = postsCount;
        this.creation_date = creationDate;
        this.vacation_date = vacationDate;
    }

    public long getCreationDateInMillis() {
        return creation_date * 1000;
    }

    public long getVacationDateInMillis() {
        return vacation_date * 1000;
    }

    public boolean isVacationModeActive() {
        return getVacationDateInMillis() != 0;
    }

    @Override
    public void save() {
        DatabaseManager.saveUser(this);
    }
}