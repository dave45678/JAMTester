/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.plaf.ProgressBarUI;
import org.jext.gui.JextProgressBarUI;

public class JextProgressBar
extends JProgressBar {
    private static final String uiClassID = "JextProgressBarUI";

    public JextProgressBar() {
    }

    public JextProgressBar(int n) {
        super(n);
    }

    public JextProgressBar(int n, int n2) {
        super(n, n2);
    }

    public JextProgressBar(int n, int n2, int n3) {
        super(n, n2, n3);
    }

    public String getUIClassID() {
        return "JextProgressBarUI";
    }

    public void updateUI() {
        this.setUI((JextProgressBarUI)UIManager.getUI(this));
    }

    static {
        UIManager.getDefaults().put("JextProgressBarUI", "org.jext.gui.JextProgressBarUI");
    }
}

