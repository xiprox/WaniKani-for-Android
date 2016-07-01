package tr.xip.wanikani;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

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

public class JapaneseIME {

    public static class Replacement {

        public int start;

        public int end;

        public String text;

        public Replacement (int start, int end, String text)
        {
            this.start = start;
            this.end = end;
            this.text = text;
        }

    }

    private Map<String, String> map;

    public JapaneseIME ()
    {
        map = new Hashtable<> ();

        populateTable ();
    }

    protected String lookup (String s, int start, int len)
    {
        s = s.substring (start, start + len);

        return map.get (s);
    }

    public String parse (String s)
    {
        StringBuffer sb;
        String xlated, res;
        boolean changed;
        int i, j;

        s = s.toLowerCase (Locale.US);

        changed = false;
        sb = new StringBuffer ();
        for (i = 0; i < s.length (); i++) {
            xlated = s.substring (i, i + 1);
            if (s.codePointAt (i) < 128) {
                for (j = 1; j <= 4; j++) {
                    if (i + j > s.length ())
                        break;
                    res = lookup (s, i, j);
                    if (res != null) {
                        xlated = res;
                        i += j - 1;
                        changed = true;
                        break;
                    }
                }
            }
            if (xlated.equals ("n"))
                xlated = "ん";
            sb.append (xlated);
        }

        return changed ? sb.toString () : s;
    }

    public Replacement replace (String s, int pos)
    {
        String xlated;
        int i;

        for (i = pos; i > 0; i--)
            if (s.codePointAt (i - 1) >= 128)
                break;

        if (i == pos)
            return null;

        s = s.substring (i, pos);
        xlated = parse (s);

        return !(s.equals(xlated)) ? new Replacement (i, pos, xlated) : null;
    }

    private void populateTable ()
    {
        sput ("a", "あ");
        sput ("i", "い");
        sput ("u", "う");
        sput ("e", "え");
        sput ("o", "お");

        put ("ka", "か");
        putI ("k", "き");
        putU ("k", "く");
        put ("ke", "け");
        put ("ko", "こ");

        put ("ga", "が");
        putI ("g", "ぎ");
        putU ("g", "ぐ");
        put ("ge", "げ");
        put ("go", "ご");

        put ("sa", "さ");
        putI ("s", "sh", "し");
        putU ("s", "す");
        put ("se", "せ");
        put ("so", "そ");

        put ("za", "ざ");
        putI ("z", "じ");
        putU ("z", "ず");
        put ("ze", "ぜ");
        put ("zo", "ぞ");

        put ("ta", "た");
        putI ("t", "ち");
        putU ("ts", "t", "つ");
        put ("te", "て");
        put ("to", "と");

        put ("da", "だ");
        putI ("d", "dj", "ぢ");
        putU ("d", "dz", "づ");
        put ("de", "で");
        put ("do", "ど");

        put ("ha", "は");
        putI ("h", "ひ");
        putU ("h", "ふ");
        put ("he", "へ");
        put ("ho", "ほ");

        put ("ba", "ば");
        putI ("b", "び");
        putU ("b", "ぶ");
        put ("be", "べ");
        put ("bo", "ぼ");

        put ("pa", "ぱ");
        putI ("p", "ぴ");
        putU ("p", "ぷ");
        put ("pe", "ぺ");
        put ("po", "ぽ");

        put ("na", "な");
        putI ("n", "に");
        putU ("n", "ぬ");
        put ("ne", "ね");
        put ("no", "の");

        put ("ma", "ま");
        putI ("m", "み");
        putU ("m", "む");
        put ("me", "め");
        put ("mo", "も");

        put ("ra", "ら");
        putI ("r", "り");
        putU ("r", "る");
        put ("re", "れ");
        put ("ro", "ろ");

        put ("ya", "や");
        put ("yi", "い");
        put ("yu", "ゆ");
        put ("ye", "いぇ");
        put ("yo", "よ");

        put ("wa", "わ");
        put ("wi", "うぃ");
        put ("wu", "う");
        put ("wo", "を");
        put ("we", "うぇ");

        put ("nn", "ん");

        put ("xa", "la", "ぁ");
        put ("xi", "li", "ぃ");
        put ("xu", "lu", "ぅ");
        put ("xe", "le", "ぇ");
        put ("xo", "lo", "ぉ");
        put ("xya", "lya", "ゃ");
        put ("xyu", "lyu", "ゅ");
        put ("xyo", "lyo", "ょ");
        put ("xtsu", "ltsu", "xtu", "ltu", "っ");

		/* Alt spellings */
        put ("ca", "か");
        putI ("c", "し");
        putU ("c", "く");
        put ("ce", "せ");
        put ("co", "こ");

        put ("cha", "ちゃ");
        putI ("ch", "ち");
        put ("cho", "ちょ");
        put ("che", "ちぇ");
        put ("chu", "ちゅ");

        put ("fa", "ふぁ");
        putI ("f", "ふ");
        put ("fi", "ふぃ");
        put ("fu", "ふ");
        put ("fe", "ふぇ");
        put ("fo", "ふぉ");

        put ("ja", "じゃ");
        putI ("j", "じ");
        put ("ji", "じ");
        put ("ju", "じゅ");
        put ("je", "じぇ");
        put ("jo", "じょ");

        put ("qa", "qwa", "くぁ");
        put ("qya", "くゃ");
        put ("qi", "qwi", "qyi", "くぃ");
        put ("qe", "qwe", "qye", "くぇ");
        put ("qo", "qwo", "qyo", "くぉ");

        put ("va", "ヴぁ");
        put ("vya", "ヴゃ");
        put ("vi", "vyi", "ヴィ");
        put ("vu", "vyu", "ヴ");
        put ("ve", "vye", "ヴぇ");
        put ("vo", "vyo", "ヴぉ");

        put ("sha", "しゃ");
        put ("shu", "しゅ");
        put ("sho", "しょ");

        put ("ja", "じゃ");
        put ("ju", "じゅ");
        put ("jo", "じょ");

        put ("-", "_", "ー");
    }

    public String fixup (String s)
    {
        return s.replace ("n", "ん");
    }

    private void putI (String... s)
    {
        String ja;
        int i;

        ja = s [s.length - 1];
        for (i = 0; i < s.length - 1; i++) {
            put (s [i] + "i", ja);
            put (s [i] + "ya", ja + "ゃ");
            put (s [i] + "yi", ja + "ぃ");
            put (s [i] + "yu", ja + "ゅ");
            put (s [i] + "ye", ja + "ぇ");
            put (s [i] + "yo", ja + "ょ");
        }
    }

    private void putU (String... s)
    {
        String ja;
        int i;

        ja = s [s.length - 1];
        for (i = 0; i < s.length - 1; i++) {
            put (s [i] + "u", ja);
            if (!s[i].equals("n")) put (s [i] + "wa", ja + "ぁ"); // ensures "nwa" outputs as "んわ"
        }
    }

    private void put (String... s)
    {
        int i;

        sput (s);
        for (i = 0; i < s.length - 1; i++) {
            map.put (s [i].charAt (0) + s [i], "っ" + s [s.length - 1]);
        }
    }

    private void sput (String... s)
    {
        int i;

        for (i = 0; i < s.length - 1; i++)
            map.put (s [i], s [s.length - 1]);
    }
}