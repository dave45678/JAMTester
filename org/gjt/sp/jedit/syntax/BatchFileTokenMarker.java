/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class BatchFileTokenMarker
extends TokenMarker {
    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        int n3 = n2 = segment.offset;
        int n4 = segment.count + n2;
        if (SyntaxUtilities.regionMatches(true, segment, n2, "rem")) {
            this.addToken(segment.count, 1);
            return 0;
        }
        block11 : for (int i = n2; i < n4; ++i) {
            int n5 = i + 1;
            switch (by) {
                case 0: {
                    switch (arrc[i]) {
                        case '%': {
                            this.addToken(i - n3, by);
                            n3 = i;
                            if (n4 - i <= 3 || arrc[i + 2] == ' ') {
                                this.addToken(2, 7);
                                n3 = i+=2;
                                continue block11;
                            }
                            by = (byte)7;
                            continue block11;
                        }
                        case '\"': {
                            this.addToken(i - n3, by);
                            by = (byte)3;
                            n3 = i;
                            continue block11;
                        }
                        case ':': {
                            if (i != n2) break;
                            this.addToken(segment.count, 5);
                            n3 = n4;
                            break block11;
                        }
                        case ' ': {
                            if (n3 != n2) break;
                            this.addToken(i - n3, 6);
                            n3 = i;
                        }
                    }
                    continue block11;
                }
                case 7: {
                    if (arrc[i] != '%') continue block11;
                    this.addToken(n5 - n3, by);
                    by = 0;
                    n3 = n5;
                    continue block11;
                }
                case 3: {
                    if (arrc[i] != '\"') continue block11;
                    this.addToken(n5 - n3, by);
                    by = 0;
                    n3 = n5;
                    continue block11;
                }
                default: {
                    throw new InternalError("Invalid state: " + by);
                }
            }
        }
        if (n3 != n4) {
            if (by != 0) {
                by = (byte)10;
            } else if (n3 == n2) {
                by = (byte)6;
            }
            this.addToken(n4 - n3, by);
        }
        return 0;
    }

    public boolean supportsMultilineTokens() {
        return false;
    }
}

