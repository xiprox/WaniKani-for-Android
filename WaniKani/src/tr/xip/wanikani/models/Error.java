package tr.xip.wanikani.models;

import java.io.Serializable;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class Error implements Serializable {
    private String code;
    private String message;

    public String getErrorCode() {
        return code;
    }

    public String getErrorMessage() {
        return message;
    }
}
