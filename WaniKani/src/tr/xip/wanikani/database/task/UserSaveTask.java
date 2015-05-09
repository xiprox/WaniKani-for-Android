package tr.xip.wanikani.database.task;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.models.User;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.task.callback.UserSaveTaskCallbacks;

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