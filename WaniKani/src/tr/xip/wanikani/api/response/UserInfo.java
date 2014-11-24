package tr.xip.wanikani.api.response;

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

    public long getVacationDate() {
        return vacation_date * 1000;
    }
}