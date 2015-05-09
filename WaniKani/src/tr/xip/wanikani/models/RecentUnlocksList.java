package tr.xip.wanikani.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class RecentUnlocksList implements Serializable {
    private UserInfo user_information;
    private List<UnlockItem> requested_information;

    public UserInfo getUserInfo() {
        return user_information;
    }

    public List<UnlockItem> getList() {
        return requested_information;
    }
}
