package tr.xip.wanikani.client.task.callback;

import tr.xip.wanikani.models.LevelProgression;

/**
 * Created by Hikari on 1/3/15.
 */
public interface LevelProgressionGetTaskCallbacks {
    public void onLevelProgressionGetTaskPreExecute();

    public void onLevelProgressionGetTaskPostExecute(LevelProgression progression);
}
