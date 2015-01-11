package tr.xip.wanikani.db.tasks.callbacks;

import java.util.List;

import tr.xip.wanikani.api.response.BaseItem;

/**
 * Created by Hikari on 1/7/15.
 */
public interface ItemsLoadTaskCallbacks {
    public void onItemsLoaded(List<BaseItem> items);
}
