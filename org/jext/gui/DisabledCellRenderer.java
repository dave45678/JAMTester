/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class DisabledCellRenderer
extends JLabel
implements TableCellRenderer {
    public DisabledCellRenderer() {
        this.setOpaque(true);
        this.setBorder(new EmptyBorder(1, 1, 1, 1));
    }

    public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
        this.setBackground(jTable.getBackground());
        this.setForeground(jTable.getForeground());
        this.setText(object.toString());
        return this;
    }
}

