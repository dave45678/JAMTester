/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  org.python.core.PyObject
 */
package org.jext.search;

import javax.swing.text.Segment;
import org.jext.scripting.python.Run;
import org.jext.search.SearchMatcher;
import org.python.core.PyObject;

public class BoyerMooreSearchMatcher
implements SearchMatcher {
    private char[] pattern;
    private String replace;
    private boolean ignoreCase;
    private boolean reverseSearch;
    private boolean script;
    private String pythonScript;
    private Object[] replaceArgs;
    private int[] skip;
    private int[] suffix;

    public BoyerMooreSearchMatcher(String string, String string2, boolean bl, boolean bl2, boolean bl3, String string3) {
        this.pattern = bl ? string.toUpperCase().toCharArray() : string.toCharArray();
        if (bl2) {
            char[] arrc = new char[this.pattern.length];
            for (int i = 0; i < arrc.length; ++i) {
                arrc[i] = this.pattern[this.pattern.length - (i + 1)];
            }
            this.pattern = arrc;
        }
        this.replace = string2;
        this.ignoreCase = bl;
        this.reverseSearch = bl2;
        this.script = bl3;
        this.pythonScript = string3;
        this.replaceArgs = new Object[10];
        this.generateSkipArray();
        this.generateSuffixArray();
    }

    public int[] nextMatch(Segment segment) {
        int n = this.match(segment.array, segment.offset, segment.offset + segment.count);
        if (n == -1) {
            return null;
        }
        return new int[]{n - segment.offset, n + this.pattern.length - segment.offset};
    }

    public String substitute(String string) throws Exception {
        if (this.script) {
            Object[] arrobject = new String[10];
            arrobject[0] = string;
            PyObject pyObject = Run.eval(this.pythonScript, "_m", arrobject, null);
            if (pyObject == null) {
                return null;
            }
            return pyObject.toString();
        }
        return this.replace;
    }

    public int match(char[] arrc, int n, int n2) {
        int n3 = this.reverseSearch ? n - 1 : n;
        int n4 = this.reverseSearch ? this.pattern.length - 1 : n2 - this.pattern.length;
        int n5 = this.pattern.length - 1;
        char c = '\u0000';
        block0 : while (this.reverseSearch ? n3 >= n4 : n3 <= n4) {
            for (int i = n5; i >= 0; --i) {
                int n6;
                int n7 = this.reverseSearch ? n3 - i : n3 + i;
                char c2 = c = this.ignoreCase ? Character.toUpperCase(arrc[n7]) : arrc[n7];
                if (c == this.pattern[i]) continue;
                int n8 = i - this.skip[BoyerMooreSearchMatcher.getSkipIndex(c)];
                int n9 = n8 > (n6 = this.suffix[i]) ? n8 : n6;
                n3+=this.reverseSearch ? - n9 : n9;
                continue block0;
            }
            return this.reverseSearch ? n3 - (this.pattern.length - 1) : n3;
        }
        return -1;
    }

    private void generateSkipArray() {
        this.skip = new int[256];
        if (this.pattern.length == 0) {
            return;
        }
        int n = 0;
        do {
            this.skip[BoyerMooreSearchMatcher.getSkipIndex((char)this.pattern[n])] = n++;
        } while (n < this.pattern.length);
    }

    private static final int getSkipIndex(char c) {
        return c & 255;
    }

    private void generateSuffixArray() {
        int n;
        int n2 = this.pattern.length;
        int n3 = n2 + 1;
        this.suffix = new int[n3];
        int[] arrn = new int[n3];
        arrn[n2] = n3;
        for (n = n2; n > 0; --n) {
            while (n3 <= n2 && this.pattern[n - 1] != this.pattern[n3 - 1]) {
                if (this.suffix[n3] == 0) {
                    this.suffix[n3] = n3 - n;
                }
                n3 = arrn[n3];
            }
            arrn[n - 1] = --n3;
        }
        n = arrn[0];
        for (n3 = 0; n3 <= n2; ++n3) {
            if (n3 > 0) {
                int n4 = this.suffix[n3 - 1] = this.suffix[n3] == 0 ? n : this.suffix[n3];
            }
            if (n3 != n) continue;
            n = arrn[n];
        }
    }
}

