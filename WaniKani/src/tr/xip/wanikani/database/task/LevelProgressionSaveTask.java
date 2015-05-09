package tr.xip.wanikani.database.task;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.models.LevelProgression;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.task.callback.LevelProgressionSaveTaskCallbacks;

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