package tr.xip.wanikani.client.task;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.LevelProgression;
import tr.xip.wanikani.database.task.LevelProgressionLoadTask;
import tr.xip.wanikani.database.task.LevelProgressionSaveTask;
import tr.xip.wanikani.database.task.callback.LevelProgressionLoadTaskCallbacks;
import tr.xip.wanikani.client.task.callback.LevelProgressionGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class LevelProgressionGetTask extends AsyncTask<Void, Void, LevelProgression> {

    private Context context;

    private LevelProgressionGetTaskCallbacks mCallbacks;

    public LevelProgressionGetTask(Context context, LevelProgressionGetTaskCallbacks callbacks) {
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
            mCallbacks.onLevelProgressionGetTaskPreExecute();
    }

    @Override
    protected LevelProgression doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getLevelProgression();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(final LevelProgression progression) {
        super.onPostExecute(progression);

        if (progression != null) {
            new LevelProgressionSaveTask(context, progression, null).executeParallel();

            if (mCallbacks != null)
                mCallbacks.onLevelProgressionGetTaskPostExecute(progression);
        } else
            try {
                new LevelProgressionLoadTask(context, new LevelProgressionLoadTaskCallbacks() {
                    @Override
                    public void onLevelProgressLoaded(LevelProgression progress) {
                        if (mCallbacks != null)
                            mCallbacks.onLevelProgressionGetTaskPostExecute(progress);
                    }
                }).executeParallel();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
