/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class PatchTokenMarker
extends TokenMarker {
    public byte markTokensImpl(byte by, Segment segment, int n) {
        if (segment.count == 0) {
            return 0;
        }
        switch (segment.array[segment.offset]) {
            case '+': 
            case '>': {
                this.addToken(segment.count, 6);
                break;
            }
            case '-': 
            case '<': {
                this.addToken(segment.count, 7);
                break;
            }
            case '*': 
            case '@': {
                this.addToken(segment.count, 8);
                break;
            }
            default: {
                this.addToken(segment.count, 0);
            }
        }
        return 0;
    }

    public boolean supportsMultilineTokens() {
        return false;
    }
}

