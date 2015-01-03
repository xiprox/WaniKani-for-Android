package tr.xip.wanikani.tasks;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.StudyQueue;
import tr.xip.wanikani.tasks.callbacks.StudyQueueGetTaskCallbacks;

/**
 * Created by Hikari on 1/3/15.
 */
public class StudyQueueGetTask extends AsyncTask<Void, Void, StudyQueue> {

    private Context context;

    private StudyQueueGetTaskCallbacks mCallbacks;

    public StudyQueueGetTask(Context context, StudyQueueGetTaskCallbacks callbacks) {
        this.context = context;
        mCallbacks = callbacks;
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
            mCallbacks.onStudyQueueGetTaskPreExecute();
    }

    @Override
    protected StudyQueue doInBackground(Void... params) {
        try {
            return new WaniKaniApi(context).getStudyQueue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(StudyQueue queue) {
        super.onPostExecute(queue);
/*
        if (queue != null)
            // TODO: Save to database
        else
            queue = // TODO: Get from database
*/

        if (mCallbacks != null)
            mCallbacks.onStudyQueueGetTaskPostExecute(queue);
    }
}
