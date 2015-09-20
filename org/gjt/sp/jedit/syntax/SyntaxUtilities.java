/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.Utilities;
import org.gjt.sp.jedit.syntax.SyntaxStyle;
import org.gjt.sp.jedit.syntax.Token;

public class SyntaxUtilities {
    public static boolean regionMatches(boolean bl, Segment segment, int n, String string) {
        int n2 = n + string.length();
        char[] arrc = segment.array;
        if (n2 > segment.offset + segment.count) {
            return false;
        }
        int n3 = n;
        int n4 = 0;
        while (n3 < n2) {
            char c = arrc[n3];
            char c2 = string.charAt(n4);
            if (bl) {
                c = Character.toUpperCase(c);
                c2 = Character.toUpperCase(c2);
            }
            if (c != c2) {
                return false;
            }
            ++n3;
            ++n4;
        }
        return true;
    }

    public static boolean regionMatches(boolean bl, Segment segment, int n, char[] arrc) {
        int n2 = n + arrc.length;
        char[] arrc2 = segment.array;
        if (n2 > segment.offset + segment.count) {
            return false;
        }
        int n3 = n;
        int n4 = 0;
        while (n3 < n2) {
            char c = arrc2[n3];
            char c2 = arrc[n4];
            if (bl) {
                c = Character.toUpperCase(c);
                c2 = Character.toUpperCase(c2);
            }
            if (c != c2) {
                return false;
            }
            ++n3;
            ++n4;
        }
        return true;
    }

    public static SyntaxStyle[] getDefaultSyntaxStyles() {
        SyntaxStyle[] arrsyntaxStyle = new SyntaxStyle[12];
        arrsyntaxStyle[1] = new SyntaxStyle(Color.black, true, false);
        arrsyntaxStyle[2] = new SyntaxStyle(new Color(10027059), true, false);
        arrsyntaxStyle[6] = new SyntaxStyle(Color.black, false, true);
        arrsyntaxStyle[7] = new SyntaxStyle(Color.magenta, false, false);
        arrsyntaxStyle[8] = new SyntaxStyle(new Color(38400), false, false);
        arrsyntaxStyle[3] = new SyntaxStyle(new Color(6619289), false, false);
        arrsyntaxStyle[4] = new SyntaxStyle(new Color(6619289), false, true);
        arrsyntaxStyle[5] = new SyntaxStyle(new Color(10027059), false, true);
        arrsyntaxStyle[9] = new SyntaxStyle(Color.black, false, true);
        arrsyntaxStyle[10] = new SyntaxStyle(Color.red, false, true);
        arrsyntaxStyle[11] = new SyntaxStyle(Color.black, false, true);
        return arrsyntaxStyle;
    }

    public static int paintSyntaxLine(Segment segment, Token token, SyntaxStyle[] arrsyntaxStyle, TabExpander tabExpander, Graphics graphics, int n, int n2) {
        byte by;
        Font font = graphics.getFont();
        Color color = graphics.getColor();
        int n3 = 0;
        while ((by = token.id) != 127) {
            int n4 = token.length;
            if (by == 0) {
                if (!color.equals(graphics.getColor())) {
                    graphics.setColor(color);
                }
                if (!font.equals(graphics.getFont())) {
                    graphics.setFont(font);
                }
            } else {
                arrsyntaxStyle[by].setGraphicsFlags(graphics, font);
            }
            segment.count = n4;
            n = Utilities.drawTabbedText(segment, n, n2, graphics, tabExpander, 0);
            segment.offset+=n4;
            n3+=n4;
            token = token.next;
        }
        return n;
    }

    private SyntaxUtilities() {
    }
}

