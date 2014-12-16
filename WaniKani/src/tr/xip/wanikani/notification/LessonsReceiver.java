package tr.xip.wanikani.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tr.xip.wanikani.managers.PrefManager;

/**
 * This receiver is called on every screen unlock. It checks whether a specific amount of time has
 * passed since the last time a notification was shown and calls NotificationPublisher to display
 * a new notification if any lessons are available.
 * <p/>
 * This is done as reminder to the user that they still have lessons waiting for them. Otherwise,
 * this isn't really necessary as new lessons never come up by themselves. It is only when a user
 * gets done with their reviews that a new lesson may become available. And we are already watching
 * out for new reviews with NotificationReceiver.
 */
public class LessonsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationPreferences prefs = new NotificationPreferences(context);
        PrefManager prefManager = new PrefManager(context);

        if (prefManager.notificationsEnabled())
            if ((System.currentTimeMillis() - prefs.getLastLessonsShown()) > prefManager.getPendingLessonsReminderInterval())
                context.sendBroadcast(new Intent(NotificationPreferences.BROADCAST_SHOWN_NOTIFICATIONS));
    }
}
