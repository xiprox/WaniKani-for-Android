package tr.xip.wanikani.tasks.callbacks;

import tr.xip.wanikani.api.response.LevelProgression;

/**
 * Created by Hikari on 1/3/15.
 */
public interface LevelProgressionGetTaskCallbacks {
    public void onLevelProgressionGetTaskPreExecute();

    public void onLevelProgressionGetTaskPostExecute(LevelProgression progression);
}
