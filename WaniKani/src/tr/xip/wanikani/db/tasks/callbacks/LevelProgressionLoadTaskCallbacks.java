package tr.xip.wanikani.db.tasks.callbacks;

import tr.xip.wanikani.api.response.LevelProgression;

/**
 * Created by Hikari on 1/7/15.
 */
public interface LevelProgressionLoadTaskCallbacks {
    public void onLevelProgressLoaded(LevelProgression progress);
}
