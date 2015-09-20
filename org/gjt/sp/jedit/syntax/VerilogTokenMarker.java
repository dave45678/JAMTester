/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class VerilogTokenMarker
extends TokenMarker {
    static final int NORMAL = 0;
    static final int SIMPLE_QUOTE = 1;
    static final int BACK_ACCENT = 2;
    static final int DOLLAR = 3;
    int env;
    private static KeywordMap vKeywords;
    private boolean cpp;
    private KeywordMap keywords;
    private int lastOffset;
    private int lastKeyword;

    public VerilogTokenMarker() {
        this(VerilogTokenMarker.getKeywords());
    }

    public VerilogTokenMarker(KeywordMap keywordMap) {
        this.keywords = keywordMap;
    }

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        int n3 = segment.count + n2;
        boolean bl = false;
        boolean bl2 = false;
        this.env = 0;
        block24 : for (int i = n2; i < n3; ++i) {
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
                            if (bl) {
                                bl = false;
                                continue block24;
                            }
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, by);
                            this.addToken(1, 6);
                            this.lastOffset = this.lastKeyword = i + 1;
                            continue block24;
                        }
                        case '@': {
                            if (bl) {
                                bl = false;
                                continue block24;
                            }
                            bl2 = true;
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, by);
                            by = 0;
                            this.lastOffset = this.lastKeyword = i;
                            continue block24;
                        }
                        case '$': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block24;
                            }
                            this.env = 3;
                            this.addToken(i - this.lastOffset, by);
                            by = 0;
                            this.lastOffset = this.lastKeyword = i;
                            continue block24;
                        }
                        case '`': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block24;
                            }
                            this.env = 2;
                            this.addToken(i - this.lastOffset, by);
                            by = 0;
                            this.lastOffset = this.lastKeyword = i;
                            continue block24;
                        }
                        case '\'': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block24;
                            }
                            this.env = 1;
                            this.addToken(i - this.lastOffset, by);
                            by = 0;
                            this.lastOffset = this.lastKeyword = i;
                            continue block24;
                        }
                        case '\"': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block24;
                            }
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)3;
                            this.lastOffset = this.lastKeyword = i;
                            continue block24;
                        }
                        case ':': {
                            if (this.lastKeyword == n2) {
                                if (this.doKeyword(segment, i, c)) continue block24;
                                bl = false;
                                this.addToken(n4 - this.lastOffset, 5);
                                this.lastOffset = this.lastKeyword = n4;
                                continue block24;
                            }
                            if (!this.doKeyword(segment, i, c)) continue block24;
                            continue block24;
                        }
                        case '/': {
                            bl = false;
                            this.doKeyword(segment, i, c);
                            if (n3 - i <= 1) continue block24;
                            switch (arrc[n4]) {
                                case '*': {
                                    this.addToken(i - this.lastOffset, by);
                                    this.lastOffset = this.lastKeyword = i;
                                    if (n3 - i > 2 && arrc[i + 2] == '*') {
                                        by = (byte)2;
                                        continue block24;
                                    }
                                    by = 1;
                                    continue block24;
                                }
                                case '/': {
                                    this.addToken(i - this.lastOffset, by);
                                    this.addToken(n3 - i, 1);
                                    this.lastOffset = this.lastKeyword = n3;
                                    break block24;
                                }
                            }
                            continue block24;
                        }
                    }
                    bl = false;
                    if (bl2) {
                        bl2 = false;
                        this.doKeyword(segment, i, c);
                        this.addToken(n4 - this.lastOffset, by);
                        by = 0;
                        this.lastOffset = this.lastKeyword = n4;
                        continue block24;
                    }
                    if (Character.isLetterOrDigit(c) || c == '_') continue block24;
                    this.doKeyword(segment, i, c);
                    continue block24;
                }
                case 1: 
                case 2: {
                    bl = false;
                    if (c != '*' || n3 - i <= 1 || arrc[n4] != '/') continue block24;
                    this.addToken(++i + 1 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = i + 1;
                    continue block24;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block24;
                    }
                    if (c != '\"') continue block24;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    this.env = 0;
                    continue block24;
                }
                case 4: {
                    if (Character.isLetterOrDigit(c) || c == '_') continue block24;
                    this.addToken(n4 - this.lastOffset, 4);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    this.env = 0;
                    continue block24;
                }
                case 7: {
                    if (Character.isLetterOrDigit(c) || c == '_') continue block24;
                    this.addToken(n4 - this.lastOffset, 7);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    this.env = 0;
                    continue block24;
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
            case 4: 
            case 7: {
                this.addToken(n3 - this.lastOffset, by);
                if (bl) break;
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
        if (vKeywords == null) {
            vKeywords = new KeywordMap(false);
            vKeywords.add("reg", 8);
            vKeywords.add("wire", 8);
            vKeywords.add("wand", 8);
            vKeywords.add("wor", 8);
            vKeywords.add("integer", 8);
            vKeywords.add("parameter", 8);
            vKeywords.add("integer", 8);
            vKeywords.add("real", 8);
            vKeywords.add("time", 8);
            vKeywords.add("realtime", 8);
            vKeywords.add("event", 8);
            vKeywords.add("input", 6);
            vKeywords.add("output", 6);
            vKeywords.add("inout", 6);
            vKeywords.add("module", 6);
            vKeywords.add("endmodule", 6);
            vKeywords.add("assign", 6);
            vKeywords.add("always", 6);
            vKeywords.add("posedge", 6);
            vKeywords.add("negedge", 6);
            vKeywords.add("initial", 6);
            vKeywords.add("forever", 6);
            vKeywords.add("while", 6);
            vKeywords.add("for", 6);
            vKeywords.add("if", 6);
            vKeywords.add("else", 6);
            vKeywords.add("case", 6);
            vKeywords.add("casex", 6);
            vKeywords.add("casez", 6);
            vKeywords.add("default", 6);
            vKeywords.add("endcase", 6);
            vKeywords.add("or", 6);
            vKeywords.add("#", 6);
            vKeywords.add("@", 6);
            vKeywords.add("begin", 6);
            vKeywords.add("end", 6);
            vKeywords.add("fork", 6);
            vKeywords.add("join", 6);
            vKeywords.add("wait", 6);
            vKeywords.add("function", 6);
            vKeywords.add("endfunction", 6);
            vKeywords.add("task", 6);
            vKeywords.add("endtask", 6);
            vKeywords.add("$display", 7);
            vKeywords.add("$write", 7);
            vKeywords.add("$time", 7);
            vKeywords.add("$monitor", 7);
            vKeywords.add("$finish", 7);
            vKeywords.add("$readmemb", 7);
            vKeywords.add("$readmemh", 7);
            vKeywords.add("$stop", 7);
            vKeywords.add("$define_group_waves", 7);
            vKeywords.add("$gr_waves_memsize", 7);
            vKeywords.add("$gr_waves", 7);
            vKeywords.add("`include", 7);
            vKeywords.add("`define", 7);
            vKeywords.add("`ifdef", 7);
            vKeywords.add("`define", 7);
        }
        return vKeywords;
    }

    private boolean doKeyword(Segment segment, int n, char c) {
        int n2 = n + 1;
        int n3 = n - this.lastKeyword;
        byte by = this.keywords.lookup(segment, this.lastKeyword, n3);
        switch (this.env) {
            case 2: {
                if (by != 0) break;
                by = 4;
                break;
            }
            case 1: {
                by = 4;
                this.lastKeyword+=2;
                n3-=2;
            }
        }
        if (by != 0) {
            if (this.lastKeyword != this.lastOffset) {
                this.addToken(this.lastKeyword - this.lastOffset, 0);
            }
            this.addToken(n3, by);
            this.lastOffset = n;
        }
        this.lastKeyword = n2;
        this.env = 0;
        return false;
    }
}

