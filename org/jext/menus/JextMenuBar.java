/*
 * Decompiled with CFR 0_102.
 */
package org.jext.menus;

import java.awt.Component;
import java.awt.Container;
import java.io.PrintStream;
import java.util.Hashtable;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.gui.JextMenuSeparator;

public class JextMenuBar
extends JMenuBar {
    private Hashtable menus = new Hashtable();
    private int fileMenusAdded;
    private int fileItemsAdded;
    private int editMenusAdded;
    private int editItemsAdded;

    public void addIdentifiedMenu(JMenu jMenu, String string) {
        if (this.menus.containsKey(string)) {
            System.err.println("JextMenuBar: There is already a menu with ID '" + string + "'!");
        }
        this.menus.put(string, jMenu);
        this.add(jMenu);
        JextFrame jextFrame = this.getJextFrame();
        if (jextFrame != null) {
            jextFrame.itemAdded(jMenu);
        }
    }

    public void addMenu(JMenu jMenu, String string) {
        int n = -1;
        JMenu jMenu2 = (JMenu)this.menus.get(string);
        if (jMenu2 == null) {
            return;
        }
        if (string.equals("Edit")) {
            n = 13 + this.editMenusAdded;
            ++this.editMenusAdded;
        } else if (string.equals("File")) {
            n = 22 + this.fileMenusAdded;
            ++this.fileMenusAdded;
        }
        if (n == -1) {
            if (!(jMenu2.getMenuComponent(jMenu2.getItemCount() - 2) instanceof JSeparator)) {
                if (Jext.getFlatMenus()) {
                    jMenu2.getPopupMenu().add(new JextMenuSeparator());
                } else {
                    jMenu2.getPopupMenu().addSeparator();
                }
            }
            jMenu2.add(jMenu);
        } else {
            jMenu2.insert(jMenu, n);
        }
        JextFrame jextFrame = this.getJextFrame();
        if (jextFrame != null) {
            jextFrame.itemAdded(jMenu);
        }
    }

    public void addMenuItem(JMenuItem jMenuItem, String string) {
        int n = -1;
        JMenu jMenu = (JMenu)this.menus.get(string);
        if (jMenu == null) {
            return;
        }
        if (string.equals("Edit")) {
            n = 16 + this.editMenusAdded + this.editItemsAdded;
            ++this.editItemsAdded;
        } else if (string.equals("File")) {
            n = 22 + this.fileMenusAdded + this.fileItemsAdded;
            ++this.fileItemsAdded;
        }
        if (n == -1) {
            if (!(jMenu.getMenuComponent(jMenu.getItemCount() - 2) instanceof JSeparator)) {
                if (Jext.getFlatMenus()) {
                    jMenu.getPopupMenu().add(new JextMenuSeparator());
                } else {
                    jMenu.getPopupMenu().addSeparator();
                }
            }
            jMenu.add(jMenuItem);
        } else {
            if (this.fileItemsAdded == 1) {
                if (Jext.getFlatMenus()) {
                    jMenu.getPopupMenu().insert(new JextMenuSeparator(), n);
                } else {
                    jMenu.getPopupMenu().insert(new JSeparator(), n);
                }
            }
            jMenu.insert(jMenuItem, n);
        }
        JextFrame jextFrame = this.getJextFrame();
        if (jextFrame != null) {
            jextFrame.itemAdded(jMenuItem);
        }
    }

    public void reset() {
        this.fileMenusAdded = 0;
        this.fileItemsAdded = 0;
        this.editMenusAdded = 0;
        this.editItemsAdded = 0;
    }

    private JextFrame getJextFrame() {
        Container container = this.getParent();
        if (container != null) {
            container = container.getParent();
        }
        if (container != null) {
            container = container.getParent();
        }
        return (JextFrame)container;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.menus.clear();
    }
}

