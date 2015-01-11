package tr.xip.wanikani.db.tasks;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.api.response.LevelProgression;
import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.db.DatabaseManager;
import tr.xip.wanikani.db.tasks.callbacks.LevelProgressionSaveTaskCallbacks;
import tr.xip.wanikani.db.tasks.callbacks.UserSaveTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class LevelProgressionSaveTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    private LevelProgression progression;

    private LevelProgressionSaveTaskCallbacks mCallbacks;

    public LevelProgressionSaveTask(Context context, LevelProgression progression, LevelProgressionSaveTaskCallbacks callbacks) {
        this.context = context;
        this.mCallbacks = callbacks;
        this.progression = progression;
    }

    public void executeSerial() {
        execute();
    }

    public void executeParallel() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected Void doInBackground(Void... params) {
        new DatabaseManager(context).saveLevelProgression(progression);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (mCallbacks != null)
            mCallbacks.onLevelProgressionSaveTaskFinished();
    }
}