package tr.xip.wanikani.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class KanjiList implements Serializable {
    private UserInfo user_information;
    private List<BaseItem> requested_information;
    private Error error;

    public UserInfo getUserInfo() {
        return user_information;
    }

    public List<BaseItem> getList() {
        for (BaseItem item : requested_information)
            item.setType(BaseItem.ItemType.KANJI);

        return requested_information;
    }

    public Error getError() {
        return error;
    }
}
