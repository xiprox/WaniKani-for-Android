package tr.xip.wanikani.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import tr.xip.wanikani.R;
import tr.xip.wanikani.settings.SettingsActivity;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class PrefManager {
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefeditor;
    private static Context context;

    public static final String PREF_API_KEY = "pref_api_key";
    public static final String PREF_DASHBOARD_RECENT_UNLOCKS_NUMBER = "pref_dashboard_recent_unlock_number";
    public static final String PREF_DASHBOARD_CRITICAL_ITEMS_PERCENTAGE = "pref_dashboard_critical_items_percentage";

    public PrefManager(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefeditor = prefs.edit();
    }

    public String getApiKey() {
        return prefs.getString("api_key", "0");
    }

    public boolean isFirstLaunch() {
        return prefs.getBoolean("first_launch", true);
    }

    public void setFirstLaunch(boolean value) {
        prefeditor.putBoolean("first_launch", value).commit();
    }

    public void setApiKey(String key) {
        prefeditor.putString("api_key", key).commit();
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

    public void setRadicalsLegendLearned(boolean value) {
        PrefManager.prefeditor.putBoolean("pref_legend_radicals_learned", value).commit();
    }

    public boolean isRadicalsLegendLearned() {
        return PrefManager.prefs.getBoolean("pref_legend_radicals_learned", false);
    }

    public void setKanjiLegendLearned(boolean value) {
        prefeditor.putBoolean("pref_legend_kanji_learned", value).commit();
    }

    public boolean isKanjiLegendLearned() {
        return prefs.getBoolean("pref_legend_kanji_learned", false);
    }

    public void setVocabularyLegendLearned(boolean value) {
        prefeditor.putBoolean("pref_legend_vocabulary_learned", value).commit();
    }

    public boolean isVocabularyLegendLearned() {
        return prefs.getBoolean("pref_legend_vocabulary_learned", false);
    }

    public void setDashboardLastUpdateDate(long date) {
        prefeditor.putLong("pref_update_date_dashboard", date).commit();
    }

    public String getDashboardLastUpdateTime() {
        Date currentTime = new Date(System.currentTimeMillis());
        Date updateDate = new Date(prefs.getLong("pref_update_date_dashboard", 0));

        long differenceInMilliseconds = currentTime.getTime() - updateDate.getTime();
        long differenceInMinutes = differenceInMilliseconds / 60000;
        long differenceInHours = differenceInMinutes / 60;
        long differenceInDays = differenceInHours / 24;

        if (differenceInMinutes == 0) {
            return context.getString(R.string.less_than_a_minute_ago);
        } else if (differenceInMinutes >= 60) {
            if (differenceInHours >= 24) {
                if (differenceInDays == 1)
                    return context.getString(R.string.day_ago);
                else
                    return differenceInDays + " " + context.getString(R.string.days_ago);
            } else {
                if (differenceInHours == 1)
                    return context.getString(R.string.hour_ago);
                else
                    return differenceInHours + " " + context.getString(R.string.hours_ago);
            }
        } else {
            if (differenceInMinutes == 1)
                return context.getString(R.string.minute_ago);
            else
                return differenceInMinutes + " " + context.getString(R.string.minutes_ago);
        }
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
