/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class DawnTokenMarker
extends TokenMarker {
    private static KeywordMap dawnKeywords;
    private KeywordMap keywords = DawnTokenMarker.getKeywords();
    private int lastOffset;
    private int lastKeyword;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        int n3 = segment.count + n2;
        block14 : for (int i = n2; i < n3; ++i) {
            int n4 = i + 1;
            char c = arrc[i];
            switch (by) {
                case 0: {
                    switch (c) {
                        case '#': {
                            this.addToken(i - this.lastOffset, by);
                            this.addToken(n3 - i, 1);
                            by = 0;
                            this.lastOffset = this.lastKeyword = n3;
                            break block14;
                        }
                        case '\"': {
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)3;
                            this.lastOffset = this.lastKeyword = i;
                            continue block14;
                        }
                        case '\'': {
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)4;
                            this.lastOffset = this.lastKeyword = i;
                            continue block14;
                        }
                        case ' ': {
                            this.doKeyword(segment, i, c);
                        }
                    }
                    continue block14;
                }
                case 3: {
                    if (c != '\"') continue block14;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block14;
                }
                case 4: {
                    if (c != '\'') continue block14;
                    this.addToken(n4 - this.lastOffset, 3);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block14;
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
            default: {
                this.addToken(n3 - this.lastOffset, by);
            }
        }
        return by;
    }

    public static KeywordMap getKeywords() {
        if (dawnKeywords == null) {
            dawnKeywords = new KeywordMap(false);
            dawnKeywords.add("do", 6);
            dawnKeywords.add("loop", 6);
            dawnKeywords.add("until", 6);
            dawnKeywords.add("for", 6);
            dawnKeywords.add("next", 6);
            dawnKeywords.add("if", 6);
            dawnKeywords.add("then", 6);
            dawnKeywords.add("else", 6);
            dawnKeywords.add("end", 6);
            dawnKeywords.add("while", 6);
            dawnKeywords.add("repeat", 6);
            dawnKeywords.add("wend", 6);
            dawnKeywords.add("try", 6);
            dawnKeywords.add("catch", 6);
            dawnKeywords.add("err", 6);
            dawnKeywords.add("exit", 7);
            dawnKeywords.add("needs", 7);
            dawnKeywords.add("needsGlobal", 7);
            dawnKeywords.add("array", 8);
            dawnKeywords.add("->", 8);
            dawnKeywords.add("->lit", 8);
            dawnKeywords.add("lit->", 8);
            dawnKeywords.add("->str", 8);
            dawnKeywords.add("str->", 8);
            dawnKeywords.add("sto", 8);
            dawnKeywords.add("rcl", 8);
            dawnKeywords.add("function", 8);
            dawnKeywords.add("endFunction", 8);
            dawnKeywords.add("global", 8);
            dawnKeywords.add("endGlobal", 8);
            dawnKeywords.add("e", 4);
            dawnKeywords.add("pi", 4);
            dawnKeywords.add("null", 4);
            dawnKeywords.add("and", 9);
            dawnKeywords.add("&", 9);
            dawnKeywords.add("or", 9);
            dawnKeywords.add("xor", 9);
            dawnKeywords.add("|", 9);
        }
        return dawnKeywords;
    }

    private boolean doKeyword(Segment segment, int n, char c) {
        int n2 = n + 1;
        int n3 = n - this.lastKeyword;
        byte by = this.keywords.lookup(segment, this.lastKeyword, n3);
        if (by != 0) {
            if (this.lastKeyword != this.lastOffset) {
                this.addToken(this.lastKeyword - this.lastOffset, 0);
            }
            this.addToken(n3, by);
            this.lastOffset = n;
        }
        this.lastKeyword = n2;
        return false;
    }
}

