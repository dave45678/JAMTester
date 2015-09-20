/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import org.gjt.sp.jedit.syntax.TokenMarkerContext;

class TokenMarkerDebugger {
    public static final int MAX_COUNT = 100;
    private int pos = -1;
    private int count = 0;

    TokenMarkerDebugger() {
    }

    public boolean isOK(TokenMarkerContext tokenMarkerContext) {
        if (tokenMarkerContext.pos <= this.pos) {
            ++this.count;
            if (this.count > 100) {
                this.pos = tokenMarkerContext.pos + 1;
                this.count = 0;
                return false;
            }
            return true;
        }
        this.pos = tokenMarkerContext.pos;
        this.count = 0;
        return true;
    }

    public void reset() {
        this.pos = -1;
        this.count = 0;
    }
}

