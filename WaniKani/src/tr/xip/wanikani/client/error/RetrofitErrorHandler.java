package tr.xip.wanikani.client.error;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import tr.xip.wanikani.app.App;
import tr.xip.wanikani.content.receiver.BroadcastIntents;

public class RetrofitErrorHandler {
    public static void handleError(Throwable throwable) {

        if (throwable != null) {
            if (throwable.getCause().toString().contains("GaiException")) {
                Intent intent = new Intent(BroadcastIntents.RETROFIT_ERROR_CONNECTION());
                LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
            }
        } else {
            Intent intent = new Intent(BroadcastIntents.RETROFIT_ERROR_UNKNOWN());
            LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
        }
    }
}