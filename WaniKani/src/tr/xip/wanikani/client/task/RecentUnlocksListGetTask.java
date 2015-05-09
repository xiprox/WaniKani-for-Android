package tr.xip.wanikani.client.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.UnlockItem;
import tr.xip.wanikani.database.task.RecentUnlocksLoadTask;
import tr.xip.wanikani.database.task.RecentUnlocksSaveTask;
import tr.xip.wanikani.database.task.callback.RecentUnlocksLoadTaskCallbacks;
import tr.xip.wanikani.client.task.callback.RecentUnlocksListGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class RecentUnlocksListGetTask extends AsyncTask<Void, Void, List<UnlockItem>> {

    private Context context;

    private RecentUnlocksListGetTaskCallbacks mCallbacks;

    private int limit;

    public RecentUnlocksListGetTask(Context context, int limit, RecentUnlocksListGetTaskCallbacks callbacks) {
        this.context = context;
        this.limit = limit;
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
            mCallbacks.onRecentUnlocksListGetTaskPreExecute();
    }

    @Override
    protected List<UnlockItem> doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getRecentUnlocksList(limit);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<UnlockItem> list) {
        super.onPostExecute(list);

        if (list != null) {
            new RecentUnlocksSaveTask(context, list, null).executeParallell();

            if (mCallbacks != null)
                mCallbacks.onRecentUnlocksListGetTaskPostExecute(list);
        } else
            try {
                new RecentUnlocksLoadTask(context, limit, new RecentUnlocksLoadTaskCallbacks() {
                    @Override
                    public void onRecentUnlocksLoaded(List<UnlockItem> items) {
                        if (mCallbacks != null)
                            mCallbacks.onRecentUnlocksListGetTaskPostExecute(items);
                    }
                }).executeParallel();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
