package tr.xip.wanikani.db.tasks.callbacks;

import tr.xip.wanikani.api.response.StudyQueue;

/**
 * Created by Hikari on 1/7/15.
 */
public interface StudyQueueLoadTaskCallbacks {
    public void onStudyQueueLoaded(StudyQueue queue);
}
