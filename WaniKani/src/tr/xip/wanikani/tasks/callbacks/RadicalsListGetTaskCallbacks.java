package tr.xip.wanikani.tasks.callbacks;

import java.util.List;

import tr.xip.wanikani.api.response.BaseItem;

/**
 * Created by Hikari on 1/3/15.
 */
public interface RadicalsListGetTaskCallbacks {
    public void onRadicalsListGetTaskPreExecute();

    public void onRadicalsListGetTaskPostExecute(List<BaseItem> list);
}
