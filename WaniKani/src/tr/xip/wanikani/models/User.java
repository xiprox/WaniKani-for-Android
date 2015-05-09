package tr.xip.wanikani.models;

import java.io.Serializable;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class User implements Serializable {
    public UserInfo user_information;

    public User(String username, String gravatar, int level, String title, String about,
                String website, String twitter, int topicsCount, int postsCount, long creationDate,
                long vacationDate) {
        this.user_information = new UserInfo(
                username,
                gravatar,
                level,
                title,
                about,
                website,
                twitter,
                topicsCount,
                postsCount,
                creationDate,
                vacationDate
        );
    }

    public UserInfo getUserInformation() {
        return user_information;
    }

    public String getUsername() {
        return user_information != null ? user_information.getUsername() : null;
    }

    public String getGravatar() {
        return user_information != null ? user_information.getGravatar() : null;
    }

    public int getLevel() {
        return user_information != null ? user_information.getLevel() : 0;
    }

    public String getTitle() {
        return user_information != null ? user_information.getTitle() : null;
    }

    public String getAbout() {
        return user_information != null ? user_information.getAbout() : null;
    }

    public String getWebsite() {
        return user_information != null ? user_information.getWebsite() : null;
    }

    public String getTwitter() {
        return user_information != null ? user_information.getTwitter() : null;
    }

    public int getTopicsCount() {
        return user_information != null ? user_information.getTopicsCount() : 0;
    }

    public int getPostsCount() {
        return user_information != null ? user_information.getPostsCount() : 0;
    }

    public long getCreationDate() {
        return user_information != null ? user_information.getCreationDate() : 0;
    }

    public long getCreationDateInSeconds() {
        return user_information != null ? user_information.getCreationDateInSeconds() : 0;
    }

    public long getVacationDate() {
        return user_information != null ? user_information.getVacationDate() : 0;
    }

    public long getVacationDateInSeconds() {
        return user_information != null ? user_information.getVacationDateInSeconds() : 0;
    }

    public boolean isVacationModeActive() {
        return user_information != null ? user_information.isVacationModeActive() : false;
    }
}
