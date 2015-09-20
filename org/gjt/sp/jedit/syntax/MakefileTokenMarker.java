/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class MakefileTokenMarker
extends TokenMarker {
    /*
     * Enabled aggressive block sorting
     */
    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        int n3 = n2 = segment.offset;
        int n4 = segment.count + n2;
        boolean bl = false;
        block17 : for (int i = n2; i < n4; ++i) {
            int n5 = i + 1;
            char c = arrc[i];
            if (c == '\\') {
                bl = !bl;
                continue;
            }
            switch (by) {
                case 0: {
                    switch (c) {
                        case '\t': 
                        case ' ': 
                        case ':': 
                        case '=': {
                            bl = false;
                            if (n3 != n2) break;
                            this.addToken(n5 - n3, 6);
                            n3 = n5;
                            break;
                        }
                        case '#': {
                            if (bl) {
                                bl = false;
                                break;
                            }
                            this.addToken(i - n3, by);
                            this.addToken(n4 - i, 1);
                            n3 = n4;
                            break block17;
                        }
                        case '$': {
                            if (bl) {
                                bl = false;
                                break;
                            }
                            if (n3 == n2) break;
                            this.addToken(i - n3, by);
                            n3 = i;
                            if (n4 - i <= 1) break;
                            char c2 = arrc[n5];
                            if (c2 == '(' || c2 == '{') {
                                by = (byte)7;
                                break;
                            }
                            this.addToken(2, 7);
                            n3+=2;
                            ++i;
                            break;
                        }
                        case '\"': {
                            if (bl) {
                                bl = false;
                                break;
                            }
                            this.addToken(i - n3, by);
                            by = (byte)3;
                            n3 = i;
                            break;
                        }
                        case '\'': {
                            if (bl) {
                                bl = false;
                                break;
                            }
                            this.addToken(i - n3, by);
                            by = (byte)4;
                            n3 = i;
                            break;
                        }
                        default: {
                            bl = false;
                        }
                    }
                }
                case 7: {
                    bl = false;
                    if (c != ')' && c != '}') break;
                    this.addToken(n5 - n3, by);
                    by = 0;
                    n3 = n5;
                    break;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        break;
                    }
                    if (c == '\"') {
                        this.addToken(n5 - n3, by);
                        by = 0;
                        n3 = n5;
                        break;
                    }
                    bl = false;
                    break;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        break;
                    }
                    if (c == '\'') {
                        this.addToken(n5 - n3, 3);
                        by = 0;
                        n3 = n5;
                        break;
                    }
                    bl = false;
                }
            }
        }
        switch (by) {
            case 7: {
                this.addToken(n4 - n3, 10);
                return 0;
            }
            case 4: {
                this.addToken(n4 - n3, 3);
                return by;
            }
        }
        this.addToken(n4 - n3, by);
        return by;
    }
}

