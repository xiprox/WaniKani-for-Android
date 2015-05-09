package tr.xip.wanikani.userscripts;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Pattern;

import tr.xip.wanikani.app.activity.FocusWebView;

public class PartOfSpeech {

    private static final String SCRIPT_FNAME = "scripts/partofspeech.js";

    public static final Pattern URLS [] = new Pattern []  {
            Pattern.compile (".*://www.wanikani.com/.*vocabulary/.*"),
            Pattern.compile (".*://www.wanikani.com/review/session.*"),
            Pattern.compile (".*://www.wanikani.com/lesson/session.*")
    };

    public static void enter (Context ctxt, FocusWebView wv, String url)
    {
        AssetManager mgr;
        InputStream is;
        Reader r;
        int rd;
        char buf [];
        StringBuffer sb;

        if (!matches (url))
            return;

        mgr = ctxt.getAssets ();
        r = null;
        try {
            is = mgr.open (SCRIPT_FNAME);
            r = new InputStreamReader (is);
            sb = wv.jsStart ();
            buf = new char [1024];
            while (true) {
                rd = r.read (buf);
                if (rd < 0)
                    break;
                sb.append (buf, 0, rd);
            }
            wv.jsEnd (sb);
        } catch (Throwable t) {
			t.printStackTrace();
        } finally {
            try {
                if (r != null)
                    r.close ();
            } catch (IOException e) {
				e.printStackTrace();
            }
        }
    }

    protected static boolean matches (String url)
    {
        int i;

        for (i = 0; i < URLS.length; i++)
            if (URLS [i].matcher (url).matches ())
                return true;

        return false;
    }

}