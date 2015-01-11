package tr.xip.wanikani.utils;

import android.content.Context;
import android.graphics.Typeface;

import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by xihsa_000 on 3/23/14.
 */
public class Fonts {

    public Typeface getKanjiFont(Context context) {
        if (new PrefManager(context).isUseCustomFonts())
            return Typeface.createFromAsset(context.getAssets(), "fonts/MTLmr3m.ttf");
        else
            return Typeface.create("sans-serif", Typeface.NORMAL);
    }
}
