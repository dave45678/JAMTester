/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.plaf.SeparatorUI;
import org.jext.gui.JextMenuSeparatorUI;

public class JextMenuSeparator
extends JPopupMenu.Separator {
    private static final String uiClassID = "JextMenuSeparatorUI";

    public String getUIClassID() {
        return "JextMenuSeparatorUI";
    }

    public void updateUI() {
        this.setUI((JextMenuSeparatorUI)UIManager.getUI(this));
    }

    static {
        UIManager.getDefaults().put("JextMenuSeparatorUI", "org.jext.gui.JextMenuSeparatorUI");
    }
}

