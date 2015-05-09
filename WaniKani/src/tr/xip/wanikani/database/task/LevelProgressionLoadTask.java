package tr.xip.wanikani.database.task;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.models.LevelProgression;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.task.callback.LevelProgressionLoadTaskCallbacks;

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