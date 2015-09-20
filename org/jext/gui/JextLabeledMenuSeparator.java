/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.plaf.SeparatorUI;
import org.jext.gui.JextLabeledMenuSeparatorUI;

public class JextLabeledMenuSeparator
extends JPopupMenu.Separator {
    private static final String uiClassID = "JextLabeledMenuSeparatorUI";
    private String stext;

    public JextLabeledMenuSeparator() {
    }

    public JextLabeledMenuSeparator(String string) {
        this.stext = string;
        this.updateUI();
    }

    public String getSeparatorText() {
        return this.stext;
    }

    public String getUIClassID() {
        return "JextLabeledMenuSeparatorUI";
    }

    public void updateUI() {
        this.setUI((JextLabeledMenuSeparatorUI)UIManager.getUI(this));
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.stext = null;
    }

    static {
        UIManager.getDefaults().put("JextLabeledMenuSeparatorUI", "org.jext.gui.JextLabeledMenuSeparatorUI");
    }
}

