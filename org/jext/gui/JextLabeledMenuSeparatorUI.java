/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalSeparatorUI;
import org.jext.gui.JextLabeledMenuSeparator;

public class JextLabeledMenuSeparatorUI
extends MetalSeparatorUI {
    private String stext;
    private Font labelFont;

    public JextLabeledMenuSeparatorUI(JComponent jComponent) {
        if (jComponent instanceof JextLabeledMenuSeparator) {
            this.stext = ((JextLabeledMenuSeparator)jComponent).getSeparatorText();
        }
        if (this.stext != null) {
            this.labelFont = new Font("Monospaced", 0, 8);
            this.shadow = UIManager.getColor("controlDkShadow");
            this.highlight = UIManager.getColor("controlLtHighlight");
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new JextLabeledMenuSeparatorUI(jComponent);
    }

    public void paint(Graphics graphics, JComponent jComponent) {
        if (this.stext != null) {
            graphics.setFont(this.labelFont);
            FontMetrics fontMetrics = graphics.getFontMetrics();
            graphics.setColor(this.highlight);
            int n = fontMetrics.getHeight() / 2;
            graphics.drawString(this.stext, 4, n + 1);
            graphics.setColor(this.shadow);
            graphics.drawString(this.stext, 5, n);
            graphics.setColor(Color.black);
            graphics.drawLine(fontMetrics.stringWidth(this.stext) + 8, 4, jComponent.getSize().width, 4);
        } else {
            graphics.setColor(Color.black);
            graphics.drawLine(0, 0, jComponent.getSize().width, 0);
        }
    }

    public Dimension getPreferredSize(JComponent jComponent) {
        return new Dimension(0, this.stext == null ? 1 : 10);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.stext = null;
        this.labelFont = null;
    }
}

