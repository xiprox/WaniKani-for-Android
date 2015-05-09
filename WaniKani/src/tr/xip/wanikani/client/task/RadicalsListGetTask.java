package tr.xip.wanikani.client.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.task.ItemsLoadTask;
import tr.xip.wanikani.database.task.ItemsSaveTask;
import tr.xip.wanikani.database.task.callback.ItemsLoadTaskCallbacks;
import tr.xip.wanikani.client.task.callback.RadicalsListGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class RadicalsListGetTask extends AsyncTask<Void, Void, List<BaseItem>> {

    private Context context;

    private DatabaseManager db;

    private RadicalsListGetTaskCallbacks mCallbacks;

    private String level;

    public RadicalsListGetTask(Context context, String level, RadicalsListGetTaskCallbacks callbacks) {
        this.context = context;
        this.level = level;
        this.mCallbacks = callbacks;

        this.db = new DatabaseManager(context);
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
            mCallbacks.onRadicalsListGetTaskPreExecute();
    }

    @Override
    protected List<BaseItem> doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getRadicalsList(level);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(final List<BaseItem> list) {
        super.onPostExecute(list);

        if (list != null) {
            new ItemsSaveTask(context, BaseItem.ItemType.RADICAL, list, null).executeParallel();

            if (mCallbacks != null)
                mCallbacks.onRadicalsListGetTaskPostExecute(list);
        } else
            try {
                String[] levelStrings = level.split(",");
                int[] levels = new int[levelStrings.length];
                for (int i = 0; i < levelStrings.length; i++)
                    levels[i] = Integer.parseInt(levelStrings[i]);

                new ItemsLoadTask(context, BaseItem.ItemType.RADICAL, levels, new ItemsLoadTaskCallbacks() {
                    @Override
                    public void onItemsLoaded(List<BaseItem> items) {
                        if (mCallbacks != null)
                            mCallbacks.onRadicalsListGetTaskPostExecute(items);
                    }
                }).executeParallel();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}