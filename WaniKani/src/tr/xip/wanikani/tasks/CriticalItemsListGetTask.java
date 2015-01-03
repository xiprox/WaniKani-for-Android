package tr.xip.wanikani.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.CriticalItem;
import tr.xip.wanikani.tasks.callbacks.CriticalItemsListGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class CriticalItemsListGetTask extends AsyncTask<Void, Void, List<CriticalItem>> {

    private Context context;

    private CriticalItemsListGetTaskCallbacks mCallbacks;

    private int precentage;

    public CriticalItemsListGetTask(Context context, int percentage, CriticalItemsListGetTaskCallbacks callbacks) {
        this.context = context;
        this.precentage = percentage;
        this.mCallbacks = callbacks;
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
            mCallbacks.onCriticalItemsListGetTaskPreExecute();
    }

    @Override
    protected List<CriticalItem> doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getCriticalItemsList(precentage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<CriticalItem> list) {
        super.onPostExecute(list);
/*
        if (list != null)
            // TODO: Save to database
        else
            list = // TODO: Get from database
*/

        if (mCallbacks != null)
            mCallbacks.onCriticalItemsListGetTaskPostExecute(list);
    }
}
