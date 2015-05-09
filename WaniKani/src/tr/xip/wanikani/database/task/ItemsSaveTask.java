package tr.xip.wanikani.database.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.task.callback.ItemsSaveTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class ItemsSaveTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    private List<BaseItem> mList;

    private BaseItem.ItemType type;

    private ItemsSaveTaskCallbacks mCallbacks;

    public ItemsSaveTask(Context context, BaseItem.ItemType type, List<BaseItem> list, ItemsSaveTaskCallbacks callbacks) {
        this.context = context;
        this.type = type;
        this.mCallbacks = callbacks;
        this.mList = list;
    }

    public void executeSerial() {
        execute();
    }

    public void executeParallel() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected Void doInBackground(Void... params) {
        new DatabaseManager(context).saveItems(mList, type);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (mCallbacks != null)
            mCallbacks.onItemsSaveTaskFinished();
    }
}