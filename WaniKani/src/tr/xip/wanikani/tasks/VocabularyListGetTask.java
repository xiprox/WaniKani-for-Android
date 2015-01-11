package tr.xip.wanikani.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.BaseItem;
import tr.xip.wanikani.db.tasks.ItemsLoadTask;
import tr.xip.wanikani.db.tasks.ItemsSaveTask;
import tr.xip.wanikani.db.tasks.callbacks.ItemsLoadTaskCallbacks;
import tr.xip.wanikani.tasks.callbacks.VocabularyListGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class VocabularyListGetTask extends AsyncTask<Void, Void, List<BaseItem>> {

    private Context context;

    private VocabularyListGetTaskCallbacks mCallbacks;

    private String level;

    public VocabularyListGetTask(Context context, String level, VocabularyListGetTaskCallbacks callbacks) {
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
            mCallbacks.onVocabularyListGetTaskPreExecute();
    }

    @Override
    protected List<BaseItem> doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getVocabularyList(level);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<BaseItem> list) {
        super.onPostExecute(list);

        if (list != null) {
            new ItemsSaveTask(context, BaseItem.ItemType.VOCABULARY, list, null).executeParallel();

            if (mCallbacks != null)
                mCallbacks.onVocabularyListGetTaskPostExecute(list);
        } else
            try {
                String[] levelStrings = level.split(",");
                int[] levels = new int[levelStrings.length];
                for (int i = 0; i < levelStrings.length; i++)
                    levels[i] = Integer.parseInt(levelStrings[i]);

                new ItemsLoadTask(context, BaseItem.ItemType.VOCABULARY, levels, new ItemsLoadTaskCallbacks() {
                    @Override
                    public void onItemsLoaded(List<BaseItem> items) {
                        if (mCallbacks != null)
                            mCallbacks.onVocabularyListGetTaskPostExecute(items);
                    }
                }).executeParallel();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
