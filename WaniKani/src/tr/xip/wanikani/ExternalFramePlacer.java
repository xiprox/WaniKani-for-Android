package tr.xip.wanikani;

import tr.xip.wanikani.app.activity.FocusWebView;

/*
 * I've basically copied here only the contents of unsafeWindow.WKO_ignoreAnswer, while the
 * triggering is made by users of this class (only LocalIMEKeyboard, so far).
 * Also, had to move the onItemUpdated() function outside of main(), to call it at startup.
 * This is needed to make the frame appear on the first item.
 * 		-- Alberto
 */
public class ExternalFramePlacer {

    /* The tags must be in sync with arrays.xml: SettingsActivity depends on this */
    public enum Dictionary {
        JISHO {
            public String kanji ()
            {
                return "http://jisho.org/kanji/details/{0}";
            }

            public String vocab ()
            {
                return "http://jisho.org/words?common=on&jap={0}";
            }
        },
        TANGORIN {
            public String kanji ()
            {
                return "http://tangorin.com/kanji/{0}";
            }

            public String vocab ()
            {
                return "http://tangorin.com/general/{0}";
            }
        },
        EJJE {
            public String kanji ()
            {
                return "http://ejje.weblio.jp/content/{0}";
            }

            public String vocab ()
            {
                return "http://ejje.weblio.jp/content/{0}";
            }
        },
        JISHO_BETA {
            public String kanji ()
            {
                return "http://beta.jisho.org/search/{0}%23kanji";
            }

            public String vocab ()
            {
                return "http://beta.jisho.org/search/{0}";
            }
        };

        public abstract String kanji ();
        public abstract String vocab ();
    }

    public static final String JS_CODE =
            "(function() {\r\n" +
                    "  /**\r\n" +
                    "   * Custom String.format() method. Can be called using \"\".format(args).\r\n" +
                    "   * Does very basic formatting, Python3 style.\r\n" +
                    "   */\r\n" +
                    "  if (typeof String.prototype.format != 'function') {\r\n" +
                    "    String.prototype.format = function() {\r\n" +
                    "      var s = this.valueOf();\r\n" +
                    "      for (var i = 0; i < arguments.length; i++) {\r\n" +
                    "        s = s.replace(new RegExp(\"\\\\{\" + i + \"\\\\}\", \"gm\"), arguments[i]);\r\n" +
                    "      }\r\n" +
                    "\r\n" +
                    "      return s;\r\n" +
                    "    }\r\n" +
                    "  }\r\n" +
                    "\r\n" +
/*
"  // Register GM commands for changing to definition source and URLs.\r\n" +
"  if (GM_registerMenuCommand) {\r\n" +
"    GM_registerMenuCommand('WaniKani External Frame Placer: Change Kanji URL', function() {\r\n" +
"      var newKanjiURL = prompt('New Kanji URL:');\r\n" +
"      if (newKanjiURL) {\r\n" +
"        $.jStorage.set(Utility.URL_KEY_FORMAT.format(Utility.KANJI), newKanjiURL);\r\n" +
"      }\r\n" +
"    });\r\n" +
"\r\n" +
"    GM_registerMenuCommand('WaniKani External Frame Placer: Change Vocab URL', function() {\r\n" +
"      var newVocabURL = prompt('New Vocab URL:');\r\n" +
"      if (newVocabURL) {\r\n" +
"        $.jStorage.set(Utility.URL_KEY_FORMAT.format(Utility.VOCAB), newVocabURL);\r\n" +
"      }\r\n" +
"    });\r\n" +
"  }\r\n" +
"  \r\n" +
*/
                    "  var Utility = {\r\n" +
                    "    /*\r\n" +
                    "     * Some variables (which don't have to be configurable) but make the code\r\n" +
                    "     * nicer to read.\r\n" +
                    "     */\r\n" +
                    "    KANJI: 'kan', VOCAB: 'voc', HEADING_FORMAT: 'External Definition',\r\n" +
                    "    LESSON_INSERTION_POINT_FORMATS: ['#supplement-{0} #supplement-{0}-meaning .col2', '#supplement-{0} #supplement-{0}-reading .col2'],\r\n" +
                    "    DETAIL_INSERTION_POINT: 'section.information',\r\n" +
                    "    \r\n" +
                    "    /*\r\n" +
                    "     * Keys to use in local storage and their defaults.\r\n" +
                    "     */\r\n" +
                    "    URL_KEY_FORMAT: 'jisho-frame-placer-{0}-url',\r\n" +
                    "    DEFINITION_SOURCE_KEY: 'jisho-frame-placer-definition-source', DEFINITION_SOURCE_DEFAULT: 'Jisho.org',\r\n" +
                    "    WK_CURRENT_ITEM_KEY: 'l/currentLesson',\r\n" +
                    "    KANJI_URL_DEFAULT: 'http://jisho.org/kanji/details/{0}', VOCAB_URL_DEFAULT: 'http://jisho.org/words?common=on&jap={0}',\r\n" +
                    "    \r\n" +
                    "    /*\r\n" +
                    "     * Utility functions to make the main body simpler.\r\n" +
                    "     */\r\n" +
                    "    /**\r\n" +
                    "     * Obtain the frame URL type from storage. Defaults to vocabulary if the given\r\n" +
                    "     * type is not recognized.\r\n" +
                    "     */\r\n" +
                    "    getFrameURL: function(type) {\r\n" +
                    "      // Instantiate the defaults if they aren't in place already.\r\n" +
/*
"      if (!$.jStorage.get(Utility.URL_KEY_FORMAT.format(Utility.KANJI))) {\r\n" +
"        $.jStorage.set(Utility.URL_KEY_FORMAT.format(Utility.KANJI), Utility.KANJI_URL_DEFAULT);\r\n" +
"      }\r\n" +
"      \r\n" +
"      if (!$.jStorage.get(Utility.URL_KEY_FORMAT.format(Utility.VOCAB))) {\r\n" +
"        $.jStorage.set(Utility.URL_KEY_FORMAT.format(Utility.VOCAB), Utility.VOCAB_URL_DEFAULT);\r\n" +
"      }\r\n" +
"      \r\n" +
*/
/* Glue code */
                    "        $.jStorage.set(Utility.URL_KEY_FORMAT.format(Utility.KANJI), '%s');\r\n" +
                    "        $.jStorage.set(Utility.URL_KEY_FORMAT.format(Utility.VOCAB), '%s');\r\n" +

                    "      // Get the requested type, or default to a vocab item if `type` is invalid.\r\n" +
                    "      return $.jStorage.get(Utility.URL_KEY_FORMAT.format(type)) || $.jStorage.get(Utility.URL_KEY_FORMAT.format(Utility.VOCAB));\r\n" +
                    "    },\r\n" +
                    "    \r\n" +
                    "    /**\r\n" +
                    "     * Obtain the name of the source of the definition.\r\n" +
                    "     */\r\n" +
                    "    getDefinitionSource: function() {\r\n" +
                    "      if (!$.jStorage.get(Utility.DEFINITION_SOURCE_KEY)) {\r\n" +
                    "        $.jStorage.set(Utility.DEFINITION_SOURCE_KEY, Utility.DEFINITION_SOURCE_DEFAULT);\r\n" +
                    "      }\r\n" +
                    "      \r\n" +
                    "      return $.jStorage.get(Utility.DEFINITION_SOURCE_KEY);\r\n" +
                    "    },\r\n" +
                    "     \r\n" +
                    "    /**\r\n" +
                    "     * Given a container to store a frame, insert both a frame and heading, matching roughly this format:\r\n" +
                    "     *\r\n" +
                    "     *  <h2>{Heading}</h2>\r\n" +
                    "     *  <div>\r\n" +
                    "     *    <iframe />\r\n" +
                    "     *  </div>\r\n" +
                    "     *\r\n" +
                    "     * If the heading is clicked, the following div will collapse and hide.\r\n" +
                    "     * @param container A HTML element (using document.createElement).\r\n" +
                    "     * @param japanese The Japanese query to inject into the frame.\r\n" +
                    "     * @param type The type of query to make (either KANJI or VOCAB).\r\n" +
                    "     */\r\n" +
                    "    insertFrame: function(container, japanese, type) {\r\n" +
                    "      // Get the URL to construct the frame with, based on 'type'. Assume it is\r\n" +
                    "      // vocabulary if the defined type is invalid.\r\n" +
                    "      var frameURL = Utility.getFrameURL(type).format(japanese);\r\n" +
                    "      \r\n" +
                    "      // Try and find an iframe in the target container. It may already exist there\r\n" +
                    "      // from a previous invocation of this method. If it does, just replace the URL.\r\n" +
                    "      var currentIframe = container.querySelector('iframe');\r\n" +
                    "      if (currentIframe) {\r\n" +
                    "        currentIframe.setAttribute('src', frameURL);\r\n" +
                    "        return;\r\n" +
                    "      }\r\n" +
                    "      \r\n" +
                    "      // There must be no iframe... let's construct a new one! Frames can be a little\r\n" +
                    "      // tricky... if an input is selected in the frame, all keyboard shortcuts (e.g.\r\n" +
                    "      // during lessons) won't work; to work around this, create a text box in the main\r\n" +
                    "      // DOM, focus it and then remove it.\r\n" +
                    "      // If there is no iframe, create a new one.\r\n" +
                    "      var newIframe = document.createElement('iframe');\r\n" +
                    "      newIframe.onload = function() {\r\n" +
                    "        // Create a temporary input and get a reference to the body.\r\n" +
                    "        var tempInput = document.createElement('input');\r\n" +
                    "        var body = document.querySelector('body');\r\n" +
                    "        \r\n" +
                    "        // Insert the temporary element and focus it.\r\n" +
                    "        body.insertBefore(tempInput, body.firstChild);\r\n" +
                    "        tempInput.focus();\r\n" +
                    "        \r\n" +
                    "        // Remove the temporary element.\r\n" +
                    "        body.removeChild(tempInput);\r\n" +
                    "      }\r\n" +
                    "      newIframe.setAttribute('src', frameURL);\r\n" +
                    "      newIframe.style.width = '100%%';\r\n" +
                    "      newIframe.style.height = '600px';\r\n" +
                    "      \r\n" +
                    "      // Create a container div for the iframe and insert it.\r\n" +
                    "      var iframeContainerDiv = document.createElement('div');\r\n" +
                    "      iframeContainerDiv.appendChild(newIframe);\r\n" +
                    "      \r\n" +
                    "      // Create a header to put into the top level of the container.\r\n" +
                    "      // When the header is clicked, hide the iframe container div.\r\n" +
                    "      var header = document.createElement('h2');\r\n" +
                    "      header.innerHTML = '<i class=\"icon-chevron-down\"></i> ' + Utility.HEADING_FORMAT.format(Utility.getDefinitionSource());\r\n" +
                    "      header.onclick = function() {\r\n" +
                    "        if (iframeContainerDiv.style.display === 'none') {\r\n" +
                    "          iframeContainerDiv.style.display = 'block';\r\n" +
                    "          header.querySelector('i').setAttribute('class', 'icon-chevron-down');\r\n" +
                    "        } else {\r\n" +
                    "          iframeContainerDiv.style.display = 'none';\r\n" +
                    "          header.querySelector('i').setAttribute('class', 'icon-chevron-right');\r\n" +
                    "        }\r\n" +
                    "      }\r\n" +
                    "      \r\n" +
                    "      // Add the header first, then the frame to the container.\r\n" +
                    "      container.appendChild(header);\r\n" +
                    "      container.appendChild(iframeContainerDiv);\r\n" +
                    "    }\r\n" +
                    "  };\r\n" +
                    "  function onItemUpdated(key) {" +
                    "        // Get the new value of the current item.\r\n" +
                    "        var newValue = $.jStorage.get(key);\r\n" +
                    "        \r\n" +
                    "        // Get the Japanese text and they type.\r\n" +
                    "        var japaneseType = japanese = null;\r\n" +
                    "        if (newValue.voc) {\r\n" +
                    "          japaneseType = Utility.VOCAB;\r\n" +
                    "          japanese = newValue[Utility.VOCAB];\r\n" +
                    "        } else if (newValue.kan) {\r\n" +
                    "          japaneseType = Utility.KANJI;\r\n" +
                    "          japanese = newValue[Utility.KANJI];\r\n" +
                    "        } else {\r\n" +
                    "          // Unrecognized type?\r\n" +
                    "          return;\r\n" +
                    "        }\r\n" +
                    "        \r\n" +
                    "        // Find the div containing the meaning.\r\n" +
                    "        for (var i in Utility.LESSON_INSERTION_POINT_FORMATS) {\r\n" +
                    "          var container = document.querySelector(Utility.LESSON_INSERTION_POINT_FORMATS[i].format(japaneseType));\r\n" +
                    "          Utility.insertFrame(container, japanese, japaneseType);\r\n" +
                    "        }\r\n" +
                    "  }" +
                    "  \r\n" +
                    "  function main() {\r\n" +
                    "    // If we are currently in a lesson...\r\n" +
                    "    var currentURL = window.location.href;\r\n" +
                    "    if (currentURL.indexOf('lesson/session') >= 0) {\r\n" +
                    "      $.jStorage.listenKeyChange(Utility.WK_CURRENT_ITEM_KEY, onItemUpdated);\r\n" +
                    "      item = $.jStorage.get(Utility.WK_CURRENT_ITEM_KEY, null);\r\n" +
                    "      if (item != null)\r\n" +
                    "          onItemUpdated (Utility.WK_CURRENT_ITEM_KEY);\r\n" +
                    "    }\r\n" +
                    "    \r\n" +
                    "    // Otherwise, we must be one either the vocab or kanji page.\r\n" +
                    "    else {\r\n" +
                    "      // Decide what type the word is, and get the word.\r\n" +
                    "      var japanese = currentURL.substr(currentURL.lastIndexOf('/') + 1);\r\n" +
                    "      var japaneseType = Utility.VOCAB;\r\n" +
                    "      if (currentURL.indexOf('kanji') >= 0) {\r\n" +
                    "        japaneseType = Utility.KANJI;\r\n" +
                    "      }\r\n" +
                    "      \r\n" +
                    "      // Make a container for the iframe.\r\n" +
                    "      var container = document.createElement('div');\r\n" +
                    "      document.querySelector('section#information').appendChild(container);\r\n" +
                    "      \r\n" +
                    "      // Insert the frame into the page.\r\n" +
                    "      Utility.insertFrame(container, japanese, japaneseType);\r\n" +
                    "    }\r\n" +
                    "  }\r\n" +
                    "\r\n" +
                    "  var waitForjStorageInterval = setInterval(function() {\r\n" +
                    "    if ($ && $.jStorage) {\r\n" +
                    "      clearInterval(waitForjStorageInterval);\r\n" +
                    "      main();\r\n" +
                    "    }\r\n" +
                    "  }, 50);\r\n" +
                    "})()";


    public static void run (FocusWebView wv, Dictionary dict)
    {
        wv.js (String.format (JS_CODE, dict.kanji (), dict.vocab ()));
    }
}