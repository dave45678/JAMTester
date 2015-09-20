/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  gnu.regexp.RE
 *  gnu.regexp.REMatch
 *  gnu.regexp.RESyntax
 *  org.python.core.PyObject
 */
package org.jext.search;

import gnu.regexp.RE;
import gnu.regexp.REMatch;
import gnu.regexp.RESyntax;
import javax.swing.text.Segment;
import org.jext.scripting.python.Run;
import org.jext.search.SearchMatcher;
import org.python.core.PyObject;

public class RESearchMatcher
implements SearchMatcher {
    public static final RESyntax RE_SYNTAX_JEXT = new RESyntax(RESyntax.RE_SYNTAX_PERL5).set(2).setLineSeparator("\n");
    private String replace;
    private RE re;
    private boolean script;
    private String pythonScript;
    String[] replaceArgs;

    public RESearchMatcher(String string, String string2, boolean bl, boolean bl2, String string3) throws Exception {
        this.replace = string2;
        this.script = bl2;
        this.pythonScript = string3;
        this.replaceArgs = new String[10];
        this.re = new RE((Object)string, (bl ? 2 : 0) | 8, RE_SYNTAX_JEXT);
    }

    public int[] nextMatch(Segment segment) {
        REMatch rEMatch = this.re.getMatch((Object)segment);
        if (rEMatch == null) {
            return null;
        }
        int[] arrn = new int[]{rEMatch.getStartIndex(), rEMatch.getEndIndex()};
        return arrn;
    }

    public String substitute(String string) throws Exception {
        REMatch rEMatch = this.re.getMatch((Object)string);
        if (rEMatch == null) {
            return null;
        }
        if (this.script) {
            int n = this.re.getNumSubs();
            for (int i = 1; i <= n; ++i) {
                this.replaceArgs[i - 1] = rEMatch.toString(i);
            }
            PyObject pyObject = Run.eval(this.pythonScript, "_m", this.replaceArgs, null);
            if (pyObject == null) {
                return null;
            }
            return pyObject.toString();
        }
        return rEMatch.substituteInto(this.replace);
    }
}

