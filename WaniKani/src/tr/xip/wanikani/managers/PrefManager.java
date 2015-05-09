package tr.xip.wanikani.managers;

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

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class PrefManager {
    public static final String PREF_API_KEY = "pref_api_key";
    public static final String PREF_DASHBOARD_RECENT_UNLOCKS_NUMBER = "pref_dashboard_recent_unlock_number";
    public static final String PREF_DASHBOARD_CRITICAL_ITEMS_PERCENTAGE = "pref_dashboard_critical_items_percentage";
    public static final String PREF_CRITICAL_ITEMS_NUMBER = "pref_critical_items_number";
    public static final String PREF_USE_CUSTOM_FONTS = "pref_use_custom_fonts";
    public static final String PREF_USE_SPECIFIC_DATES = "pref_use_specific_dates";
    public static final String PREF_REVIEWS_IMPROVEMENTS = "pref_reviews_improvements";
    public static final String PREF_IGNORE_BUTTON = "pref_ignore_button";
    public static final String PREF_SINGLE_BUTTON = "pref_single_button";
    public static final String PREF_PORTRAIT_MODE = "pref_portrait_mode";
    public static final String PREF_WANIKANI_IMPROVE = "pref_wanikani_improve";
    public static final String PREF_REVIEW_ORDER = "pref_review_order";
    public static final String PREF_LESSON_ORDER = "pref_lesson_order";
    public static final String PREF_EXTERNAL_FRAME_PLACER = "pref_eternal_frame_placer";
    public static final String PREF_EXTERNAL_FRAME_PLACER_DICTIONARY = "pref_external_frame_placer_dictionary";
    public static final String PREF_PART_OF_SPEECH = "pref_part_of_speech";
    public static final String PREF_AUTO_POPUP = "pref_auto_popup";
    public static final String PREF_MISTAKE_DELAY = "pref_mistake_delay";
    public static final String PREF_ROMAJI = "pref_romaji";
    public static final String PREF_NO_SUGGESTION = "pref_no_suggestions";
    public static final String PREF_MUTE_BUTTON = "pref_mute_button";
    public static final String PREF_SRS_INDCATION = "pref_srs_indication";
    public static final String PREF_IGNORE_BUTTON_MESSAGE = "pref_ignore_button_message";
    public static final String PREF_HW_ACCEL_MESSAGE = "pref_hw_accel_message";
    public static final String PREF_MUTE = "pref_mute";
    public static final String PREF_HW_ACCEL = "pref_hw_accel";
    public static final String PREF_REVIEWS_LESSONS_FULLSCREEN = "pref_rev_les_fullscreen";
    public static final String PREF_SHOW_NOTIFICATIONS = "pref_show_notifications";
    public static final String PREF_NOTIFICATION_REMINDER_INTERVAL = "pref_lesson_reminder_interval";

    private static Context context;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefeditor;
    private static SharedPreferences reviewsPrefs;
    private static SharedPreferences.Editor reviewsPrefsEditor;

    public PrefManager(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefeditor = prefs.edit();
        reviewsPrefs = context.getSharedPreferences("review-lessons_prefs", Context.MODE_PRIVATE);
        reviewsPrefsEditor = reviewsPrefs.edit();
    }

    public String getApiKey() {
        return prefs.getString("api_key", "0");
    }

    public void setApiKey(String key) {
        prefeditor.putString("api_key", key).commit();
    }

    public boolean isFirstLaunch() {
        return prefs.getBoolean("first_launch", true);
    }

    public void setFirstLaunch(boolean value) {
        prefeditor.putBoolean("first_launch", value).commit();
    }

    public boolean isProfileFirstTime() {
        return prefs.getBoolean("first_time_profile", true);
    }

    public void setProfileFirstTime(boolean value) {
        prefeditor.putBoolean("first_time_profile", value).commit();
    }

    public int getDashboardRecentUnlocksNumber() {
        return prefs.getInt(PREF_DASHBOARD_RECENT_UNLOCKS_NUMBER, 5);
    }

    public void setDashboardRecentUnlocksNumber(int number) {
        prefeditor.putInt(PREF_DASHBOARD_RECENT_UNLOCKS_NUMBER, number).commit();
    }

    public int getDashboardCriticalItemsPercentage() {
        return prefs.getInt(PREF_DASHBOARD_CRITICAL_ITEMS_PERCENTAGE, 75);
    }

    public void setDashboardCriticalItemsPercentage(int number) {
        prefeditor.putInt(PREF_DASHBOARD_CRITICAL_ITEMS_PERCENTAGE, number).commit();
    }

    public int getCriticalItemsNumber() {
        return prefs.getInt(PREF_CRITICAL_ITEMS_NUMBER, 5);
    }

    public void setCriticalItemsNumber(int value) {
        prefeditor.putInt(PREF_CRITICAL_ITEMS_NUMBER, value).commit();
    }

    public boolean isLegendLearned() {
        return PrefManager.prefs.getBoolean("pref_legend_learned", false);
    }

    public void setLegendLearned(boolean value) {
        PrefManager.prefeditor.putBoolean("pref_legend_learned", value).commit();
    }

    public void setDashboardLastUpdateDate(long date) {
        prefeditor.putLong("pref_update_date_dashboard", date).commit();
    }

    public long getDashboardLastUpdateTime() {
        return prefs.getLong("pref_update_date_dashboard", 0);
    }

    public boolean isUseCustomFonts() {
        return prefs.getBoolean(PREF_USE_CUSTOM_FONTS, true);
    }

    public void setUseCUstomFonts(boolean value) {
        prefeditor.putBoolean(PREF_USE_CUSTOM_FONTS, value).commit();
    }

    public boolean isUseSpecificDates() {
        return prefs.getBoolean(PREF_USE_SPECIFIC_DATES, false);
    }

    public void setUseSpecificDates(boolean value) {
        prefeditor.putBoolean(PREF_USE_SPECIFIC_DATES, value).commit();
    }

    public boolean getReviewsImprovements() {
        return prefs.getBoolean(PREF_REVIEWS_IMPROVEMENTS, true);
    }

    public void setReviewsImprovements(boolean value) {
        prefeditor.putBoolean(PREF_REVIEWS_IMPROVEMENTS, value).commit();
    }

    public boolean getIgnoreButton() {
        return prefs.getBoolean(PREF_IGNORE_BUTTON, true);
    }

    public void setIgnoreButton(boolean value) {
        prefeditor.putBoolean(PREF_IGNORE_BUTTON, value).commit();
    }

    public boolean getSingleButton() {
        return prefs.getBoolean(PREF_SINGLE_BUTTON, true);
    }

    public void setSingleButton(boolean value) {
        prefeditor.putBoolean(PREF_SINGLE_BUTTON, value).commit();
    }

    public boolean getPortraitMode() {
        return prefs.getBoolean(PREF_PORTRAIT_MODE, true);
    }

    public void setPortraitMode(boolean value) {
        prefeditor.putBoolean(PREF_PORTRAIT_MODE, value).commit();
    }

    public boolean getWaniKaniImprove() {
        return prefs.getBoolean(PREF_WANIKANI_IMPROVE, false);
    }

    public void setWaniKaniImprove(boolean value) {
        prefeditor.putBoolean(PREF_WANIKANI_IMPROVE, value).commit();
    }

    public boolean getReviewOrder() {
        return prefs.getBoolean(PREF_REVIEW_ORDER, false);
    }

    public void setReviewOrder(boolean value) {
        prefeditor.putBoolean(PREF_REVIEW_ORDER, value).commit();
    }

    public boolean getLessonOrder() {
        return prefs.getBoolean(PREF_LESSON_ORDER, false);
    }

    public void setLessonOrder(boolean value) {
        prefeditor.putBoolean(PREF_LESSON_ORDER, value).commit();
    }

    public boolean getExternalFramePlacer() {
        return prefs.getBoolean(PREF_EXTERNAL_FRAME_PLACER, false);
    }

    public void setExternalFramePlacer(boolean value) {
        prefeditor.putBoolean(PREF_EXTERNAL_FRAME_PLACER, value).commit();
    }

    public ExternalFramePlacer.Dictionary getExternalFramePlacerDictionary() {
        ExternalFramePlacer.Dictionary dict;
        String tag = prefs.getString(PREF_EXTERNAL_FRAME_PLACER_DICTIONARY,
                ExternalFramePlacer.Dictionary.JISHO.name());

        dict = ExternalFramePlacer.Dictionary.valueOf(tag);
        if (dict == null)
            dict = ExternalFramePlacer.Dictionary.JISHO;

        return dict;
    }

    public void setExternalFramePlacerDictionary(String value) {
        prefeditor.putString(PREF_EXTERNAL_FRAME_PLACER_DICTIONARY, value).commit();
    }

    public boolean getPartOfSpeech() {
        return prefs.getBoolean(PREF_PART_OF_SPEECH, false); // TODO - Make true after integration
    }

    public void setPartOfSpeech(boolean value) {
        prefeditor.putBoolean(PREF_PART_OF_SPEECH, value).commit();
    }

    public boolean getAutoPopup() {
        return prefs.getBoolean(PREF_AUTO_POPUP, false);
    }

    public void setAutoPopup(boolean value) {
        prefeditor.putBoolean(PREF_AUTO_POPUP, value).commit();
    }

    public boolean getMistakeDelay() {
        return prefs.getBoolean(PREF_MISTAKE_DELAY, false);
    }

    public void setMistakeDelay(boolean value) {
        prefeditor.putBoolean(PREF_MISTAKE_DELAY, value).commit();
    }

    public boolean getRomaji() {
        return prefs.getBoolean(PREF_ROMAJI, false);
    }

    public void setRomaji(boolean value) {
        prefeditor.putBoolean(PREF_ROMAJI, value).commit();
    }

    public boolean getNoSuggestion() {
        return prefs.getBoolean(PREF_NO_SUGGESTION, true);
    }

    public void setNoSuggestion(boolean value) {
        prefeditor.putBoolean(PREF_NO_SUGGESTION, value).commit();
    }

    public boolean getMuteButton() {
        return prefs.getBoolean(PREF_MUTE_BUTTON, true);
    }

    public void setMuteButton(boolean value) {
        prefeditor.putBoolean(PREF_MUTE_BUTTON, value).commit();
    }

    public boolean getSRSIndication() {
        return prefs.getBoolean(PREF_SRS_INDCATION, true);
    }

    public void setSRSIndication(boolean value) {
        prefeditor.putBoolean(PREF_SRS_INDCATION, value).commit();
    }

    public Keyboard getReviewsKeyboard() {
        return getReviewsImprovements() ? Keyboard.LOCAL_IME : Keyboard.NATIVE;
    }

    public Intent getWebViewIntent() {
        boolean accel = getHWAccel();
        return new Intent(context, accel ? WebReviewActivity.class : SWWebReviewActivity.class);
    }

    public boolean getIgnoreButtonMessage() {
        return reviewsPrefs.getBoolean(PREF_IGNORE_BUTTON_MESSAGE, true);
    }

    public void setIgnoreButtonMessage(boolean value) {
        reviewsPrefsEditor.putBoolean(PREF_IGNORE_BUTTON_MESSAGE, value).commit();
    }

    public boolean getHWAccelMessage() {
        return reviewsPrefs.getBoolean(PREF_HW_ACCEL_MESSAGE, true);
    }

    public void setHWAccelMessage(boolean value) {
        reviewsPrefsEditor.putBoolean(PREF_HW_ACCEL_MESSAGE, value).commit();
    }

    public boolean getHWAccel() {
        return prefs.getBoolean(PREF_HW_ACCEL, true);
    }

    public void setHWAccel(boolean value) {
        prefeditor.putBoolean(PREF_HW_ACCEL, value).commit();
    }

    public boolean toggleMute() {
        boolean mute = !getMute();
        setMute(mute);
        return mute;
    }

    public boolean getMute() {
        return prefs.getBoolean(PREF_MUTE, false);
    }

    public void setMute(boolean value) {
        prefeditor.putBoolean(PREF_MUTE, value).commit();
    }

    public boolean getReviewsLessonsFullscreen() {
        return prefs.getBoolean(PREF_REVIEWS_LESSONS_FULLSCREEN, false);
    }

    public void setReviewsLessonsFullscreen(boolean value) {
        prefeditor.putBoolean(PREF_REVIEWS_LESSONS_FULLSCREEN, value).commit();
    }

    public void setNotificationsEnabled(boolean value) {
        prefeditor.putBoolean(PREF_SHOW_NOTIFICATIONS, value).commit();
    }

    public boolean notificationsEnabled() {
        return prefs.getBoolean(PREF_SHOW_NOTIFICATIONS, true);
    }

    public long getNotificationReminderInterval() {
        return prefs.getLong(PREF_NOTIFICATION_REMINDER_INTERVAL, 7200000); // 2 hours
    }

    public void setNotificationReminderInterval(long milliseconds) {
        prefeditor.putLong(PREF_NOTIFICATION_REMINDER_INTERVAL, milliseconds).commit();
    }

    public void logout() {
        prefeditor.clear().commit();
        reviewsPrefsEditor.clear().commit();

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

    public static enum Keyboard {
        LOCAL_IME, NATIVE
    }
}
