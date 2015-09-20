/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarSeparatorUI;

public class JextSeparatorUI
extends BasicToolBarSeparatorUI {
    public JextSeparatorUI() {
        this.shadow = UIManager.getColor("controlDkShadow");
        this.highlight = UIManager.getColor("controlLtHighlight");
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new JextSeparatorUI();
    }

    public void paint(Graphics graphics, JComponent jComponent) {
        Dimension dimension = jComponent.getSize();
        int n = dimension.width / 2;
        graphics.setColor(this.shadow);
        graphics.drawLine(n, 0, n, dimension.height);
        graphics.setColor(this.highlight);
        graphics.drawLine(n + 1, 0, n + 1, dimension.height);
    }
}

