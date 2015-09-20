/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class PropsTokenMarker
extends TokenMarker {
    public static final byte VALUE = 100;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        int n3 = n2 = segment.offset;
        int n4 = segment.count + n2;
        block10 : for (int i = n2; i < n4; ++i) {
            int n5 = i + 1;
            switch (by) {
                case 0: {
                    switch (arrc[i]) {
                        case '#': 
                        case ';': {
                            if (i != n2) break;
                            this.addToken(segment.count, 1);
                            n3 = n4;
                            break block10;
                        }
                        case '[': {
                            if (i != n2) break;
                            this.addToken(i - n3, by);
                            by = (byte)7;
                            n3 = i;
                            continue block10;
                        }
                        case '=': {
                            this.addToken(i - n3, 6);
                            by = (byte)100;
                            n3 = i;
                        }
                    }
                    continue block10;
                }
                case 7: {
                    if (arrc[i] != ']') continue block10;
                    this.addToken(n5 - n3, by);
                    by = 0;
                    n3 = n5;
                    continue block10;
                }
                case 100: {
                    continue block10;
                }
                default: {
                    throw new InternalError("Invalid state: " + by);
                }
            }
        }
        if (n3 != n4) {
            this.addToken(n4 - n3, 0);
        }
        return 0;
    }

    public boolean supportsMultilineTokens() {
        return false;
    }
}

