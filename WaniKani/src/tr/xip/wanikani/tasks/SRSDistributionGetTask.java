package tr.xip.wanikani.tasks;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.SRSDistribution;
import tr.xip.wanikani.tasks.callbacks.SRSDistributionGetTaskCallbacks;

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
/*
        if (distribution != null)
            // TODO: Save to database
        else
            distribution = // TODO: Get from database
*/

        if (mCallbacks != null)
            mCallbacks.onSRSDistributionGetTaskPostExecute(distribution);
    }
}
