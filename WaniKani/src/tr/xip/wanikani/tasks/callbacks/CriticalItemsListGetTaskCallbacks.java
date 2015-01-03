package tr.xip.wanikani.tasks.callbacks;

import java.util.List;

import tr.xip.wanikani.api.response.CriticalItem;

/**
 * Created by Hikari on 1/3/15.
 */
public interface CriticalItemsListGetTaskCallbacks {
    public void onCriticalItemsListGetTaskPreExecute();

    public void onCriticalItemsListGetTaskPostExecute(List<CriticalItem> list);
}
