package tr.xip.wanikani.client.task.callback;

import tr.xip.wanikani.models.StudyQueue;

/**
 * Created by Hikari on 1/3/15.
 */
public interface StudyQueueGetTaskCallbacks {
    public void onStudyQueueGetTaskPreExecute();

    public void onStudyQueueGetTaskPostExecute(StudyQueue queue);
}
