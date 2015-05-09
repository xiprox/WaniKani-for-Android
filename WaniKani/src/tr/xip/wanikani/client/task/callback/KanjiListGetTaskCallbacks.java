package tr.xip.wanikani.client.task.callback;

import java.util.List;

import tr.xip.wanikani.models.BaseItem;

/**
 * Created by Hikari on 1/3/15.
 */
public interface KanjiListGetTaskCallbacks {
    public void onKanjiListGetTaskPreExecute();

    public void onKanjiListGetTaskPostExecute(List<BaseItem> list);
}
