/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.JavaScriptTokenMarker;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class HTMLTokenMarker
extends TokenMarker {
    public static final byte JAVASCRIPT = 100;
    public static final byte HTML_LITERAL_QUOTE = 101;
    public static final byte HTML_LITERAL_NO_QUOTE = 102;
    public static final byte INSIDE_TAG = 103;
    private KeywordMap keywords;
    private boolean js;
    private boolean javascript;
    private int lastOffset;
    private int lastKeyword;
    private int lastWhitespace;

    public HTMLTokenMarker() {
        this(true);
    }

    public HTMLTokenMarker(boolean bl) {
        this.js = bl;
        this.keywords = JavaScriptTokenMarker.getKeywords();
    }

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        int n3 = segment.count + n2;
        boolean bl = false;
        this.lastWhitespace = n2 - 1;
        block35 : for (int i = n2; i < n3; ++i) {
            int n4 = i + 1;
            char c = arrc[i];
            if (c == '\\') {
                boolean bl2 = bl = !bl;
                if (by == 100) continue;
            }
            switch (by) {
                case 0: {
                    bl = false;
                    switch (c) {
                        case '\\': {
                            this.addToken(i - this.lastOffset, by);
                            this.lastOffset = this.lastKeyword = i;
                            by = (byte)9;
                            continue block35;
                        }
                        case '<': {
                            this.addToken(i - this.lastOffset, by);
                            this.lastOffset = this.lastKeyword = i;
                            if (SyntaxUtilities.regionMatches(false, segment, n4, "!--")) {
                                i+=3;
                                by = 1;
                                continue block35;
                            }
                            if (this.js && SyntaxUtilities.regionMatches(true, segment, n4, "script")) {
                                this.addToken(1, 6);
                                this.lastOffset = this.lastKeyword = n4;
                                by = (byte)11;
                                this.javascript = true;
                                continue block35;
                            }
                            this.addToken(1, 6);
                            this.lastOffset = this.lastKeyword = n4;
                            by = (byte)11;
                            continue block35;
                        }
                        case '&': {
                            this.addToken(i - this.lastOffset, by);
                            this.lastOffset = this.lastKeyword = i;
                            by = (byte)7;
                        }
                    }
                    continue block35;
                }
                case 9: {
                    bl = false;
                    if (c == '<') continue block35;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = 0;
                    continue block35;
                }
                case 11: {
                    bl = false;
                    if (c == '>') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 6);
                        this.lastOffset = this.lastKeyword = n4;
                        if (!this.javascript) {
                            by = 0;
                            continue block35;
                        }
                        this.javascript = false;
                        this.lastWhitespace = i;
                        by = (byte)100;
                        continue block35;
                    }
                    if (c == ':') {
                        this.addToken(n4 - this.lastOffset, 4);
                        this.lastOffset = this.lastKeyword = n4;
                        continue block35;
                    }
                    if (c != ' ' && c != '\t') continue block35;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)103;
                    continue block35;
                }
                case 103: {
                    if (c == '>') {
                        this.addToken(i - this.lastOffset, 11);
                        this.addToken(1, 6);
                        this.lastOffset = this.lastKeyword = n4;
                        if (!this.javascript) {
                            by = 0;
                            continue block35;
                        }
                        this.javascript = false;
                        by = (byte)100;
                        continue block35;
                    }
                    if (c == '/' || c == '?') {
                        this.addToken(1, 11);
                        this.lastOffset = this.lastKeyword = n4;
                        by = (byte)11;
                        continue block35;
                    }
                    this.addToken(i - this.lastOffset, 0);
                    this.lastOffset = this.lastKeyword = i;
                    by = (byte)8;
                    continue block35;
                }
                case 7: {
                    bl = false;
                    if (c != ';') continue block35;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = 0;
                    continue block35;
                }
                case 8: {
                    if (c == '/' || c == '?') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 11);
                        this.lastOffset = this.lastKeyword = n4;
                        continue block35;
                    }
                    if (c == '=') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 5);
                        this.lastOffset = this.lastKeyword = n4;
                        if (n4 < arrc.length && arrc[n4] == '\"') {
                            by = (byte)101;
                            ++i;
                            continue block35;
                        }
                        by = (byte)102;
                        continue block35;
                    }
                    if (c == '>') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 6);
                        this.lastOffset = this.lastKeyword = n4;
                        by = 0;
                        continue block35;
                    }
                    if (c != ' ' && c != '\t') continue block35;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)103;
                    continue block35;
                }
                case 101: {
                    if (c != '\"') continue block35;
                    this.addToken(n4 - this.lastOffset, 3);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)103;
                    continue block35;
                }
                case 102: {
                    if (c == ' ' || c == '\t') {
                        this.addToken(n4 - this.lastOffset, 3);
                        this.lastOffset = this.lastKeyword = n4;
                        by = (byte)103;
                        continue block35;
                    }
                    if (c != '>') continue block35;
                    this.addToken(i - this.lastOffset, 3);
                    this.addToken(1, 6);
                    this.lastOffset = this.lastKeyword = n4;
                    by = 0;
                    continue block35;
                }
                case 1: {
                    bl = false;
                    if (!SyntaxUtilities.regionMatches(false, segment, i, "-->")) continue block35;
                    this.addToken(i + 3 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = i + 3;
                    by = 0;
                    continue block35;
                }
                case 100: {
                    switch (c) {
                        case '<': {
                            bl = false;
                            this.doKeyword(segment, i, c);
                            if (!SyntaxUtilities.regionMatches(true, segment, n4, "/script>")) continue block35;
                            this.addToken(i - this.lastOffset, 0);
                            this.addToken(1, 6);
                            this.addToken(7, 11);
                            this.addToken(1, 6);
                            this.lastOffset = this.lastKeyword = (i+=9);
                            by = 0;
                            continue block35;
                        }
                        case '(': {
                            if (bl) {
                                this.doKeyword(segment, i, c);
                                bl = false;
                                continue block35;
                            }
                            if (this.doKeyword(segment, i, c)) continue block35;
                            this.addToken(this.lastWhitespace - this.lastOffset + 1, 0);
                            this.addToken(i - this.lastWhitespace - 1, 11);
                            this.addToken(1, 0);
                            by = (byte)100;
                            this.lastOffset = this.lastKeyword = n4;
                            this.lastWhitespace = i;
                            continue block35;
                        }
                        case '\"': {
                            if (bl) {
                                bl = false;
                                continue block35;
                            }
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, 0);
                            this.lastOffset = this.lastKeyword = i;
                            by = (byte)3;
                            continue block35;
                        }
                        case '\'': {
                            if (bl) {
                                bl = false;
                                continue block35;
                            }
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, 0);
                            this.lastOffset = this.lastKeyword = i;
                            by = (byte)4;
                            continue block35;
                        }
                        case '/': {
                            bl = false;
                            this.doKeyword(segment, i, c);
                            if (n3 - i <= 1) continue block35;
                            this.addToken(i - this.lastOffset, 0);
                            this.lastOffset = this.lastKeyword = i;
                            if (arrc[n4] == '/') {
                                this.addToken(n3 - i, 2);
                                this.lastOffset = this.lastKeyword = n3;
                                break block35;
                            }
                            if (arrc[n4] != '*') continue block35;
                            by = (byte)2;
                            continue block35;
                        }
                    }
                    bl = false;
                    if (!(Character.isLetterOrDigit(c) || c == '_')) {
                        this.doKeyword(segment, i, c);
                    }
                    if (" \t~!%^*()-+=|\\#/{}[]:;\"'<>,.?@".indexOf(c) == -1) continue block35;
                    this.lastWhitespace = i;
                    continue block35;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block35;
                    }
                    if (c != '\"') continue block35;
                    this.addToken(n4 - this.lastOffset, 3);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)100;
                    continue block35;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        continue block35;
                    }
                    if (c != '\'') continue block35;
                    this.addToken(n4 - this.lastOffset, 3);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)100;
                    continue block35;
                }
                case 2: {
                    bl = false;
                    if (c != '*' || n3 - i <= 1 || arrc[n4] != '/') continue block35;
                    this.addToken((i+=2) - this.lastOffset, 1);
                    this.lastOffset = this.lastKeyword = i;
                    by = (byte)100;
                    continue block35;
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
                by = (byte)100;
                break;
            }
            case 7: {
                this.addToken(n3 - this.lastOffset, 10);
                by = 0;
                break;
            }
            case 100: {
                this.doKeyword(segment, n3, '\u0000');
                this.addToken(n3 - this.lastOffset, 0);
                break;
            }
            case 2: {
                this.addToken(n3 - this.lastOffset, 1);
                break;
            }
            case 103: {
                break;
            }
            case 101: 
            case 102: {
                this.addToken(n3 - this.lastOffset, 3);
                break;
            }
            default: {
                this.addToken(n3 - this.lastOffset, by);
            }
        }
        return by;
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
            this.lastKeyword = n2;
            this.lastOffset = n;
            this.lastWhitespace = n;
            return true;
        }
        this.lastKeyword = n2;
        return false;
    }
}

