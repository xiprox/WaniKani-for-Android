package tr.xip.wanikani.userscripts;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import tr.xip.wanikani.app.activity.FocusWebView;
import tr.xip.wanikani.LocalIMEKeyboard;
import tr.xip.wanikani.R;
import tr.xip.wanikani.app.activity.WebReviewActivity;

/**
 * This is an integration of Seiji's "WaniKani Improve":
 * 			https://www.wanikani.com/chat/api-and-third-party-apps/2952
 * This script provides two features:
 * <ul>
 * 	<li>Fast forward: skip to next item, if previous has been entered correctly
 *  <li>Add HTML button opens a window showing information about previous item
 * </ul>
 * I integrated the first feature directly from the original script.
 * The second one is implemented using a native Android dialog, and the code is something
 * like a translation of JS to Java.
 */
/*
 * Implementation details: Seiji's script stores state information as a set of separate
 * variables. Here, instead, we push all this information through the JS bridge into this
 * class, where it is stored as an instance of the State structure.
 * I had also to break the original script into an initialization code, which builds
 * the HTML button, and a trigger part, that is run each time the "next" button is pressed.
 *
 * Since an upgrade broke the "last item info", I added some code from 2.2.12 (which is rather
 * difficult to integrate) that brings the info popup back
 */
public class WaniKaniImprove {

    /**
     * The current state.
     */
    private class State implements Runnable {

        /// Last item
        private String item;

        /// Last item type
        private String type;

        /// In case this is a radical, this is its name
        private String jstoreEn [];

        /**
         * Constrcutor
         * @param item last item
         * @param type last item type
         * @param jstoreEn in case this is a radical, its name
         */
        public State (String item, String type, String jstoreEn [])
        {
            this.item = item;
            this.type = type;
            this.jstoreEn = jstoreEn;
        }

        /**
         * Called when we should display the last item info dialog.
         * This method can be safely called from any thread.
         */
        public void publish ()
        {
            activity.runOnUiThread (this);
        }

        /**
         * Returns the quickview URL for the current item
         * @return the URL
         */
        public String getURL ()
        {
            StringBuffer sb;

			/* This is obsolete: will be fixed as soon as I integrate version WKI 2.0 */
            sb = new StringBuffer ("https://www.wanikani.com/");

            if (type.equals ("kanji"))
                sb.append ("kanji/").append (item).append ('/');
            else if (type.equals ("vocabulary"))
                sb.append ("vocabulary/").append (item).append ('/');
            else
                sb.append ("radicals/").
                        append (jstoreEn [0].toLowerCase ().replace (' ', '-')).
                        append ('/');

            return sb.toString ();
        }

        /**
         * Shows the dialog. To be called inside the UI thread.
         */
        public void run ()
        {
            showDialog (this);
        }
    }

    /**
     * WebViewClient implementation for the intergrated browser inside the item info dialog.
     */
    private class WebViewClientImpl extends WebViewClient {

        /// The dialog owning the webview
        Dialog dialog;

        /// Progress bar, to show how the download is going
        ProgressBar pb;

        /// A text view that can display error messages, if something goes wrong
        TextView tv;

        /// The dialog title
        String title;

        /// This code comes from WKI 2.2.12
        private static final String JS_TAILOR =
                "var wki_iframe_content = $('body');\r\n" +
                        "wki_iframe_content.append('<style>.footer-adjustment, footer {display: none !important} body {margin: 10px !important;} section {margin: 0 !important; } .container {margin: 0 !important; } .level-icon { min-height: 52px; float: left;} .vocabulary-icon, .kanji-icon, .radical-icon {float: right; width: 83%; height: auto; padding-left: 0 !important; padding-right: 0 !important; min-height: 52px;} .wki_iframe_header {font-weight: bold; text-align: center; line-height: 55px} .wki_iframe_section {margin: 30px 0 0 !important} .wki_iframe_section:after {clear: both; } .wki_iframe_section h2 {border-bottom: 1px solid rgb(212, 212, 212) !important; margin: 15px 0 7px !important;} .wki_iframe_header .enlarge-hover { display: none !important; } </style>');\r\n" +
                        "\r\n" +
                        "var wki_iframe_item = wki_iframe_content.find('header>h1');\r\n" +
                        "var wki_iframe_item_progress = wki_iframe_content.find('#progress').addClass('wki_iframe_section').wrap('<div></div>').parent().html();\r\n" +
                        "var wki_iframe_item_alternative_meaning = wki_iframe_content.find('#information').addClass('individual-item').wrap('<div></div>').parent();\r\n" +
                        "{\r\n" +
                        "       var wki_iframe_item_reading = wki_iframe_content.find('h2:contains(\"Reading\")').parent('section').addClass('wki_iframe_section');\r\n" +
                        "\r\n" +
                        "       $('<h2>', {'class' : 'wki_iframe_header'}).appendTo(wki_iframe_content).append(wki_iframe_item.children()).append('<br style=\"clear: both;\" />');\r\n" +
                        "       wki_iframe_content.append(wki_iframe_item_reading);\r\n" +
                        "}\r\n" +
                        "{\r\n" +
                        "       if(document.URL.indexOf ('radical') >= 0)\r\n" +
                        "       {\r\n" +
                        "                var wki_iframe_item_meaning = wki_iframe_content.find('h2:contains(\"Name\")').parent('section').addClass('wki_iframe_section');\r\n" +
                        "       }\r\n" +
                        "       else\r\n" +
                        "       {\r\n" +
                        "                var wki_iframe_item_meaning = wki_iframe_content.find('h2:contains(\"Meaning\")').parent('section').addClass('wki_iframe_section');\r\n" +
                        "       }\r\n" +
                        "       wki_iframe_content.append('<h2 class=\"wki_iframe_header\">' + wki_iframe_item.html() + '</h2>');\r\n" +
                        "       wki_iframe_content.append(wki_iframe_item_alternative_meaning);\r\n" +
                        "       wki_iframe_content.append(wki_iframe_item_meaning);\r\n" +
                        "}\r\n" +
                        "wki_iframe_content.append(wki_iframe_item_progress);\r\n";


        /**
         * Constructor
         * @param dialog the enclosing dialog
         * @param title its title
         */
        WebViewClientImpl (Dialog dialog, String title)
        {
            this.dialog = dialog;
            this.title = title;

            pb = (ProgressBar) dialog.findViewById (R.id.pb_lastitem);
            tv = (TextView) dialog.findViewById (R.id.tv_lastitem_message);
        }

        @Override
        public void onPageStarted (WebView view, String url, Bitmap favicon)
        {
            pb.setVisibility (View.VISIBLE);
        }

        @Override
        public void onReceivedError (WebView view, int errorCode, String description, String failingUrl)
        {
            dialog.setTitle (title);
            pb.setVisibility (View.GONE);
            tv.setText (activity.getResources ().getString (R.string.error_no_connection));
            tv.setVisibility (View.VISIBLE);
            view.setVisibility (View.GONE);
        }

        @Override
        public void onPageFinished (WebView view, String url)
        {
            dialog.setTitle (title);
            pb.setVisibility (View.GONE);

            view.loadUrl ("javascript:(function() { " + JS_TAILOR + "})()");
        }
    }

    /// This code must be run when the page is initially shown, to show the info button
    private static final String JS_INIT_PAGE =

// Original script
            "\r\n" +
                    "$('<li id=\"option-show-previous\"><span title=\"Check previous item\" lang=\"ja\"><i class=\"icon-question-sign\"></i></span></li>').insertAfter('#option-last-items').addClass('disabled');\r\n" +
                    "$('<style type=\"text/css\"> .qtip{ max-width: 380px !important; } #additional-content ul li { width: 16% !important; } #additional-content {text-align: center;} #option-show-previous img { max-width: 12px; background-color: #00A2F3; padding: 2px 3px; }</style>').appendTo('head');\r\n" +
                    "\r\n" +

// Glue code, to bind the button to the JS bridge
                    "$('#option-show-previous').on('click', function (event)" +
                    "{" +
                    "	wknWanikaniImprove.show ();" +
                    "});";

    private static final String JS_UNINIT_PAGE =
            "$('#option-show-previous').remove ()";

    /// This code must be run when the "next" button is pressed.
    private static final String JS_CODE =
// Original script
            "function checkAnswer()\r\n" +
                    "{\r\n" +
                    "    answerException = $.trim($('#answer-exception').text());\r\n" +

                    // Added by @Aralox, use these lines to print the page HTML, for debugging.
//                    "    console.log('answer message: '+answerException);" +
//                    "    console.log('document: ');" +
//                    "    doclines = $('body').html().split('\\n');" +
//                    "    for (var di = 0; di < doclines.length; di++) { console.log(doclines[di]); } " +

                    "    if(answerException)\r\n" +
                    "    {\r\n" +
                    "        if(answerException.indexOf('answer was a bit off') !== -1)\r\n" +
                    "        {\r\n" +
                    "            console.log('answerException: your answer was a bit off');\r\n" +
                    "            $('#option-show-previous span').css('background-color', '#F5F7AB').attr('title', 'Your answer was a bit off');\r\n" +
                    "        }\r\n" +
                    "        else if(answerException.indexOf('possible readings') !== -1)\r\n" +
                    "        {\r\n" +
                    "            console.log('answerException: other possible readings');\r\n" +
                    "            $('#option-show-previous span').css('background-color', '#CDE0F7').attr('title', 'There are other possible readings');\r\n" +
                    "        }\r\n" +
                    "        else if(answerException.indexOf('possible meanings') !== -1)\r\n" +
                    "        {\r\n" +
                    "            console.log('answerException: other possible meanings');\r\n" +
                    "            $('#option-show-previous span').css('background-color', '#CDE0F7').attr('title', 'There are other possible meanings');\r\n" +
                    "        }\r\n" +
                    "        else if(answerException.indexOf('View the correct') !== -1)\r\n" +
                    "        {\r\n" +
                    "            console.log('answerException: wrong answer');\r\n" +
                    "        }\r\n" +
                    "        else\r\n" +
                    "        {\r\n" +
                    "            console.log('answerException: ' + answerException);\r\n" +
                    "            $('#option-show-previous span').css('background-color', '#FBFBFB');\r\n" +
                    "        }\r\n" +
                    "    }\r\n" +
                    "    else\r\n" +
                    "    {\r\n" +
                    "        $('#option-show-previous span').css('background-color', '#FBFBFB');\r\n" +
                    "    }\r\n" +
                    "    \r\n" +
                    "    if ($('#answer-form form fieldset').hasClass('correct'))\r\n" +
                    "    {\r\n" +
                    "        console.log('Correct answer');\r\n" +
                    "        moveNext();\r\n" +
                    "\r\n" +
                    "    }\r\n" +
                    "    else if ($('#answer-form form fieldset').hasClass('incorrect'))\r\n" +
                    "    {\r\n" +
                    "        console.log('Wrong answer');\r\n" +
                    "    }\r\n" +
                    "}\r\n" +
                    "\r\n" +
                    "function moveNext()\r\n" +
                    "{\r\n" +
                    "    console.log('Moving to next question');\r\n" +
                    "    $('#answer-form button').click();\r\n" +
                    "}" +

// Glue code, pushing status info and triggering checkAnswer()
                    "	 jstored_currentItem = $.jStorage.get('currentItem');" +
                    "    $('#option-show-previous').removeClass('disabled');" +
                    "    currentItem = $.trim($('#character span').html());" +
                    "    currentType = $('#character').attr('class');" +
                    "    currentQuestionType = $.trim($('#question-type').text());" +
                    "    wknWanikaniImprove.save (currentItem, currentType, currentQuestionType, jstored_currentItem.en);" +


                    // @Aralox introduced the delay, to allow enough time for the javascript to update the html so that this
                    // script works properly after the 'info-panel' is opened. (https://github.com/xiprox/WaniKani-for-Android/issues/46)
                    "    setTimeout(checkAnswer, 300);";
                    //"    checkAnswer();"; // old

    /// The current state
    private State currentState;

    /// The main activity
    private WebReviewActivity activity;

    /// The main WebView
    private FocusWebView wv;

    /**
     * Constructor.
     * @param activity the main actibity
     * @param wv the webview
     */
    public WaniKaniImprove (WebReviewActivity activity, FocusWebView wv)
    {
        this.activity = activity;
        this.wv = wv;

        wv.addJavascriptInterface (this, "wknWanikaniImprove");
    }

    /**
     * Initializes the page. Must be called when the reviews page is entered
     */
    public void initPage ()
    {
        currentState = null;
        wv.js (LocalIMEKeyboard.ifReviews(JS_INIT_PAGE));
    }

    /**
     * Deinitializes the page.
     */
    public void uninitPage ()
    {
        wv.js (JS_UNINIT_PAGE);
    }

    /**
     * Save state.
     * @param currentItem current item name
     * @param currentType current item type
     * @param currentQuestionType current question tyoe
     * @param jstoreEn radical name
     */
    @JavascriptInterface
    public void save (String currentItem, String currentType, String currentQuestionType, String jstoreEn [])
    {
        currentState = new State (currentItem, currentType, jstoreEn);
    }

    /**
     * Shows the dialog. Must be bound to the "last item info" button.
     */
    @JavascriptInterface
    public void show ()
    {
        if (currentState != null)
            currentState.publish ();
    }

    /**
     * Shows the dialog. Must be run on the main UI thread
     * @param state the state to be published
     */
    private void showDialog (State state)
    {
        Resources res;
        WebView webview;
        Dialog dialog;
        String title;

        if (!activity.visible)
            return;

        res = activity.getResources ();

        dialog = new Dialog (activity);
        dialog.setTitle (res.getString (R.string.fmt_last_item_wait));
        dialog.setContentView (R.layout.lastitem);
        webview = (WebView) dialog.findViewById (R.id.wv_lastitem);
        webview.getSettings ().setJavaScriptEnabled (true);
        title = res.getString (R.string.fmt_last_item, state.type);
        webview.setWebViewClient (new WebViewClientImpl (dialog, title));
        webview.loadUrl (state.getURL ());

        dialog.show ();
    }

    public static String getCode ()
    {
        return LocalIMEKeyboard.ifReviews (JS_CODE);
    }

}