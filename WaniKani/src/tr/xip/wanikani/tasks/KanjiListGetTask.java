package tr.xip.wanikani.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.KanjiItem;
import tr.xip.wanikani.tasks.callbacks.KanjiListGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class KanjiListGetTask extends AsyncTask<Void, Void, List<KanjiItem>> {

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
    protected List<KanjiItem> doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getKanjiList(level);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<KanjiItem> list) {
        super.onPostExecute(list);
/*
        if (list != null)
            // TODO: Save to database
        else
            list = // TODO: Get from database
*/

        if (mCallbacks != null)
            mCallbacks.onKanjiListGetTaskPostExecute(list);
    }
}
