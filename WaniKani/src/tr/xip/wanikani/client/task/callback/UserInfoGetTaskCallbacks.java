package tr.xip.wanikani.client.task.callback;

import tr.xip.wanikani.models.User;

/**
 * Created by Hikari on 1/3/15.
 */
public interface UserInfoGetTaskCallbacks {
    public void onUserInfoGetTaskPreExecute();

    public void onUserInfoGetTaskPostExecute(User user);
}
