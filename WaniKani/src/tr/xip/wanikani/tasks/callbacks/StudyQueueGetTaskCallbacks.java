package tr.xip.wanikani.tasks.callbacks;

import tr.xip.wanikani.api.response.StudyQueue;

/**
 * Created by Hikari on 1/3/15.
 */
public interface StudyQueueGetTaskCallbacks {
    public void onStudyQueueGetTaskPreExecute();

    public void onStudyQueueGetTaskPostExecute(StudyQueue queue);
}
