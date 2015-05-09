package tr.xip.wanikani.database.task;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.task.callback.StudyQueueSaveTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class StudyQueueSaveTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    private StudyQueue queue;

    private StudyQueueSaveTaskCallbacks mCallbacks;

    public StudyQueueSaveTask(Context context, StudyQueue queue, StudyQueueSaveTaskCallbacks callbacks) {
        this.context = context;
        this.mCallbacks = callbacks;
        this.queue = queue;
    }

    public void executeSerial() {
        execute();
    }

    public void executeParallel() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected Void doInBackground(Void... params) {
        new DatabaseManager(context).saveStudyQueue(queue);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (mCallbacks != null)
            mCallbacks.onStudyQueueSaveTaskFinished();
    }
}