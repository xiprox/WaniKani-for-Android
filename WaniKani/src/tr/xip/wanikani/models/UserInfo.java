package tr.xip.wanikani.models;

import java.io.Serializable;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class UserInfo implements Serializable {
    private String username;
    private String gravatar;
    private int level;
    private String title;
    private String about;
    private String website;
    private String twitter;
    private int topics_count;
    private int posts_count;
    private long creation_date;
    private long vacation_date;

    public UserInfo(String username, String gravatar, int level, String title, String about,
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

    public String getUsername() {
        return username;
    }

    public String getGravatar() {
        return gravatar;
    }

    public int getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public String getAbout() {
        return about;
    }

    public String getWebsite() {
        return website;
    }

    public String getTwitter() {
        return twitter;
    }

    public int getTopicsCount() {
        return topics_count;
    }

    public int getPostsCount() {
        return posts_count;
    }

    public long getCreationDate() {
        return creation_date * 1000;
    }

    public long getCreationDateInSeconds() {
        return creation_date;
    }

    public long getVacationDate() {
        return vacation_date * 1000;
    }

    public long getVacationDateInSeconds() {
        return vacation_date;
    }

    public boolean isVacationModeActive() {
        return getVacationDate() != 0;
    }
}