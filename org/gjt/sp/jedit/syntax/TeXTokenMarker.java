/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class TeXTokenMarker
extends TokenMarker {
    public static final byte BDFORMULA = 100;
    public static final byte EDFORMULA = 101;

    /*
     * Enabled aggressive block sorting
     */
    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        int n3 = n2 = segment.offset;
        int n4 = segment.count + n2;
        boolean bl = false;
        block5 : for (int i = n2; i < n4; ++i) {
            int n5 = i + 1;
            char c = arrc[i];
            if (Character.isLetter(c)) {
                bl = false;
            } else {
                if (bl) {
                    bl = false;
                    if (by == 7 || by == 101) {
                        by = (byte)7;
                    }
                    this.addToken(n5 - n3, by);
                    n3 = n5;
                    if (by != 6) continue;
                    by = 0;
                    continue;
                }
                if (by == 100 || by == 101) {
                    by = (byte)7;
                }
                this.addToken(i - n3, by);
                if (by == 6) {
                    by = 0;
                }
                n3 = i;
            }
            switch (c) {
                case '%': {
                    if (bl) {
                        bl = false;
                        break;
                    }
                    this.addToken(i - n3, by);
                    this.addToken(n4 - i, 1);
                    n3 = n4;
                    break block5;
                }
                case '\\': {
                    bl = true;
                    if (by != 0) continue block5;
                    by = (byte)6;
                    this.addToken(i - n3, 0);
                    n3 = i;
                    break;
                }
                case '$': {
                    bl = false;
                    if (by == 0) {
                        by = (byte)7;
                        this.addToken(i - n3, 0);
                        n3 = i;
                        break;
                    }
                    if (by == 6) {
                        by = (byte)7;
                        this.addToken(i - n3, 6);
                        n3 = i;
                        break;
                    }
                    if (by == 7) {
                        if (i - n3 == 1 && arrc[i - 1] == '$') {
                            by = (byte)100;
                            break;
                        }
                        by = 0;
                        this.addToken(n5 - n3, 7);
                        n3 = n5;
                        break;
                    }
                    if (by == 100) {
                        by = (byte)101;
                        break;
                    }
                    if (by != 101) break;
                    by = 0;
                    this.addToken(n5 - n3, 7);
                    n3 = n5;
                }
            }
        }
        if (n3 != n4) {
            this.addToken(n4 - n3, by == 100 || by == 101 ? 7 : by);
        }
        if (by == 6) return 0;
        byte by2 = by;
        return by2;
    }
}

