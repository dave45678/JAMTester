/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.SeparatorUI;
import org.jext.gui.JextSeparatorUI;

public class JextSeparator
extends JToolBar.Separator {
    private static final String uiClassID = "JextSeparatorUI";

    public String getUIClassID() {
        return "JextSeparatorUI";
    }

    public void updateUI() {
        this.setUI((JextSeparatorUI)UIManager.getUI(this));
    }

    static {
        UIManager.getDefaults().put("JextSeparatorUI", "org.jext.gui.JextSeparatorUI");
    }
}

