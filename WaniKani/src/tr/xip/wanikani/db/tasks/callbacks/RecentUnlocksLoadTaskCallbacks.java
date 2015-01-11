package tr.xip.wanikani.db.tasks.callbacks;

import java.util.List;

import tr.xip.wanikani.api.response.UnlockItem;

/**
 * Created by Hikari on 1/7/15.
 */
public interface RecentUnlocksLoadTaskCallbacks {
    public void onRecentUnlocksLoaded(List<UnlockItem> items);
}
