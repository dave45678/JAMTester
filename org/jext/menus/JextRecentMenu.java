/*
 * Decompiled with CFR 0_102.
 */
package org.jext.menus;

import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.MenuAction;
import org.jext.Utilities;
import org.jext.gui.EnhancedMenuItem;

public class JextRecentMenu {
    private MenuAction opener;
    private JMenu recentMenu;
    private JextFrame parent;
    private int maxRecent;
    private String[] recent = new String[8];

    public JextRecentMenu(JextFrame jextFrame, JMenu jMenu) {
        this.parent = jextFrame;
        this.recentMenu = jMenu;
        this.opener = Jext.getAction("open_recent");
    }

    public void removeRecent() {
        this.recentMenu.removeAll();
        for (int i = 0; i < this.maxRecent; ++i) {
            String string = Jext.getProperty("recent." + i);
            if (!(string == null || string.equals(""))) {
                Jext.unsetProperty("recent." + i);
            }
            this.recent[i] = null;
        }
        EnhancedMenuItem enhancedMenuItem = new EnhancedMenuItem(Jext.getProperty("editor.norecent"));
        enhancedMenuItem.setEnabled(false);
        this.recentMenu.add(enhancedMenuItem);
        Jext.recentChanged(this.parent);
    }

    public void createRecent() {
        int n;
        try {
            this.maxRecent = Integer.parseInt(Jext.getProperty("max.recent"));
        }
        catch (NumberFormatException var1_1) {
            this.maxRecent = 8;
        }
        String[] arrstring = this.recent;
        this.recent = new String[this.maxRecent];
        for (n = 0; n < arrstring.length; ++n) {
            if (n == this.recent.length) break;
            this.recent[n] = arrstring[n];
        }
        n = 1;
        this.recentMenu.removeAll();
        for (int i = 0; i < this.maxRecent; ++i) {
            this.recent[i] = Jext.getProperty("recent." + i);
            if (this.recent[i] != null && !this.recent[i].equals("") && new File(this.recent[i]).exists()) {
                EnhancedMenuItem enhancedMenuItem = new EnhancedMenuItem(Utilities.getShortStringOf(this.recent[i], 70));
                enhancedMenuItem.setActionCommand(this.recent[i]);
                enhancedMenuItem.addActionListener(this.opener);
                this.recentMenu.add(enhancedMenuItem);
                n = 0;
                continue;
            }
            Jext.unsetProperty("recent." + i);
        }
        if (n != 0) {
            EnhancedMenuItem enhancedMenuItem = new EnhancedMenuItem(Jext.getProperty("editor.norecent"));
            enhancedMenuItem.setEnabled(false);
            this.recentMenu.add(enhancedMenuItem);
        }
    }

    public void saveRecent(String string) {
        int n;
        if (string == null) {
            return;
        }
        for (n = 0; n < this.maxRecent; ++n) {
            if (!string.equals(this.recent[n])) continue;
            return;
        }
        for (n = this.maxRecent - 1; n > 0; --n) {
            this.recent[n] = this.recent[n - 1];
            if (this.recent[n] == null || this.recent[n].equals("")) continue;
            Jext.setProperty("recent." + n, this.recent[n]);
        }
        this.recent[0] = string;
        Jext.setProperty("recent.0", string);
        this.createRecent();
        Jext.recentChanged(this.parent);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.opener = null;
        this.recentMenu = null;
        this.parent = null;
        this.recent = null;
    }
}

