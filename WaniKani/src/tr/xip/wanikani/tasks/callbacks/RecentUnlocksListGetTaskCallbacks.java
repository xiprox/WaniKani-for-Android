package tr.xip.wanikani.tasks.callbacks;

import java.util.List;

import tr.xip.wanikani.api.response.UnlockItem;

/**
 * Created by Hikari on 1/3/15.
 */
public interface RecentUnlocksListGetTaskCallbacks {
    public void onRecentUnlocksListGetTaskPreExecute();

    public void onRecentUnlocksListGetTaskPostExecute(List<UnlockItem> list);
}
