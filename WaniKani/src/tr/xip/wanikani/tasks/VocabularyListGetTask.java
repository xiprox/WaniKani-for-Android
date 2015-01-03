package tr.xip.wanikani.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.VocabularyItem;
import tr.xip.wanikani.tasks.callbacks.VocabularyListGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class VocabularyListGetTask extends AsyncTask<Void, Void, List<VocabularyItem>> {

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
    protected List<VocabularyItem> doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getVocabularyList(level);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<VocabularyItem> list) {
        super.onPostExecute(list);
/*
        if (list != null)
            // TODO: Save to database
        else
            list = // TODO: Get from database
*/

        if (mCallbacks != null)
            mCallbacks.onVocabularyListGetTaskPostExecute(list);
    }
}
