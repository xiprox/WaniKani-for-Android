package tr.xip.wanikani.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by xihsa_000 on 3/23/14.
 */
public class Fonts {

    public Typeface getKanjiFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/MTLmr3m.ttf");
    }
}
