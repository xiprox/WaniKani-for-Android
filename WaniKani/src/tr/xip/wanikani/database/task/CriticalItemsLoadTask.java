package tr.xip.wanikani.database.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.models.CriticalItem;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.task.callback.CriticalItemsLoadTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class CriticalItemsLoadTask extends AsyncTask<Void, Void, List<CriticalItem>> {

    private Context context;

    private int percentage;

    private CriticalItemsLoadTaskCallbacks mCallbacks;

    public CriticalItemsLoadTask(Context context, int percentage, CriticalItemsLoadTaskCallbacks callbacks) {
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
    protected List<CriticalItem> doInBackground(Void... params) {
        return new DatabaseManager(context).getCriticalItems(percentage);
    }

    @Override
    protected void onPostExecute(List<CriticalItem> list) {
        super.onPostExecute(list);

        if (mCallbacks != null)
            mCallbacks.onCriticalItemsLoaded(list);
    }
}
