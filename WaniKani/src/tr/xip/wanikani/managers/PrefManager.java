package tr.xip.wanikani.managers;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import tr.xip.wanikani.ExternalFramePlacer;
import tr.xip.wanikani.app.activity.SWWebReviewActivity;
import tr.xip.wanikani.app.activity.WebReviewActivity;
import tr.xip.wanikani.content.notification.NotificationPublisher;
import tr.xip.wanikani.content.notification.NotificationScheduler;

@SuppressLint("CommitPrefEdits")
public abstract class PrefManager {
    public static final String PREF_API_KEY = "pref_api_key";
    private static final String PREF_DASHBOARD_RECENT_UNLOCKS_NUMBER = "pref_dashboard_recent_unlock_number";
    private static final String PREF_DASHBOARD_CRITICAL_ITEMS_PERCENTAGE = "pref_dashboard_critical_items_percentage";
    private static final String PREF_CRITICAL_ITEMS_NUMBER = "pref_critical_items_number";
    private static final String PREF_USE_CUSTOM_FONTS = "pref_use_custom_fonts";
    private static final String PREF_USE_SPECIFIC_DATES = "pref_use_specific_dates";
    private static final String PREF_REVIEWS_IMPROVEMENTS = "pref_reviews_improvements";
    private static final String PREF_IGNORE_BUTTON = "pref_ignore_button";
    private static final String PREF_SINGLE_BUTTON = "pref_single_button";
    private static final String PREF_PORTRAIT_MODE = "pref_portrait_mode";
    private static final String PREF_WANIKANI_IMPROVE = "pref_wanikani_improve";
    private static final String PREF_REVIEW_ORDER = "pref_review_order";
    private static final String PREF_LESSON_ORDER = "pref_lesson_order";
    private static final String PREF_EXTERNAL_FRAME_PLACER = "pref_eternal_frame_placer";
    private static final String PREF_EXTERNAL_FRAME_PLACER_DICTIONARY = "pref_external_frame_placer_dictionary";
    private static final String PREF_PART_OF_SPEECH = "pref_part_of_speech";
    private static final String PREF_AUTO_POPUP = "pref_auto_popup";
    private static final String PREF_MISTAKE_DELAY = "pref_mistake_delay";
    private static final String PREF_ROMAJI = "pref_romaji";
    private static final String PREF_NO_SUGGESTION = "pref_no_suggestions";
    private static final String PREF_MUTE_BUTTON = "pref_mute_button";
    private static final String PREF_SRS_INDCATION = "pref_srs_indication";
    private static final String PREF_IGNORE_BUTTON_MESSAGE = "pref_ignore_button_message";
    private static final String PREF_HW_ACCEL_MESSAGE = "pref_hw_accel_message";
    private static final String PREF_MUTE = "pref_mute";
    private static final String PREF_HW_ACCEL = "pref_hw_accel";
    private static final String PREF_REVIEWS_LESSONS_FULLSCREEN = "pref_rev_les_fullscreen";
    private static final String PREF_SHOW_NOTIFICATIONS = "pref_show_notifications";
    private static final String PREF_ENABLE_REMINDER_NOTIFICATION = "pref_enable_reminder_notification";
    private static final String PREF_REMINDER_NOTIFICATION_INTERVAL = "pref_reminder_notification_interval";

    private static SharedPreferences prefs;
    private static SharedPreferences reviewsPrefs;

    public static void init(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        reviewsPrefs = context.getSharedPreferences("review-lessons_prefs", Context.MODE_PRIVATE);
    }

    public static String getApiKey() {
        return prefs.getString("api_key", "0");
    }

    public static void setApiKey(String key) {
        prefs.edit().putString("api_key", key).commit();
    }

    public static boolean isFirstLaunch() {
        return prefs.getBoolean("first_launch", true);
    }

    public static void setFirstLaunch(boolean value) {
        prefs.edit().putBoolean("first_launch", value).commit();
    }

    public static boolean isProfileFirstTime() {
        return prefs.getBoolean("first_time_profile", true);
    }

    public static void setProfileFirstTime(boolean value) {
        prefs.edit().putBoolean("first_time_profile", value).commit();
    }

    public static int getDashboardRecentUnlocksNumber() {
        return prefs.getInt(PREF_DASHBOARD_RECENT_UNLOCKS_NUMBER, 5);
    }

    public static void setDashboardRecentUnlocksNumber(int number) {
        prefs.edit().putInt(PREF_DASHBOARD_RECENT_UNLOCKS_NUMBER, number).commit();
    }

    public static int getDashboardCriticalItemsPercentage() {
        return prefs.getInt(PREF_DASHBOARD_CRITICAL_ITEMS_PERCENTAGE, 75);
    }

    public static void setDashboardCriticalItemsPercentage(int number) {
        prefs.edit().putInt(PREF_DASHBOARD_CRITICAL_ITEMS_PERCENTAGE, number).commit();
    }

    public static int getCriticalItemsNumber() {
        return prefs.getInt(PREF_CRITICAL_ITEMS_NUMBER, 5);
    }

    public static void setCriticalItemsNumber(int value) {
        prefs.edit().putInt(PREF_CRITICAL_ITEMS_NUMBER, value).commit();
    }

    public static boolean isLegendLearned() {
        return prefs.getBoolean("pref_legend_learned", false);
    }

    public static void setLegendLearned(boolean value) {
        prefs.edit().putBoolean("pref_legend_learned", value).commit();
    }

    public static void setDashboardLastUpdateDate(long date) {
        prefs.edit().putLong("pref_update_date_dashboard", date).commit();
    }

    public static long getDashboardLastUpdateTime() {
        return prefs.getLong("pref_update_date_dashboard", 0);
    }

    public static boolean isUseCustomFonts() {
        return prefs.getBoolean(PREF_USE_CUSTOM_FONTS, true);
    }

    public static void setUseCUstomFonts(boolean value) {
        prefs.edit().putBoolean(PREF_USE_CUSTOM_FONTS, value).commit();
    }

    public static boolean isUseSpecificDates() {
        return prefs.getBoolean(PREF_USE_SPECIFIC_DATES, false);
    }

    public static void setUseSpecificDates(boolean value) {
        prefs.edit().putBoolean(PREF_USE_SPECIFIC_DATES, value).commit();
    }

    public static boolean getReviewsImprovements() {
        return prefs.getBoolean(PREF_REVIEWS_IMPROVEMENTS, true);
    }

    public static void setReviewsImprovements(boolean value) {
        prefs.edit().putBoolean(PREF_REVIEWS_IMPROVEMENTS, value).commit();
    }

    public static boolean getIgnoreButton() {
        return prefs.getBoolean(PREF_IGNORE_BUTTON, true);
    }

    public static void setIgnoreButton(boolean value) {
        prefs.edit().putBoolean(PREF_IGNORE_BUTTON, value).commit();
    }

    public static boolean getSingleButton() {
        return prefs.getBoolean(PREF_SINGLE_BUTTON, true);
    }

    public static void setSingleButton(boolean value) {
        prefs.edit().putBoolean(PREF_SINGLE_BUTTON, value).commit();
    }

    public static boolean getPortraitMode() {
        return prefs.getBoolean(PREF_PORTRAIT_MODE, true);
    }

    public static void setPortraitMode(boolean value) {
        prefs.edit().putBoolean(PREF_PORTRAIT_MODE, value).commit();
    }

    public static boolean getWaniKaniImprove() {
        return prefs.getBoolean(PREF_WANIKANI_IMPROVE, false);
    }

    public static void setWaniKaniImprove(boolean value) {
        prefs.edit().putBoolean(PREF_WANIKANI_IMPROVE, value).commit();
    }

    public static boolean getReviewOrder() {
        return prefs.getBoolean(PREF_REVIEW_ORDER, false);
    }

    public static void setReviewOrder(boolean value) {
        prefs.edit().putBoolean(PREF_REVIEW_ORDER, value).commit();
    }

    public static boolean getLessonOrder() {
        return prefs.getBoolean(PREF_LESSON_ORDER, false);
    }

    public static void setLessonOrder(boolean value) {
        prefs.edit().putBoolean(PREF_LESSON_ORDER, value).commit();
    }

    public static boolean getExternalFramePlacer() {
        return prefs.getBoolean(PREF_EXTERNAL_FRAME_PLACER, false);
    }

    public static void setExternalFramePlacer(boolean value) {
        prefs.edit().putBoolean(PREF_EXTERNAL_FRAME_PLACER, value).commit();
    }

    public static ExternalFramePlacer.Dictionary getExternalFramePlacerDictionary() {
        ExternalFramePlacer.Dictionary dict;
        String tag = prefs.getString(PREF_EXTERNAL_FRAME_PLACER_DICTIONARY,
                ExternalFramePlacer.Dictionary.JISHO.name());
        dict = ExternalFramePlacer.Dictionary.valueOf(tag);
        dict = ExternalFramePlacer.Dictionary.JISHO;
        return dict;
    }

    public static void setExternalFramePlacerDictionary(String value) {
        prefs.edit().putString(PREF_EXTERNAL_FRAME_PLACER_DICTIONARY, value).commit();
    }

    public static boolean getPartOfSpeech() {
        return prefs.getBoolean(PREF_PART_OF_SPEECH, false); // TODO - Make true after integration
    }

    public static void setPartOfSpeech(boolean value) {
        prefs.edit().putBoolean(PREF_PART_OF_SPEECH, value).commit();
    }

    public static boolean getAutoPopup() {
        return prefs.getBoolean(PREF_AUTO_POPUP, false);
    }

    public static void setAutoPopup(boolean value) {
        prefs.edit().putBoolean(PREF_AUTO_POPUP, value).commit();
    }

    public static boolean getMistakeDelay() {
        return prefs.getBoolean(PREF_MISTAKE_DELAY, false);
    }

    public static void setMistakeDelay(boolean value) {
        prefs.edit().putBoolean(PREF_MISTAKE_DELAY, value).commit();
    }

    public static boolean getRomaji() {
        return prefs.getBoolean(PREF_ROMAJI, false);
    }

    public static void setRomaji(boolean value) {
        prefs.edit().putBoolean(PREF_ROMAJI, value).commit();
    }

    public static boolean getNoSuggestion() {
        return prefs.getBoolean(PREF_NO_SUGGESTION, true);
    }

    public static void setNoSuggestion(boolean value) {
        prefs.edit().putBoolean(PREF_NO_SUGGESTION, value).commit();
    }

    public static boolean getMuteButton() {
        return prefs.getBoolean(PREF_MUTE_BUTTON, true);
    }

    public static void setMuteButton(boolean value) {
        prefs.edit().putBoolean(PREF_MUTE_BUTTON, value).commit();
    }

    public static boolean getSRSIndication() {
        return prefs.getBoolean(PREF_SRS_INDCATION, true);
    }

    public static void setSRSIndication(boolean value) {
        prefs.edit().putBoolean(PREF_SRS_INDCATION, value).commit();
    }

    public static Keyboard getReviewsKeyboard() {
        return getReviewsImprovements() ? Keyboard.LOCAL_IME : Keyboard.NATIVE;
    }

    public static Intent getWebViewIntent(Context context) {
        boolean accel = getHWAccel();
        return new Intent(context, accel ? WebReviewActivity.class : SWWebReviewActivity.class);
    }

    public static boolean getIgnoreButtonMessage() {
        return reviewsPrefs.getBoolean(PREF_IGNORE_BUTTON_MESSAGE, true);
    }

    public static void setIgnoreButtonMessage(boolean value) {
        reviewsPrefs.edit().putBoolean(PREF_IGNORE_BUTTON_MESSAGE, value).commit();
    }

    public static boolean getHWAccelMessage() {
        return reviewsPrefs.getBoolean(PREF_HW_ACCEL_MESSAGE, true);
    }

    public static void setHWAccelMessage(boolean value) {
        reviewsPrefs.edit().putBoolean(PREF_HW_ACCEL_MESSAGE, value).commit();
    }

    public static boolean getHWAccel() {
        return prefs.getBoolean(PREF_HW_ACCEL, true);
    }

    public static void setHWAccel(boolean value) {
        prefs.edit().putBoolean(PREF_HW_ACCEL, value).commit();
    }

    public static boolean toggleMute() {
        boolean mute = !getMute();
        setMute(mute);
        return mute;
    }

    public static boolean getMute() {
        return prefs.getBoolean(PREF_MUTE, false);
    }

    public static void setMute(boolean value) {
        prefs.edit().putBoolean(PREF_MUTE, value).commit();
    }

    public static boolean getReviewsLessonsFullscreen() {
        return prefs.getBoolean(PREF_REVIEWS_LESSONS_FULLSCREEN, false);
    }

    public static void setReviewsLessonsFullscreen(boolean value) {
        prefs.edit().putBoolean(PREF_REVIEWS_LESSONS_FULLSCREEN, value).commit();
    }

    public static void setNotificationsEnabled(Context context, boolean value) {
        prefs.edit().putBoolean(PREF_SHOW_NOTIFICATIONS, value).commit();
        if (!value) {
            new NotificationScheduler(context).cancelNotifications();
        }
    }

    public static boolean notificationsEnabled() {
        return prefs.getBoolean(PREF_SHOW_NOTIFICATIONS, true);
    }

    public static boolean reminderNotificationEnabled() {
        return prefs.getBoolean(PREF_ENABLE_REMINDER_NOTIFICATION, true);
    }

    public static void setReminderNotificationEnabled(boolean value) {
        prefs.edit().putBoolean(PREF_ENABLE_REMINDER_NOTIFICATION, value).commit();
    }

    public static long getReminderNotificationInterval() {
        return prefs.getLong(PREF_REMINDER_NOTIFICATION_INTERVAL, 7200000); // 2 hours by default
    }

    public static void setReminderNotificationInterval(long milliseconds) {
        prefs.edit().putLong(PREF_REMINDER_NOTIFICATION_INTERVAL, milliseconds).commit();
    }

    public static void logout(Context context) {
        prefs.edit().clear().commit();
        reviewsPrefs.edit().clear().commit();

        File offlineData = new File(Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/shared_prefs/offline_data.xml");
        File cacheDir = new File(Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/cache");
        File webviewCacheDir = new File(Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/app_webview");

        try {
            if (offlineData.exists()) {
                offlineData.delete();
            }
            FileUtils.deleteDirectory(cacheDir);
            FileUtils.deleteDirectory(webviewCacheDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cancel the notification alarm...
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                NotificationPublisher.REQUEST_CODE,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public enum Keyboard {
        LOCAL_IME, NATIVE
    }
}
