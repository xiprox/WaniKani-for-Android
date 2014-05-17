package tr.xip.wanikani.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import tr.xip.wanikani.R;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class ThemeManager {
    private static SharedPreferences prefs;

    public static final String PREF_THEME = "pref_theme";

    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";

    public ThemeManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getThemeName() {
        return prefs.getString(PREF_THEME, "light");
    }

    public int getTheme() {
        return getThemeName().equals(THEME_LIGHT) ? R.style.ThemeWanikanilight : R.style.ThemeWanikanidark;
    }

    public int getCard() {
        return getThemeName().equals(THEME_LIGHT) ? R.drawable.card_light : R.drawable.card_dark;
    }

    public int getWindowBackgroundColor() {
        return getThemeName().equals(THEME_LIGHT) ? R.color.window_background_light : R.color.window_background_dark;
    }

    public int getNavDrawerBackgroundColor() {
        return getThemeName().equals(THEME_LIGHT) ? R.color.nav_drawer_background_light : R.color.nav_drawer_background_dark;
    }
}
