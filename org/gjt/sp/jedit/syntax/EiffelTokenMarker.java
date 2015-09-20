/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class EiffelTokenMarker
extends TokenMarker {
    private static KeywordMap eiffelKeywords;
    private boolean cpp;
    private KeywordMap keywords = EiffelTokenMarker.getKeywords();
    private int lastOffset;
    private int lastKeyword;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        int n3 = segment.count + n2;
        boolean bl = false;
        block19 : for (int i = n2; i < n3; ++i) {
            int n4 = i + 1;
            char c = arrc[i];
            if (c == '%') {
                bl = !bl;
                continue;
            }
            switch (by) {
                case 0: {
                    switch (c) {
                        case '\"': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block19;
                            }
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)3;
                            this.lastOffset = this.lastKeyword = i;
                            continue block19;
                        }
                        case '\'': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block19;
                            }
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)4;
                            this.lastOffset = this.lastKeyword = i;
                            continue block19;
                        }
                        case ':': {
                            if (this.lastKeyword == n2) {
                                if (this.doKeyword(segment, i, c)) continue block19;
                                bl = false;
                                this.addToken(n4 - this.lastOffset, 5);
                                this.lastOffset = this.lastKeyword = n4;
                                continue block19;
                            }
                            if (!this.doKeyword(segment, i, c)) continue block19;
                            continue block19;
                        }
                        case '-': {
                            bl = false;
                            this.doKeyword(segment, i, c);
                            if (n3 - i <= 1) continue block19;
                            switch (arrc[n4]) {
                                case '-': {
                                    this.addToken(i - this.lastOffset, by);
                                    this.addToken(n3 - i, 1);
                                    this.lastOffset = this.lastKeyword = n3;
                                    break block19;
                                }
                            }
                            continue block19;
                        }
                    }
                    bl = false;
                    if (Character.isLetterOrDigit(c) || c == '_') continue block19;
                    this.doKeyword(segment, i, c);
                    continue block19;
                }
                case 1: 
                case 2: {
                    throw new RuntimeException("Wrong eiffel parser state");
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block19;
                    }
                    if (c != '\"') continue block19;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block19;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        continue block19;
                    }
                    if (c != '\'') continue block19;
                    this.addToken(n4 - this.lastOffset, 3);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block19;
                }
                default: {
                    throw new InternalError("Invalid state: " + by);
                }
            }
        }
        if (by == 0) {
            this.doKeyword(segment, n3, '\u0000');
        }
        switch (by) {
            case 3: 
            case 4: {
                this.addToken(n3 - this.lastOffset, 10);
                by = 0;
                break;
            }
            case 7: {
                this.addToken(n3 - this.lastOffset, by);
                if (!bl) {
                    by = 0;
                }
            }
            default: {
                this.addToken(n3 - this.lastOffset, by);
            }
        }
        return by;
    }

    public static KeywordMap getKeywords() {
        if (eiffelKeywords == null) {
            eiffelKeywords = new KeywordMap(true);
            eiffelKeywords.add("alias", 6);
            eiffelKeywords.add("all", 6);
            eiffelKeywords.add("and", 6);
            eiffelKeywords.add("as", 6);
            eiffelKeywords.add("check", 6);
            eiffelKeywords.add("class", 6);
            eiffelKeywords.add("creation", 6);
            eiffelKeywords.add("debug", 6);
            eiffelKeywords.add("deferred", 6);
            eiffelKeywords.add("do", 6);
            eiffelKeywords.add("else", 6);
            eiffelKeywords.add("elseif", 6);
            eiffelKeywords.add("end", 6);
            eiffelKeywords.add("ensure", 6);
            eiffelKeywords.add("expanded", 6);
            eiffelKeywords.add("export", 6);
            eiffelKeywords.add("external", 6);
            eiffelKeywords.add("feature", 6);
            eiffelKeywords.add("from", 6);
            eiffelKeywords.add("frozen", 6);
            eiffelKeywords.add("if", 6);
            eiffelKeywords.add("implies", 6);
            eiffelKeywords.add("indexing", 6);
            eiffelKeywords.add("infix", 6);
            eiffelKeywords.add("inherit", 6);
            eiffelKeywords.add("inspect", 6);
            eiffelKeywords.add("invariant", 6);
            eiffelKeywords.add("is", 6);
            eiffelKeywords.add("like", 6);
            eiffelKeywords.add("local", 6);
            eiffelKeywords.add("loop", 6);
            eiffelKeywords.add("not", 6);
            eiffelKeywords.add("obsolete", 6);
            eiffelKeywords.add("old", 6);
            eiffelKeywords.add("once", 6);
            eiffelKeywords.add("or", 6);
            eiffelKeywords.add("prefix", 6);
            eiffelKeywords.add("redefine", 6);
            eiffelKeywords.add("rename", 6);
            eiffelKeywords.add("require", 6);
            eiffelKeywords.add("rescue", 6);
            eiffelKeywords.add("retry", 6);
            eiffelKeywords.add("select", 6);
            eiffelKeywords.add("separate", 6);
            eiffelKeywords.add("then", 6);
            eiffelKeywords.add("undefine", 6);
            eiffelKeywords.add("until", 6);
            eiffelKeywords.add("variant", 6);
            eiffelKeywords.add("when", 6);
            eiffelKeywords.add("xor", 6);
            eiffelKeywords.add("current", 4);
            eiffelKeywords.add("false", 4);
            eiffelKeywords.add("precursor", 4);
            eiffelKeywords.add("result", 4);
            eiffelKeywords.add("strip", 4);
            eiffelKeywords.add("true", 4);
            eiffelKeywords.add("unique", 4);
            eiffelKeywords.add("void", 4);
        }
        return eiffelKeywords;
    }

    private boolean doKeyword(Segment segment, int n, char c) {
        int n2 = n + 1;
        boolean bl = false;
        int n3 = n - this.lastKeyword;
        int n4 = this.keywords.lookup(segment, this.lastKeyword, n3);
        if (n4 == 0) {
            bl = true;
            for (int i = this.lastKeyword; i < this.lastKeyword + n3; ++i) {
                char c2 = segment.array[i];
                if (c2 == '_' || Character.isUpperCase(c2)) continue;
                bl = false;
                break;
            }
            if (bl) {
                n4 = 8;
            }
        }
        if (n4 != 0) {
            if (this.lastKeyword != this.lastOffset) {
                this.addToken(this.lastKeyword - this.lastOffset, 0);
            }
            this.addToken(n3, (byte)n4);
            this.lastOffset = n;
        }
        this.lastKeyword = n2;
        return false;
    }
}

