/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class PHPTokenMarker
extends TokenMarker {
    public static final byte SCRIPT = 101;
    public static final byte HTML_LITERAL_QUOTE = 102;
    public static final byte HTML_LITERAL_NO_QUOTE = 103;
    public static final byte INSIDE_TAG = 104;
    public static final byte PHP_VARIABLE = 105;
    private static KeywordMap keywords = new KeywordMap(false);
    private int lastOffset;
    private int lastKeyword;
    private int lastWhitespace;
    private boolean script = false;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        this.lastWhitespace = n2 - 1;
        int n3 = segment.count + n2;
        boolean bl = false;
        block41 : for (int i = n2; i < n3; ++i) {
            int n4 = i + 1;
            char c = arrc[i];
            if (c == '\\') {
                bl = !bl;
                continue;
            }
            switch (by) {
                case 0: {
                    bl = false;
                    switch (c) {
                        case '<': {
                            this.addToken(i - this.lastOffset, by);
                            this.lastOffset = this.lastKeyword = i++;
                            if (SyntaxUtilities.regionMatches(false, segment, n4, "!--")) {
                                i+=3;
                                by = 1;
                                continue block41;
                            }
                            if (SyntaxUtilities.regionMatches(true, segment, n4, "?php")) {
                                this.addToken(1, 6, true);
                                this.addToken(4, 5, true);
                                this.lastOffset = this.lastKeyword = (i+=4) + 1;
                                this.lastWhitespace = this.lastOffset - 1;
                                by = (byte)101;
                                continue block41;
                            }
                            if (SyntaxUtilities.regionMatches(true, segment, n4, "?")) {
                                this.addToken(1, 6, true);
                                this.addToken(1, 5, true);
                                this.lastOffset = this.lastKeyword = i + 1;
                                this.lastWhitespace = this.lastOffset - 1;
                                by = (byte)101;
                                continue block41;
                            }
                            if (SyntaxUtilities.regionMatches(true, segment, n4, "script")) {
                                this.addToken(1, 6);
                                this.lastOffset = this.lastKeyword = n4;
                                by = (byte)11;
                                this.script = true;
                                continue block41;
                            }
                            this.addToken(1, 6);
                            this.lastOffset = this.lastKeyword = n4;
                            by = (byte)11;
                            continue block41;
                        }
                        case '&': {
                            this.addToken(i - this.lastOffset, by);
                            this.lastOffset = this.lastKeyword = i;
                            by = (byte)7;
                        }
                    }
                    continue block41;
                }
                case 9: {
                    bl = false;
                    if (c == '<') continue block41;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = 0;
                    continue block41;
                }
                case 11: {
                    bl = false;
                    if (c == '>') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 6);
                        this.lastOffset = this.lastKeyword = n4;
                        if (!this.script) {
                            by = 0;
                            continue block41;
                        }
                        this.script = false;
                        this.lastWhitespace = i;
                        by = (byte)101;
                        continue block41;
                    }
                    if (c == ':') {
                        this.addToken(n4 - this.lastOffset, 4);
                        this.lastOffset = this.lastKeyword = n4;
                        continue block41;
                    }
                    if (c != ' ' && c != '\t') continue block41;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)104;
                    continue block41;
                }
                case 104: {
                    if (c == '>') {
                        this.addToken(i - this.lastOffset, 11);
                        this.addToken(1, 6);
                        this.lastOffset = this.lastKeyword = n4;
                        if (!this.script) {
                            by = 0;
                            continue block41;
                        }
                        this.script = false;
                        this.lastWhitespace = i;
                        by = (byte)101;
                        continue block41;
                    }
                    if (c == '/' || c == '?') {
                        this.addToken(1, 11);
                        this.lastOffset = this.lastKeyword = n4;
                        by = (byte)11;
                        continue block41;
                    }
                    this.addToken(i - this.lastOffset, 0);
                    this.lastOffset = this.lastKeyword = i;
                    by = (byte)8;
                    continue block41;
                }
                case 7: {
                    bl = false;
                    if (c != ';') continue block41;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = 0;
                    continue block41;
                }
                case 8: {
                    if (c == '/' || c == '?') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 11);
                        this.lastOffset = this.lastKeyword = n4;
                        continue block41;
                    }
                    if (c == '=') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 5);
                        this.lastOffset = this.lastKeyword = n4;
                        if (n4 < arrc.length && arrc[n4] == '\"') {
                            by = (byte)102;
                            ++i;
                            continue block41;
                        }
                        by = (byte)103;
                        continue block41;
                    }
                    if (c == '>') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 6);
                        this.lastOffset = this.lastKeyword = n4;
                        by = 0;
                        continue block41;
                    }
                    if (c != ' ' && c != '\t') continue block41;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)104;
                    continue block41;
                }
                case 102: {
                    if (c != '\"') continue block41;
                    this.addToken(n4 - this.lastOffset, 3);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)104;
                    continue block41;
                }
                case 103: {
                    if (c == ' ' || c == '\t') {
                        this.addToken(n4 - this.lastOffset, 3);
                        this.lastOffset = this.lastKeyword = n4;
                        by = (byte)104;
                        continue block41;
                    }
                    if (c != '>') continue block41;
                    this.addToken(i - this.lastOffset, 3);
                    this.addToken(1, 6);
                    this.lastOffset = this.lastKeyword = n4;
                    by = 0;
                    continue block41;
                }
                case 1: {
                    bl = false;
                    if (!SyntaxUtilities.regionMatches(false, segment, i, "-->")) continue block41;
                    this.addToken(i + 3 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = i + 3;
                    by = 0;
                    continue block41;
                }
                case 101: {
                    switch (c) {
                        case '<': {
                            bl = false;
                            if (!this.doKeyword(segment, i, c)) {
                                this.addToken(i - this.lastOffset, by, true);
                            }
                            if (SyntaxUtilities.regionMatches(true, segment, n4, "/script>")) {
                                this.addToken(1, 6);
                                this.addToken(7, 11);
                                this.addToken(1, 6);
                                this.lastOffset = this.lastKeyword = (i+=8) + 1;
                                by = 0;
                                continue block41;
                            }
                            if (SyntaxUtilities.regionMatches(true, segment, n4, "<<HERE")) {
                                this.addToken(7, 2);
                                this.lastOffset = this.lastKeyword = (i+=6) + 1;
                                continue block41;
                            }
                            this.addToken(1, 9, true);
                            this.lastOffset = this.lastKeyword = n4;
                            continue block41;
                        }
                        case '?': {
                            bl = false;
                            if (!this.doKeyword(segment, i, c)) {
                                this.addToken(i - this.lastOffset, by, true);
                            }
                            if (SyntaxUtilities.regionMatches(true, segment, n4, ">")) {
                                this.addToken(1, 5, true);
                                this.addToken(1, 6, true);
                                this.lastOffset = this.lastKeyword = ++i + 1;
                                this.lastWhitespace = this.lastOffset - 1;
                                by = 0;
                                continue block41;
                            }
                            this.addToken(1, 9, true);
                            this.lastOffset = this.lastKeyword = n4;
                            this.lastWhitespace = i;
                            continue block41;
                        }
                        case '(': {
                            if (bl) {
                                this.doKeyword(segment, i, c);
                                bl = false;
                                continue block41;
                            }
                            if (!this.doKeyword(segment, i, c)) {
                                this.addToken(this.lastWhitespace - this.lastOffset + 1, by, true);
                                this.addToken(i - this.lastWhitespace - 1, 11, true);
                            }
                            this.addToken(1, 9, true);
                            by = (byte)101;
                            this.lastOffset = this.lastKeyword = n4;
                            this.lastWhitespace = i;
                            continue block41;
                        }
                        case '\"': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block41;
                            }
                            this.addToken(i - this.lastOffset, by, true);
                            this.lastOffset = this.lastKeyword = i;
                            by = (byte)3;
                            continue block41;
                        }
                        case '\'': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block41;
                            }
                            this.addToken(i - this.lastOffset, by, true);
                            this.lastOffset = this.lastKeyword = i;
                            by = (byte)4;
                            continue block41;
                        }
                        case '#': {
                            if (this.doKeyword(segment, i, c)) continue block41;
                            this.addToken(i - this.lastOffset, by, true);
                            this.addToken(n3 - i, 2, true);
                            this.lastOffset = this.lastKeyword = n3;
                            break block41;
                        }
                        case '/': {
                            bl = false;
                            this.doKeyword(segment, i, c);
                            if (n3 - i > 1) {
                                if (arrc[n4] == '/') {
                                    this.addToken(i - this.lastOffset, by, true);
                                    this.addToken(n3 - i, 1, true);
                                    this.lastOffset = this.lastKeyword = n3;
                                    break block41;
                                }
                                if (arrc[n4] == '*') {
                                    this.addToken(i - this.lastOffset, by, true);
                                    this.lastOffset = this.lastKeyword = i;
                                    by = (byte)2;
                                    continue block41;
                                }
                                this.addToken(i - this.lastOffset, by, true);
                                this.addToken(1, 9, true);
                                this.lastOffset = this.lastKeyword = n4;
                                continue block41;
                            }
                            this.doKeyword(segment, i, c);
                            this.addToken(1, 9, true);
                            this.lastOffset = this.lastKeyword = n4;
                            continue block41;
                        }
                        case '$': {
                            this.doKeyword(segment, i, c);
                            if (bl) {
                                bl = false;
                                continue block41;
                            }
                            this.addToken(i - this.lastOffset, by, true);
                            by = (byte)105;
                            this.lastOffset = this.lastKeyword = i;
                            continue block41;
                        }
                    }
                    bl = false;
                    if (Character.isLetterOrDigit(c) || c == '_') continue block41;
                    this.doKeyword(segment, i, c);
                    if (" \t~!%^*()-+=|\\#/{}[]:;\"'<>,.?@".indexOf(c) != -1) {
                        this.lastWhitespace = i;
                    }
                    if (c == ' ') continue block41;
                    this.addToken(i - this.lastOffset, by, true);
                    this.addToken(1, 9, true);
                    this.lastOffset = this.lastKeyword = n4;
                    continue block41;
                }
                case 105: {
                    if (Character.isLetterOrDigit(c) || c == '_') continue block41;
                    this.addToken(i - this.lastOffset, 4, true);
                    this.addToken(1, 9, true);
                    this.lastOffset = this.lastKeyword = n4;
                    this.lastWhitespace = i;
                    by = (byte)101;
                    continue block41;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block41;
                    }
                    if (c != '\"') continue block41;
                    this.addToken(n4 - this.lastOffset, 3, true);
                    this.lastOffset = this.lastKeyword = n4;
                    this.lastWhitespace = i;
                    by = (byte)101;
                    continue block41;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        continue block41;
                    }
                    if (c != '\'') continue block41;
                    this.addToken(n4 - this.lastOffset, 4, true);
                    this.lastOffset = this.lastKeyword = n4;
                    this.lastWhitespace = i;
                    by = (byte)101;
                    continue block41;
                }
                case 2: {
                    bl = false;
                    if (c != '*' || n3 - i <= 1 || arrc[n4] != '/') continue block41;
                    this.addToken(i + 2 - this.lastOffset, 2, true);
                    this.lastOffset = this.lastKeyword = ++i + 1;
                    this.lastWhitespace = i;
                    by = (byte)101;
                    continue block41;
                }
                default: {
                    throw new InternalError("Invalid state: " + by);
                }
            }
        }
        switch (by) {
            case 3: {
                this.addToken(n3 - this.lastOffset, 3);
                break;
            }
            case 4: {
                this.addToken(n3 - this.lastOffset, 4);
                break;
            }
            case 7: {
                this.addToken(n3 - this.lastOffset, 10);
                by = 0;
                break;
            }
            case 101: {
                this.doKeyword(segment, n3, '\u0000');
                this.addToken(n3 - this.lastOffset, 0, true);
                break;
            }
            case 2: {
                this.addToken(n3 - this.lastOffset, 1);
                break;
            }
            case 104: {
                break;
            }
            case 102: 
            case 103: {
                this.addToken(n3 - this.lastOffset, 3);
                break;
            }
            case 105: {
                this.addToken(n3 - this.lastOffset, 8, true);
                by = (byte)101;
                break;
            }
            case 9: 
            case 11: {
                this.addToken(n3 - this.lastOffset, by, true);
                by = (byte)101;
                break;
            }
            default: {
                this.addToken(n3 - this.lastOffset, by);
            }
        }
        return by;
    }

    protected void addToken(int n, byte by) {
        this.addToken(n, by, false);
    }

    protected void addToken(int n, byte by, boolean bl) {
        if (by == 101) {
            by = 0;
        }
        super.addToken(n, by, bl);
    }

    private boolean doKeyword(Segment segment, int n, char c) {
        int n2 = n + 1;
        int n3 = n - this.lastKeyword;
        byte by = keywords.lookup(segment, this.lastKeyword, n3);
        if (by != 0) {
            if (this.lastKeyword != this.lastOffset) {
                this.addToken(this.lastKeyword - this.lastOffset, 0, true);
            }
            this.addToken(n3, by, true);
            this.lastOffset = n;
            this.lastKeyword = n2;
            this.lastWhitespace = n;
            return true;
        }
        this.lastKeyword = n2;
        return false;
    }

    static {
        keywords.add("function", 7);
        keywords.add("class", 7);
        keywords.add("var", 7);
        keywords.add("require", 7);
        keywords.add("include", 7);
        keywords.add("and", 6);
        keywords.add("or", 6);
        keywords.add("else", 6);
        keywords.add("elseif", 6);
        keywords.add("do", 6);
        keywords.add("for", 6);
        keywords.add("foreach", 6);
        keywords.add("if", 6);
        keywords.add("endif", 6);
        keywords.add("in", 6);
        keywords.add("new", 6);
        keywords.add("return", 6);
        keywords.add("while", 6);
        keywords.add("endwhile", 6);
        keywords.add("with", 6);
        keywords.add("break", 6);
        keywords.add("switch", 6);
        keywords.add("case", 6);
        keywords.add("continue", 6);
        keywords.add("default", 6);
        keywords.add("echo", 6);
        keywords.add("false", 6);
        keywords.add("this", 6);
        keywords.add("true", 6);
        keywords.add("array", 6);
        keywords.add("extends", 6);
    }
}

