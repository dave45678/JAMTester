/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.Token;

public abstract class TokenMarker {
    protected Token firstToken;
    protected Token lastToken;
    protected LineInfo[] lineInfo;
    protected int length;
    protected int lastLine = -1;
    protected boolean nextLineRequested;

    public Token markTokens(Segment segment, int n) {
        byte by;
        if (n >= this.length) {
            throw new IllegalArgumentException("Tokenizing invalid line: " + n);
        }
        this.lastToken = null;
        LineInfo lineInfo = this.lineInfo[n];
        LineInfo lineInfo2 = n == 0 ? null : this.lineInfo[n - 1];
        byte by2 = lineInfo.token;
        lineInfo.token = by = this.markTokensImpl(lineInfo2 == null ? 0 : lineInfo2.token, segment, n);
        if (!(this.lastLine == n && this.nextLineRequested)) {
            this.nextLineRequested = by2 != by;
        }
        this.lastLine = n;
        this.addToken(0, 127);
        return this.firstToken;
    }

    protected abstract byte markTokensImpl(byte var1, Segment var2, int var3);

    public boolean supportsMultilineTokens() {
        return true;
    }

    public void insertLines(int n, int n2) {
        if (n2 <= 0) {
            return;
        }
        this.length+=n2;
        this.ensureCapacity(this.length);
        int n3 = n + n2;
        System.arraycopy(this.lineInfo, n, this.lineInfo, n3, this.lineInfo.length - n3);
        for (int i = n + n2 - 1; i >= n; --i) {
            this.lineInfo[i] = new LineInfo(this);
        }
    }

    public void deleteLines(int n, int n2) {
        if (n2 <= 0) {
            return;
        }
        int n3 = n + n2;
        this.length-=n2;
        System.arraycopy(this.lineInfo, n3, this.lineInfo, n, this.lineInfo.length - n3);
    }

    public int getLineCount() {
        return this.length;
    }

    public boolean isNextLineRequested() {
        return this.nextLineRequested;
    }

    protected TokenMarker() {
    }

    protected void ensureCapacity(int n) {
        if (this.lineInfo == null) {
            this.lineInfo = new LineInfo[n + 1];
        } else if (this.lineInfo.length <= n) {
            LineInfo[] arrlineInfo = new LineInfo[(n + 1) * 2];
            System.arraycopy(this.lineInfo, 0, arrlineInfo, 0, this.lineInfo.length);
            this.lineInfo = arrlineInfo;
        }
    }

    public int getMaxLineWidth(int n, int n2) {
        int n3 = 0;
        for (int i = n; i <= n + n2; ++i) {
            if (i >= this.length) break;
            n3 = Math.max(this.lineInfo[i].width, n3);
        }
        return n3;
    }

    protected void addToken(int n, byte by) {
        this.addToken(n, by, false);
    }

    protected void addToken(int n, byte by, boolean bl) {
        if (by >= 100 && by <= 126) {
            throw new InternalError("Invalid id: " + by);
        }
        if (n <= 0 && by != 127) {
            return;
        }
        if (this.firstToken == null) {
            this.lastToken = this.firstToken = new Token(n, by);
        } else if (this.lastToken == null) {
            this.lastToken = this.firstToken;
            this.firstToken.length = n;
            this.firstToken.id = by;
        } else if (this.lastToken.next == null) {
            this.lastToken = this.lastToken.next = new Token(n, by);
        } else {
            this.lastToken = this.lastToken.next;
            this.lastToken.length = n;
            this.lastToken.id = by;
        }
        this.lastToken.highlightBackground = bl;
    }

    public boolean setLineWidth(int n, int n2) {
        LineInfo lineInfo = this.lineInfo[n];
        int n3 = lineInfo.width;
        lineInfo.width = n2;
        return n2 != n3;
    }

    public class LineInfo {
        public int width;
        public byte token;
        public Object obj;
        private final /* synthetic */ TokenMarker this$0;

        public LineInfo(TokenMarker tokenMarker) {
            this.this$0 = tokenMarker;
        }

        public LineInfo(TokenMarker tokenMarker, byte by, Object object) {
            this.this$0 = tokenMarker;
            this.token = by;
            this.obj = object;
        }
    }

}

