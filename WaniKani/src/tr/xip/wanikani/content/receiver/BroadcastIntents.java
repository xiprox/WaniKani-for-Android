package tr.xip.wanikani.content.receiver;

/**
 * Created by xihsa_000 on 3/16/14.
 */
public class BroadcastIntents {

    public static String SYNC() {
        return "action.SYNC";
    }

    public static String RETROFIT_ERROR_CONNECTION() {
        return "error.retrofit.CONNECTION";
    }

    public static String RETROFIT_ERROR_UNKNOWN() {
        return "error.retrofit.UNKNOWN";
    }

    public static String NOTIFICATION() {
        return "NOTIFICATION";
    }

}
