package tr.xip.wanikani;

/**
 * Created by xihsa_000 on 3/16/14.
 */
public class BroadcastIntents {

    public static String SYNC() {
        return "action.SYNC";
    }

    public static String FINISHED_SYNC_AVAILABLE_CARD() {
        return "action.FINISHED_SYNC.AVAILABLE_CARD";
    }

    public static String FINISHED_SYNC_REVIEWS_CARD() {
        return "action.FINISHED_SYNC.REVIEWS_CARD";
    }

    public static String FINISHED_SYNC_STATUS_CARD() {
        return "action.FINISHED_SYNC.STATUS_CARD";
    }

    public static String FINISHED_SYNC_PROGRESS_CARD() {
        return "action.FINISHED_SYNC.PROGRESS_CARD";
    }

    public static String FINISHED_SYNC_RECENT_UNLOCKS_CARD() {
        return "action.FINISHED_SYNC.RECENT_UNLOCKS_CARD";
    }

    public static String FINISHED_SYNC_CRITICAL_ITEMS_CARD() {
        return "action.FINISHED_SYNC.CRITICAL_ITEMS_CARD";
    }

    public static String RETROFIT_ERROR_TIMEOUT() {
        return "error.retrofit.TIMEOUT";
    }

    public static String RETROFIT_ERROR_CONNECTION() {
        return "error.retrofit.CONNECTION";
    }

    public static String RETROFIT_ERROR_UNKNOWN() {
        return "error.retrofit.UNKNOWN";
    }

}
