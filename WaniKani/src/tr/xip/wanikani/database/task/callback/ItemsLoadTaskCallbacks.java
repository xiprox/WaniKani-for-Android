package tr.xip.wanikani.database.task.callback;

import java.util.List;

import tr.xip.wanikani.models.BaseItem;

/**
 * Created by Hikari on 1/7/15.
 */
public interface ItemsLoadTaskCallbacks {
    public void onItemsLoaded(List<BaseItem> items);
}
