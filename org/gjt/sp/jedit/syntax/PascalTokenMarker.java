/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class PascalTokenMarker
extends TokenMarker {
    public static final String METHOD_DELIMITERS = " \t~!%^*()-+=|\\#/{}[]:;\"'<>,.?";
    private static KeywordMap pascalKeywords;
    private KeywordMap keywords = PascalTokenMarker.getKeywords();
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
        block17 : for (int i = n2; i < n3; ++i) {
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
                                continue block17;
                            }
                            boolean bl2 = this.doKeyword(segment, i, c);
                            if (n3 - i > 1) {
                                switch (arrc[n4]) {
                                    case '*': {
                                        this.addToken(i - this.lastOffset, by);
                                        by = (byte)2;
                                        this.lastOffset = this.lastKeyword = i;
                                        continue block17;
                                    }
                                }
                            }
                            if (bl2) continue block17;
                            this.addToken(this.lastWhitespace - this.lastOffset + 1, by);
                            this.addToken(i - this.lastWhitespace - 1, 11);
                            this.addToken(1, 0);
                            by = 0;
                            this.lastOffset = this.lastKeyword = n4;
                            this.lastWhitespace = i;
                            continue block17;
                        }
                        case '{': {
                            bl = false;
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, by);
                            by = 1;
                            this.lastOffset = this.lastKeyword = n4;
                            continue block17;
                        }
                        case '\'': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block17;
                            }
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)3;
                            this.lastOffset = this.lastKeyword = i;
                            continue block17;
                        }
                    }
                    bl = false;
                    if (!(Character.isLetterOrDigit(c) || c == '_' || c == '{' || c == '}')) {
                        this.doKeyword(segment, i, c);
                    }
                    if (" \t~!%^*()-+=|\\#/{}[]:;\"'<>,.?".indexOf(c) == -1) continue block17;
                    this.lastWhitespace = i;
                    continue block17;
                }
                case 1: {
                    bl = false;
                    if (c != '}') continue block17;
                    this.addToken(i + 1 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = i;
                    this.lastWhitespace = i;
                    continue block17;
                }
                case 2: {
                    bl = false;
                    if (c != '*' || n3 - i <= 1 || arrc[n4] != ')') continue block17;
                    this.addToken(++i + 1 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = i + 1;
                    this.lastWhitespace = i;
                    continue block17;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block17;
                    }
                    if (c != '\'') continue block17;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    this.lastWhitespace = i;
                    continue block17;
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
            case 3: {
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
        if (pascalKeywords == null) {
            pascalKeywords = new KeywordMap(false);
            pascalKeywords.add("absolute", 6);
            pascalKeywords.add("and", 9);
            pascalKeywords.add("array", 8);
            pascalKeywords.add("asm", 6);
            pascalKeywords.add("begin", 6);
            pascalKeywords.add("case", 6);
            pascalKeywords.add("const", 6);
            pascalKeywords.add("constructor", 6);
            pascalKeywords.add("destructor", 6);
            pascalKeywords.add("div", 9);
            pascalKeywords.add("do", 6);
            pascalKeywords.add("downto", 6);
            pascalKeywords.add("else", 6);
            pascalKeywords.add("end", 6);
            pascalKeywords.add("external", 6);
            pascalKeywords.add("file", 6);
            pascalKeywords.add("for", 6);
            pascalKeywords.add("forward", 6);
            pascalKeywords.add("function", 6);
            pascalKeywords.add("goto", 6);
            pascalKeywords.add("if", 6);
            pascalKeywords.add("implementation", 7);
            pascalKeywords.add("in", 6);
            pascalKeywords.add("inherited", 6);
            pascalKeywords.add("inline", 6);
            pascalKeywords.add("interface", 6);
            pascalKeywords.add("interrupt", 6);
            pascalKeywords.add("label", 7);
            pascalKeywords.add("library", 7);
            pascalKeywords.add("mod", 9);
            pascalKeywords.add("nil", 5);
            pascalKeywords.add("not", 9);
            pascalKeywords.add("object", 8);
            pascalKeywords.add("of", 6);
            pascalKeywords.add("on", 6);
            pascalKeywords.add("packed", 6);
            pascalKeywords.add("private", 6);
            pascalKeywords.add("procedure", 6);
            pascalKeywords.add("program", 6);
            pascalKeywords.add("public", 6);
            pascalKeywords.add("record", 8);
            pascalKeywords.add("repeat", 6);
            pascalKeywords.add("set", 8);
            pascalKeywords.add("shl", 6);
            pascalKeywords.add("shr", 6);
            pascalKeywords.add("string", 8);
            pascalKeywords.add("then", 6);
            pascalKeywords.add("to", 6);
            pascalKeywords.add("type", 6);
            pascalKeywords.add("unit", 6);
            pascalKeywords.add("until", 6);
            pascalKeywords.add("uses", 7);
            pascalKeywords.add("var", 6);
            pascalKeywords.add("virtual", 6);
            pascalKeywords.add("while", 6);
            pascalKeywords.add("with", 6);
            pascalKeywords.add("xor", 9);
            pascalKeywords.add("true", 5);
            pascalKeywords.add("false", 5);
            pascalKeywords.add("maxint", 5);
            pascalKeywords.add("maxlongint", 5);
            pascalKeywords.add("boolean", 8);
            pascalKeywords.add("byte", 8);
            pascalKeywords.add("char", 8);
            pascalKeywords.add("extended", 8);
            pascalKeywords.add("longint", 8);
            pascalKeywords.add("integer", 8);
        }
        return pascalKeywords;
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

