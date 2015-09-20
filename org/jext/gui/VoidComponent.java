/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

public class VoidComponent
extends JComponent {
    private Dimension zero = new Dimension(0, 0);

    public int getHeight() {
        return 0;
    }

    public Dimension getMaximumSize() {
        return this.zero;
    }

    public Dimension getMinimumSize() {
        return this.zero;
    }

    public Dimension getPreferredSize() {
        return this.zero;
    }

    public Dimension getSize() {
        return this.zero;
    }

    public int getWidth() {
        return 0;
    }

    public void paint(Graphics graphics) {
    }

    public void setSize(Dimension dimension) {
    }

    public void setSize(int n, int n2) {
    }

    public void update(Graphics graphics) {
    }
}

