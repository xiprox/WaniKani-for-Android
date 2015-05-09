package tr.xip.wanikani.database.task.callback;

import tr.xip.wanikani.models.User;

/**
 * Created by Hikari on 1/7/15.
 */
public interface UserLoadTaskCallbacks {
    public void onUserLoaded(User user);
}
