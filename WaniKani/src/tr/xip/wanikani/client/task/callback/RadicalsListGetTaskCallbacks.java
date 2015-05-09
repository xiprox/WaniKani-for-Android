package tr.xip.wanikani.client.task.callback;

import java.util.List;

import tr.xip.wanikani.models.BaseItem;

/**
 * Created by Hikari on 1/3/15.
 */
public interface RadicalsListGetTaskCallbacks {
    public void onRadicalsListGetTaskPreExecute();

    public void onRadicalsListGetTaskPostExecute(List<BaseItem> list);
}
