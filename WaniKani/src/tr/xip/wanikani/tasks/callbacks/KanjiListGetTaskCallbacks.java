package tr.xip.wanikani.tasks.callbacks;

import java.util.List;

import tr.xip.wanikani.api.response.KanjiItem;
import tr.xip.wanikani.api.response.RadicalItem;

/**
 * Created by Hikari on 1/3/15.
 */
public interface KanjiListGetTaskCallbacks {
    public void onKanjiListGetTaskPreExecute();

    public void onKanjiListGetTaskPostExecute(List<KanjiItem> list);
}
