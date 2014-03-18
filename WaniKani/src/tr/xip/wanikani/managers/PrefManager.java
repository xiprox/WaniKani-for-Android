package tr.xip.wanikani.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class PrefManager {
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefeditor;
    private static Context context;

    public PrefManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("prefs", 0);
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
