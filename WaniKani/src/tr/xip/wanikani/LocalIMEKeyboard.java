package tr.xip.wanikani;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

import tr.xip.wanikani.app.activity.FocusWebView;
import tr.xip.wanikani.app.activity.WebReviewActivity;
import tr.xip.wanikani.models.WaniKaniItem;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.userscripts.IgnoreButton;
import tr.xip.wanikani.userscripts.LessonOrder;
import tr.xip.wanikani.userscripts.MistakeDelay;
import tr.xip.wanikani.userscripts.ReviewOrder;
import tr.xip.wanikani.userscripts.WaniKaniImprove;
import tr.xip.wanikani.userscripts.WaniKaniKunOn;
import tr.xip.wanikani.utils.Fonts;
import tr.xip.wanikani.utils.Utils;

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

/**
 * Implementation of the "Custom IME" keyboard. This is now the preferred choice,
 * since it is the only one that features tilde substitution, ignore button, and
 * is much more appealing on small devices. Basically we put a textbox on top
 * of the real answer form, and use a regular keyboard to input text. If it is
 * a "reading" question, we perform kana translation directly inside the app,
 * instead of using the WK JS-based IME.
 */
public class LocalIMEKeyboard implements Keyboard {

    /**
     * A listener of meaningful webview events. We need this to synchronized the position
     * of our text box with that of the HTML form.
     */
    private class WebViewListener implements FocusWebView.Listener {

        /**
         * Called when the webview scrolls vertically. We relay the event
         * to {@link LocalIMEKeyboard#scroll(int, int)}.
         */
        @Override
        public void onScroll (int dx, int dy)
        {
            Handler handler;

            if (!bpos.visible)
                return;

            scroll (dx, dy);
            handler = new Handler ();
            handler.postDelayed (new UpdatePositionTask (), 707);
        }
    }

    private class UpdatePositionTask implements Runnable {

        public UpdatePositionTask ()
        {
            lpt = this;
        }

        @Override
        public void run ()
        {
            if (lpt == this && bpos.visible)
                wv.js (JS_UPDATE_POSITION);
        }

    }

    /**
     * Listener of all the events related to the IME. This class is the glue between the app
     * and {@link JapaneseIME}: it handles text changes, delivers them to the IME and updates
     * the contents accordingly.
     */
    private class IMEListener implements TextWatcher, OnEditorActionListener, View.OnClickListener {

        /// Set if we need to perform kana translation
        boolean translate;

        /// The list of chars are not allowed when entering a meaning
        private static final String M_BANNED_CHARS = ",/;[]\\`\"=+";

        /// The list of chars are not allowed when entering a reading
        private static final String R_BANNED_CHARS = M_BANNED_CHARS + " ";

        private int findFirstBannedChar (String s, boolean kana)
        {
            String chars;
            int i;

            chars = kana ? R_BANNED_CHARS : M_BANNED_CHARS;

            for (i = 0; i < s.length (); i++)
                if (chars.indexOf (s.charAt (i)) >= 0)
                    return i;

            return -1;
        }


        /**
         * Called after the text is changed to perform kana translation (if enabled).
         * In that case, the new text is sent the IME and, if some changes need to be performed,
         * they are applied to the text view. After that, android will call this method again,
         * but that's safe because the IME won't ask for a replacement any more.
         */
        @Override
        public void afterTextChanged (Editable et)
        {
            JapaneseIME.Replacement repl;
            int pos, i;

            i = findFirstBannedChar (et.toString (), translate);
            if (i >= 0) {
                et.replace (i, i + 1, "");
                return;
            }

            if (!translate)
                return;

            pos = ew.getSelectionStart ();
            repl = ime.replace (et.toString (), pos);
            if (repl != null)
                et.replace (repl.start, repl.end, repl.text);
        }

        @Override
        public void beforeTextChanged (CharSequence cs, int start, int count, int after)
        {
	    	/* empty */
        }

        @Override
        public void onTextChanged (CharSequence cs, int start, int before, int count)
        {
	    	/* empty */
        }

        /**
         * Enable or disable kana translation.
         * @param enable set if should be enabled
         */
        public void translate (boolean enable)
        {
            translate = enable;
        }

        /**
         * Handler of editor actions. It intercepts the "enter" key and moves to the
         * next question, by calling {@link #next()}.
         */
        @Override
        public boolean onEditorAction (TextView tv, int actionId, KeyEvent event)
        {

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                next ();
                return true;
            }

            return false;
        }

        /**
         * Handler of the next button. Calls {@link #next()}.
         */
        @Override
        public void onClick (View view)
        {
            next ();
        }

        /**
         * Called when the user presses the "next" button. It completes kana translation
         * (to fix trailing 'n's), and injects the answer.
         */
        private void next ()
        {
            String s, orgs;

            s = ew.getText ().toString ();

        	/* This prevents some race conditions */
            if (s.length () == 0 || frozen)
                return;

            frozen = true;

            if (translate) {
                s = ime.fixup (orgs = s);
                if (!s.equals (orgs))	// We do this only if needed to avoid glitch on cursor
                    ew.setText (s);
            }
            if (!editable)
                wv.js (JS_ENTER);
            else if (isWKIEnabled)
                wv.js (String.format (JS_INJECT_ANSWER, s) +  WaniKaniImprove.getCode());
            else
                wv.js (String.format (JS_INJECT_ANSWER, s));

        }
    }

    /**
     * Chain runnable that handles "setText" events on the UI thread.
     */
    private class JSSetText implements Runnable {

        /// The text to set
        public String text;

        /**
         * Constructor
         * @param text the text to set
         */
        public JSSetText (String text)
        {
            this.text = text;

            wav.runOnUiThread (this);
        }

        /**
         * Sets the text on the EditView.
         */
        public void run ()
        {
            ew.setText(text);
        }

    }

    /**
     * Chain runnable that handles "setClass" events on the UI thread.
     */
    private class JSListenerSetClass implements Runnable {

        /// Set if the classes should be added. If unset, all CSS classes should be removed
        public boolean enable;

        /// The CSS class to emulate
        public String clazz;

        /// Set if we reviewing, unset if we are in the lessons quiz module
        public boolean reviews;

        /// SRS level
        public int level;

        /**
         * Constructor. Used when the edittext changes class.
         * @param clazz the CSS class
         * @param reviews are we in the review module
         */
        public JSListenerSetClass (String clazz, boolean reviews)
        {
            this.clazz = clazz;
            this.reviews = reviews;

            enable = true;

            wav.runOnUiThread (this);
        }

        /**
         * Constructor. Used when the edittext goes back to the default class
         */
        public JSListenerSetClass (int level)
        {
            this.level = level;

            enable = false;

            wav.runOnUiThread (this);
        }

        /**
         * Delivers the event to {@link LocalIMEKeyboard#setClass(String)}
         * or {@link LocalIMEKeyboard#unsetClass()}, depending on the type of event.
         */
        public void run ()
        {
            if (enable)
                setClass (clazz, reviews);
            else
                unsetClass (level);

            setInputType();
        }

    }

    private class JSHideShow implements Runnable {

        boolean show;

        public JSHideShow (boolean show)
        {
            this.show = show;

            wav.runOnUiThread (this);
        }

        public void run ()
        {
            if (show) {
                divw.setVisibility (View.VISIBLE);
                if (!hwkeyb) {
                    imm.showSoftInput (wv, InputMethodManager.SHOW_FORCED);
                    if (prefMan.getPortraitMode())
                        wav.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                ew.requestFocus ();
                if (hwkeyb)
                    imm.hideSoftInputFromWindow (ew.getWindowToken (), 0);
            } else {
                if (!hwkeyb) {
                    if (prefMan.getPortraitMode())
                        wav.setRequestedOrientation (orientation);
                }
                divw.setVisibility (View.GONE);
                imm.hideSoftInputFromWindow (ew.getWindowToken (), 0);
            }

            showQuestionPatch (bpos.qvisible && show);
        }

    }

    private class JSListenerShow implements Runnable {

        int sequence;

        Rect frect, trect;

        public JSListenerShow (int sequence, Rect frect, Rect trect)
        {
            this.sequence = sequence;
            this.frect = frect;
            this.trect = trect;

            wav.runOnUiThread (this);
        }

        public void run ()
        {
            if (updateSequence (sequence))
                replace(frect, trect);
        }
    }

    private class JSListenerShowQuestion implements Runnable {

        int sequence;

        WaniKaniItem.Type type;

        String name;

        Rect rect;

        int size;

        public JSListenerShowQuestion (int sequence, WaniKaniItem.Type type, String name, Rect rect, int size)
        {
            this.sequence = sequence;
            this.type = type;
            this.name = name;
            this.rect = rect;
            this.size = size;

            wav.runOnUiThread (this);
        }

        public void run ()
        {
            if (updateSequence (sequence))
                showQuestion (type, name, rect, size);
        }
    }

    private class BoxPosition {

        Rect frect;

        Rect trect;

        /// The box is visible, or ought to be unless there is a timeout screen showing
        boolean visible;

        /// Is the timeout screen visible
        boolean timeout;

        /// Is the qbox visible
        boolean qvisible;

        /// Is this a review session
        boolean reviews;

        public boolean update (Rect frect, Rect trect)
        {
            boolean changed;

            changed = false;
            if (this.frect == null || !this.frect.equals (frect)) {
                changed = true;
                this.frect = frect;
            }

            if (this.trect == null || !this.trect.equals (trect)) {
                changed = true;
                this.trect = trect;
            }

            return changed;
        }

        public void shift (int yofs)
        {
            if (frect != null)
                frect.offset (0, yofs);

            if (trect != null)
                trect.offset (0, yofs);
        }

        public boolean shallShow ()
        {
            return visible && !timeout;
        }
    }

    /**
     * A JS bridge that handles the answer text-box events.
     */
    private class JSListener {

        /**
         * Called by {@link LocalIMEKeyboard#JS_INIT_TRIGGERS} when the form changes its position.
         * @param fleft form top-left X coordinate
         * @param ftop form top-left Y coordinate
         * @param fright form bottom-right X coordinate
         * @param fbottom form bottom-right Y coordinate
         * @param tleft textbox top-left X coordinate
         * @param ttop textbox top-left Y coordinate
         * @param tright textbox bottom-right X coordinate
         * @param tbottom textbox bottom-right Y coordinate
         */
        @JavascriptInterface
        public void replace (int sequence, int fleft, int ftop, int fright, int fbottom,
                             int tleft, int ttop, int tright, int tbottom)
        {
            fleft = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, fleft, dm);
            fright = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, fright, dm);
            ftop = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, ftop, dm);
            fbottom = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, fbottom, dm);

            tleft = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, tleft, dm);
            tright = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, tright, dm);
            ttop = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, ttop, dm);
            tbottom = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, tbottom, dm);

            new JSListenerShow (sequence, new Rect (fleft, ftop, fright, fbottom),
                    new Rect (tleft, ttop, tright, tbottom));
        }

        /**
         * Called when the question changes.
         * @param qtype the type of question (reading vs meaning)
         */
        @JavascriptInterface
        public void newQuestion (String qtype, int level)
        {
            imel.translate (qtype.equals ("reading"));
            new JSListenerSetClass (level);
        }

        @JavascriptInterface
        public void overrideQuestion (int sequence, String radical, String kanji, String vocab,
                                      int left, int top, int right, int bottom, String size)
        {
            WaniKaniItem.Type type;
            String name;
            int xsize;

            left = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, left, dm);
            right = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, right, dm);
            top = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, top, dm);
            bottom = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, bottom, dm);

            if (radical != null) {
                type = WaniKaniItem.Type.RADICAL;
                name = radical;
            } else if (kanji != null) {
                type = WaniKaniItem.Type.KANJI;
                name = kanji;
            } else if (vocab != null) {
                type = WaniKaniItem.Type.VOCABULARY;
                name = vocab;
            } else {
                type = null;
                name = null;
            }

            try {
                if (size.endsWith ("px"))
                    xsize = Integer.parseInt (size.substring (0, size.length () - 2));
                else
                    xsize = 0;
            } catch (NumberFormatException e) {
                xsize = 0;
            }

            new JSListenerShowQuestion (sequence, type, name, new Rect (left, top, right, bottom), xsize);
        }

        /**
         * Called when the text box changes its class
         * @param clazz the new CSS class
         * @param reviews if we are doing reviews
         */
        @JavascriptInterface
        public void setClass (String clazz, boolean reviews)
        {
            new JSListenerSetClass (clazz, reviews);
        }

        /**
         * Called when a full CSS class and contents synchronization needs to be done.
         * @param correct
         * @param incorrect
         * @param text
         * @param reviews if we are doing reviews
         */
        @JavascriptInterface
        public void sync (boolean correct, boolean incorrect, String text, boolean reviews)
        {
            boolean needMenuUpdate;

            needMenuUpdate = reviews != bpos.reviews;
            bpos.reviews = reviews;

            if (correct)
                new JSListenerSetClass ("correct", reviews);
            if (incorrect)
                new JSListenerSetClass ("incorrect", reviews);
            new JSSetText (text);
        }

        /**
         * Called when the text box should be shown
         */
        @JavascriptInterface
        public void showKeyboard ()
        {
            bpos.visible = true;
            new JSHideShow (bpos.shallShow ());
        }

        /**
         * Called when the text box should be hidden
         */
        @JavascriptInterface
        public void hideKeyboard ()
        {
            bpos.visible = false;
            new JSHideShow (bpos.shallShow ());
        }

        /**
         * Called when the timeout div is shown or hidden. Need to catch this event to hide
         * and show the windows as well.
         * @param enabled if the div is shown
         */
        @JavascriptInterface
        public void timeout (boolean enabled)
        {
            bpos.timeout = enabled;
            new JSHideShow (bpos.shallShow ());
        }


        /**
         * Re-enables the edit box, because the event has been delivered.
         */
        @JavascriptInterface ()
        public void unfreeze ()
        {
            frozen = false;
        }

        @JavascriptInterface ()
        public void refreshWKLO ()
        {
            if (prefMan.getLessonOrder())
                wv.js (ifLessons (LessonOrder.JS_REFRESH_CODE));
        }
    }


    /**
     * A JS condition that evaluates to true if we are reviewing, false if we are in the lessons module
     */
    private static final String JS_REVIEWS_P =
            "(document.getElementById (\"quiz\") == null)";

    /**
     * The javascript triggers. They are installed when the keyboard is shown.
     */
    private static final String JS_INIT_TRIGGERS =
            "window.wknSequence = 1;" +
                    "window.wknReplace = function () {" +
                    "   var form, frect, txt, trect, button, brect;" +
                    "   form = document.getElementById (\"answer-form\");" +
                    "   txt = document.getElementById (\"user-response\");" +
                    "   button = form.getElementsByTagName (\"button\") [0];" +
                    "   frect = form.getBoundingClientRect ();" +
                    "   trect = txt.getBoundingClientRect ();" +
                    "   brect = button.getBoundingClientRect ();" +
                    "   wknJSListener.replace (window.wknSequence, frect.left, frect.top, frect.right, frect.bottom," +
                    "						   trect.left, trect.top, brect.left, trect.bottom);" +
                    "   window.wknSequence++;" +
                    "};" +
                    "window.wknOverrideQuestion = function () {" +
                    "   var item, question, rect, style;" +
                    "   item = $.jStorage.get (\"currentItem\");" +
                    "   question = document.getElementById (\"character\");" +
                    "   if (question.getElementsByTagName (\"span\").length > 0)" +
                    "       question = question.getElementsByTagName (\"span\") [0];" +
                    "   rect = question.getBoundingClientRect ();" +
                    "   style = window.getComputedStyle (question, null);" +
                    "   wknJSListener.overrideQuestion (window.wknSequence," +
                    "                                   item.rad ? item.rad : null," +
                    "							        item.kan ? item.kan : null," +
                    "							        item.voc ? item.voc : null, " +
                    "							        rect.left, rect.top, rect.right, rect.bottom," +
                    "							        style.getPropertyValue(\"font-size\"));" +
                    "   window.wknSequence++;" +
                    "};" +
                    "window.wknNewQuestion = function (entry, type) {" +
                    "   var qtype, e, item;" +
                    "   qtype = $.jStorage.get (\"questionType\");" +
                    "   window.wknReplace ();" +
                    "   if (" + JS_REVIEWS_P + ")" +
                    "        window.wknOverrideQuestion ();" +
                    "   if ($(\"#character\").hasClass (\"vocabulary\")) {" +
                    "        e = $(\"#character span\");" +
                    "        e.text (e.text ().replace (/〜/g, \"~\")); " +
                    "   }" +
                    "   item = $.jStorage.get (\"currentItem\");" +
                    "   wknJSListener.newQuestion (qtype, item.srs);" +
                    "};" +
                    "$.jStorage.listenKeyChange (\"currentItem\", window.wknNewQuestion);" +
                    "var oldAddClass = jQuery.fn.addClass;" +
                    "jQuery.fn.addClass = function () {" +
                    "    var res;" +
                    "    res = oldAddClass.apply (this, arguments);" +
                    "    if (this.selector == \"#answer-form fieldset\")" +
                    "         wknJSListener.setClass (arguments [0], " + JS_REVIEWS_P + "); " +
                    "    if (arguments [0] == \"hidden\" && " +
                    "        (this.selector == \"#screen-quiz-ready\" || " +
                    "         this.selector == \"#screen-lesson-ready\" || " +
                    "         this.selector == \"#screen-lesson-done\" || " +
                    "         this.selector == \"#screen-time-out\"))" +
                    "			wknJSListener.timeout (false);" +
                    "    if (arguments [0] == \"hidden\" && " +
                    "        (this.selector == \"#screen-lesson-ready\"))" +
                    "			wknJSListener.refreshWKLO ();" +
                    "    return res;" +
                    "};" +
                    "var oldRemoveClass = jQuery.fn.removeClass;" +
                    "jQuery.fn.removeClass = function () {" +
                    "    var res;" +
                    "    res = oldRemoveClass.apply (this, arguments);" +
                    "    if (arguments [0] == \"hidden\" && " +
                    "        (this.selector == \"#screen-quiz-ready\" || " +
                    "         this.selector == \"#screen-lesson-ready\" || " +
                    "         this.selector == \"#screen-lesson-done\" || " +
                    "         this.selector == \"#screen-time-out\"))" +
                    "			wknJSListener.timeout (true);" +
                    "    return res;" +
                    "};" +
                    "window.wknNewQuiz = function (entry, type) {" +
                    "   var qtype, e;" +
                    "   qtype = $.jStorage.get (\"l/questionType\");" +
                    "   window.wknReplace ();" +
                    "   if ($(\"#main-info\").hasClass (\"vocabulary\")) {" +
                    "        e = $(\"#character\");" +
                    "        e.text (e.text ().replace (/〜/g, \"~\")); " +
                    "   }" +
                    "   if ($(\"#quiz\").is (\":visible\"))" +
                    "       wknJSListener.newQuestion (qtype, -1);" +
                    "};" +
                    "$.jStorage.listenKeyChange (\"l/currentQuizItem\", window.wknNewQuiz);" +
                    "var oldShow = jQuery.fn.show;" +
                    "var oldHide = jQuery.fn.hide;" +
                    "jQuery.fn.show = function () {" +
                    "    var res;" +
                    "    res = oldShow.apply (this, arguments);" +
                    "    if (this.selector == \"#quiz\")" +
                    "         wknJSListener.showKeyboard (); " +
                    "    if (this.selector == \"#timeout\") " +
                    "         wknJSListener.timeout (true);" +
                    "    return res;" +
                    "};" +
                    "jQuery.fn.hide = function () {" +
                    "    var res;" +
                    "    res = oldHide.apply (this, arguments);" +
                    "    if (this.selector == \"#quiz\")" +
                    "         wknJSListener.hideKeyboard (); " +
                    "    if (this.selector == \"#timeout\") " +
                    "         wknJSListener.timeout (false);" +
                    "    return res;" +
                    "};" +
                    "if (" + JS_REVIEWS_P + ") {" +
                    "  wknJSListener.showKeyboard ();" +
                    "  wknJSListener.timeout ($(\"#timeout\").is (\":visible\"));" +
                    "  window.wknNewQuestion ();" +
                    "} else if ($(\"#quiz\").is (\":visible\")) {" +
                    "  window.wknNewQuiz ();" +
                    "  wknJSListener.showKeyboard ();" +
                    "  wknJSListener.timeout (" +
                    "        $(\"#screen-quiz-ready\").is (\":visible\") || " +
                    "        $(\"#screen-lesson-ready\").is (\":visible\") || " +
                    "        $(\"#screen-lesson-done\").is (\":visible\") || " +
                    "        $(\"#screen-time-out\").is (\":visible\")" +
                    "			); " +
                    "} else {" +
                    "	wknJSListener.hideKeyboard ();" +
                    "   wknJSListener.timeout (false);" +
                    "}" +
                    "if (typeof idleTime === \"object\") {" +	 // An explicit reset when entering an answer would be much better,
                    "	idleTime.view = function() { };" +       // but the class does not expose it
                    "}" +
                    "var form, tbox;" +
                    "form = $(\"#answer-form fieldset\");" +
                    "form.css ('visibility', 'hidden');" +
                    "tbox = $(\"#user-response\");" +
                    "wknJSListener.sync (form.hasClass (\"correct\"), form.hasClass (\"incorrect\"), " +
                    "                    tbox.val (), " + JS_REVIEWS_P + ");";
    /**
     * Uninstalls the triggers, when the keyboard is hidden
     */
    private static final String JS_STOP_TRIGGERS =
            "var form;" +
                    "form = $(\"#answer-form fieldset\");" +
                    "form.css ('visibility','visible');" +
                    "$.jStorage.stopListening (\"currentItem\", window.wknNewQuestion);" +
                    "$.jStorage.stopListening (\"l/currentQuizItem\", window.wknNewQuiz);";

    private static final String JS_UPDATE_POSITION =
            "if (window.wknReplace != null) { " +
                    "    window.wknReplace ();" +
                    "    if (" + JS_REVIEWS_P + ")" +
                    "       window.wknOverrideQuestion ();" +
                    "}";

    /**
     * Clicks the "next" button. Similar to {@link #JS_INJECT_ANSWER}, but it does
     * not touch the user-response field
     */
    private static final String JS_ENTER =
            MistakeDelay.injectAnswer(
                    "$(\"#answer-form button\").click ();") +
                    "wknJSListener.unfreeze ();";

    /**
     * Injects an answer into the HTML text box and clickes the "next" button.
     */
    private static final String JS_INJECT_ANSWER =
            "$(\"#user-response\").val (\"%s\");" +
                    JS_ENTER;

    private static final String JS_OVERRIDE =
            "window.wknOverrideQuestion ();";

    private static final String JS_INFO_POPUP =
            "$('#option-item-info').removeClass ();" +
                    "$('#option-item-info span').click ();";

    private static final String JS_SHOW_QUESTION =
            "$('#character span').css ('visibility', '%s');";

    private static final String JS_LESSONS_MUTE =
            "window.wkAutoplay = $.jStorage.get (\"l/audioAutoplay\"); " +
                    "$.jStorage.set(\"l/audioAutoplay\",false);";

    private static final String JS_LESSONS_UNMUTE =
            "if (window.wkAutoplay != null)" +
                    "	$.jStorage.set(\"l/audioAutoplay\",window.wkAutoplay);";

    private static final String JS_REVIEWS_MUTE =
            "window.wkAutoplay = audioAutoplay;" +
                    "audioAutoplay = false;";

    private static final String JS_REVIEWS_UNMUTE =
            "if (window.wkAutoplay != null) " +
                    "	audioAutoplay = window.wkAutoplay;";

    /// Preferences Manager
    PrefManager prefMan;

    /// Parent activity
    WebReviewActivity wav;

    /// Internal browser
    FocusWebView wv;

    /// The manager, used to popup the keyboard when needed
    InputMethodManager imm;

    /// The IME
    JapaneseIME ime;

    /// The view that is placed on top of the answer form
    View divw;

    /// The edit text
    EditText ew;

    /// The question
    TextView qvw;

    /// The handler of all the ime events
    IMEListener imel;

    /// Bridge between JS and the main class
    JSListener jsl;

    /// Display metrics, needed to translate HTML coordinates into pixels
    DisplayMetrics dm;

    /// The next button
    Button next;

    /// The current box position and state
    BoxPosition bpos;

    /// The SRS view
    View srsv;

    int correctFG, incorrectFG, ignoredFG;

    int correctBG, incorrectBG, ignoredBG;

    Map<Integer, Integer> srsCols;

    EnumMap<WaniKaniItem.Type, Integer> cmap;

    WaniKaniImprove wki;

    boolean isWKIEnabled;

    private static final String PREFIX = LocalIMEKeyboard.class + ".";

    private static final String PREF_FONT_OVERRIDE = PREFIX + "PREF_FONT_OVERRIDE";

    /// Set if the ignore button must be shown, because the answer is incorrect
    boolean canIgnore;

    boolean isMuted;

    /// Is the text box frozen because it is waiting for a class change
    boolean frozen;

    /// Is the text box disabled
    boolean editable;

    /// Was suggestion disabled last time we checked?
    boolean disableSuggestions;

    /// Was romaji enabled last time we checked?
    boolean romaji;

    /// Last Sequence number received from JS
    int lastSequence;

    /// Last scheduled position task
    UpdatePositionTask lpt;

    /// Use hw keyboard
    boolean hwkeyb;

    /// Default orientation
    int orientation;

    /**
     * Constructor
     * @param wav parent activity
     * @param wv the integrated browser
     */
    public LocalIMEKeyboard (WebReviewActivity wav, FocusWebView wv)
    {
        Resources res;

        this.wav = wav;
        this.wv = wv;
        this.prefMan = new PrefManager(wav);

        editable = true;
        bpos = new BoxPosition ();

        imm = (InputMethodManager) wav.getSystemService (Context.INPUT_METHOD_SERVICE);

        disableSuggestions = prefMan.getNoSuggestion() | prefMan.getRomaji();

        dm = wav.getResources ().getDisplayMetrics ();

        ime = new JapaneseIME ();

        ew = (EditText) wav.findViewById (R.id.ime);
        divw = wav.findViewById (R.id.ime_div);
        imel = new IMEListener ();
        ew.addTextChangedListener (imel);
        ew.setInputType (InputType.TYPE_CLASS_TEXT);
        ew.setOnEditorActionListener (imel);
        ew.setGravity (Gravity.CENTER);
        ew.setImeActionLabel (">>", EditorInfo.IME_ACTION_DONE);
        ew.setImeOptions (EditorInfo.IME_ACTION_DONE);

        qvw = (TextView) wav.findViewById (R.id.txt_question_override);

        next = (Button) wav.findViewById (R.id.ime_next);
        next.setOnClickListener (imel);

        srsv = wav.findViewById (R.id.v_srs);

        jsl = new JSListener ();
        wv.addJavascriptInterface (jsl, "wknJSListener");

        wki = new WaniKaniImprove (wav, wv);
        wv.registerListener (new WebViewListener ());

        res = wav.getResources ();
        correctFG = res.getColor (R.color.correctfg);
        incorrectFG = res.getColor (R.color.incorrectfg);
        ignoredFG = res.getColor (R.color.ignoredfg);

        setupSRSColors (res);

        correctBG = R.drawable.card_reviews_edittext_correct;
        incorrectBG = R.drawable.card_reviews_edittext_incorrect;
        ignoredBG = R.drawable.card_reviews_edittext_ignored;

        cmap = new EnumMap<WaniKaniItem.Type, Integer> (WaniKaniItem.Type.class);
        cmap.put (WaniKaniItem.Type.RADICAL, res.getColor (R.color.wanikani_radical));
        cmap.put (WaniKaniItem.Type.KANJI, res.getColor (R.color.wanikani_kanji));
        cmap.put (WaniKaniItem.Type.VOCABULARY, res.getColor (R.color.wanikani_vocabulary));
    }

    private void setupSRSColors (Resources res)
    {
        int c;

        srsCols = new Hashtable<Integer, Integer> ();
        c = R.drawable.oval_apprentice;
        srsCols.put (1, c);
        srsCols.put (2, c);
        srsCols.put (3, c);
        srsCols.put(4, c);

        c = R.drawable.oval_guru;
        srsCols.put (5, c);
        srsCols.put(6, c);

        c = R.drawable.oval_master;
        srsCols.put (7, c);

        c = R.drawable.oval_enlightened;
        srsCols.put (8, c);

        c = R.drawable.oval_burned;
        srsCols.put(9, c);
    }

    /**
     * Shows the keyboard. Actually we only inject the triggers: if the javascript code detects that
     * the form must be shown, it call the appropriate event listeners.
     * @param hasEnter
     */
    @Override
    public void show (boolean hasEnter)
    {
        hwkeyb = false; // We no longer have a preference for this

        lastSequence = -1;
        wv.js (JS_INIT_TRIGGERS);

        orientation = wav.getRequestedOrientation ();

        if (prefMan.getReviewOrder())
            wv.js (ifReviews (ReviewOrder.JS_CODE));
        if (prefMan.getLessonOrder())
            wv.js (ifLessons (LessonOrder.JS_CODE));
        wv.js (MistakeDelay.JS_INIT);

        isWKIEnabled = prefMan.getWaniKaniImprove();
        if (isWKIEnabled)
            wki.initPage ();

        setInputType ();

        wv.enableFocus ();
    }

    /**
     * Called when the keyboard should be iconized. This does never happen when using the
     * native keyboard, so this method does nothing
     * @param hasEnter if the keyboard contains the enter key. If unset, the hide button is shown instead
     */
    @Override
    public void iconize (boolean hasEnter)
    {
		/* empty */
    }


    /**
     * Hides the keyboard and uninstalls the triggers.
     */
    @Override
    public void hide ()
    {
        reset();
        bpos.visible = false;
        wv.js(JS_STOP_TRIGGERS);
        if (isWKIEnabled)
            wki.uninitPage ();
        if (prefMan.getPortraitMode())
            wav.setRequestedOrientation(orientation);
        if (prefMan.getReviewOrder())
            wv.js (ifReviews (ReviewOrder.JS_UNINIT_CODE));
        if (prefMan.getLessonOrder())
            wv.js (ifReviews (LessonOrder.JS_UNINIT_CODE));
    }

    /**
     * Hides the keyboard.
     */
    @Override
    public void reset ()
    {
        imm.hideSoftInputFromWindow (ew.getWindowToken (), 0);
        divw.setVisibility (View.GONE);
        showQuestionPatch(false);
    }

    @Override
    public void setMute (boolean m)
    {
        this.isMuted = m;

/*      Doesn't seem to work
        wv.js (ifLessons (m ? JS_LESSONS_MUTE : JS_LESSONS_UNMUTE) +
                ifReviews (m ? JS_REVIEWS_MUTE : JS_REVIEWS_UNMUTE));
*/
        // Use the old method instead:
        AudioManager am;
        am = (AudioManager) wav.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamMute (AudioManager.STREAM_MUSIC, m);
    }

    public void showQuestionPatch (boolean enable)
    {
        qvw.setVisibility (enable ? View.VISIBLE : View.GONE);
        bpos.qvisible = enable;
        wv.js (String.format (JS_SHOW_QUESTION, enable ? "hidden" : "visible"));
    }

    /**
     * Called when the HTML textbox is moved. It moves the edittext as well
     * @param frect the form rect
     * @param trect text textbox rect
     */
    protected void replace (Rect frect, Rect trect)
    {
        RelativeLayout.LayoutParams rparams;

        if (bpos.update (frect, trect)) {
            Utils utils = new Utils(wav);
            rparams = (RelativeLayout.LayoutParams) divw.getLayoutParams ();
            rparams.topMargin = frect.top + (int) utils.pxFromDp(8);
            rparams.leftMargin = frect.left + (int) utils.pxFromDp(8);
            rparams.width = LayoutParams.MATCH_PARENT;
            divw.setLayoutParams (rparams);
        }

        if (bpos.shallShow ()) {
            divw.setVisibility (View.VISIBLE);
            ew.requestFocus ();
            if (hwkeyb)
                imm.hideSoftInputFromWindow (ew.getWindowToken (), 0);
        }
    }

    private void adjustWidth (TextView view, RelativeLayout.LayoutParams params, String text)
    {
        Paint tpaint;

        tpaint = view.getPaint ();
        params.width = (int) Math.max (params.width, tpaint.measureText (text));
    }

    protected void showQuestion (WaniKaniItem.Type type, String name, Rect rect, int size)
    {
        RelativeLayout.LayoutParams params;
        Typeface jtf;

        if (!getOverrideFonts ())
            return;

        params = (RelativeLayout.LayoutParams) qvw.getLayoutParams ();
        params.topMargin = rect.top - 5;
        params.leftMargin = rect.left - 5;
        params.height = rect.height () + 10;
        params.width = rect.width () + 10;
        params.addRule (RelativeLayout.CENTER_HORIZONTAL);

        if (!name.endsWith (".png")) {
            qvw.setTextSize (size);
            //qvw.setBackgroundColor (cmap.get (type));
            qvw.setTextColor (Color.WHITE);
            qvw.setText (name);
            jtf = new Fonts().getKanjiFont(wav);
            qvw.setTypeface (jtf);
            adjustWidth (qvw, params, name);
            showQuestionPatch (jtf != null);
        } else
            showQuestionPatch (false);


        qvw.setLayoutParams (params);
    }

    /**
     * Called when the webview is scrolled. It moves the edittext as well
     * @param dx the horizontal displacement
     * @param dy the vertical displacement
     */
    protected void scroll (int dx, int dy)
    {
        RelativeLayout.LayoutParams rparams;

        rparams = (RelativeLayout.LayoutParams) divw.getLayoutParams ();
        rparams.topMargin -= dy;
        divw.setLayoutParams (rparams);

        rparams = (RelativeLayout.LayoutParams) qvw.getLayoutParams ();
        rparams.topMargin -= dy;
        qvw.setLayoutParams (rparams);

        bpos.shift (-dy);
    }

    /**
     * Called when the HTML textbox changes class. It changes the edit text accordingly.
     * @param clazz the CSS class
     * @param reviews if we are reviewing
     */
    public void setClass (String clazz, boolean reviews)
    {
        if (clazz.equals ("correct")) {
            enableIgnoreButton (false);
            disable (correctFG, correctBG);
        } else if (clazz.equals ("incorrect")) {
            if (prefMan.getAutoPopup())
                errorPopup ();
            if (prefMan.getMistakeDelay())
                wv.js (MistakeDelay.JS_MISTAKE);
            enableIgnoreButton (reviews && prefMan.getIgnoreButton());
            disable (incorrectFG, incorrectBG);
        } else if (clazz.equals ("WKO_ignored")) {
            enableIgnoreButton (false);
            disable (ignoredFG, ignoredBG);
        }
    }

    /**
     * Show the info popup
     */
    protected void errorPopup ()
    {
        imm.hideSoftInputFromWindow (ew.getWindowToken (), 0);
        wv.js (JS_INFO_POPUP);
    }

    /**
     * Called when the HTML textbox goes back to the default CSS class.
     */
    public void unsetClass (int level)
    {
        Integer res;

        enableIgnoreButton (false);
        enable ();

        wv.js (ifReviews (WaniKaniKunOn.JS_CODE));

        res = srsCols.get (level);
        if (prefMan.getSRSIndication() && res != null) {
            srsv.setVisibility (View.VISIBLE);
            srsv.setBackgroundResource(res);
        } else
            srsv.setVisibility (View.GONE);
    }

    /**
     * Called to set/unset ignore button visbility
     * @param enable if it should be visible
     */
    public void enableIgnoreButton (boolean enable)
    {
        canIgnore = enable;
        wav.updateCanIgnore ();
    }

    /**
     * Disables editing on the edit text
     * @param fg foreground color
     * @param backgroundResource background resource
     */
    private void disable (int fg, int backgroundResource)
    {
        editable = false;
        ew.setTextColor (fg);
        next.setTextColor(fg);
        divw.setBackgroundResource(backgroundResource);
        ew.setCursorVisible(false);
    }

    /**
     * Enable editing on the edit text. It also clears the contents.
     */
    private void enable ()
    {
        editable = true;
        ew.setText ("");
        ew.setTextColor(wav.getResources().getColor(R.color.text_gray));
        next.setTextColor(wav.getResources().getColor(R.color.text_gray));
        divw.setBackgroundResource(R.drawable.card_reviews_edittext);
        ew.setCursorVisible(true);

        if (!hwkeyb)
            imm.showSoftInput (ew, 0);
        ew.requestFocus ();
    }

    /**
     * Runs the ignore button script.
     */
    @Override
    public void ignore ()
    {
        wv.js (IgnoreButton.JS_CODE);
    }

    /**
     * Tells if the ignore button can be shown
     * @return <tt>true</tt> if the current answer is wrong
     */
    @Override
    public boolean canIgnore ()
    {
        return canIgnore;
    }

    public static String ifReviews (String js)
    {
        return "if (" + JS_REVIEWS_P + ") {" + js + "}";
    }

    public static String ifLessons (String js)
    {
        return "if (!" + JS_REVIEWS_P + ") {" + js + "}";
    }

    @Override
    public boolean getOverrideFonts ()
    {
        SharedPreferences prefs;

        if (!canOverrideFonts ())
            return false;

        prefs = PreferenceManager.getDefaultSharedPreferences (wav);

        return prefs.getBoolean (PREF_FONT_OVERRIDE, false);
    }

    protected boolean toggleFontOverride ()
    {
        SharedPreferences prefs;
        boolean ans;

        prefs = PreferenceManager.getDefaultSharedPreferences (wav);
        ans = !prefs.getBoolean (PREF_FONT_OVERRIDE, false);
        prefs.edit ().putBoolean (PREF_FONT_OVERRIDE, ans).commit ();

        return ans;
    }

    @Override
    public boolean canOverrideFonts ()
    {
        return true;
    }

    @Override
    public void overrideFonts ()
    {
        if (toggleFontOverride ())
            wv.js (JS_OVERRIDE);
        else
            showQuestionPatch (false);
    }

    protected void setInputType ()
    {
        boolean tren, trjp;
        trjp = prefMan.getNoSuggestion();
        tren = prefMan.getRomaji();

        if (trjp || tren) {
            disableSuggestions = true;
            if ((imel.translate && trjp) || (!imel.translate && tren))
                ew.setInputType (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            else
                ew.setInputType (InputType.TYPE_CLASS_TEXT);
        } else if (disableSuggestions) {	// This strange trick is to avoid messing with inputtype when not needed
            disableSuggestions = false;
            ew.setInputType (InputType.TYPE_CLASS_TEXT);
        }
    }

    private boolean updateSequence (int sequence)
    {
        if (sequence > lastSequence) {
            lastSequence = sequence;
            return true;
        } else
            return false;
    }

}