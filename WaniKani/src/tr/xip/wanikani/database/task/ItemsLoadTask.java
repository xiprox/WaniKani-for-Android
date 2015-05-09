package tr.xip.wanikani.database.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.task.callback.ItemsLoadTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class ItemsLoadTask extends AsyncTask<Void, Void, List<BaseItem>> {

    private Context context;

    private int[] levels;

    private BaseItem.ItemType type;

    private ItemsLoadTaskCallbacks mCallbacks;

    public ItemsLoadTask(Context context, BaseItem.ItemType type, int[] levels, ItemsLoadTaskCallbacks callbacks) {
        this.context = context;
        this.type = type;
        this.levels = levels;
        this.mCallbacks = callbacks;
    }

    public void executeSerial() {
        execute();
    }

    public void executeParallel() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected List<BaseItem> doInBackground(Void... params) {
        return new DatabaseManager(context).getItems(type, levels);
    }

    @Override
    protected void onPostExecute(List<BaseItem> list) {
        super.onPostExecute(list);

        if (mCallbacks != null)
            mCallbacks.onItemsLoaded(list);
    }
}
