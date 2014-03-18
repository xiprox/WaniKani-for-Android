package tr.xip.wanikani.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class PrefManager {
    private static SharedPreferences prefs;
    private static SharedPreferences offlineData;
    private static SharedPreferences.Editor prefeditor;
    private static SharedPreferences.Editor offlineDataEditor;
    private static Context context;

    public PrefManager(Context mContext) {
        context = mContext;
        prefs = context.getSharedPreferences("prefs", 0);
        offlineData = context.getSharedPreferences("offline_data", 0);
        prefeditor = prefs.edit();
        offlineDataEditor = offlineData.edit();
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

    public void logout() {
        prefeditor.clear().commit();
        offlineDataEditor.clear().commit();
    }

}
