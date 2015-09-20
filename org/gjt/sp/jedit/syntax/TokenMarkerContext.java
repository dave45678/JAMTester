/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  gnu.regexp.RE
 *  gnu.regexp.REMatch
 */
package org.gjt.sp.jedit.syntax;

import gnu.regexp.RE;
import gnu.regexp.REMatch;
import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;
import org.gjt.sp.jedit.syntax.TokenMarker;
import org.gjt.sp.jedit.syntax.TokenMarkerWithAddToken;

public class TokenMarkerContext {
    public Segment line;
    public int lineIndex;
    public char[] array;
    public int offset;
    public int lastOffset;
    public int lastKeyword;
    public int length;
    public int pos;
    private TokenMarkerWithAddToken marker;
    public TokenMarker.LineInfo prevLineInfo;
    public TokenMarker.LineInfo currLineInfo;

    public TokenMarkerContext(Segment segment, int n, TokenMarkerWithAddToken tokenMarkerWithAddToken, TokenMarker.LineInfo[] arrlineInfo) {
        this.line = segment;
        this.lineIndex = n;
        this.marker = tokenMarkerWithAddToken;
        this.array = segment.array;
        this.offset = segment.offset;
        this.lastOffset = segment.offset;
        this.lastKeyword = segment.offset;
        this.length = segment.offset + segment.count;
        this.pos = segment.offset;
        if (arrlineInfo != null) {
            this.currLineInfo = arrlineInfo[n];
            this.prevLineInfo = n == 0 ? null : arrlineInfo[n - 1];
        }
    }

    public TokenMarkerContext(Segment segment, int n, TokenMarkerWithAddToken tokenMarkerWithAddToken) {
        this(segment, n, tokenMarkerWithAddToken, null);
    }

    public boolean atFirst() {
        return this.pos == this.line.offset;
    }

    public boolean hasMoreChars() {
        return this.pos < this.length;
    }

    public int remainingChars() {
        return this.length - 1 - this.pos;
    }

    public char getChar() {
        return this.array[this.pos];
    }

    public char getChar(int n) {
        return this.array[this.pos + n];
    }

    public char lastChar() {
        return this.array[this.length - 1];
    }

    public void addToken(int n, byte by) {
        this.marker.addToken(n, by);
    }

    public void addTokenToPos(byte by) {
        if (this.pos > this.lastOffset) {
            this.addToken(this.pos - this.lastOffset, by);
            this.lastOffset = this.lastKeyword = this.pos;
        }
    }

    public void addTokenToPos(int n, byte by) {
        if (n > this.lastOffset) {
            this.addToken(n - this.lastOffset, by);
            this.lastOffset = this.lastKeyword = n;
        }
    }

    public void addTokenToEnd(byte by) {
        if (this.length > this.lastOffset) {
            this.addToken(this.length - this.lastOffset, by);
            this.lastOffset = this.lastKeyword = this.length;
            this.pos = this.lastKeyword;
        }
    }

    public byte doKeywordToPos(int n, KeywordMap keywordMap) {
        int n2 = n - this.lastKeyword;
        byte by = keywordMap.lookup(this.line, this.lastKeyword, n2);
        if (by != 0) {
            this.addTokenToPos(this.lastKeyword, 0);
            this.addTokenToPos(n, by);
        }
        this.lastKeyword = n + 1;
        return by;
    }

    public byte doKeywordToPos(KeywordMap keywordMap) {
        int n = this.pos - this.lastKeyword;
        byte by = keywordMap.lookup(this.line, this.lastKeyword, n);
        if (by != 0) {
            this.addTokenToPos(this.lastKeyword, 0);
            this.addTokenToPos(this.pos, by);
        }
        this.lastKeyword = this.pos + 1;
        return by;
    }

    public byte doKeywordToEnd(KeywordMap keywordMap) {
        return this.doKeywordToPos(this.length, keywordMap);
    }

    public boolean regionMatches(boolean bl, String string) {
        return SyntaxUtilities.regionMatches(bl, this.line, this.pos, string);
    }

    public REMatch RERegionMatches(RE rE) {
        return TokenMarkerContext.RERegionMatches(this.line, this.pos, rE);
    }

    private static REMatch RERegionMatches(Segment segment, int n, RE rE) {
        try {
            String string = String.copyValueOf(segment.array, n, segment.count - (n - segment.offset));
            return rE.getMatch((Object)string, 0, 64);
        }
        catch (IllegalArgumentException var3_4) {
            return null;
        }
    }

    public String toString() {
        String string = "Line: " + (this.lineIndex + 1) + ", pos:" + (this.pos - this.offset) + "\n";
        string = string + new String(this.line.array, this.line.offset, this.line.count);
        string = string + "\n";
        int n = this.pos - this.offset;
        StringBuffer stringBuffer = new StringBuffer(n + 2);
        for (int i = 0; i < n; ++i) {
            stringBuffer.append('.');
        }
        stringBuffer.append('^');
        stringBuffer.append('\n');
        string = string + stringBuffer.toString();
        return string;
    }
}

