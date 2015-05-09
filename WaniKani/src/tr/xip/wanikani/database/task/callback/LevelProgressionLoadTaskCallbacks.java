package tr.xip.wanikani.database.task.callback;

import tr.xip.wanikani.models.LevelProgression;

/**
 * Created by Hikari on 1/7/15.
 */
public interface LevelProgressionLoadTaskCallbacks {
    public void onLevelProgressLoaded(LevelProgression progress);
}
