package tr.xip.wanikani.client.task.callback;

import java.util.List;

import tr.xip.wanikani.models.UnlockItem;

/**
 * Created by Hikari on 1/3/15.
 */
public interface RecentUnlocksListGetTaskCallbacks {
    public void onRecentUnlocksListGetTaskPreExecute();

    public void onRecentUnlocksListGetTaskPostExecute(List<UnlockItem> list);
}
