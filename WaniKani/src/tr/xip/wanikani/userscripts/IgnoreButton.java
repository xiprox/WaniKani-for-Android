package tr.xip.wanikani.userscripts;


/* -- Original description and copyright statements -- */

/* ==== Wanikani Override 1.0.4 ==== == by LordGravewish ==
 *
 * For more information about this script including its changelog go to
 * https://www.wanikani.com/chat/api-and-third-party-apps/2940/

 * DISCLAIMER: I am not responsible for any problems caused by this script.
 * Also, anyone using this script is responsible for using it correctly.
 * This should be used only if you make an honest mistake but actually knew
 * the correct answer. Using it in any other way will harm your Kanji learning
 * process, cheating will only make learning Japanese harder and you'll end up
 * harming only yourselves!

 *  This script is licensed under the Creative Commons License
 *  "Attribution-NonCommercial 3.0 Unported"
 */

/*
 * I've basically copied here only the contents of unsafeWindow.WKO_ignoreAnswer, while the
 * triggering is made by users of this class (only LocalIMEKeyboard, so far).
 * 		-- Alberto
 */
public class IgnoreButton {

    public static final String JS_CODE =
            "/* Check if the current item was answered incorrectly */\r\n" +
                    "   var elmnts = document.getElementsByClassName(\"incorrect\");\r\n" +
                    "	if(elmnts[0] === undefined)\r\n" +
                    "	{\r\n" +
                    "		alert(\"Wanikani Override error: Current item wasn't answered incorrectly!\");\r\n" +
                    "		return false;\r\n" +
                    "	}\r\n" +
                    "	\r\n" +
                    "	/* Grab information about current question */\r\n" +
                    "	var curItem = $.jStorage.get(\"currentItem\");\r\n" +
                    "	var questionType = $.jStorage.get(\"questionType\");\r\n" +
                    "  \r\n" +
                    "	/* Build item name */\r\n" +
                    "	var itemName;\r\n" +
                    "	\r\n" +
                    "	if(curItem.rad)\r\n" +
                    "	{" +
                    "	itemName = \"r\";\r\n" +
                    "   }" +
                    "	else if(curItem.kan)\r\n" +
                    "   {" +
                    "		itemName = \"k\";\r\n" +
                    "	}" +
                    "   else\r\n" +
                    "	{" +
                    "   	itemName = \"v\";\r\n" +
                    "	}" +
                    "\r\n" +
                    "	itemName += curItem.id;\r\n" +
                    "	\r\n" +
                    "	/* Grab item from jStorage.\r\n" +
                    "	 * \r\n" +
                    "	 * item.rc and item.mc => Reading/Meaning Completed (if answered the item correctly)\r\n" +
                    "	 * item.ri and item.mi => Reading/Meaning Invalid (number of mistakes before answering correctly)\r\n" +
                    "	 */\r\n" +
                    "	var item = $.jStorage.get(itemName) || {};\r\n" +
                    "	\r\n" +
                    "	/* Update the item data to ignore the fact we got it wrong this time */\r\n" +
                    "	if(questionType === \"meaning\")\r\n" +
                    "  	{\r\n" +
                    "		if(typeof item.mi == \"undefined\")\r\n" +
                    "		{\r\n" +
                    "	  		alert(\"Wanikani Override error: i.mi undefined.\");\r\n" +
                    "	  		return false;\r\n" +
                    "	  	}\r\n" +
                    "	  	else if(item.mi <= 0)\r\n" +
                    "	  	{\r\n" +
                    "	  		alert(\"Wanikani Override error: i.mi <= 0\");\r\n" +
                    "	  		return false;\r\n" +
                    "	  	}\r\n" +
                    "	  	\r\n" +
                    "	  	item.mi -= 1;\r\n" +
                    "	  	delete item.mc;\r\n" +
                    "	}\r\n" +
                    "	else\r\n" +
                    "	{\r\n" +
                    "		if(typeof item.ri == \"undefined\")\r\n" +
                    "		{\r\n" +
                    "	  		alert(\"Wanikani Override error: i.ri undefined.\");\r\n" +
                    "	  		return false;\r\n" +
                    "	  	}\r\n" +
                    "	  	else if(item.ri <= 0)\r\n" +
                    "	  	{\r\n" +
                    "	  		alert(\"Wanikani Override error: i.ri <= 0\");\r\n" +
                    "	  		return false;\r\n" +
                    "	  	}\r\n" +
                    "	  	\r\n" +
                    "	  	item.ri -= 1;\r\n" +
                    "	  	delete item.rc;\r\n" +
                    "	}\r\n" +
                    "	\r\n" +
                    "	/* Save the new state back into jStorage */\r\n" +
                    "	$.jStorage.set(itemName, item);\r\n" +
                    "	\r\n" +
                    "	/* Decrement the questions counter and wrong counter */\r\n" +
                    "	var wrongCount = $.jStorage.get(\"wrongCount\");\r\n" +
                    "	var questionCount = $.jStorage.get(\"questionCount\");\r\n" +
                    "	$.jStorage.set(\"wrongCount\", wrongCount-1);\r\n" +
                    "	$.jStorage.set(\"questionCount\", questionCount-1);\r\n" +
                    "	\r\n" +
                    "	/* Make the answer field yellow instead of red */\r\n" +
                    "	$(\"#answer-form fieldset\").removeClass(\"incorrect\");\r\n" +
                    "	$(\"#answer-form fieldset\").addClass(\"WKO_ignored\");\r\n" +
                    "	\r\n" +
                    "	return true;";
}