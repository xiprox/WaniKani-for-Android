package tr.xip.wanikani.client.task.callback;

import java.util.List;

import tr.xip.wanikani.models.CriticalItem;

/**
 * Created by Hikari on 1/3/15.
 */
public interface CriticalItemsListGetTaskCallbacks {
    public void onCriticalItemsListGetTaskPreExecute();

    public void onCriticalItemsListGetTaskPostExecute(List<CriticalItem> list);
}
