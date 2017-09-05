package tr.xip.wanikani.utils;

import android.content.Context;
import android.graphics.Typeface;

import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by xihsa_000 on 3/23/14.
 */
public class Fonts {
    private static Typeface custom = null;
    private static Typeface normal = Typeface.create("sans-serif", Typeface.NORMAL);

    public Typeface getKanjiFont(Context context) {
        if (PrefManager.isUseCustomFonts()) {
            if (custom == null)
                custom = Typeface.createFromAsset(context.getAssets(), "fonts/MTLmr3m.ttf");
            return custom;
        } else {
            return normal;
        }
    }
}
