/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.JavaScriptTokenMarker;
import org.gjt.sp.jedit.syntax.JavaTokenMarker;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class JSPTokenMarker
extends TokenMarker {
    public static final byte JAVASCRIPT = 100;
    public static final byte HTML_LITERAL_QUOTE = 101;
    public static final byte HTML_LITERAL_NO_QUOTE = 102;
    public static final byte INSIDE_TAG = 103;
    public static final byte JSP_NULL = 104;
    public static final byte JSP_KEYWORD1 = 110;
    public static final byte JSP_KEYWORD2 = 111;
    public static final byte JSP_KEYWORD3 = 112;
    public static final byte JSP_COMMENT1 = 105;
    public static final byte JSP_COMMENT2 = 106;
    public static final byte JSP_METHOD = 115;
    public static final byte JSP_LITERAL1 = 107;
    public static final byte JSP_LITERAL2 = 108;
    public static final byte JSP_LABEL = 109;
    private KeywordMap keywords = JavaScriptTokenMarker.getKeywords();
    private KeywordMap javaKeywords = JavaTokenMarker.getKeywords();
    private boolean js = true;
    private boolean javascript;
    private int lastOffset;
    private int lastKeyword;
    private int lastWhitespace;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        int n3 = segment.count + n2;
        boolean bl = false;
        this.lastWhitespace = n2 - 1;
        block58 : for (int i = n2; i < n3; ++i) {
            int n4 = i + 1;
            char c = arrc[i];
            if (c == '\\') {
                boolean bl2 = bl = !bl;
                if (by == 100) continue;
                if (by >= 104) continue;
            }
            switch (by) {
                case 0: {
                    bl = false;
                    switch (c) {
                        case '\\': {
                            this.addToken(i - this.lastOffset, by);
                            this.lastOffset = this.lastKeyword = i;
                            by = (byte)9;
                            continue block58;
                        }
                        case '<': {
                            this.addToken(i - this.lastOffset, by);
                            this.lastOffset = this.lastKeyword = i;
                            if (SyntaxUtilities.regionMatches(false, segment, n4, "!--")) {
                                i+=3;
                                by = 1;
                                continue block58;
                            }
                            if (this.js && SyntaxUtilities.regionMatches(true, segment, n4, "script")) {
                                this.addToken(1, 6);
                                this.lastOffset = this.lastKeyword = n4;
                                this.lastWhitespace = i;
                                by = (byte)11;
                                this.javascript = true;
                                continue block58;
                            }
                            if (SyntaxUtilities.regionMatches(true, segment, n4, "%@")) {
                                this.addToken(2, 9);
                                this.addToken(1, 5);
                                this.lastOffset = this.lastKeyword = (i+=3);
                                this.lastWhitespace = i + 2;
                                by = (byte)109;
                                continue block58;
                            }
                            if (SyntaxUtilities.regionMatches(true, segment, n4, "%=")) {
                                this.addToken(3, 9);
                                this.lastOffset = this.lastKeyword = (i+=3);
                                this.lastWhitespace = i + 2;
                                by = (byte)104;
                                continue block58;
                            }
                            if (SyntaxUtilities.regionMatches(true, segment, n4, "%!")) {
                                this.addToken(3, 9);
                                this.lastOffset = this.lastKeyword = (i+=3);
                                this.lastWhitespace = i + 2;
                                by = (byte)104;
                                continue block58;
                            }
                            if (SyntaxUtilities.regionMatches(true, segment, n4, "%")) {
                                this.addToken(2, 9);
                                this.lastOffset = this.lastKeyword = (i+=2);
                                this.lastWhitespace = n4;
                                by = (byte)104;
                                continue block58;
                            }
                            this.addToken(1, 6);
                            this.lastOffset = this.lastKeyword = n4;
                            by = (byte)11;
                            continue block58;
                        }
                        case '&': {
                            this.addToken(i - this.lastOffset, by);
                            this.lastOffset = this.lastKeyword = i;
                            by = (byte)7;
                        }
                    }
                    continue block58;
                }
                case 9: {
                    bl = false;
                    if (c == '<') continue block58;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = 0;
                    continue block58;
                }
                case 11: {
                    bl = false;
                    if (c == '>') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 6);
                        this.lastOffset = this.lastKeyword = n4;
                        if (!this.javascript) {
                            by = 0;
                            continue block58;
                        }
                        this.javascript = false;
                        by = (byte)100;
                        continue block58;
                    }
                    if (c == ':') {
                        this.addToken(n4 - this.lastOffset, 4);
                        this.lastOffset = this.lastKeyword = n4;
                        continue block58;
                    }
                    if (c != ' ' && c != '\t') continue block58;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)103;
                    continue block58;
                }
                case 103: {
                    bl = false;
                    if (c == '>') {
                        this.addToken(i - this.lastOffset, 11);
                        this.addToken(1, 6);
                        this.lastOffset = this.lastKeyword = n4;
                        if (!this.javascript) {
                            by = 0;
                            continue block58;
                        }
                        this.javascript = false;
                        by = (byte)100;
                        continue block58;
                    }
                    if (c == '/' || c == '?') {
                        this.addToken(1, 11);
                        this.lastOffset = this.lastKeyword = n4;
                        by = (byte)11;
                        continue block58;
                    }
                    this.addToken(i - this.lastOffset, 0);
                    this.lastOffset = this.lastKeyword = i;
                    by = (byte)8;
                    continue block58;
                }
                case 7: {
                    bl = false;
                    if (c != ';') continue block58;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = 0;
                    continue block58;
                }
                case 8: {
                    bl = false;
                    if (c == '/' || c == '?') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 11);
                        this.lastOffset = this.lastKeyword = n4;
                        continue block58;
                    }
                    if (c == '=') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 5);
                        this.lastOffset = this.lastKeyword = n4;
                        if (n4 < arrc.length && arrc[n4] == '\"') {
                            by = (byte)101;
                            ++i;
                            continue block58;
                        }
                        by = (byte)102;
                        continue block58;
                    }
                    if (c == '>') {
                        this.addToken(i - this.lastOffset, by);
                        this.addToken(1, 6);
                        this.lastOffset = this.lastKeyword = n4;
                        by = 0;
                        continue block58;
                    }
                    if (c != ' ' && c != '\t') continue block58;
                    this.addToken(n4 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)103;
                    continue block58;
                }
                case 101: {
                    bl = false;
                    if (c != '\"') continue block58;
                    this.addToken(n4 - this.lastOffset, 3);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)103;
                    continue block58;
                }
                case 102: {
                    bl = false;
                    if (c == ' ' || c == '\t') {
                        this.addToken(n4 - this.lastOffset, 3);
                        this.lastOffset = this.lastKeyword = n4;
                        by = (byte)103;
                        continue block58;
                    }
                    if (c != '>') continue block58;
                    this.addToken(i - this.lastOffset, 3);
                    this.addToken(1, 6);
                    this.lastOffset = this.lastKeyword = n4;
                    by = 0;
                    continue block58;
                }
                case 1: {
                    bl = false;
                    if (!SyntaxUtilities.regionMatches(false, segment, i, "-->")) continue block58;
                    this.addToken(i + 3 - this.lastOffset, by);
                    this.lastOffset = this.lastKeyword = i + 3;
                    by = 0;
                    continue block58;
                }
                case 100: {
                    switch (c) {
                        case '<': {
                            bl = false;
                            this.doJSKeyword(segment, i, c);
                            if (!SyntaxUtilities.regionMatches(true, segment, n4, "/script>")) continue block58;
                            this.addToken(i - this.lastOffset, 0);
                            this.addToken(1, 6);
                            this.addToken(7, 11);
                            this.addToken(1, 6);
                            this.lastOffset = this.lastKeyword = (i+=9);
                            by = 0;
                            continue block58;
                        }
                        case '(': {
                            if (bl) {
                                this.doJSKeyword(segment, i, c);
                                bl = false;
                                continue block58;
                            }
                            if (this.doJSKeyword(segment, i, c)) continue block58;
                            this.addToken(this.lastWhitespace - this.lastOffset + 1, 0);
                            this.addToken(i - this.lastWhitespace - 1, 11);
                            this.addToken(1, 0);
                            by = (byte)100;
                            this.lastOffset = this.lastKeyword = n4;
                            this.lastWhitespace = i;
                            continue block58;
                        }
                        case '\"': {
                            if (bl) {
                                bl = false;
                                continue block58;
                            }
                            this.doJSKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, 0);
                            this.lastOffset = this.lastKeyword = i;
                            by = (byte)3;
                            continue block58;
                        }
                        case '\'': {
                            if (bl) {
                                bl = false;
                                continue block58;
                            }
                            this.doJSKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, 0);
                            this.lastOffset = this.lastKeyword = i;
                            by = (byte)4;
                            continue block58;
                        }
                        case '/': {
                            bl = false;
                            this.doJSKeyword(segment, i, c);
                            if (n3 - i <= 1) continue block58;
                            this.addToken(i - this.lastOffset, 0);
                            this.lastOffset = this.lastKeyword = i;
                            if (arrc[n4] == '/') {
                                this.addToken(n3 - i, 2);
                                this.lastOffset = this.lastKeyword = n3;
                                break block58;
                            }
                            if (arrc[n4] != '*') continue block58;
                            by = (byte)2;
                            continue block58;
                        }
                    }
                    bl = false;
                    if (!(Character.isLetterOrDigit(c) || c == '_')) {
                        this.doJSKeyword(segment, i, c);
                    }
                    if (" \t~!%^*()-+=|\\#/{}[]:;\"'<>,.?@".indexOf(c) == -1) continue block58;
                    this.lastWhitespace = i;
                    continue block58;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block58;
                    }
                    if (c != '\"') continue block58;
                    this.addToken(n4 - this.lastOffset, 3);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)100;
                    continue block58;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        continue block58;
                    }
                    if (c != '\'') continue block58;
                    this.addToken(n4 - this.lastOffset, 3);
                    this.lastOffset = this.lastKeyword = n4;
                    by = (byte)100;
                    continue block58;
                }
                case 2: {
                    bl = false;
                    if (c != '*' || n3 - i <= 1 || arrc[n4] != '/') continue block58;
                    this.addToken((i+=2) - this.lastOffset, 1);
                    this.lastOffset = this.lastKeyword = i;
                    by = (byte)100;
                    continue block58;
                }
                default: {
                    if (by > 103) {
                        switch (by) {
                            case 104: {
                                switch (c) {
                                    case '%': {
                                        bl = false;
                                        if (n3 - i > 1 && arrc[n4] == '>') {
                                            this.addToken(i - this.lastOffset, (byte)(by - 104));
                                            this.addToken(2, 9);
                                            this.lastOffset = this.lastKeyword = ++i + 1;
                                            by = 0;
                                            continue block58;
                                        }
                                        this.addToken(i - this.lastOffset, (byte)(by - 104));
                                        this.addToken(1, 0);
                                        this.lastKeyword = this.lastWhitespace = ++i;
                                        this.lastOffset = this.lastWhitespace;
                                        by = (byte)104;
                                        continue block58;
                                    }
                                    case '(': {
                                        if (bl) {
                                            this.doJavaKeyword(segment, i, c);
                                            bl = false;
                                            continue block58;
                                        }
                                        if (this.doJavaKeyword(segment, i, c)) continue block58;
                                        this.addToken(this.lastWhitespace - this.lastOffset + 1, (byte)(by - 104));
                                        this.addToken(i - this.lastWhitespace - 1, 11);
                                        this.addToken(1, 0);
                                        by = (byte)104;
                                        this.lastOffset = this.lastKeyword = n4;
                                        this.lastWhitespace = i;
                                        continue block58;
                                    }
                                    case '\"': {
                                        this.doJavaKeyword(segment, i, c);
                                        if (bl) {
                                            bl = false;
                                            continue block58;
                                        }
                                        this.addToken(i - this.lastOffset, (byte)(by - 104));
                                        by = (byte)107;
                                        this.lastOffset = this.lastKeyword = i;
                                        continue block58;
                                    }
                                    case '\'': {
                                        this.doJavaKeyword(segment, i, c);
                                        if (bl) {
                                            bl = false;
                                            continue block58;
                                        }
                                        this.addToken(i - this.lastOffset, (byte)(by - 104));
                                        by = (byte)108;
                                        this.lastOffset = this.lastKeyword = i;
                                        continue block58;
                                    }
                                    case ':': {
                                        if (this.lastKeyword == n2) {
                                            if (this.doJavaKeyword(segment, i, c)) continue block58;
                                            bl = false;
                                            this.addToken(n4 - this.lastOffset, 5);
                                            this.lastOffset = this.lastKeyword = n4;
                                            continue block58;
                                        }
                                        if (!this.doJavaKeyword(segment, i, c)) continue block58;
                                        continue block58;
                                    }
                                    case '/': {
                                        bl = false;
                                        this.doJavaKeyword(segment, i, c);
                                        if (n3 - i <= 1) continue block58;
                                        switch (arrc[n4]) {
                                            case '*': {
                                                this.addToken(i - this.lastOffset, (byte)(by - 104));
                                                this.lastOffset = this.lastKeyword = i;
                                                if (n3 - i > 2 && arrc[i + 2] == '*') {
                                                    by = (byte)106;
                                                    continue block58;
                                                }
                                                by = (byte)105;
                                                continue block58;
                                            }
                                            case '/': {
                                                this.addToken(i - this.lastOffset, (byte)(by - 104));
                                                this.addToken(n3 - i, 1);
                                                this.lastOffset = this.lastKeyword = n3;
                                                break block58;
                                            }
                                        }
                                        continue block58;
                                    }
                                }
                                bl = false;
                                if (!(Character.isLetterOrDigit(c) || c == '_')) {
                                    this.doJavaKeyword(segment, i, c);
                                }
                                if (" \t~!%^*()-+=|\\#/{}[]:;\"'<>,.?@".indexOf(c) == -1) continue block58;
                                this.lastWhitespace = i;
                                continue block58;
                            }
                            case 105: 
                            case 106: {
                                bl = false;
                                if (c != '*' || n3 - i <= 1 || arrc[n4] != '/') continue block58;
                                this.addToken(++i + 1 - this.lastOffset, (byte)(by - 104));
                                by = (byte)104;
                                this.lastOffset = this.lastKeyword = i + 1;
                                this.lastWhitespace = i;
                                continue block58;
                            }
                            case 109: {
                                bl = false;
                                if (c == ' ' || c == '\t') {
                                    this.addToken(n4 - this.lastOffset, (byte)(by - 104));
                                    this.lastOffset = this.lastKeyword = n4;
                                    continue block58;
                                }
                                if (c == '=') {
                                    this.addToken(i - this.lastOffset, 8);
                                    this.addToken(1, 5);
                                    this.lastOffset = this.lastKeyword = n4;
                                    continue block58;
                                }
                                if (c == '\'') {
                                    this.addToken(i - this.lastOffset, 8);
                                    by = (byte)108;
                                    this.lastOffset = this.lastKeyword = i;
                                    continue block58;
                                }
                                if (c != '\"') continue block58;
                                this.addToken(i - this.lastOffset, 8);
                                by = (byte)107;
                                this.lastOffset = this.lastKeyword = i;
                                continue block58;
                            }
                            case 107: {
                                if (bl) {
                                    bl = false;
                                    continue block58;
                                }
                                if (c != '\"') continue block58;
                                this.addToken(n4 - this.lastOffset, (byte)(by - 104));
                                by = (byte)104;
                                this.lastOffset = this.lastKeyword = n4;
                                this.lastWhitespace = i;
                                continue block58;
                            }
                            case 108: {
                                if (bl) {
                                    bl = false;
                                    continue block58;
                                }
                                if (c != '\'') continue block58;
                                this.addToken(n4 - this.lastOffset, (byte)(by - 104));
                                by = (byte)104;
                                this.lastOffset = this.lastKeyword = n4;
                                this.lastWhitespace = i;
                                continue block58;
                            }
                        }
                        throw new InternalError("Invalid state: " + by);
                    }
                    throw new InternalError("Invalid state: " + by);
                }
            }
        }
        block46 : switch (by) {
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
                this.doJSKeyword(segment, n3, '\u0000');
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
                if (by < 104) {
                    this.addToken(n3 - this.lastOffset, by);
                    break;
                }
                if (by == 104) {
                    this.doJavaKeyword(segment, n3, '\u0000');
                }
                switch (by) {
                    case 107: 
                    case 108: {
                        this.addToken(n3 - this.lastOffset, 10);
                        by = (byte)104;
                        break block46;
                    }
                    case 111: {
                        this.addToken(n3 - this.lastOffset, (byte)(by - 104));
                        if (bl) break block46;
                        by = (byte)104;
                        break block46;
                    }
                    default: {
                        this.addToken(n3 - this.lastOffset, (byte)(by - 104));
                    }
                }
            }
        }
        return by;
    }

    private boolean doJSKeyword(Segment segment, int n, char c) {
        return this.doKeyword(segment, n, c, true);
    }

    private boolean doJavaKeyword(Segment segment, int n, char c) {
        return this.doKeyword(segment, n, c, false);
    }

    private boolean doKeyword(Segment segment, int n, char c, boolean bl) {
        int n2;
        int n3 = n + 1;
        byte by = (bl ? this.keywords : this.javaKeywords).lookup(segment, this.lastKeyword, n2 = n - this.lastKeyword);
        if (by != 0) {
            if (this.lastKeyword != this.lastOffset) {
                this.addToken(this.lastKeyword - this.lastOffset, 0);
            }
            this.addToken(n2, by);
            this.lastKeyword = n3;
            this.lastOffset = n;
            this.lastWhitespace = n;
            return true;
        }
        this.lastKeyword = n3;
        return false;
    }
}

