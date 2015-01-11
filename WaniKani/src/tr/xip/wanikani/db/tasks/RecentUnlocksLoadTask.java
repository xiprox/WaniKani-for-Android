package tr.xip.wanikani.db.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.api.response.BaseItem;
import tr.xip.wanikani.api.response.UnlockItem;
import tr.xip.wanikani.db.DatabaseManager;
import tr.xip.wanikani.db.tasks.callbacks.ItemsLoadTaskCallbacks;
import tr.xip.wanikani.db.tasks.callbacks.RecentUnlocksLoadTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class RecentUnlocksLoadTask extends AsyncTask<Void, Void, List<UnlockItem>> {

    private Context context;

    private int limit;

    private RecentUnlocksLoadTaskCallbacks mCallbacks;

    public RecentUnlocksLoadTask(Context context, int limit, RecentUnlocksLoadTaskCallbacks callbacks) {
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
    protected List<UnlockItem> doInBackground(Void... params) {
        return new DatabaseManager(context).getRecentUnlocks(limit);
    }

    @Override
    protected void onPostExecute(List<UnlockItem> list) {
        super.onPostExecute(list);

        if (mCallbacks != null)
            mCallbacks.onRecentUnlocksLoaded(list);
    }
}
