package tr.xip.wanikani.api.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class VocabularyList implements Serializable {
    private UserInfo user_information;
    private List<VocabularyItem> requested_information;
    private Error error;

    public UserInfo getUserInfo() {
        return user_information;
    }

    public List<VocabularyItem> getList() {
        return requested_information;
    }

    public Error getError() {
        return error;
    }
}
