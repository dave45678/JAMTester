/*
 * Decompiled with CFR 0_102.
 */
package org.jext.toolbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Mode;
import org.jext.event.JextEvent;
import org.jext.event.JextListener;
import org.jext.gui.ModifiedCellRenderer;

public class FastSyntax
extends JComboBox
implements ActionListener,
JextListener {
    private JextFrame parent;
    private static String[] modeNames = new String[Jext.modes.size()];

    public FastSyntax(JextFrame jextFrame) {
        super(modeNames);
        this.parent = jextFrame;
        this.addActionListener(this);
        jextFrame.addJextListener(this);
        this.setRenderer(new ModifiedCellRenderer());
        this.selectMode(jextFrame.getTextArea());
        this.setMaximumSize(this.getPreferredSize());
    }

    public void jextEventFired(JextEvent jextEvent) {
        int n = jextEvent.getWhat();
        if (n == 77 || n == 0) {
            this.selectMode(jextEvent.getTextArea());
        }
    }

    private void selectMode(JextTextArea jextTextArea) {
        int n;
        String string = jextTextArea == null ? Jext.getProperty("editor.colorize.mode") : jextTextArea.getColorizingMode();
        for (n = 0; n < modeNames.length; ++n) {
            if (string.equals(((Mode)Jext.modes.get(n)).getModeName())) break;
        }
        this.setSelectedItem(modeNames[n]);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = this.parent.getTextArea();
        if (actionEvent.getSource() == this && jextTextArea != null) {
            String string = ((Mode)Jext.modes.get(this.getSelectedIndex())).getModeName();
            if (!string.equalsIgnoreCase(jextTextArea.getColorizingMode())) {
                jextTextArea.setColorizing((Mode)Jext.modes.get(this.getSelectedIndex()));
            }
            jextTextArea.grabFocus();
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.parent = null;
    }

    static {
        for (int i = 0; i < modeNames.length; ++i) {
            FastSyntax.modeNames[i] = ((Mode)Jext.modes.get(i)).getUserModeName();
        }
    }
}

