package tr.xip.wanikani.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.RadicalItem;
import tr.xip.wanikani.tasks.callbacks.RadicalsListGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class RadicalsListGetTask extends AsyncTask<Void, Void, List<RadicalItem>> {

    private Context context;

    private RadicalsListGetTaskCallbacks mCallbacks;

    private String level;

    public RadicalsListGetTask(Context context, String level, RadicalsListGetTaskCallbacks callbacks) {
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
            mCallbacks.onRadicalsListGetTaskPreExecute();
    }

    @Override
    protected List<RadicalItem> doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getRadicalsList(level);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<RadicalItem> list) {
        super.onPostExecute(list);
/*
        if (list != null)
            // TODO: Save to database
        else
            list = // TODO: Get from database
*/

        if (mCallbacks != null)
            mCallbacks.onRadicalsListGetTaskPostExecute(list);
    }
}
