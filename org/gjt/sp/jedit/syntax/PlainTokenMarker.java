/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class PlainTokenMarker
extends TokenMarker {
    public byte markTokensImpl(byte by, Segment segment, int n) {
        this.addToken(segment.count, 0);
        return 0;
    }
}

