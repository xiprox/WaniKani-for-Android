package tr.xip.wanikani.notification;

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
    public static final int NOTIFICATION_CHECK_DELAY = 180000; // 3 minutes

    public static final String BROADCAST_SHOWN_NOTIFICATIONS = "tr.xip.wanikani.action.SHOW_NOTIFICATION";
    public static final String BROADCAST_SCHEDULE_NOTIF_ALARM = "tr.xip.wanikani.action.SCHEDULE_NOTIF_ALARM";

    private static final String PREFS_NOTIF = "notifications";

    private static final String PREF_ALARM_SET = "alarm_set";
    private static final String PREF_LAST_LESSONS_SHOWN = "last_lessons_shown";

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

    public long getLastLessonsShown() {
        return prefs.getLong(PREF_LAST_LESSONS_SHOWN,
                System.currentTimeMillis() + prefMan.getPendingLessonsReminderInterval());
    }

    public void saveLastLessonsShown(long lastShown) {
        editor.putLong(PREF_LAST_LESSONS_SHOWN, lastShown).commit();
    }
}