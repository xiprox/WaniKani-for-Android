package tr.xip.wanikani.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import tr.xip.wanikani.content.receiver.BroadcastIntents;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.Notification;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Map<String, String> data = remoteMessage.getData();

            int id = Integer.valueOf(data.get(Notification.DATA_NOTIFICATION_ID));
            String title = data.get(Notification.DATA_NOTIFICATION_TITLE);
            String shortText = data.get(Notification.DATA_NOTIFICATION_SHORT_TEXT);
            String text = data.get(Notification.DATA_NOTIFICATION_TEXT);
            String image = data.get(Notification.DATA_NOTIFICATION_IMAGE);
            String actionUrl = data.get(Notification.DATA_NOTIFICATION_ACTION_URL);
            String actionText = data.get(Notification.DATA_NOTIFICATION_ACTION_TEXT);

            DatabaseManager.saveNotification(new Notification(
                    id,
                    title,
                    shortText,
                    text,
                    image,
                    actionUrl,
                    actionText,
                    false
            ));

            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastIntents.NOTIFICATION()));
        }
    }
}
