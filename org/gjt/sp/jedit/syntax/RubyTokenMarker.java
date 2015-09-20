/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class RubyTokenMarker
extends TokenMarker {
    private static KeywordMap rubyKeywords;
    private KeywordMap keywords = RubyTokenMarker.getKeywords();
    private int lastOffset;
    private int lastKeyword;
    private int lastWhitespace;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        this.lastWhitespace = n2 - 1;
        int n3 = segment.count + n2;
        boolean bl = false;
        block15 : for (int i = n2; i < n3; ++i) {
            int n4 = i + 1;
            char c = arrc[i];
            if (c == '\\') {
                bl = !bl;
                continue;
            }
            switch (by) {
                case 0: {
                    switch (c) {
                        case '(': {
                            if (bl) {
                                this.doKeyword(segment, i, c);
                                bl = false;
                                continue block15;
                            }
                            if (this.doKeyword(segment, i, c)) continue block15;
                            this.addToken(this.lastWhitespace - this.lastOffset + 1, by);
                            this.addToken(i - this.lastWhitespace - 1, 11);
                            this.addToken(1, 0);
                            by = 0;
                            this.lastOffset = this.lastKeyword = n4;
                            this.lastWhitespace = i;
                            continue block15;
                        }
                        case '#': {
                            if (bl) {
                                bl = false;
                                continue block15;
                            }
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, by);
                            this.addToken(n3 - i, 1);
                            this.lastOffset = this.lastKeyword = n3;
                            break block15;
                        }
                        case '\"': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block15;
                            }
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)3;
                            this.lastOffset = this.lastKeyword = i;
                            continue block15;
                        }
                        case '\'': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block15;
                            }
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)4;
                            this.lastOffset = this.lastKeyword = i;
                            continue block15;
                        }
                    }
                    bl = false;
                    if (!(Character.isLetterOrDigit(c) || c == '_')) {
                        this.doKeyword(segment, i, c);
                    }
                    if (" \t~!%^*()-+=|\\#/{}[]:;\"'<>,.?@".indexOf(c) == -1) continue block15;
                    this.lastWhitespace = i;
                    continue block15;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block15;
                    }
                    if (c != '\"') continue block15;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block15;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        continue block15;
                    }
                    if (c != '\'') continue block15;
                    this.addToken(n4 - this.lastOffset, 3);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block15;
                }
                default: {
                    throw new InternalError("Invalid state: " + by);
                }
            }
        }
        switch (by) {
            case 3: 
            case 4: {
                this.addToken(n3 - this.lastOffset, 10);
                by = 0;
                break;
            }
            case 0: {
                this.doKeyword(segment, n3, '\u0000');
            }
            default: {
                this.addToken(n3 - this.lastOffset, by);
            }
        }
        return by;
    }

    public static KeywordMap getKeywords() {
        if (rubyKeywords == null) {
            rubyKeywords = new KeywordMap(false);
            rubyKeywords.add("__FILE__", 5);
            rubyKeywords.add("and", 7);
            rubyKeywords.add("def", 6);
            rubyKeywords.add("end", 6);
            rubyKeywords.add("in", 6);
            rubyKeywords.add("or", 7);
            rubyKeywords.add("self", 4);
            rubyKeywords.add("unless", 6);
            rubyKeywords.add("__LINE__", 5);
            rubyKeywords.add("begin", 6);
            rubyKeywords.add("defined?", 6);
            rubyKeywords.add("ensure", 6);
            rubyKeywords.add("module", 8);
            rubyKeywords.add("require", 7);
            rubyKeywords.add("redo", 6);
            rubyKeywords.add("super", 4);
            rubyKeywords.add("until", 6);
            rubyKeywords.add("BEGIN", 5);
            rubyKeywords.add("break", 6);
            rubyKeywords.add("do", 6);
            rubyKeywords.add("false", 4);
            rubyKeywords.add("next", 6);
            rubyKeywords.add("rescue", 6);
            rubyKeywords.add("then", 6);
            rubyKeywords.add("when", 6);
            rubyKeywords.add("END", 5);
            rubyKeywords.add("case", 6);
            rubyKeywords.add("else", 6);
            rubyKeywords.add("for", 6);
            rubyKeywords.add("nil", 4);
            rubyKeywords.add("retry", 6);
            rubyKeywords.add("true", 4);
            rubyKeywords.add("while", 6);
            rubyKeywords.add("alias", 8);
            rubyKeywords.add("class", 8);
            rubyKeywords.add("elsif", 6);
            rubyKeywords.add("if", 6);
            rubyKeywords.add("not", 7);
            rubyKeywords.add("return", 6);
            rubyKeywords.add("undef", 6);
            rubyKeywords.add("yield", 6);
        }
        return rubyKeywords;
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
            this.lastWhitespace = n2;
            this.lastKeyword = n2;
            return true;
        }
        this.lastKeyword = n2;
        return false;
    }
}

