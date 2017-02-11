package tr.xip.wanikani.client.error;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.lang.reflect.Field;

import tr.xip.wanikani.app.App;
import tr.xip.wanikani.content.receiver.BroadcastIntents;

public class RetrofitErrorHandler {
    public static void handleError(Throwable throwable) {

        if (throwable != null) {
            try {
                // Ghhhhh!
                Field f = Throwable.class.getDeclaredField("cause");
                f.setAccessible(true);
                String message = f.get(throwable).toString();

                if (message.contains("GaiException") || message.contains("UnknownHostException")) {
                    Intent intent = new Intent(BroadcastIntents.RETROFIT_ERROR_CONNECTION());
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
                } else {
                    Intent intent = new Intent(BroadcastIntents.RETROFIT_ERROR_UNKNOWN());
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}