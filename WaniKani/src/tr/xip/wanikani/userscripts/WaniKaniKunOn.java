package tr.xip.wanikani.userscripts;

/* -- Original description and copyright statements -- */

/*
 *  ====  Wanikani  KunOn  ====
 *    == by ruipgpinheiro  ==
 *
 *  This script changes "Kanji Reading" to "Kanji On'yomi" or "Kanji Kun'yomi" during reviews.
 *
 */

/*
 *  ====  LICENSE INFORMATION  ====
 *
 *  This script is licensed under the Creative Commons License
 *  "Attribution-NonCommercial 3.0 Unported"
 *
 *  More information at:
 *  http://creativecommons.org/licenses/by-nc/3.0/
 */

/*
 * This is almost an exact copy of the script. I removed the triggering code, because made
 * by users of this class (only LocalIMEKeyboard, so far).
 * 		-- Alberto
 */
public class WaniKaniKunOn {

    public static final String JS_CODE = "var debugLogEnabled = false;\r\n" +
                    "var scriptShortName = \"WKKO\";\r\n" +
                    "scriptLog = debugLogEnabled ? function(msg) { if(typeof msg === 'string'){ console.log(scriptShortName + \": \" + msg); }else{ console.log(msg); } } : function() {};\r\n" +
                    "\r\n" +
                    "function updateReadingText() {\r\n" +
                    "	var curItem = $.jStorage.get(\"currentItem\");\r\n" +
                    "	var questionType = $.jStorage.get(\"questionType\");\r\n" +
                    "	if(questionType == \"reading\" && \"kan\" in curItem) {\r\n" +
                    "		scriptLog(\"Kanji Reading!\");\r\n" +
                    "		var readingType = \"Reading\";\r\n" +
                    "		if(curItem.emph == \"onyomi\") readingType = \"On'yomi\";\r\n" +
                    "		else readingType = \"Kun'yomi\";\r\n" +
                    "		$('#question-type').html('<h1>Kanji <strong>' + readingType + '</strong></h1>');\r\n" +
                    "	}\r\n" +
                    "}\r\n" +
                    "updateReadingText();";

}