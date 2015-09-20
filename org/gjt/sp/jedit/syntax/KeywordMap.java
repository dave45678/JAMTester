/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;

public class KeywordMap {
    protected int mapLength;
    private Keyword[] map;
    private boolean ignoreCase;

    public KeywordMap(boolean bl) {
        this(bl, 52);
        this.ignoreCase = bl;
    }

    public KeywordMap(boolean bl, int n) {
        this.mapLength = n;
        this.ignoreCase = bl;
        this.map = new Keyword[n];
    }

    public byte lookup(Segment segment, int n, int n2) {
        if (n2 == 0) {
            return 0;
        }
        Keyword keyword = this.map[this.getSegmentMapKey(segment, n, n2)];
        while (keyword != null) {
            if (n2 != keyword.keyword.length) {
                keyword = keyword.next;
                continue;
            }
            if (SyntaxUtilities.regionMatches(this.ignoreCase, segment, n, keyword.keyword)) {
                return keyword.id;
            }
            keyword = keyword.next;
        }
        return 0;
    }

    public void add(String string, byte by) {
        int n = this.getStringMapKey(string);
        this.map[n] = new Keyword(string.toCharArray(), by, this.map[n]);
    }

    public boolean getIgnoreCase() {
        return this.ignoreCase;
    }

    public void setIgnoreCase(boolean bl) {
        this.ignoreCase = bl;
    }

    protected int getStringMapKey(String string) {
        return (Character.toUpperCase(string.charAt(0)) + Character.toUpperCase(string.charAt(string.length() - 1))) % this.mapLength;
    }

    protected int getSegmentMapKey(Segment segment, int n, int n2) {
        return (Character.toUpperCase(segment.array[n]) + Character.toUpperCase(segment.array[n + n2 - 1])) % this.mapLength;
    }

    class Keyword {
        public char[] keyword;
        public byte id;
        public Keyword next;

        public Keyword(char[] arrc, byte by, Keyword keyword) {
            this.keyword = arrc;
            this.id = by;
            this.next = keyword;
        }
    }

}

