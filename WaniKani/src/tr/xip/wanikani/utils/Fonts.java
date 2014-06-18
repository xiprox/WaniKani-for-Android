package tr.xip.wanikani.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

/**
 * Created by xihsa_000 on 3/23/14.
 */
public class Fonts {

    public Typeface getKanjiFont(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
            return Typeface.createFromAsset(context.getAssets(), "fonts/MTLmr3m.ttf");
        else
            return Typeface.create("sans-serif", Typeface.NORMAL);
    }
}
