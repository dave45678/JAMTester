/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Component;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.jext.Jext;

public class JextMenu
extends JMenu {
    private Component[] menuComponents;

    public JextMenu() {
        this.setBorders();
    }

    public JextMenu(String string) {
        super(string);
        this.setBorders();
    }

    private void setBorders() {
        if (Jext.getFlatMenus()) {
            this.setBorder(new EmptyBorder(2, 2, 2, 2));
            this.getPopupMenu().setBorder(LineBorder.createBlackLineBorder());
        }
    }

    public void freeze() {
        this.menuComponents = this.getMenuComponents();
    }

    public void reset() {
        if (this.menuComponents == null) {
            return;
        }
        this.removeAll();
        for (int i = 0; i < this.menuComponents.length; ++i) {
            this.add(this.menuComponents[i]);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.menuComponents = null;
    }
}

