package tr.xip.wanikani.database.task;

import android.content.Context;
import android.os.AsyncTask;

import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.task.callback.StudyQueueLoadTaskCallbacks;

/**
 * Created by Hikari on 1/7/15.
 */
public class StudyQueueLoadTask extends AsyncTask<Void, Void, StudyQueue> {

    private Context context;

    private StudyQueueLoadTaskCallbacks mCallbacks;

    public StudyQueueLoadTask(Context context, StudyQueueLoadTaskCallbacks callbacks) {
        this.context = context;
        this.mCallbacks = callbacks;
    }

    public void executeSerial() {
        execute();
    }

    public void executeParallel() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected StudyQueue doInBackground(Void... params) {
        return new DatabaseManager(context).getStudyQueue();
    }

    @Override
    protected void onPostExecute(StudyQueue queue) {
        super.onPostExecute(queue);

        if (mCallbacks != null)
            mCallbacks.onStudyQueueLoaded(queue);
    }
}