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
        if (getThemeName().equals(THEME_LIGHT)) {
            return R.style.ThemeWanikanilight;
        } else if (getThemeName().equals(THEME_DARK)) {
            return R.style.ThemeWanikanidark;
        } else {
            return R.style.ThemeWanikanilight;
        }
    }

    public int getCard() {
        if (getThemeName().equals(THEME_LIGHT)) {
            return R.drawable.card_light;
        } else if (getThemeName().equals(THEME_DARK)) {
            return R.drawable.card_dark;
        } else {
            return R.drawable.card_light;
        }
    }

    public int getWindowBackgroundColor() {
        if (getThemeName().equals(THEME_LIGHT)) {
            return R.color.window_background_light;
        } else if (getThemeName().equals(THEME_DARK)) {
            return R.color.window_background_dark;
        } else {
            return R.color.window_background_light;
        }
    }
}
