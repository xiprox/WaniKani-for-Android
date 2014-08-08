package tr.xip.wanikani.settings;

import android.os.Build;
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

    Preference mApiKey;
    Preference mLessonsScreenOrientation;
    Preference mReviewsScreenOrientation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        prefMan = new PrefManager(this);

        mApiKey = findPreference(PrefManager.PREF_API_KEY);
        mLessonsScreenOrientation = findPreference(PrefManager.PREF_LESSONS_SCREEN_ORIENTATION);
        mReviewsScreenOrientation = findPreference(PrefManager.PREF_REVIEWS_SCREEN_ORIENTATION);

        mLessonsScreenOrientation.setOnPreferenceChangeListener(new onLessonsScreenOrientationChangedListener());
        mReviewsScreenOrientation.setOnPreferenceChangeListener(new onReviewsScreenOrientationChangedListener());

        loadPreferences();
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= 16) {
            super.onNavigateUp();
        } else {
            super.onBackPressed();
        }
    }

    private void loadPreferences() {
        setApiKey();
        loadLessonsScreenOrientation();
        loadReviewsScreenOrientation();
    }

    private void setApiKey() {
        String apiKey = prefMan.getApiKey();
        String maskedApiKey = "************************";

        for (int i = 25; i < apiKey.length(); i++) {
            maskedApiKey += apiKey.charAt(i);
        }

        mApiKey.setSummary(maskedApiKey);
    }

    private void loadLessonsScreenOrientation() {
        mLessonsScreenOrientation.setSummary(prefMan.getLessonsScreenOrientation());
    }

    private void loadReviewsScreenOrientation() {
        mReviewsScreenOrientation.setSummary(prefMan.getReviewsScreenOrientation());
    }

    private class onLessonsScreenOrientationChangedListener implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            preference.setSummary(o.toString());
            return true;
        }
    }

    private class onReviewsScreenOrientationChangedListener implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            preference.setSummary(o.toString());
            return true;
        }
    }
}
