package tr.xip.wanikani.app.activity;

/*
 *  Copyright (c) 2013 Alberto Cuda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

/**
 * A webview that allows activities to disable the soft keyboard even
 * if the user focuses on a text field
 */
public class FocusWebView extends WebView {

    public interface Listener {

        public void onScroll (int deltax, int deltay);

    }

    private class JSExecutor  implements Runnable {

        String js;

        public JSExecutor (String js)
        {
            this.js = js;
        }

        public void run ()
        {
            loadUrl (js);
        }

    }

    /// The input manager
    private InputMethodManager imm;

    /// If set, the soft keyboard should not be shown
    private boolean disable;

    /// The onPause method
    private Method onPause_m;

    /// The onResume method
    private Method onResume_m;

    /// List of registered listeners
    private List<Listener> listeners;

    /**
     * Constructor
     * @param ctxt the context
     * @param attrs the attributes
     */
    public FocusWebView (Context ctxt, AttributeSet attrs)
    {
        super (ctxt, attrs);

        imm = (InputMethodManager) ctxt.getSystemService (Context.INPUT_METHOD_SERVICE);
        listeners = new Vector<Listener> ();

        try {
            onPause_m = getClass ().getMethod ("onPause");
            onResume_m = getClass ().getMethod ("onResume");
        } catch (Throwable t) {
            onPause_m = onResume_m = null;
        }
    }

    /**
     * Enables soft keyboard
     */
    public void enableFocus ()
    {
        disable = false;
    }

    /**
     * Disables soft keyboard
     */
    public void disableFocus ()
    {
        disable = true;
        hideNative ();
    }

    /**
     * Registers a new listener.
     * @param listener the new listener
     */
    public void registerListener (Listener listener)
    {
        listeners.add (listener);
    }

    /**
     * This is the event we use to detect whether the keyboard is showing up.
     * Actually, this method is used in a lot of other cases, but the
     * operations done here are idempotent.
     * 	@param widthMeasureSpec width specs
     * 	@param heightMeasureSpec height specs
     */
    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (disable)
            hideNative ();
    }

    @Override
    protected void onScrollChanged (int l, int t, int oldl, int oldt)
    {
        int dx, dy;

        super.onScrollChanged (l, t, oldl, oldt);

        dx = l - oldl;
        dy = t - oldt;
        for (Listener listener : listeners)
            listener.onScroll (dx, dy);
    }

    /**
     * Hides native keyboard, if shown
     */
    protected void hideNative ()
    {
        View w;

        w = getFocusedChild ();
        if (w != null && w.getApplicationWindowToken () != null)
            imm.hideSoftInputFromWindow (w.getApplicationWindowToken (), 0);

    }

    /**
     * Does its best to release all the resources. Should be called by
     * enclosing activity in its own {@link Activity#onPause} implementation
     * to avoid consuming CPU when hidden.
     */
    public void release ()
    {
        try {
            if (onPause_m != null)
                onPause_m.invoke (this);
        } catch (Throwable t) {
			/* empty */
        }
        pauseTimers ();
        CookieSyncManager.getInstance ().stopSync ();
    }

    /**
     * Acquires all the resources previously released by {@link #release()}.
     * Should be called by enclosing activity in its own
     * {@link Activity#onResume} implementation.
     */
    public void acquire ()
    {
        try {
            if (onResume_m != null)
                onResume_m.invoke (this);
        } catch (Throwable t) {
			/* empty */
        }
        resumeTimers ();
        CookieSyncManager.getInstance ().startSync ();
    }

    /**
     * Executes a javascript on the web page.
     * @param js the javascript statements. This method wraps it into a function
     */
    public void js (String s)
    {
        jsEnd (jsStart ().append (s));
    }

    public StringBuffer jsStart ()
    {
        StringBuffer sb;

        sb = new StringBuffer ();
        sb.append ("javascript:(function() {");

        return sb;
    }


    public void jsEnd (StringBuffer sb)
    {
        sb.append ("})()");

        if (getContext () instanceof Activity)
            ((Activity) getContext ()).runOnUiThread (new JSExecutor (sb.toString ()));
    }

}