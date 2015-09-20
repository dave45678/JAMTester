/*
 * Decompiled with CFR 0_102.
 */
package org.jext.search;

import javax.swing.text.AttributeSet;
import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.Jext;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.search.BoyerMooreSearchMatcher;
import org.jext.search.RESearchMatcher;
import org.jext.search.SearchMatcher;

public class Search {
    public static SearchMatcher matcher;
    public static String replacePattern;
    public static String findPattern;
    public static String pythonScript;
    public static boolean useRegexp;
    public static boolean ignoreCase;
    public static boolean script;
    public static boolean reverseSearch;

    public static void load() {
        findPattern = Jext.getProperty("find");
        replacePattern = Jext.getProperty("replace");
        useRegexp = Jext.getBooleanProperty("useregexp");
        ignoreCase = Jext.getBooleanProperty("ignorecase");
        script = Jext.getBooleanProperty("replacescript");
        pythonScript = Jext.getProperty("pythonscript");
    }

    public static void save() {
        Jext.setProperty("find", findPattern);
        Jext.setProperty("replace", replacePattern);
        Jext.setProperty("pythonscript", pythonScript);
        Jext.setProperty("ignorecase", ignoreCase ? "on" : "off");
        Jext.setProperty("useregexp", useRegexp ? "on" : "off");
        Jext.setProperty("replacescript", script ? "on" : "off");
    }

    public static String getPythonScriptString() {
        return pythonScript;
    }

    public static void setPythonScriptString(String string) {
        pythonScript = string;
    }

    public static boolean getPythonScript() {
        return script;
    }

    public static void setPythonScript(boolean bl) {
        script = bl;
    }

    public static boolean getRegexp() {
        return useRegexp;
    }

    public static void setRegexp(boolean bl) {
        useRegexp = bl;
    }

    public static boolean getIgnoreCase() {
        return ignoreCase;
    }

    public static void setIgnoreCase(boolean bl) {
        ignoreCase = bl;
    }

    public static void setFindPattern(String string) {
        findPattern = string;
    }

    public static String getFindPattern() {
        return findPattern;
    }

    public static void setReplacePattern(String string) {
        replacePattern = string;
    }

    public static String getReplacePattern() {
        return replacePattern;
    }

    public static SearchMatcher getSearchMatcher() throws Exception {
        return Search.getSearchMatcher(true);
    }

    public static SearchMatcher getSearchMatcher(boolean bl) throws Exception {
        String string;
        if (findPattern == null || "".equals(findPattern)) {
            return null;
        }
        String string2 = string = replacePattern == null ? "" : replacePattern;
        matcher = useRegexp ? new RESearchMatcher(findPattern, string, ignoreCase, script, pythonScript) : new BoyerMooreSearchMatcher(findPattern, string, ignoreCase, reverseSearch && bl, script, pythonScript);
        return matcher;
    }

    public static boolean find(JextTextArea jextTextArea, int n) throws Exception {
        SearchMatcher searchMatcher = Search.getSearchMatcher(true);
        Segment segment = new Segment();
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        syntaxDocument.getText(n, syntaxDocument.getLength() - n, segment);
        int[] arrn = searchMatcher.nextMatch(segment);
        if (arrn != null) {
            jextTextArea.select(n + arrn[0], n + arrn[1]);
            return true;
        }
        return false;
    }

    public static boolean replace(JextTextArea jextTextArea) {
        if (!jextTextArea.isEditable()) {
            Utilities.beep();
            return false;
        }
        int n = jextTextArea.getSelectionStart();
        boolean bl = jextTextArea.isSelectionRectangular();
        if (n == jextTextArea.getSelectionEnd()) {
            Utilities.beep();
            return false;
        }
        try {
            SearchMatcher searchMatcher = Search.getSearchMatcher(false);
            if (searchMatcher == null) {
                Utilities.beep();
                return false;
            }
            String string = jextTextArea.getSelectedText();
            String string2 = searchMatcher.substitute(string);
            if (string2 == null || string2.equals(string)) {
                return false;
            }
            jextTextArea.setSelectedText(string2);
            return true;
        }
        catch (Exception var3_4) {
            return false;
        }
    }

    public static int replaceAll(JextTextArea jextTextArea, int n, int n2) throws Exception {
        if (!jextTextArea.isEditable()) {
            return 0;
        }
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        SearchMatcher searchMatcher = Search.getSearchMatcher(false);
        if (searchMatcher == null) {
            return 0;
        }
        int n3 = 0;
        Segment segment = new Segment();
        int n4 = n;
        do {
            syntaxDocument.getText(n4, n2 - n4, segment);
            int[] arrn = searchMatcher.nextMatch(segment);
            if (arrn == null) break;
            int n5 = arrn[0] + n4;
            int n6 = arrn[1] - arrn[0];
            String string = syntaxDocument.getText(n5, n6);
            String string2 = searchMatcher.substitute(string);
            n2-=string.length() - string2.length();
            if (string2 != null) {
                syntaxDocument.remove(n5, n6);
                syntaxDocument.insertString(n5, string2, null);
                ++n3;
                n4+=arrn[0] + string.length();
                continue;
            }
            n4+=n6;
        } while (true);
        return n3;
    }

    static {
        useRegexp = false;
        ignoreCase = true;
        script = false;
        reverseSearch = false;
    }
}

