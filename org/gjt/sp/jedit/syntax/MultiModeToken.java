/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

class MultiModeToken {
    public byte mode;
    public byte token;
    public Object obj = null;
    public static final MultiModeToken NULL = new MultiModeToken();

    public MultiModeToken() {
        this.mode = 0;
        this.token = 0;
        this.obj = null;
    }

    private MultiModeToken(byte by, byte by2) {
        this.mode = by;
        this.token = by2;
        this.obj = null;
    }

    public MultiModeToken(byte by, byte by2, Object object) {
        this.mode = by;
        this.token = by2;
        this.obj = object;
    }

    public MultiModeToken(MultiModeToken multiModeToken) {
        this.mode = multiModeToken.mode;
        this.token = multiModeToken.token;
        this.obj = multiModeToken.obj;
    }

    public void reset() {
        this.mode = 0;
        this.token = 0;
    }

    public void assign(MultiModeToken multiModeToken) {
        this.mode = multiModeToken.mode;
        this.token = multiModeToken.token;
    }
}

