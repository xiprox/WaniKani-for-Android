package tr.xip.wanikani.content.notification;

import android.content.Context;
import android.content.SharedPreferences;

import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by Hikari on 12/16/14.
 */
public class NotificationPreferences {
    /**
     * This delay is used to make sure that the StudyQueue returned by the API has been updated by
     * the time we fetch it.
     */
    public static final int NOTIFICATION_CHECK_DELAY = 60000; // 1 minute

    private static final String PREFS_NOTIF = "notifications";

    private static final String PREF_ALARM_SET = "alarm_set";
    private static final String PREF_LAST_NOTIFICATION_SHOWN = "last_notification_shown";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private PrefManager prefMan;

    public NotificationPreferences(Context context) {
        prefs = context.getSharedPreferences(PREFS_NOTIF, 0);
        editor = prefs.edit();

        prefMan = new PrefManager(context);
    }

    public boolean isAlarmSet() {
        return prefs.getBoolean(PREF_ALARM_SET, false);
    }

    public void setAlarmSet(boolean value) {
        editor.putBoolean(PREF_ALARM_SET, value).commit();
    }

    public long getLastNotificationShown() {
        return prefs.getLong(PREF_LAST_NOTIFICATION_SHOWN,
                System.currentTimeMillis() + prefMan.getNotificationReminderInterval());
    }

    public void saveLastNotificationShown(long lastShown) {
        editor.putLong(PREF_LAST_NOTIFICATION_SHOWN, lastShown).commit();
    }

    public boolean shouldShowNotification() {
        return (System.currentTimeMillis() - getLastNotificationShown()) > prefMan.getNotificationReminderInterval();
    }
}