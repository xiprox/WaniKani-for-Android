package tr.xip.wanikani.api.response;

import android.content.Context;
import android.content.SharedPreferences;

import tr.xip.wanikani.managers.OfflineDataManager;

/**
 * Created by xihsa_000 on 3/11/14.
 */

public class User {
    public UserInfo user_information;

    public String getUsername(Context context) {
        new OfflineDataManager(context).setUsername(user_information.username);
        return user_information.username;
    }

    public String getGravatar(Context context) {
        new OfflineDataManager(context).setGravatar(user_information.gravatar);
        return user_information.gravatar;
    }

    public int getLevel(Context context) {
        new OfflineDataManager(context).setLevel(user_information.level);
        return user_information.level;
    }

    public String getTitle(Context context) {
        new OfflineDataManager(context).setTitle(user_information.title);
        return user_information.title;
    }

    public String getAbout(Context context) {
        new OfflineDataManager(context).setAbout(user_information.about);
        return user_information.about;
    }

    public String getWebsite(Context context) {
        new OfflineDataManager(context).setWebsite(user_information.website);
        return user_information.website;
    }

    public String getTwitter(Context context) {
        new OfflineDataManager(context).setTwitter(user_information.twitter);
        return user_information.twitter;
    }

    public int getTopicsCount(Context context) {
        new OfflineDataManager(context).setTopicsCount(user_information.topics_count);
        return user_information.topics_count;
    }

    public int getPostsCount(Context context) {
        new OfflineDataManager(context).setPostsCount(user_information.posts_count);
        return user_information.posts_count;
    }

    public long getCreationDate(Context context) {
        new OfflineDataManager(context).setCreationDate(user_information.creation_date * 1000);
        return user_information.creation_date * 1000;
    }

    public long getVacationDate(Context context) {
        new OfflineDataManager(context).setVacationDate(user_information.vacation_date * 1000);
        return user_information.vacation_date * 1000;
    }
}
