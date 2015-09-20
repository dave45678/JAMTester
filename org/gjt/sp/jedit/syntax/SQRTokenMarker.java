/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class SQRTokenMarker
extends TokenMarker {
    boolean bracket = false;
    private static KeywordMap sqrKeywords;
    private boolean cpp;
    private boolean javadoc;
    private KeywordMap keywords = SQRTokenMarker.getKeywords();
    private int lastOffset;
    private int lastKeyword;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        int n3 = segment.count + n2;
        block18 : for (int i = n2; i < n3; ++i) {
            int n4 = i + 1;
            char c = arrc[i];
            switch (by) {
                case 0: {
                    switch (c) {
                        case '[': {
                            this.bracket = true;
                        }
                        case '\"': {
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)3;
                            this.lastOffset = this.lastKeyword = i;
                            continue block18;
                        }
                        case '\'': {
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)4;
                            this.lastOffset = this.lastKeyword = i;
                            continue block18;
                        }
                        case ':': {
                            if (this.lastKeyword == n2) {
                                if (this.doKeyword(segment, i, c)) continue block18;
                                this.addToken(n4 - this.lastOffset, 5);
                                this.lastOffset = this.lastKeyword = n4;
                                continue block18;
                            }
                            if (!this.doKeyword(segment, i, c)) continue block18;
                            continue block18;
                        }
                        case '!': {
                            this.doKeyword(segment, i, c);
                            if (n3 - i <= 1) continue block18;
                            this.addToken(i - this.lastOffset, by);
                            this.addToken(n3 - i, 1);
                            this.lastOffset = n3;
                            break block18;
                        }
                    }
                    if (Character.isLetterOrDigit(c) || c == '-' || c == '#') continue block18;
                    this.doKeyword(segment, i, c);
                    continue block18;
                }
                case 1: {
                    continue block18;
                }
                case 2: {
                    if (c != '*' || n3 - i <= 1 || arrc[n4] != '/') continue block18;
                    this.addToken(++i + 1 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = i + 1;
                    continue block18;
                }
                case 3: {
                    if (c != '\"' && c != ']') continue block18;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    this.bracket = false;
                    continue block18;
                }
                case 4: {
                    if (c != '\'') continue block18;
                    this.addToken(n4 - this.lastOffset, 3);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block18;
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
                this.addToken(n3 - this.lastOffset, this.bracket ? 3 : 10);
                by = (byte)(this.bracket ? 3 : 0);
                break;
            }
            case 7: {
                this.addToken(n3 - this.lastOffset, by);
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
        if (sqrKeywords == null) {
            sqrKeywords = new KeywordMap(true);
            sqrKeywords.add("BEGIN-FOOTING", 6);
            sqrKeywords.add("BEGIN-HEADING", 6);
            sqrKeywords.add("BEGIN-PROCEDURE", 6);
            sqrKeywords.add("BEGIN-PROGRAM", 6);
            sqrKeywords.add("BEGIN-REPORT", 6);
            sqrKeywords.add("BEGIN-SELECT", 6);
            sqrKeywords.add("BEGIN-SETUP", 6);
            sqrKeywords.add("END-FOOTING", 6);
            sqrKeywords.add("END-HEADING", 6);
            sqrKeywords.add("END-PROCEDURE", 6);
            sqrKeywords.add("END-PROGRAM", 6);
            sqrKeywords.add("END-REPORT", 6);
            sqrKeywords.add("END-SETUP", 6);
            sqrKeywords.add("END-SELECT", 6);
            sqrKeywords.add("INPUT", 6);
            sqrKeywords.add("#include", 7);
            sqrKeywords.add("#debug", 7);
            sqrKeywords.add("#define", 7);
            sqrKeywords.add("#else", 7);
            sqrKeywords.add("#end-if", 7);
            sqrKeywords.add("#endif", 7);
            sqrKeywords.add("#if", 7);
            sqrKeywords.add("#ifdef", 7);
            sqrKeywords.add("#ifndef", 7);
            sqrKeywords.add("add", 8);
            sqrKeywords.add("array-add", 8);
            sqrKeywords.add("array-divide", 8);
            sqrKeywords.add("array-multiply", 8);
            sqrKeywords.add("array-subtract", 8);
            sqrKeywords.add("ask", 8);
            sqrKeywords.add("break", 8);
            sqrKeywords.add("call", 8);
            sqrKeywords.add("clear-array", 8);
            sqrKeywords.add("close", 8);
            sqrKeywords.add("columns", 8);
            sqrKeywords.add("commit", 8);
            sqrKeywords.add("concat", 8);
            sqrKeywords.add("connect", 8);
            sqrKeywords.add("create-array", 8);
            sqrKeywords.add("date-time", 8);
            sqrKeywords.add("display", 8);
            sqrKeywords.add("divide", 8);
            sqrKeywords.add("do", 8);
            sqrKeywords.add("dollar-symbol", 8);
            sqrKeywords.add("else", 8);
            sqrKeywords.add("encode", 8);
            sqrKeywords.add("end-evaluate", 8);
            sqrKeywords.add("end-if", 8);
            sqrKeywords.add("end-while", 8);
            sqrKeywords.add("evaluate", 8);
            sqrKeywords.add("execute", 8);
            sqrKeywords.add("extract", 8);
            sqrKeywords.add("find", 8);
            sqrKeywords.add("font", 8);
            sqrKeywords.add("get", 8);
            sqrKeywords.add("goto", 8);
            sqrKeywords.add("graphic", 8);
            sqrKeywords.add("if", 8);
            sqrKeywords.add("last-page", 8);
            sqrKeywords.add("let", 8);
            sqrKeywords.add("lookup", 8);
            sqrKeywords.add("lowercase", 8);
            sqrKeywords.add("money-symbol", 8);
            sqrKeywords.add("move", 8);
            sqrKeywords.add("multiply", 8);
            sqrKeywords.add("new-page", 8);
            sqrKeywords.add("new-report", 8);
            sqrKeywords.add("next-column", 8);
            sqrKeywords.add("next-listing", 8);
            sqrKeywords.add("no-formfeed", 8);
            sqrKeywords.add("open", 8);
            sqrKeywords.add("page-number", 8);
            sqrKeywords.add("page-size", 8);
            sqrKeywords.add("position", 8);
            sqrKeywords.add("print", 8);
            sqrKeywords.add("print-bar-code", 8);
            sqrKeywords.add("print-chart", 8);
            sqrKeywords.add("print-direct", 8);
            sqrKeywords.add("print-image", 8);
            sqrKeywords.add("printer-deinit", 8);
            sqrKeywords.add("printer-init", 8);
            sqrKeywords.add("put", 8);
            sqrKeywords.add("read", 8);
            sqrKeywords.add("rollback", 8);
            sqrKeywords.add("show", 8);
            sqrKeywords.add("stop", 8);
            sqrKeywords.add("string", 8);
            sqrKeywords.add("subtract", 8);
            sqrKeywords.add("unstring", 8);
            sqrKeywords.add("uppercase", 8);
            sqrKeywords.add("use", 8);
            sqrKeywords.add("use-column", 8);
            sqrKeywords.add("use-printer-type", 8);
            sqrKeywords.add("use-procedure", 8);
            sqrKeywords.add("use-report", 8);
            sqrKeywords.add("use-report", 8);
            sqrKeywords.add("while", 8);
            sqrKeywords.add("write", 8);
            sqrKeywords.add("from", 8);
            sqrKeywords.add("where", 8);
            sqrKeywords.add("order", 8);
            sqrKeywords.add("by", 8);
            sqrKeywords.add("in", 8);
            sqrKeywords.add("to", 8);
            sqrKeywords.add("between", 8);
            sqrKeywords.add("and", 8);
            sqrKeywords.add("or", 8);
            sqrKeywords.add("substr", 8);
            sqrKeywords.add("instr", 8);
            sqrKeywords.add("len", 8);
        }
        return sqrKeywords;
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

