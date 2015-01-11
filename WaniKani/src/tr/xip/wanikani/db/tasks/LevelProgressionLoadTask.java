package tr.xip.wanikani.db.tasks;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.api.response.LevelProgression;
import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.db.DatabaseManager;
import tr.xip.wanikani.db.tasks.callbacks.LevelProgressionLoadTaskCallbacks;
import tr.xip.wanikani.db.tasks.callbacks.UserLoadTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class LevelProgressionLoadTask extends AsyncTask<Void, Void, LevelProgression> {

    private Context context;

    private LevelProgressionLoadTaskCallbacks mCallbacks;

    public LevelProgressionLoadTask(Context context, LevelProgressionLoadTaskCallbacks callbacks) {
        this.context = context;
        this.mCallbacks = callbacks;
    }

    public void executeSerial() {
        execute();
    }

    public void executeParallel() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected LevelProgression doInBackground(Void... params) {
        return new DatabaseManager(context).getLevelProgression();
    }

    @Override
    protected void onPostExecute(LevelProgression progress) {
        super.onPostExecute(progress);

        if (mCallbacks != null)
            mCallbacks.onLevelProgressLoaded(progress);
    }
}