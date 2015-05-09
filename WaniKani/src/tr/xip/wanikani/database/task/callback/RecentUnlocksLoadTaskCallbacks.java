package tr.xip.wanikani.database.task.callback;

import java.util.List;

import tr.xip.wanikani.models.UnlockItem;

/**
 * Created by Hikari on 1/7/15.
 */
public interface RecentUnlocksLoadTaskCallbacks {
    public void onRecentUnlocksLoaded(List<UnlockItem> items);
}
