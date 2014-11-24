package tr.xip.wanikani.api.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class RadicalsList implements Serializable {
    private UserInfo user_information;
    private List<RadicalItem> requested_information;
    private Error error;

    public UserInfo getUserInfo() {
        return user_information;
    }

    public List<RadicalItem> getList() {
        return requested_information;
    }

    public Error getError() {
        return error;
    }
}
