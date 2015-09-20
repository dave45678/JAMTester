/*
 * Decompiled with CFR 0_102.
 */
package org.jext.print;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.util.Date;
import java.util.Properties;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.syntax.SyntaxStyle;
import org.gjt.sp.jedit.syntax.SyntaxUtilities;
import org.gjt.sp.jedit.syntax.Token;
import org.gjt.sp.jedit.syntax.TokenMarker;
import org.gjt.sp.jedit.textarea.TextAreaPainter;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;

public class PrintSyntax {
    private Segment seg = new Segment();

    public void print(JextFrame jextFrame, JextTextArea jextTextArea) {
        int n;
        PrintJob printJob = jextFrame.getToolkit().getPrintJob(jextFrame, "Jext:" + jextTextArea.getName(), null);
        if (printJob == null) {
            return;
        }
        int n2 = printJob.getPageResolution();
        int n3 = (int)(0.5 * (double)n2);
        int n4 = (int)(0.5 * (double)n2);
        int n5 = (int)(0.5 * (double)n2);
        int n6 = (int)(0.5 * (double)n2);
        boolean bl = Jext.getBooleanProperty("print.header");
        boolean bl2 = Jext.getBooleanProperty("print.footer");
        boolean bl3 = Jext.getBooleanProperty("print.lineNumbers");
        String string = jextTextArea.getName();
        String string2 = new Date().toString();
        int n7 = jextTextArea.getDocument().getDefaultRootElement().getElementCount();
        PrintTabExpander printTabExpander = null;
        Graphics graphics = null;
        String string3 = Jext.getProperty("print.font");
        try {
            n = Integer.parseInt(Jext.getProperty("print.fontSize"));
        }
        catch (NumberFormatException var19_19) {
            n = 10;
        }
        int n8 = 0;
        SyntaxStyle[] arrsyntaxStyle = jextTextArea.getPainter().getStyles();
        Font font = new Font(string3, n8, n);
        FontMetrics fontMetrics = null;
        Dimension dimension = printJob.getPageDimension();
        int n9 = dimension.width;
        int n10 = dimension.height;
        int n11 = 0;
        int n12 = 0;
        int n13 = 0;
        int n14 = 0;
        int n15 = (int)Math.ceil(Math.log(n7) / Math.log(10.0));
        int n16 = 0;
        for (int i = 0; i < n7; ++i) {
            if (graphics == null) {
                ++n14;
                graphics = printJob.getGraphics();
                graphics.setFont(font);
                fontMetrics = graphics.getFontMetrics();
                n16 = bl3 ? fontMetrics.charWidth('0') * n15 : 0;
                n13 = fontMetrics.getHeight();
                n12 = jextTextArea.getTabSize() * fontMetrics.charWidth(' ');
                printTabExpander = new PrintTabExpander(n4 + n16, n12);
                n11 = n3 + n13 - fontMetrics.getDescent() - fontMetrics.getLeading();
                if (bl) {
                    graphics.setColor(Color.lightGray);
                    graphics.fillRect(n4, n3, n9 - n4 - n6, n13);
                    graphics.setColor(Color.black);
                    graphics.drawString(string, n4, n11);
                    n11+=n13;
                }
            }
            n11+=n13;
            graphics.setColor(Color.black);
            graphics.setFont(font);
            int n17 = n4;
            if (bl3) {
                String string4 = String.valueOf(i + 1);
                graphics.drawString(string4, n4 + n16 - fontMetrics.stringWidth(string4), n11);
                n17+=n16 + fontMetrics.charWidth('0');
            }
            this.paintSyntaxLine(jextTextArea, graphics, printTabExpander, jextTextArea.getTokenMarker(), arrsyntaxStyle, fontMetrics, i, font, Color.black, n17, n11);
            int n18 = n10 - n5 - n13;
            if (bl2) {
                n18-=n13 * 2;
            }
            if (n11 < n18 && i != n7 - 1) continue;
            if (bl2) {
                n11 = n10 - n5;
                graphics.setColor(Color.lightGray);
                graphics.setFont(font);
                graphics.fillRect(n4, n11 - n13, n9 - n4 - n6, n13);
                graphics.setColor(Color.black);
                graphics.drawString(string2, n4, n11-=n13 - fontMetrics.getAscent());
                String string5 = Jext.getProperty("print.page.footer", new Integer[]{new Integer(n14)});
                int n19 = fontMetrics.stringWidth(string5);
                graphics.drawString(string5, n9 - n6 - n19, n11);
            }
            graphics.dispose();
            graphics = null;
        }
        printJob.end();
    }

    protected int paintSyntaxLine(JextTextArea jextTextArea, Graphics graphics, TabExpander tabExpander, TokenMarker tokenMarker, SyntaxStyle[] arrsyntaxStyle, FontMetrics fontMetrics, int n, Font font, Color color, int n2, int n3) {
        graphics.setFont(font);
        graphics.setColor(color);
        jextTextArea.getLineText(n, this.seg);
        n2 = SyntaxUtilities.paintSyntaxLine(this.seg, tokenMarker.markTokens(this.seg, n), arrsyntaxStyle, tabExpander, graphics, n2, n3);
        return n2;
    }

    static class PrintTabExpander
    implements TabExpander {
        private int leftMargin;
        private int tabSize;

        public PrintTabExpander(int n, int n2) {
            this.leftMargin = n;
            this.tabSize = n2;
        }

        public float nextTabStop(float f, int n) {
            int n2 = ((int)f - this.leftMargin) / this.tabSize;
            return (n2 + 1) * this.tabSize + this.leftMargin;
        }
    }

}

