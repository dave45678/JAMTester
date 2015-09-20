/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalSeparatorUI;

public class JextMenuSeparatorUI
extends MetalSeparatorUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new JextMenuSeparatorUI();
    }

    public void paint(Graphics graphics, JComponent jComponent) {
        graphics.setColor(Color.black);
        graphics.drawLine(0, 0, jComponent.getSize().width, 0);
    }

    public Dimension getPreferredSize(JComponent jComponent) {
        return new Dimension(0, 1);
    }
}

