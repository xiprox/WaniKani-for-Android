package tr.xip.wanikani.db.tasks.callbacks;

import tr.xip.wanikani.api.response.User;

/**
 * Created by Hikari on 1/7/15.
 */
public interface UserLoadTaskCallbacks {
    public void onUserLoaded(User user);
}
