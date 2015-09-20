/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.event.ActionEvent;
import org.jext.JextFrame;
import org.jext.JextTabbedPane;
import org.jext.MenuAction;

public class TabSwitcher
extends MenuAction {
    private boolean right;

    public TabSwitcher(boolean bl) {
        super("TabSwitcher_" + (bl ? "left" : "right"));
        this.right = bl;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTabbedPane jextTabbedPane = TabSwitcher.getTextArea(actionEvent).getJextParent().getTabbedPane();
        if (this.right) {
            jextTabbedPane.nextTab();
        } else {
            jextTabbedPane.previousTab();
        }
    }
}

