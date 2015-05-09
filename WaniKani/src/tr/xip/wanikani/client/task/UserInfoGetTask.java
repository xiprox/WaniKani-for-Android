package tr.xip.wanikani.client.task;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.database.task.UserLoadTask;
import tr.xip.wanikani.database.task.UserSaveTask;
import tr.xip.wanikani.database.task.callback.UserLoadTaskCallbacks;
import tr.xip.wanikani.client.task.callback.UserInfoGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class UserInfoGetTask extends AsyncTask<Void, Void, User> {

    private Context context;

    private UserInfoGetTaskCallbacks mCallbacks;

    public UserInfoGetTask(Context context, UserInfoGetTaskCallbacks callbacks) {
        this.context = context;
        mCallbacks = callbacks;
    }

    public void executeSerial() {
        execute();
    }

    public void executeParallel() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mCallbacks != null)
            mCallbacks.onUserInfoGetTaskPreExecute();
    }

    @Override
    protected User doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getUser();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);

        if (user != null) {
            new UserSaveTask(context, user, null).executeParallell();

            if (mCallbacks != null)
                mCallbacks.onUserInfoGetTaskPostExecute(user);
        } else
            try {
                new UserLoadTask(context, new UserLoadTaskCallbacks() {
                    @Override
                    public void onUserLoaded(User user) {
                        if (mCallbacks != null)
                            mCallbacks.onUserInfoGetTaskPostExecute(user);
                    }
                }).executeParallell();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
