package tr.xip.wanikani.api.response;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class Error {
    private String code;
    private String message;

    public String getErrorCode() {
        return code;
    }

    public String getErrorMessage() {
        return message;
    }
}
