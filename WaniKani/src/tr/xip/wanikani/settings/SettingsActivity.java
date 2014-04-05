package tr.xip.wanikani.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import tr.xip.wanikani.R;
import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by xihsa_000 on 4/4/14.
 */
public class SettingsActivity extends PreferenceActivity {

    PrefManager prefMan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        prefMan = new PrefManager(this);

        Preference mApiKey = findPreference(PrefManager.PREF_API_KEY);
        mApiKey.setSummary(prefMan.getApiKey());
    }
}
