package tr.xip.wanikani.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class PrefManager {
    public static final String PREF_API_KEY = "pref_api_key";
    public static final String PREF_DASHBOARD_RECENT_UNLOCKS_NUMBER = "pref_dashboard_recent_unlock_number";
    public static final String PREF_DASHBOARD_CRITICAL_ITEMS_PERCENTAGE = "pref_dashboard_critical_items_percentage";
    public static final String PREF_LESSONS_SCREEN_ORIENTATION = "pref_lessons_orientation";
    public static final String PREF_REVIEWS_SCREEN_ORIENTATION = "pref_reviews_orientation";
    public static final String PREF_USE_SPECIFIC_DATES = "pref_use_specific_dates";

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefeditor;
    private static Context context;

    public PrefManager(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefeditor = prefs.edit();
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

    public boolean isLegendLearned() {
        return PrefManager.prefs.getBoolean("pref_legend_learned", false);
    }

    public void setLegendLearned(boolean value) {
        PrefManager.prefeditor.putBoolean("pref_legend_learned", value).commit();
    }

    public void setDashboardLastUpdateDate(long date) {
        prefeditor.putLong("pref_update_date_dashboard", date).commit();
    }

    public String getDashboardLastUpdateTime() {
        return Utils.getTimeDifference(context, new Date(prefs.getLong("pref_update_date_dashboard", 0)),
                Utils.getCurrentDate());
    }

    public String getLessonsScreenOrientation() {
        return prefs.getString(PREF_LESSONS_SCREEN_ORIENTATION, "Portrait");
    }

    public void setLessonsScreenOrientation(String orientation) {
        prefeditor.putString(PREF_LESSONS_SCREEN_ORIENTATION, orientation).commit();
    }

    public String getReviewsScreenOrientation() {
        return prefs.getString(PREF_REVIEWS_SCREEN_ORIENTATION, "Portrait");
    }

    public void setReviewsScreenOrientation(String orientation) {
        prefeditor.putString(PREF_REVIEWS_SCREEN_ORIENTATION, orientation).commit();
    }

    public boolean isUseSpecificDates() {
        return prefs.getBoolean(PREF_USE_SPECIFIC_DATES, false);
    }

    public void setUseSpecificDates(boolean value) {
        prefeditor.putBoolean(PREF_USE_SPECIFIC_DATES, value).commit();
    }

    public void logout() {
        prefeditor.clear().commit();

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
    }
}
