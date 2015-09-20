/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class JextProgressBarUI
extends BasicProgressBarUI {
    private static final Color START = new Color(38, 92, 147);
    private static final Color END = new Color(146, 173, 201);

    public static ComponentUI createUI(JComponent jComponent) {
        return new JextProgressBarUI();
    }

    public void paint(Graphics graphics, JComponent jComponent) {
        int n;
        Insets insets = this.progressBar.getInsets();
        int n2 = insets.left;
        int n3 = insets.top;
        int n4 = this.progressBar.getWidth() - (insets.right + n2);
        int n5 = this.getAmountFull(insets, n4, n = this.progressBar.getHeight() - (insets.bottom + n3));
        if (n5 > 0) {
            GradientPaint gradientPaint = new GradientPaint(n2, n3, START, n2 + n4, n3 + n, END);
            Graphics2D graphics2D = (Graphics2D)graphics;
            graphics2D.setPaint(gradientPaint);
            graphics2D.fill(new Rectangle(n2, n3, n5, n));
        }
        if (this.progressBar.isStringPainted()) {
            this.paintString(graphics, n2, n3, n4, n, n5, insets);
        }
    }
}

