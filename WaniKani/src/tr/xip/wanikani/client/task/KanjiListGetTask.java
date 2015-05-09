package tr.xip.wanikani.client.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.database.task.ItemsLoadTask;
import tr.xip.wanikani.database.task.ItemsSaveTask;
import tr.xip.wanikani.database.task.callback.ItemsLoadTaskCallbacks;
import tr.xip.wanikani.client.task.callback.KanjiListGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class KanjiListGetTask extends AsyncTask<Void, Void, List<BaseItem>> {

    private Context context;

    private KanjiListGetTaskCallbacks mCallbacks;

    private String level;

    public KanjiListGetTask(Context context, String level, KanjiListGetTaskCallbacks callbacks) {
        this.context = context;
        this.level = level;
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
            mCallbacks.onKanjiListGetTaskPreExecute();
    }

    @Override
    protected List<BaseItem> doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getKanjiList(level);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<BaseItem> list) {
        super.onPostExecute(list);

        if (list != null) {
            new ItemsSaveTask(context, BaseItem.ItemType.KANJI, list, null).executeParallel();

            if (mCallbacks != null)
                mCallbacks.onKanjiListGetTaskPostExecute(list);
        } else
            try {
                String[] levelStrings = level.split(",");
                int[] levels = new int[levelStrings.length];
                for (int i = 0; i < levelStrings.length; i++)
                    levels[i] = Integer.parseInt(levelStrings[i]);

                new ItemsLoadTask(context, BaseItem.ItemType.KANJI, levels, new ItemsLoadTaskCallbacks() {
                    @Override
                    public void onItemsLoaded(List<BaseItem> items) {
                        if (mCallbacks != null)
                            mCallbacks.onKanjiListGetTaskPostExecute(items);
                    }
                }).executeParallel();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
