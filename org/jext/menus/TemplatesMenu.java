/*
 * Decompiled with CFR 0_102.
 */
package org.jext.menus;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jext.Jext;
import org.jext.actions.CreateTemplate;
import org.jext.gui.EnhancedMenuItem;
import org.jext.gui.JextMenu;

public class TemplatesMenu
extends JextMenu {
    CreateTemplate creater = new CreateTemplate();

    public TemplatesMenu() {
        super(Jext.getProperty("templates.label"));
        this.processDirectory(this, Jext.getProperty("templates.directory", Jext.JEXT_HOME + File.separator + "templates"));
    }

    public void processDirectory(JMenu jMenu, String string) {
        JMenuItem jMenuItem;
        File file = new File(string);
        if (!file.exists()) {
            EnhancedMenuItem enhancedMenuItem = new EnhancedMenuItem(Jext.getProperty("templates.none"));
            enhancedMenuItem.setEnabled(false);
            jMenu.add(enhancedMenuItem);
            return;
        }
        String string2 = file.getName();
        if (file.isDirectory()) {
            jMenuItem = string.equals(Jext.getProperty("templates.directory", Jext.JEXT_HOME + File.separator + "templates")) ? jMenu : new JextMenu(string2);
            Object[] arrobject = file.list();
            if (arrobject.length == 0) {
                jMenuItem = new EnhancedMenuItem(Jext.getProperty("templates.none"));
                jMenuItem.setEnabled(false);
                JextMenu jextMenu = new JextMenu(string2);
                jextMenu.add(jMenuItem);
                jMenu.add(jextMenu);
                return;
            }
            Arrays.sort(arrobject);
            for (int i = 0; i < arrobject.length; ++i) {
                this.processDirectory((JMenu)jMenuItem, string + File.separator + (String)arrobject[i]);
            }
        } else {
            int n = string2.lastIndexOf(46);
            if (n != -1) {
                string2 = string2.substring(0, n);
            }
            jMenuItem = new EnhancedMenuItem(string2);
            jMenuItem.setActionCommand(string);
            jMenuItem.addActionListener(this.creater);
        }
        jMenu.add(jMenuItem);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.creater = null;
    }
}

