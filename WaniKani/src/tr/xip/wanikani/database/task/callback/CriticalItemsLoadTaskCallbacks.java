package tr.xip.wanikani.database.task.callback;

import java.util.List;

import tr.xip.wanikani.models.CriticalItem;

/**
 * Created by Hikari on 1/7/15.
 */
public interface CriticalItemsLoadTaskCallbacks {
    public void onCriticalItemsLoaded(List<CriticalItem> items);
}
