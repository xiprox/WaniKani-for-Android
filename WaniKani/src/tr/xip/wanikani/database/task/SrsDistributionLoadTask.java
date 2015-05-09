package tr.xip.wanikani.database.task;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.models.SRSDistribution;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.task.callback.SrsDistributionLoadTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class SrsDistributionLoadTask extends AsyncTask<Void, Void, SRSDistribution> {

    private Context context;

    private SrsDistributionLoadTaskCallbacks mCallbacks;

    public SrsDistributionLoadTask(Context context, SrsDistributionLoadTaskCallbacks callbacks) {
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
    protected SRSDistribution doInBackground(Void... params) {
        return new DatabaseManager(context).getSrsDistribution();
    }

    @Override
    protected void onPostExecute(SRSDistribution distribution) {
        super.onPostExecute(distribution);

        if (mCallbacks != null)
            mCallbacks.onSrsDistributionLoaded(distribution);
    }
}