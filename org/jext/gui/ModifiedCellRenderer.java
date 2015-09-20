/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class ModifiedCellRenderer
extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList jList, Object object, int n, boolean bl, boolean bl2) {
        super.getListCellRendererComponent(jList, object, n, bl, bl2);
        this.setOpaque(bl);
        return this;
    }
}

