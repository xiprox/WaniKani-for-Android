package tr.xip.wanikani.tasks.callbacks;

import tr.xip.wanikani.api.response.User;

/**
 * Created by Hikari on 1/3/15.
 */
public interface UserInfoGetTaskCallbacks {
    public void onUserInfoGetTaskPreExecute();

    public void onUserInfoGetTaskPostExecute(User user);
}
