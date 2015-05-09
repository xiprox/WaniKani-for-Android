package tr.xip.wanikani.client.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.CriticalItem;
import tr.xip.wanikani.database.task.CriticalItemsLoadTask;
import tr.xip.wanikani.database.task.CriticalItemsSaveTask;
import tr.xip.wanikani.database.task.callback.CriticalItemsLoadTaskCallbacks;
import tr.xip.wanikani.client.task.callback.CriticalItemsListGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class CriticalItemsListGetTask extends AsyncTask<Void, Void, List<CriticalItem>> {

    private Context context;

    private CriticalItemsListGetTaskCallbacks mCallbacks;

    private int percentage;

    public CriticalItemsListGetTask(Context context, int percentage, CriticalItemsListGetTaskCallbacks callbacks) {
        this.context = context;
        this.percentage = percentage;
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
            return new WaniKaniApi(context).getCriticalItemsList(percentage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<CriticalItem> list) {
        super.onPostExecute(list);

        if (list != null) {
            new CriticalItemsSaveTask(context, list, null).executeParallel();

            if (mCallbacks != null)
                mCallbacks.onCriticalItemsListGetTaskPostExecute(list);
        } else
            try {
                new CriticalItemsLoadTask(context, percentage, new CriticalItemsLoadTaskCallbacks() {

                    @Override
                    public void onCriticalItemsLoaded(List<CriticalItem> items) {
                        if (mCallbacks != null)
                            mCallbacks.onCriticalItemsListGetTaskPostExecute(items);
                    }
                }).executeParallel();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
