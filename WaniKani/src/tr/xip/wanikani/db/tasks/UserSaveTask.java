package tr.xip.wanikani.db.tasks;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.db.DatabaseManager;
import tr.xip.wanikani.db.tasks.callbacks.UserSaveTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class UserSaveTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    private User mUser;

    private UserSaveTaskCallbacks mCallbacks;

    public UserSaveTask(Context context, User user, UserSaveTaskCallbacks callbacks) {
        this.context = context;
        this.mCallbacks = callbacks;
        this.mUser = user;
    }

    public void executeSerial() {
        execute();
    }

    public void executeParallell() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected Void doInBackground(Void... params) {
        new DatabaseManager(context).saveUser(mUser);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (mCallbacks != null)
            mCallbacks.onUserSaveTaskFinished();
    }
}