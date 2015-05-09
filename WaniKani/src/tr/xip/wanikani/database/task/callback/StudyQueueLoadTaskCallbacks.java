package tr.xip.wanikani.database.task.callback;

import tr.xip.wanikani.models.StudyQueue;

/**
 * Created by Hikari on 1/7/15.
 */
public interface StudyQueueLoadTaskCallbacks {
    public void onStudyQueueLoaded(StudyQueue queue);
}
