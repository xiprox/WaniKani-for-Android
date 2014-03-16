package tr.xip.wanikani.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class PrefManager {
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefeditor;
    private static Context context;

    public PrefManager(Context mContext)
    {
        context = mContext;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefeditor = prefs.edit();
    }

    public String getApiKey() {
        return prefs.getString("api_key", "0");
    }

    public boolean isProfileFirstTime() {
        return prefs.getBoolean("first_time_profile", true);
    }

    public void setProfileFirstTime(boolean value) {
        prefeditor.putBoolean("first_time_profile", value);
        prefeditor.commit();
    }

}
