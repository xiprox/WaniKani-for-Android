package tr.xip.wanikani.client.task;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.SRSDistribution;
import tr.xip.wanikani.database.task.SrsDistributionLoadTask;
import tr.xip.wanikani.database.task.SrsDistributionSaveTask;
import tr.xip.wanikani.database.task.callback.SrsDistributionLoadTaskCallbacks;
import tr.xip.wanikani.client.task.callback.SRSDistributionGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class SRSDistributionGetTask extends AsyncTask<Void, Void, SRSDistribution> {

    private Context context;

    private SRSDistributionGetTaskCallbacks mCallbacks;

    public SRSDistributionGetTask(Context context, SRSDistributionGetTaskCallbacks callbacks) {
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
            mCallbacks.onSRSDistributionGetTaskPreExecute();
    }

    @Override
    protected SRSDistribution doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getSRSDistribution();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(SRSDistribution distribution) {
        super.onPostExecute(distribution);

        if (distribution != null) {
            new SrsDistributionSaveTask(context, distribution, null).executeParallel();

            if (mCallbacks != null)
                mCallbacks.onSRSDistributionGetTaskPostExecute(distribution);
        } else
            try {
                new SrsDistributionLoadTask(context, new SrsDistributionLoadTaskCallbacks() {
                    @Override
                    public void onSrsDistributionLoaded(SRSDistribution distribution) {
                        if (mCallbacks != null)
                            mCallbacks.onSRSDistributionGetTaskPostExecute(distribution);
                    }
                }).executeParallel();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
