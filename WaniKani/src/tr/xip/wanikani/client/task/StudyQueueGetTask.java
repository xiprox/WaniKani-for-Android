package tr.xip.wanikani.client.task;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.database.task.StudyQueueLoadTask;
import tr.xip.wanikani.database.task.StudyQueueSaveTask;
import tr.xip.wanikani.database.task.callback.StudyQueueLoadTaskCallbacks;
import tr.xip.wanikani.client.task.callback.StudyQueueGetTaskCallbacks;

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

        if (queue != null) {
            new StudyQueueSaveTask(context, queue, null).executeParallel();

            if (mCallbacks != null)
                mCallbacks.onStudyQueueGetTaskPostExecute(queue);
        } else
            try {
                new StudyQueueLoadTask(context, new StudyQueueLoadTaskCallbacks() {
                    @Override
                    public void onStudyQueueLoaded(StudyQueue queue) {
                        if (mCallbacks != null)
                            mCallbacks.onStudyQueueGetTaskPostExecute(queue);
                    }
                }).executeParallel();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
