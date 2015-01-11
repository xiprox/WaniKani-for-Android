package tr.xip.wanikani.db.tasks;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.api.response.SRSDistribution;
import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.db.DatabaseManager;
import tr.xip.wanikani.db.tasks.callbacks.SRSDistributionSaveTaskCallbacks;
import tr.xip.wanikani.db.tasks.callbacks.UserSaveTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class SrsDistributionSaveTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    private SRSDistribution distribution;

    private SRSDistributionSaveTaskCallbacks mCallbacks;

    public SrsDistributionSaveTask(Context context, SRSDistribution distribution, SRSDistributionSaveTaskCallbacks callbacks) {
        this.context = context;
        this.distribution = distribution;
        this.mCallbacks = callbacks;
    }

    public void executeSerial() {
        execute();
    }

    public void executeParallel() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected Void doInBackground(Void... params) {
        new DatabaseManager(context).saveSrsDistribution(distribution);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (mCallbacks != null)
            mCallbacks.onSrsDistributionSaveTaskFinished();
    }
}