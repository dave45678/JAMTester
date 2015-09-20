/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class CTokenMarker
extends TokenMarker {
    public static final String METHOD_DELIMITERS = " \t~!%^*()-+=|\\#/{}[]:;\"'<>,.?@";
    private static KeywordMap cKeywords;
    private boolean cpp;
    private boolean javadoc;
    private KeywordMap keywords;
    private int lastOffset;
    private int lastKeyword;
    private int lastWhitespace;

    public CTokenMarker() {
        this(true, false, CTokenMarker.getKeywords());
    }

    public CTokenMarker(boolean bl, boolean bl2, KeywordMap keywordMap) {
        this.cpp = bl;
        this.javadoc = bl2;
        this.keywords = keywordMap;
    }

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        this.lastWhitespace = n2 - 1;
        int n3 = segment.count + n2;
        boolean bl = false;
        block22 : for (int i = n2; i < n3; ++i) {
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
                                continue block22;
                            }
                            if (this.doKeyword(segment, i, c)) continue block22;
                            this.addToken(this.lastWhitespace - this.lastOffset + 1, by);
                            this.addToken(i - this.lastWhitespace - 1, 11);
                            this.addToken(1, 0);
                            by = 0;
                            this.lastOffset = this.lastKeyword = n4;
                            this.lastWhitespace = i;
                            continue block22;
                        }
                        case '#': {
                            if (bl) {
                                bl = false;
                                continue block22;
                            }
                            if (!this.cpp) continue block22;
                            if (this.doKeyword(segment, i, c)) continue block22;
                            this.addToken(i - this.lastOffset, by);
                            this.addToken(n3 - i, 7);
                            this.lastOffset = this.lastKeyword = n3;
                            break block22;
                        }
                        case '\"': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block22;
                            }
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)3;
                            this.lastOffset = this.lastKeyword = i;
                            continue block22;
                        }
                        case '\'': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block22;
                            }
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)4;
                            this.lastOffset = this.lastKeyword = i;
                            continue block22;
                        }
                        case ':': {
                            if (this.lastKeyword == n2) {
                                if (this.doKeyword(segment, i, c)) continue block22;
                                if (n4 < arrc.length && arrc[n4] == ':') {
                                    this.addToken(n4 - this.lastOffset, 0);
                                } else {
                                    this.addToken(n4 - this.lastOffset, 5);
                                }
                                this.lastOffset = this.lastKeyword = n4;
                                this.lastWhitespace = n4;
                                bl = false;
                                continue block22;
                            }
                            if (!this.doKeyword(segment, i, c)) continue block22;
                            continue block22;
                        }
                        case '/': {
                            bl = false;
                            this.doKeyword(segment, i, c);
                            if (n3 - i <= 1) continue block22;
                            switch (arrc[n4]) {
                                case '*': {
                                    this.addToken(i - this.lastOffset, by);
                                    this.lastOffset = this.lastKeyword = i;
                                    if (this.javadoc && n3 - i > 2 && arrc[i + 2] == '*') {
                                        by = (byte)2;
                                        continue block22;
                                    }
                                    by = 1;
                                    continue block22;
                                }
                                case '/': {
                                    this.addToken(i - this.lastOffset, by);
                                    this.addToken(n3 - i, 1);
                                    this.lastOffset = this.lastKeyword = n3;
                                    break block22;
                                }
                            }
                            continue block22;
                        }
                    }
                    bl = false;
                    if (!(Character.isLetterOrDigit(c) || c == '_')) {
                        this.doKeyword(segment, i, c);
                    }
                    if (" \t~!%^*()-+=|\\#/{}[]:;\"'<>,.?@".indexOf(c) == -1) continue block22;
                    this.lastWhitespace = i;
                    continue block22;
                }
                case 1: 
                case 2: {
                    bl = false;
                    if (c != '*' || n3 - i <= 1 || arrc[n4] != '/') continue block22;
                    this.addToken(++i + 1 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = i + 1;
                    this.lastWhitespace = i;
                    continue block22;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block22;
                    }
                    if (c != '\"') continue block22;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    this.lastWhitespace = i;
                    continue block22;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        continue block22;
                    }
                    if (c != '\'') continue block22;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    this.lastWhitespace = i;
                    continue block22;
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
        if (cKeywords == null) {
            cKeywords = new KeywordMap(false);
            cKeywords.add("char", 8);
            cKeywords.add("double", 8);
            cKeywords.add("enum", 8);
            cKeywords.add("float", 8);
            cKeywords.add("int", 8);
            cKeywords.add("long", 8);
            cKeywords.add("short", 8);
            cKeywords.add("signed", 8);
            cKeywords.add("struct", 8);
            cKeywords.add("typedef", 8);
            cKeywords.add("union", 8);
            cKeywords.add("unsigned", 8);
            cKeywords.add("void", 8);
            cKeywords.add("auto", 6);
            cKeywords.add("const", 6);
            cKeywords.add("extern", 6);
            cKeywords.add("register", 6);
            cKeywords.add("static", 6);
            cKeywords.add("volatile", 6);
            cKeywords.add("break", 6);
            cKeywords.add("case", 6);
            cKeywords.add("continue", 6);
            cKeywords.add("default", 6);
            cKeywords.add("do", 6);
            cKeywords.add("else", 6);
            cKeywords.add("for", 6);
            cKeywords.add("goto", 6);
            cKeywords.add("if", 6);
            cKeywords.add("return", 6);
            cKeywords.add("sizeof", 6);
            cKeywords.add("switch", 6);
            cKeywords.add("while", 6);
            cKeywords.add("asm", 7);
            cKeywords.add("asmlinkage", 7);
            cKeywords.add("far", 7);
            cKeywords.add("huge", 7);
            cKeywords.add("inline", 7);
            cKeywords.add("near", 7);
            cKeywords.add("pascal", 7);
            cKeywords.add("true", 4);
            cKeywords.add("false", 4);
            cKeywords.add("NULL", 4);
        }
        return cKeywords;
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
            this.lastKeyword = n2;
            this.lastWhitespace = n;
            return true;
        }
        this.lastKeyword = n2;
        return false;
    }
}

