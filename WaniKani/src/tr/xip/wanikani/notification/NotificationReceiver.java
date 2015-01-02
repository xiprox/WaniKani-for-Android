package tr.xip.wanikani.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tr.xip.wanikani.managers.OfflineDataManager;
import tr.xip.wanikani.managers.PrefManager;

/**
 * This receiver is called on every screen unlock. It schedules an alarm to set off on the next review
 * time. It also checks whether a specific amount of time has passed since the last time a
 * notification was shown and calls {@link tr.xip.wanikani.notification.NotificationPublisher}
 * to display a new notification if any lessons are available.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        handleSituation(context);
    }

    public void handleSituation(Context context) {
        NotificationPreferences prefs = new NotificationPreferences(context);
        PrefManager prefManager = new PrefManager(context);

        if (prefManager.notificationsEnabled()) {
            /** Schedule an alarm if none is scheduled yet */
            if (!prefs.isAlarmSet() && new OfflineDataManager(context).getReviewsAvailable() == 0)
                new NotificationScheduler(context).schedule();

            /** Show a notification anyways if a given period of time has passed */
            if (prefs.shouldShowNotification())
                new NotificationPublisher().publish(context);
        }
    }
}