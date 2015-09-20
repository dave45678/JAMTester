/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class ShellScriptTokenMarker
extends TokenMarker {
    public static final byte LVARIABLE = 100;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        int n3 = 0;
        int n4 = n2 = segment.offset;
        int n5 = segment.count + n2;
        if (by == 3 && n != 0 && this.lineInfo[n - 1].obj != null) {
            String string = (String)this.lineInfo[n - 1].obj;
            if (string != null && string.length() == segment.count && SyntaxUtilities.regionMatches(false, segment, n2, string)) {
                this.addToken(segment.count, 3);
                return 0;
            }
            this.addToken(segment.count, 3);
            this.lineInfo[n].obj = string;
            return 3;
        }
        boolean bl = false;
        block27 : for (int i = n2; i < n5; ++i) {
            int n6 = i + 1;
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
                        case '(': 
                        case ')': {
                            bl = false;
                            if (n3 != true) continue block27;
                            this.addToken(i - n4, 6);
                            n4 = i;
                            n3 = 2;
                            continue block27;
                        }
                        case '=': {
                            bl = false;
                            if (n3 != true) continue block27;
                            this.addToken(i - n4, by);
                            n4 = i;
                            n3 = 2;
                            continue block27;
                        }
                        case '&': 
                        case ';': 
                        case '|': {
                            if (bl) {
                                bl = false;
                                continue block27;
                            }
                            n3 = 0;
                            continue block27;
                        }
                        case '#': {
                            if (bl) {
                                bl = false;
                                continue block27;
                            }
                            this.addToken(i - n4, by);
                            this.addToken(n5 - i, 1);
                            n4 = n5;
                            break block27;
                        }
                        case '$': {
                            if (bl) {
                                bl = false;
                                continue block27;
                            }
                            this.addToken(i - n4, by);
                            n3 = 2;
                            n4 = i;
                            if (n5 - i >= 2) {
                                switch (arrc[n6]) {
                                    case '(': {
                                        continue block27;
                                    }
                                    case '{': {
                                        by = (byte)100;
                                        continue block27;
                                    }
                                }
                                by = (byte)7;
                                continue block27;
                            }
                            by = (byte)7;
                            continue block27;
                        }
                        case '\"': {
                            if (bl) {
                                bl = false;
                                continue block27;
                            }
                            this.addToken(i - n4, by);
                            by = (byte)3;
                            this.lineInfo[n].obj = null;
                            n3 = 2;
                            n4 = i;
                            continue block27;
                        }
                        case '\'': 
                        case '`': {
                            if (bl) {
                                bl = false;
                                continue block27;
                            }
                            this.addToken(i - n4, by);
                            by = (byte)4;
                            n3 = 2;
                            n4 = i;
                            continue block27;
                        }
                        case '<': {
                            if (bl) {
                                bl = false;
                                continue block27;
                            }
                            if (n5 - i <= 1 || arrc[n6] != '<') continue block27;
                            this.addToken(i - n4, by);
                            by = (byte)3;
                            n4 = i;
                            this.lineInfo[n].obj = new String(arrc, i + 2, n5 - (i + 2));
                            continue block27;
                        }
                    }
                    bl = false;
                    if (!Character.isLetter(c) || n3 != 0) continue block27;
                    this.addToken(i - n4, by);
                    n4 = i;
                    n3 = (byte)(n3 + 1);
                    continue block27;
                }
                case 7: {
                    bl = false;
                    if (Character.isLetterOrDigit(c) || c == '_') continue block27;
                    if (i != n2 && arrc[i - 1] == '$') {
                        this.addToken(n6 - n4, by);
                        n4 = n6;
                        by = 0;
                        continue block27;
                    }
                    this.addToken(i - n4, by);
                    n4 = i;
                    by = 0;
                    continue block27;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        continue block27;
                    }
                    if (c == '\"') {
                        this.addToken(n6 - n4, by);
                        n3 = 2;
                        n4 = n6;
                        by = 0;
                        continue block27;
                    }
                    bl = false;
                    continue block27;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        continue block27;
                    }
                    if (c == '\'' || c == '`') {
                        this.addToken(n6 - n4, 3);
                        n3 = 2;
                        n4 = n6;
                        by = 0;
                        continue block27;
                    }
                    bl = false;
                    continue block27;
                }
                case 100: {
                    bl = false;
                    if (c != '}') continue block27;
                    this.addToken(n6 - n4, 7);
                    n4 = n6;
                    by = 0;
                    continue block27;
                }
                default: {
                    throw new InternalError("Invalid state: " + by);
                }
            }
        }
        switch (by) {
            case 0: {
                if (n3 == 1) {
                    this.addToken(n5 - n4, 6);
                    break;
                }
                this.addToken(n5 - n4, by);
                break;
            }
            case 4: {
                this.addToken(n5 - n4, 3);
                break;
            }
            case 7: {
                this.addToken(n5 - n4, by);
                by = 0;
                break;
            }
            case 100: {
                this.addToken(n5 - n4, 10);
                by = 0;
                break;
            }
            default: {
                this.addToken(n5 - n4, by);
            }
        }
        return by;
    }
}

