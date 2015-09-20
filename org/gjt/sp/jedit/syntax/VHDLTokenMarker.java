/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class VHDLTokenMarker
extends TokenMarker {
    public static final int AS_IS = 0;
    public static final int LOWER_CASE = 1;
    public static final int UPPER_CASE = 2;
    private static KeywordMap vhdlKeywords;
    private KeywordMap keywords = VHDLTokenMarker.getKeywords();
    private int lastOffset;
    private int lastKeyword;
    private int keywordCase = 0;
    private boolean allLowerCase = false;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        int n3 = segment.count + n2;
        boolean bl = false;
        block20 : for (int i = n2; i < n3; ++i) {
            int n4 = i + 1;
            char c = arrc[i];
            if (c == '\\') {
                bl = !bl;
                continue;
            }
            switch (by) {
                case 0: {
                    switch (c) {
                        case '#': {
                            if (!bl) continue block20;
                            bl = false;
                            continue block20;
                        }
                        case '\"': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block20;
                            }
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)3;
                            this.lastOffset = this.lastKeyword = i;
                            continue block20;
                        }
                        case ':': {
                            if (this.lastKeyword == n2) {
                                if (this.doKeyword(segment, i, c)) continue block20;
                                bl = false;
                                this.addToken(n4 - this.lastOffset, 5);
                                this.lastOffset = this.lastKeyword = n4;
                                continue block20;
                            }
                            if (!this.doKeyword(segment, i, c)) continue block20;
                            continue block20;
                        }
                        case '-': {
                            bl = false;
                            this.doKeyword(segment, i, c);
                            if (n3 - i <= 1) continue block20;
                            switch (arrc[n4]) {
                                case '*': {
                                    this.addToken(i - this.lastOffset, by);
                                    this.lastOffset = this.lastKeyword = i;
                                    by = 1;
                                    continue block20;
                                }
                                case '-': {
                                    this.addToken(i - this.lastOffset, by);
                                    this.addToken(n3 - i, 1);
                                    this.lastOffset = this.lastKeyword = n3;
                                    break block20;
                                }
                            }
                            continue block20;
                        }
                    }
                    bl = false;
                    if (Character.isLetterOrDigit(c) || c == '_') continue block20;
                    this.doKeyword(segment, i, c);
                    continue block20;
                }
                case 1: 
                case 2: {
                    bl = false;
                    if (c != '*' || n3 - i <= 1 || arrc[n4] != '/') continue block20;
                    this.addToken(++i + 1 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = i + 1;
                    continue block20;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block20;
                    }
                    if (c != '\"') continue block20;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block20;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        continue block20;
                    }
                    if (c != '\'') continue block20;
                    this.addToken(n4 - this.lastOffset, 3);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block20;
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
        if (vhdlKeywords == null) {
            vhdlKeywords = new KeywordMap(true);
            vhdlKeywords.add("char", 8);
            vhdlKeywords.add("double", 8);
            vhdlKeywords.add("enum", 8);
            vhdlKeywords.add("real", 8);
            vhdlKeywords.add("integer", 8);
            vhdlKeywords.add("natural", 8);
            vhdlKeywords.add("text", 8);
            vhdlKeywords.add("boolean", 8);
            vhdlKeywords.add("line", 8);
            vhdlKeywords.add("string", 8);
            vhdlKeywords.add("bit", 8);
            vhdlKeywords.add("bit_vector", 8);
            vhdlKeywords.add("std_logic", 8);
            vhdlKeywords.add("std_logic_vector", 8);
            vhdlKeywords.add("if", 6);
            vhdlKeywords.add("then", 6);
            vhdlKeywords.add("elsif", 6);
            vhdlKeywords.add("else", 6);
            vhdlKeywords.add("begin", 6);
            vhdlKeywords.add("end", 6);
            vhdlKeywords.add("for", 6);
            vhdlKeywords.add("while", 6);
            vhdlKeywords.add("loop", 6);
            vhdlKeywords.add("when", 6);
            vhdlKeywords.add("after", 6);
            vhdlKeywords.add("wait", 6);
            vhdlKeywords.add("function", 6);
            vhdlKeywords.add("procedure", 6);
            vhdlKeywords.add("case", 6);
            vhdlKeywords.add("default", 6);
            vhdlKeywords.add("transport", 6);
            vhdlKeywords.add("and", 6);
            vhdlKeywords.add("or", 6);
            vhdlKeywords.add("not", 6);
            vhdlKeywords.add("xor", 6);
            vhdlKeywords.add("entity", 6);
            vhdlKeywords.add("architecture", 6);
            vhdlKeywords.add("port", 6);
            vhdlKeywords.add("in", 6);
            vhdlKeywords.add("out", 6);
            vhdlKeywords.add("inout", 6);
            vhdlKeywords.add("map", 6);
            vhdlKeywords.add("component", 6);
            vhdlKeywords.add("of", 6);
            vhdlKeywords.add("on", 6);
            vhdlKeywords.add("is", 6);
            vhdlKeywords.add("process", 6);
            vhdlKeywords.add("return", 6);
            vhdlKeywords.add("to", 6);
            vhdlKeywords.add("downto", 6);
            vhdlKeywords.add("alias", 6);
            vhdlKeywords.add("variable", 6);
            vhdlKeywords.add("signal", 6);
            vhdlKeywords.add("constant", 6);
            vhdlKeywords.add("generic", 6);
            vhdlKeywords.add("range", 6);
            vhdlKeywords.add("event", 6);
            vhdlKeywords.add("file", 6);
            vhdlKeywords.add("time", 6);
            vhdlKeywords.add("all", 6);
            vhdlKeywords.add("package", 6);
            vhdlKeywords.add("use", 6);
            vhdlKeywords.add("library", 6);
            vhdlKeywords.add("true", 4);
            vhdlKeywords.add("false", 4);
            vhdlKeywords.add("NULL", 4);
        }
        return vhdlKeywords;
    }

    private boolean doKeyword(Segment segment, int n, char c) {
        int n2 = n + 1;
        int n3 = n - this.lastKeyword;
        int n4 = this.lastKeyword;
        int n5 = n;
        byte by = this.keywords.lookup(segment, this.lastKeyword, n3);
        if (by != 0) {
            if (this.lastKeyword != this.lastOffset) {
                this.addToken(this.lastKeyword - this.lastOffset, 0);
            }
            this.addToken(n3, by);
            this.lastOffset = n;
            if (this.keywordCase == 1 || this.allLowerCase) {
                char[] arrc = segment.array;
                for (int i = n4; i < n5; ++i) {
                    arrc[i] = Character.toLowerCase(arrc[i]);
                }
            } else if (this.keywordCase == 2) {
                char[] arrc = segment.array;
                for (int i = n4; i < n5; ++i) {
                    arrc[i] = Character.toUpperCase(arrc[i]);
                }
            }
        }
        this.lastKeyword = n2;
        return false;
    }

    public void setKeywordCase(int n) {
        this.keywordCase = n;
    }

    public int getKeywordCase() {
        return this.keywordCase;
    }

    public void setAllLowerCase(boolean bl) {
        this.allLowerCase = bl;
    }

    public boolean getAllLowerCase() {
        return this.allLowerCase;
    }
}

