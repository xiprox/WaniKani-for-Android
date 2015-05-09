package tr.xip.wanikani.database.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.models.UnlockItem;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.task.callback.RecentUnlocksSaveTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class RecentUnlocksSaveTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    private List<UnlockItem> list;

    private RecentUnlocksSaveTaskCallbacks mCallbacks;

    public RecentUnlocksSaveTask(Context context, List<UnlockItem> list, RecentUnlocksSaveTaskCallbacks callbacks) {
        this.context = context;
        this.mCallbacks = callbacks;
        this.list = list ;
    }

    public void executeSerial() {
        execute();
    }

    public void executeParallell() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected Void doInBackground(Void... params) {
        new DatabaseManager(context).saveRecentUnlocks(list);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (mCallbacks != null)
            mCallbacks.onRecentUnlocksSaveTaskFinished();
    }
}