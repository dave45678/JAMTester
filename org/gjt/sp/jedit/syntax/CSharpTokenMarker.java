/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class CSharpTokenMarker
extends TokenMarker {
    public static final byte VERBATIM_STRING = 101;
    private static KeywordMap cKeywords;
    private KeywordMap keywords = CSharpTokenMarker.getKeywords();
    private int lastOffset;
    private int lastKeyword;
    private int lastWhitespace;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        int n3 = segment.count + n2;
        this.lastWhitespace = n2 - 1;
        boolean bl = false;
        block25 : for (int i = n2; i < n3; ++i) {
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
                                continue block25;
                            }
                            if (this.doKeyword(segment, i, c)) continue block25;
                            this.addToken(this.lastWhitespace - this.lastOffset + 1, by);
                            this.addToken(i - this.lastWhitespace - 1, 11);
                            this.addToken(1, 0);
                            by = 0;
                            this.lastOffset = this.lastKeyword = n4;
                            this.lastWhitespace = i;
                            continue block25;
                        }
                        case '#': {
                            if (bl) {
                                bl = false;
                                continue block25;
                            }
                            if (this.doKeyword(segment, i, c)) continue block25;
                            this.addToken(i - this.lastOffset, by);
                            this.addToken(n3 - i, 7);
                            this.lastOffset = this.lastKeyword = n3;
                            break block25;
                        }
                        case '\"': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block25;
                            }
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)3;
                            this.lastOffset = this.lastKeyword = i;
                            continue block25;
                        }
                        case '@': {
                            if (n3 - i <= 1 || arrc[n4] != '\"') continue block25;
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)101;
                            this.lastOffset = this.lastKeyword = i++;
                            continue block25;
                        }
                        case '\'': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block25;
                            }
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)4;
                            this.lastOffset = this.lastKeyword = i;
                            continue block25;
                        }
                        case ':': {
                            if (this.lastKeyword == n2) {
                                if (this.doKeyword(segment, i, c)) continue block25;
                                bl = false;
                                this.addToken(n4 - this.lastOffset, 5);
                                this.lastOffset = this.lastKeyword = n4;
                                continue block25;
                            }
                            if (!this.doKeyword(segment, i, c)) continue block25;
                            continue block25;
                        }
                        case '/': {
                            bl = false;
                            this.doKeyword(segment, i, c);
                            if (n3 - i <= 1) continue block25;
                            switch (arrc[n4]) {
                                case '*': {
                                    this.addToken(i - this.lastOffset, by);
                                    this.lastOffset = this.lastKeyword = i;
                                    if (n3 - i > 2 && arrc[i + 2] == '*') {
                                        by = (byte)2;
                                        continue block25;
                                    }
                                    by = 1;
                                    continue block25;
                                }
                                case '/': {
                                    this.addToken(i - this.lastOffset, by);
                                    if (n3 - i > 2) {
                                        this.addToken(n3 - i, 2);
                                    } else {
                                        this.addToken(n3 - i, 1);
                                    }
                                    this.lastOffset = this.lastKeyword = n3;
                                    break block25;
                                }
                            }
                            continue block25;
                        }
                    }
                    bl = false;
                    if (!(Character.isLetterOrDigit(c) || c == '_')) {
                        this.doKeyword(segment, i, c);
                    }
                    if (" \t~!%^*()-+=|\\#/{}[]:;\"'<>,.?@".indexOf(c) == -1) continue block25;
                    this.lastWhitespace = i;
                    continue block25;
                }
                case 1: 
                case 2: {
                    bl = false;
                    if (c != '*' || n3 - i <= 1 || arrc[n4] != '/') continue block25;
                    this.addToken(++i + 1 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = i + 1;
                    this.lastWhitespace = i;
                    continue block25;
                }
                case 101: {
                    if (bl) {
                        bl = false;
                        continue block25;
                    }
                    if (c != '\"') continue block25;
                    this.addToken(n4 - this.lastOffset, 3);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    this.lastWhitespace = i;
                    continue block25;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block25;
                    }
                    if (c != '\"') continue block25;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    this.lastWhitespace = i;
                    continue block25;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        continue block25;
                    }
                    if (c != '\'') continue block25;
                    this.addToken(n4 - this.lastOffset, 3);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    this.lastWhitespace = i;
                    continue block25;
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
            case 101: {
                this.addToken(n3 - this.lastOffset, 3);
                break;
            }
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
            cKeywords.add("abstract", 6);
            cKeywords.add("as", 6);
            cKeywords.add("base", 6);
            cKeywords.add("break", 6);
            cKeywords.add("case", 6);
            cKeywords.add("catch", 6);
            cKeywords.add("checked", 6);
            cKeywords.add("const", 6);
            cKeywords.add("continue", 6);
            cKeywords.add("decimal", 6);
            cKeywords.add("default", 6);
            cKeywords.add("delegate", 6);
            cKeywords.add("do", 6);
            cKeywords.add("else", 6);
            cKeywords.add("explicit", 6);
            cKeywords.add("extern", 6);
            cKeywords.add("finally", 6);
            cKeywords.add("fixed", 6);
            cKeywords.add("for", 6);
            cKeywords.add("foreach", 6);
            cKeywords.add("get", 6);
            cKeywords.add("goto", 6);
            cKeywords.add("if", 6);
            cKeywords.add("implicit", 6);
            cKeywords.add("in", 6);
            cKeywords.add("internal", 6);
            cKeywords.add("is", 6);
            cKeywords.add("lock", 6);
            cKeywords.add("new", 6);
            cKeywords.add("operator", 6);
            cKeywords.add("out", 6);
            cKeywords.add("override", 6);
            cKeywords.add("params", 6);
            cKeywords.add("private", 6);
            cKeywords.add("protected", 6);
            cKeywords.add("public", 6);
            cKeywords.add("readonly", 6);
            cKeywords.add("ref", 6);
            cKeywords.add("return", 6);
            cKeywords.add("sealed", 6);
            cKeywords.add("set", 6);
            cKeywords.add("sizeof", 6);
            cKeywords.add("stackalloc", 6);
            cKeywords.add("static", 6);
            cKeywords.add("switch", 6);
            cKeywords.add("throw", 6);
            cKeywords.add("try", 6);
            cKeywords.add("typeof", 6);
            cKeywords.add("unchecked", 6);
            cKeywords.add("unsafe", 6);
            cKeywords.add("virtual", 6);
            cKeywords.add("while", 6);
            cKeywords.add("using", 7);
            cKeywords.add("namespace", 7);
            cKeywords.add("bool", 8);
            cKeywords.add("byte", 8);
            cKeywords.add("char", 8);
            cKeywords.add("class", 8);
            cKeywords.add("double", 8);
            cKeywords.add("enum", 8);
            cKeywords.add("event", 8);
            cKeywords.add("float", 8);
            cKeywords.add("int", 8);
            cKeywords.add("interface", 8);
            cKeywords.add("long", 8);
            cKeywords.add("object", 8);
            cKeywords.add("sbyte", 8);
            cKeywords.add("short", 8);
            cKeywords.add("string", 8);
            cKeywords.add("struct", 8);
            cKeywords.add("uint", 8);
            cKeywords.add("ulong", 8);
            cKeywords.add("ushort", 8);
            cKeywords.add("void", 8);
            cKeywords.add("false", 4);
            cKeywords.add("null", 4);
            cKeywords.add("this", 4);
            cKeywords.add("true", 4);
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

