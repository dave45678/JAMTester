/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class TextTokenMarker
extends TokenMarker {
    public byte markTokensImpl(byte by, Segment segment, int n) {
        char[] arrc = segment.array;
        int n2 = segment.offset;
        int n3 = segment.count + n2;
        int n4 = 0;
        boolean bl = true;
        block6 : for (int i = n2; i < n3; ++i) {
            char c = arrc[i];
            switch (c) {
                case '!': 
                case '.': 
                case '?': {
                    if (n4 != 0) {
                        this.addToken(n4, 0);
                        n4 = 0;
                    }
                    this.addToken(1, 8);
                    bl = true;
                    continue block6;
                }
                case ',': 
                case ':': 
                case ';': {
                    if (n4 != 0) {
                        this.addToken(n4, 0);
                        n4 = 0;
                    }
                    this.addToken(1, 6);
                    bl = false;
                    continue block6;
                }
                case '\"': 
                case '\'': 
                case '(': 
                case ')': 
                case '[': 
                case ']': 
                case '{': 
                case '}': {
                    if (n4 != 0) {
                        this.addToken(n4, 0);
                        n4 = 0;
                    }
                    this.addToken(1, 3);
                    bl = false;
                    continue block6;
                }
                case '%': 
                case '*': 
                case '+': 
                case '-': 
                case '/': 
                case '=': 
                case '\\': 
                case '^': {
                    if (n4 != 0) {
                        this.addToken(n4, 0);
                        n4 = 0;
                    }
                    this.addToken(1, 9);
                    bl = false;
                    continue block6;
                }
                default: {
                    if (Character.isLetter(c) && Character.isUpperCase(c) && bl) {
                        if (n4 != 0) {
                            this.addToken(n4, 0);
                            n4 = 0;
                        }
                        this.addToken(1, 1);
                    } else if (Character.isDigit(c)) {
                        if (n4 != 0) {
                            this.addToken(n4, 0);
                            n4 = 0;
                        }
                        this.addToken(1, 5);
                    } else {
                        ++n4;
                    }
                    if (Character.isWhitespace(c)) continue block6;
                    bl = false;
                }
            }
        }
        if (n4 != 0) {
            this.addToken(n4, 0);
        }
        return by;
    }
}

