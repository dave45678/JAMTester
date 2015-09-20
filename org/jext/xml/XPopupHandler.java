/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.microstar.xml.HandlerBase
 */
package org.jext.xml;

import com.microstar.xml.HandlerBase;
import java.awt.Component;
import java.io.PrintStream;
import java.util.Stack;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.gui.JextMenuSeparator;

public class XPopupHandler
extends HandlerBase {
    private JPopupMenu popup;
    private Stack stateStack;
    private JMenu currentMenu;
    private String lastAttrValue;
    private boolean enabled = true;
    private String lastAttr;
    private String lastName;
    private String lastAction;
    private String lastPicture;

    public void attribute(String string, String string2, boolean bl) {
        if (string.equalsIgnoreCase("ACTION")) {
            this.lastAction = string2;
        } else if (string.equalsIgnoreCase("PICTURE")) {
            this.lastPicture = string2;
        } else if (string.equalsIgnoreCase("ENABLED")) {
            this.enabled = string2.equalsIgnoreCase("YES");
        } else if (string.equalsIgnoreCase("LABEL")) {
            this.lastAttr = string;
            this.lastAttrValue = string2;
        }
    }

    public void doctypeDecl(String string, String string2, String string3) throws Exception {
        if (!"XPOPUP".equalsIgnoreCase(string)) {
            throw new Exception("Not a valid XPopup file !");
        }
    }

    public void startElement(String string) {
        this.stateStack.push(string);
        if ("LABEL".equalsIgnoreCase(this.lastAttr)) {
            if ("SUBMENU".equalsIgnoreCase(string)) {
                this.currentMenu = GUIUtilities.loadMenu(this.lastAttrValue, true);
            } else if (string.toUpperCase().equals("ITEM")) {
                this.lastName = this.lastAttrValue;
            }
        }
        this.lastAttr = null;
        this.lastAttrValue = null;
    }

    public void endElement(String string) {
        if (string == null) {
            return;
        }
        String string2 = (String)this.stateStack.peek();
        if (string.equalsIgnoreCase(string2)) {
            if ("ITEM".equalsIgnoreCase(string2)) {
                JMenuItem jMenuItem = GUIUtilities.loadMenuItem(this.lastName, this.lastAction, this.lastPicture, this.enabled, false);
                if (jMenuItem != null) {
                    if (this.currentMenu != null) {
                        this.currentMenu.add(jMenuItem);
                    } else {
                        this.popup.add(jMenuItem);
                    }
                }
                this.enabled = true;
                this.lastAction = null;
                this.lastName = null;
                this.lastPicture = null;
            } else if ("SEPARATOR".equalsIgnoreCase(string2)) {
                if (this.currentMenu != null) {
                    if (Jext.getFlatMenus()) {
                        this.currentMenu.getPopupMenu().add(new JextMenuSeparator());
                    } else {
                        this.currentMenu.getPopupMenu().addSeparator();
                    }
                } else if (Jext.getFlatMenus()) {
                    this.popup.add(new JextMenuSeparator());
                } else {
                    this.popup.addSeparator();
                }
            } else if ("SUBMENU".equalsIgnoreCase(string2) && this.currentMenu != null) {
                this.popup.add(this.currentMenu);
                this.currentMenu = null;
            }
            Object e = this.stateStack.pop();
        } else {
            System.err.println("XPopup: Unclosed tag: " + this.stateStack.peek());
        }
    }

    public void startDocument() {
        try {
            this.stateStack = new Stack();
            this.stateStack.push(null);
            this.popup = new JPopupMenu();
        }
        catch (Exception var1_1) {
            // empty catch block
        }
    }

    public void endDocument() {
    }

    public JPopupMenu getPopupMenu() {
        return this.popup;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.popup = null;
        this.stateStack.clear();
        this.stateStack = null;
        this.currentMenu = null;
        this.lastAttrValue = null;
        this.lastAttr = null;
        this.lastName = null;
        this.lastAction = null;
        this.lastPicture = null;
    }
}

