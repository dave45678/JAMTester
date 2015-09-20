/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class FortranTokenMarker
extends TokenMarker {
    private static final int MAYBE_KEYWORD_FIRST = 100;
    private static final int MAYBE_KEYWORD_MORE = 101;
    private static final String S_E_P = "START EDIT PAGE";
    private static KeywordMap fortranKeywords;
    private KeywordMap keywords = FortranTokenMarker.getKeywords();
    private int lastOffset;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        byte by2 = by;
        if (segment.count < 1) {
            return by2;
        }
        char[] arrc = segment.array;
        int n3 = segment.offset;
        char c = arrc[n3];
        if (c == 'C' || c == 'c' || c == '*') {
            this.addToken(segment.count, 1);
            return by2;
        }
        by = 0;
        int n4 = n3 + segment.count;
        int n5 = Math.min(n4, n3 + 5);
        for (n2 = n3; n2 < n5; ++n2) {
            c = arrc[n2];
            if (c == '@') {
                this.guardedAddToken(n2 - n3, by);
                this.addToken(n4 - n2, 2);
                return by2;
            }
            if (by != 0 || '0' > c || c > '9') continue;
            by = (byte)5;
        }
        this.addToken(n5 - n3, by);
        if (n5 == n4) {
            return 0;
        }
        c = arrc[n2];
        if (c == '@') {
            this.addToken(n4 - n2, 2);
            return 0;
        }
        if (c == ' ') {
            this.addToken(1, 0);
            by = 0;
        } else {
            this.addToken(1, 5);
            by = by2;
        }
        if (n4 == n3 + 6) {
            return 0;
        }
        n5 = Math.min(n3 + 72, n4);
        this.lastOffset = n3 + 6;
        if (this.checkStartEditPage(segment)) {
            this.addToken(n5 - this.lastOffset, 5);
            return 0;
        }
        block8 : for (n2 = this.lastOffset; n2 < n5; ++n2) {
            int n6 = n2 + 1;
            c = arrc[n2];
            if (by == 3) {
                if (c != '\'') continue;
                this.addToken(n6 - this.lastOffset, 3);
                by = 0;
                this.lastOffset = n6;
                continue;
            }
            if (c == '@') {
                this.guardedAddToken(n2 - this.lastOffset, by);
                this.addToken(n4 - n2, 2);
                return by;
            }
            if (by == 0) {
                switch (c) {
                    case '\'': {
                        this.guardedAddToken(n2 - this.lastOffset, by);
                        by = (byte)3;
                        this.lastOffset = n2;
                        continue block8;
                    }
                    case '(': 
                    case ')': 
                    case '*': 
                    case '+': 
                    case ',': 
                    case '-': 
                    case '/': 
                    case ':': 
                    case '=': {
                        this.guardedAddToken(n2 - this.lastOffset, by);
                        this.addToken(1, 9);
                        this.lastOffset = n6;
                        continue block8;
                    }
                }
                if (('A' > c || c > 'Z') && ('a' > c || c > 'z')) continue;
                this.guardedAddToken(n2 - this.lastOffset, by);
                by = (byte)100;
                this.lastOffset = n2;
                continue;
            }
            if (by == 100 || by == 101) {
                if ('A' <= c && c <= 'Z' || 'a' <= c && c <= 'z' || '0' <= c && c <= '9' || c == '$') {
                    by = (byte)101;
                    continue;
                }
                this.doKeyword(segment, n2);
                c = arrc[n2];
                switch (c) {
                    case '(': 
                    case ')': 
                    case '*': 
                    case '+': 
                    case ',': 
                    case '-': 
                    case '/': 
                    case ':': 
                    case '=': {
                        this.guardedAddToken(n2 - this.lastOffset, by);
                        this.addToken(1, 9);
                        this.lastOffset = n6;
                    }
                }
                by = 0;
                continue;
            }
            throw new InternalError("Invalid state: " + by);
        }
        if (by == 100 || by == 101) {
            this.doKeyword(segment, n2);
            by = 0;
        } else {
            this.guardedAddToken(n2 - this.lastOffset, by);
        }
        if (n5 == n4) {
            return by;
        }
        this.guardedAddToken(n4 - n2, 2);
        return by;
    }

    private boolean checkStartEditPage(Segment segment) {
        int n;
        if (segment.count < 21) {
            return false;
        }
        int n2 = segment.offset + Math.min(segment.count, 72);
        for (n = segment.offset + 6; n < n2 - 15; ++n) {
            if (segment.array[n] != ' ') break;
        }
        if (!SyntaxUtilities.regionMatches(false, segment, n, "START EDIT PAGE")) {
            return false;
        }
        n+=15;
        while (n < n2) {
            if (segment.array[n] != ' ') {
                return false;
            }
            ++n;
        }
        return true;
    }

    private void doKeyword(Segment segment, int n) {
        int n2 = n - this.lastOffset;
        if (n2 > 0) {
            byte by = this.keywords.lookup(segment, this.lastOffset, n2);
            this.addToken(n2, by);
            this.lastOffset = n;
        }
    }

    private void guardedAddToken(int n, byte by) {
        if (n > 0) {
            this.addToken(n, by);
        }
    }

    public static KeywordMap getKeywords() {
        if (fortranKeywords == null) {
            fortranKeywords = new KeywordMap(false);
            fortranKeywords.add("CALL", 6);
            fortranKeywords.add("CLOSE", 6);
            fortranKeywords.add("CONTINUE", 6);
            fortranKeywords.add("DO", 6);
            fortranKeywords.add("ELSE", 6);
            fortranKeywords.add("ELSEIF", 6);
            fortranKeywords.add("ENDIF", 6);
            fortranKeywords.add("GOTO", 6);
            fortranKeywords.add("GO TO", 6);
            fortranKeywords.add("IF", 6);
            fortranKeywords.add("INDEX", 6);
            fortranKeywords.add("INQUIRE", 6);
            fortranKeywords.add("OPEN", 6);
            fortranKeywords.add("PRINT", 6);
            fortranKeywords.add("READ", 6);
            fortranKeywords.add("RETURN", 6);
            fortranKeywords.add("THEN", 6);
            fortranKeywords.add("WRITE", 6);
            fortranKeywords.add("BLOCK DATA", 7);
            fortranKeywords.add("COMPILER", 7);
            fortranKeywords.add("END", 7);
            fortranKeywords.add("ENTRY", 7);
            fortranKeywords.add("FUNCTION", 7);
            fortranKeywords.add("INCLUDE", 7);
            fortranKeywords.add("SUBROUTINE", 7);
            fortranKeywords.add("CHARACTER", 8);
            fortranKeywords.add("DATA", 8);
            fortranKeywords.add("DEFINE", 8);
            fortranKeywords.add("EQUIVALENCE", 8);
            fortranKeywords.add("IMPLICIT", 8);
            fortranKeywords.add("INTEGER", 8);
            fortranKeywords.add("LOGICAL", 8);
            fortranKeywords.add("PARAMETER", 8);
            fortranKeywords.add("REAL", 8);
            fortranKeywords.add(".AND.", 9);
            fortranKeywords.add(".EQ.", 9);
            fortranKeywords.add(".NE.", 9);
            fortranKeywords.add(".NOT.", 9);
            fortranKeywords.add(".OR.", 9);
            fortranKeywords.add("+", 9);
            fortranKeywords.add("-", 9);
            fortranKeywords.add("*", 9);
            fortranKeywords.add("**", 9);
            fortranKeywords.add("/", 9);
            fortranKeywords.add(".FALSE.", 4);
            fortranKeywords.add(".TRUE.", 4);
        }
        return fortranKeywords;
    }
}

