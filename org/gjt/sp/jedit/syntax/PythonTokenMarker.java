/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class PythonTokenMarker
extends TokenMarker {
    private static final byte TRIPLEQUOTE1 = 100;
    private static final byte TRIPLEQUOTE2 = 126;
    private static KeywordMap pyKeywords;
    private KeywordMap keywords = PythonTokenMarker.getKeywords();
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
        block18 : for (int i = n2; i < n3; ++i) {
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
                                continue block18;
                            }
                            if (this.doKeyword(segment, i, c)) continue block18;
                            this.addToken(this.lastWhitespace - this.lastOffset + 1, by);
                            this.addToken(i - this.lastWhitespace - 1, 11);
                            this.addToken(1, 0);
                            by = 0;
                            this.lastOffset = this.lastKeyword = n4;
                            this.lastWhitespace = i;
                            continue block18;
                        }
                        case '#': {
                            if (bl) {
                                bl = false;
                                continue block18;
                            }
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, by);
                            this.addToken(n3 - i, 1);
                            this.lastOffset = this.lastKeyword = n3;
                            break block18;
                        }
                        case '\"': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block18;
                            }
                            this.addToken(i - this.lastOffset, by);
                            if (SyntaxUtilities.regionMatches(false, segment, n4, "\"\"")) {
                                this.lastOffset = this.lastKeyword = i;
                                i+=3;
                                by = (byte)100;
                                continue block18;
                            }
                            by = (byte)3;
                            this.lastOffset = this.lastKeyword = i;
                            continue block18;
                        }
                        case '\'': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block18;
                            }
                            this.addToken(i - this.lastOffset, by);
                            if (SyntaxUtilities.regionMatches(false, segment, n4, "''")) {
                                this.lastOffset = this.lastKeyword = i;
                                i+=3;
                                by = (byte)126;
                                continue block18;
                            }
                            by = (byte)4;
                            this.lastOffset = this.lastKeyword = i;
                            continue block18;
                        }
                    }
                    bl = false;
                    if (!(Character.isLetterOrDigit(c) || c == '_')) {
                        this.doKeyword(segment, i, c);
                    }
                    if (" \t~!%^*()-+=|\\#/{}[]:;\"'<>,.?@".indexOf(c) == -1) continue block18;
                    this.lastWhitespace = i;
                    continue block18;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block18;
                    }
                    if (c != '\"') continue block18;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block18;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        continue block18;
                    }
                    if (c != '\'') continue block18;
                    this.addToken(n4 - this.lastOffset, 3);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block18;
                }
                case 100: {
                    if (!SyntaxUtilities.regionMatches(false, segment, i, "\"\"\"")) continue block18;
                    this.addToken((i+=3) - this.lastOffset, 4);
                    by = 0;
                    this.lastOffset = this.lastKeyword = i;
                    continue block18;
                }
                case 126: {
                    if (!SyntaxUtilities.regionMatches(false, segment, i, "'''")) continue block18;
                    this.addToken((i+=3) - this.lastOffset, 4);
                    by = 0;
                    this.lastOffset = this.lastKeyword = i;
                    continue block18;
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
            case 100: 
            case 126: {
                this.addToken(n3 - this.lastOffset, 4);
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
        if (pyKeywords == null) {
            pyKeywords = new KeywordMap(false);
            pyKeywords.add("and", 7);
            pyKeywords.add("not", 7);
            pyKeywords.add("or", 7);
            pyKeywords.add("if", 6);
            pyKeywords.add("yield", 6);
            pyKeywords.add("for", 6);
            pyKeywords.add("assert", 6);
            pyKeywords.add("break", 6);
            pyKeywords.add("continue", 6);
            pyKeywords.add("elif", 6);
            pyKeywords.add("else", 6);
            pyKeywords.add("except", 6);
            pyKeywords.add("exec", 6);
            pyKeywords.add("finally", 6);
            pyKeywords.add("raise", 6);
            pyKeywords.add("return", 6);
            pyKeywords.add("try", 6);
            pyKeywords.add("while", 6);
            pyKeywords.add("def", 8);
            pyKeywords.add("class", 8);
            pyKeywords.add("lambda", 8);
            pyKeywords.add("del", 7);
            pyKeywords.add("from", 7);
            pyKeywords.add("global", 7);
            pyKeywords.add("import", 7);
            pyKeywords.add("in", 7);
            pyKeywords.add("is", 7);
            pyKeywords.add("pass", 7);
            pyKeywords.add("print", 7);
            pyKeywords.add("self", 4);
            pyKeywords.add("__dict__", 5);
            pyKeywords.add("__methods__", 5);
            pyKeywords.add("__members__", 5);
            pyKeywords.add("__class__", 5);
            pyKeywords.add("__bases__", 5);
            pyKeywords.add("__name__", 5);
            pyKeywords.add("Exception", 8);
            pyKeywords.add("StandardError", 8);
            pyKeywords.add("ArithmeticError", 8);
            pyKeywords.add("LookupError", 8);
            pyKeywords.add("EnvironmentError", 8);
            pyKeywords.add("AssertionError", 8);
            pyKeywords.add("AttributeError", 8);
            pyKeywords.add("EOFError", 8);
            pyKeywords.add("FloatingPointError", 8);
            pyKeywords.add("IOError", 8);
            pyKeywords.add("ImportError", 8);
            pyKeywords.add("IndexError", 8);
            pyKeywords.add("KeyError", 8);
            pyKeywords.add("KeyboardInterrupt", 8);
            pyKeywords.add("MemoryError", 8);
            pyKeywords.add("NameError", 8);
            pyKeywords.add("OSError", 8);
            pyKeywords.add("NotImplementedError", 8);
            pyKeywords.add("OverflowError", 8);
            pyKeywords.add("RuntimeError", 8);
            pyKeywords.add("SyntaxError", 8);
            pyKeywords.add("SystemError", 8);
            pyKeywords.add("SystemExit", 8);
            pyKeywords.add("TypeError", 8);
            pyKeywords.add("ValueError", 8);
            pyKeywords.add("ZeroDivisionError", 8);
        }
        return pyKeywords;
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

