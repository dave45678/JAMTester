/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.JButton;
import org.jext.Jext;

public class JextHighlightButton
extends JButton {
    private Color nColor;
    private MouseHandler _mouseListener;
    private static Color commonHighlightColor = new Color(192, 192, 210);
    private static boolean blockHighlightChange = false;

    public static void setHighlightColor(Color color) {
        if (!blockHighlightChange) {
            commonHighlightColor = color;
        }
    }

    public static Color getHighlightColor() {
        return commonHighlightColor;
    }

    public static void blockHighlightChange() {
        blockHighlightChange = true;
    }

    public static void unBlockHighlightChange() {
        blockHighlightChange = false;
    }

    private void init() {
        if (Jext.getButtonsHighlight()) {
            this.nColor = this.getBackground();
            this._mouseListener = new MouseHandler();
            this.addMouseListener(this._mouseListener);
        }
    }

    public JextHighlightButton() {
        this.init();
    }

    public JextHighlightButton(String string) {
        super(string);
        this.init();
    }

    public JextHighlightButton(Icon icon) {
        super(icon);
        this.init();
    }

    public JextHighlightButton(String string, Icon icon) {
        super(string, icon);
        this.init();
    }

    protected void finalize() throws Throwable {
        this.removeMouseListener(this._mouseListener);
        super.finalize();
        this.nColor = null;
        this._mouseListener = null;
    }

    class MouseHandler
    extends MouseAdapter {
        MouseHandler() {
        }

        public void mouseEntered(MouseEvent mouseEvent) {
            JextHighlightButton.this.nColor = JextHighlightButton.this.getBackground();
            if (JextHighlightButton.this.isEnabled()) {
                JextHighlightButton.this.setBackground(commonHighlightColor);
            }
        }

        public void mouseExited(MouseEvent mouseEvent) {
            if (JextHighlightButton.this.isEnabled()) {
                JextHighlightButton.this.setBackground(JextHighlightButton.this.nColor);
            }
        }
    }

}

