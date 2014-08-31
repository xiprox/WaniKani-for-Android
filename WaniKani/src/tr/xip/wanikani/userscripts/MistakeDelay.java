package tr.xip.wanikani.userscripts;

/* -- Original description and copyright statements -- */

/*
 *  ====  Wanikani Mistake Delay  ====
 *    ==     by Rui Pinheiro      ==
 *
 *  After the new client-side review system was introduced, reviews became blazing fast.
 *
 *  The downside of that speed was that I found myself pressing 'Enter' twice after answering
 *  any question I'm sure is correct, but then finding out it isn't, yet the second 'Enter' press
 *  already took effect and it skips automatically to the following item.
 *
 *  This script takes care of that problem, blocking 'Enter' presses for 1 second if a mistake is
 *  made. This makes 'Enter' spamming work only if the answer was correct, and if not will
 *  "force" you to take notice.
 *
 *
 *  Note that this script does not change the behaviour of the "Next" button.
 *
 *
 *  DISCLAIMER:
 *  I am not responsible for any problems caused by this script.
 *  This script was developed on Firefox 22.0 with Greasemonkey 1.10.
 *  Because I'm just one person, I can't guarantee the script works anywhere else.
 *  Though, it should also work on Chrome with Tampermonkey
 */

/*
 *	This program is free software: you can redistribute it and/or modify
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

//------------------
// Helper Functions/Variables
//------
/* Delay before reactivating enter in milliseconds
   (safe to modify according to personal preference) */


/*
 * We need only the contents of unsafeWindow.answerChecker.evaluate since most of the
 * original script code deals with intercepting the event (and we do this differently here).
 * 		-- Alberto
 */
public class MistakeDelay {

    public static final String JS_INIT =
            "window.WKMD_msDelay = 1000;" +
                    "window.WKMD_incorrectLastAnswer = false";

    public static final String JS_MISTAKE =
            "window.WKMD_incorrectLastAnswer = true;" +
                    "setTimeout (function () { " +
                    "	window.WKMD_incorrectLastAnswer = false; " +
                    "}, window.WKMD_msDelay);";

    public static final String injectAnswer (String s)
    {
        return "if (!window.WKMD_incorrectLastAnswer) { " + s + " }";
    }
}