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
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.gui.JextLabeledMenuSeparator;
import org.jext.gui.JextMenuSeparator;
import org.jext.menus.JextMenuBar;
import org.jext.menus.JextRecentMenu;
import org.jext.menus.TemplatesMenu;

public class XMenuHandler
extends HandlerBase {
    private JextFrame parent;
    private JextMenuBar mbar;
    private Stack stateStack;
    private String lastAttrValue;
    private JMenu lastMenu;
    private JMenu currentMenu;
    private boolean enabled = true;
    private boolean labelSeparator = false;
    private boolean debug = false;
    private String lastAttr;
    private String lastName;
    private String lastAction;
    private String lastPicture;
    private String lastID;
    private String lastLabel;

    public XMenuHandler(JextFrame jextFrame) {
        this.parent = jextFrame;
    }

    public void attribute(String string, String string2, boolean bl) {
        if (string.equalsIgnoreCase("ACTION")) {
            this.lastAction = string2;
        } else if (string.equalsIgnoreCase("PICTURE")) {
            this.lastPicture = string2;
        } else if (string.equalsIgnoreCase("ID")) {
            this.lastID = string2;
        } else if (string.equalsIgnoreCase("ENABLED")) {
            this.enabled = string2.equalsIgnoreCase("YES");
        } else if (string.equalsIgnoreCase("TEXT")) {
            this.lastLabel = string2;
        } else if (string.equalsIgnoreCase("LABEL")) {
            this.lastAttr = string;
            this.lastAttrValue = string2;
        } else if (string.equalsIgnoreCase("DEBUG")) {
            this.debug = string2.equalsIgnoreCase("YES");
        }
    }

    public void doctypeDecl(String string, String string2, String string3) throws Exception {
        if (!"XMENUBAR".equalsIgnoreCase(string)) {
            throw new Exception("Not a valid XMenu file !");
        }
    }

    public void startElement(String string) {
        this.stateStack.push(string);
        if (this.debug && !Jext.DEBUG) {
            return;
        }
        if ("LABEL".equalsIgnoreCase(this.lastAttr)) {
            if ("MENU".equalsIgnoreCase(string)) {
                this.lastMenu = this.currentMenu = GUIUtilities.loadMenu(this.lastAttrValue, true);
            } else if ("SUBMENU".equalsIgnoreCase(string)) {
                this.currentMenu = GUIUtilities.loadMenu(this.lastAttrValue, true);
            } else if ("RECENTS".equalsIgnoreCase(string)) {
                this.currentMenu = GUIUtilities.loadMenu(this.lastAttrValue, true);
                this.parent.setRecentMenu(new JextRecentMenu(this.parent, this.currentMenu));
            } else if ("PLUGINS".equalsIgnoreCase(string)) {
                this.currentMenu = GUIUtilities.loadMenu(this.lastAttrValue, true);
                this.parent.setPluginsMenu(this.currentMenu);
            } else if ("ITEM".equalsIgnoreCase(string)) {
                this.lastName = this.lastAttrValue;
            }
        } else if ("TEMPLATES".equalsIgnoreCase(string)) {
            this.currentMenu = new TemplatesMenu();
        }
        this.lastAttr = null;
        this.lastAttrValue = null;
    }

    public void endElement(String string) {
        if (string == null) {
            return;
        }
        if (this.debug && !Jext.DEBUG) {
            this.debug = false;
            this.stateStack.pop();
            return;
        }
        String string2 = (String)this.stateStack.peek();
        if (string.equalsIgnoreCase(string2)) {
            if ("ITEM".equalsIgnoreCase(string2)) {
                JMenuItem jMenuItem = GUIUtilities.loadMenuItem(this.lastName, this.lastAction, this.lastPicture, this.enabled);
                if (jMenuItem != null) {
                    this.currentMenu.add(jMenuItem);
                }
                this.enabled = true;
                this.lastAction = null;
                this.lastName = null;
                this.lastPicture = null;
            } else if ("SEPARATOR".equalsIgnoreCase(string2)) {
                if (this.labelSeparator && this.lastLabel != null && this.lastLabel.length() > 0) {
                    if (Jext.getFlatMenus()) {
                        this.currentMenu.getPopupMenu().add(new JextLabeledMenuSeparator(this.lastLabel));
                    } else {
                        this.currentMenu.getPopupMenu().addSeparator();
                    }
                } else if (Jext.getFlatMenus()) {
                    this.currentMenu.getPopupMenu().add(new JextMenuSeparator());
                } else {
                    this.currentMenu.getPopupMenu().addSeparator();
                }
                this.lastLabel = null;
            } else if ("MENU".equalsIgnoreCase(string2) || "PLUGINS".equalsIgnoreCase(string2)) {
                if (this.currentMenu != null) {
                    if (this.lastID == null) {
                        this.mbar.add(this.currentMenu);
                    } else {
                        this.mbar.addIdentifiedMenu(this.currentMenu, this.lastID);
                        this.lastID = null;
                    }
                    this.lastMenu = null;
                    this.currentMenu = null;
                }
            } else if (("SUBMENU".equalsIgnoreCase(string2) || "RECENTS".equalsIgnoreCase(string2) || "TEMPLATES".equalsIgnoreCase(string2)) && this.currentMenu != null) {
                this.lastMenu.add(this.currentMenu);
                this.currentMenu = this.lastMenu;
            }
            Object e = this.stateStack.pop();
        } else {
            System.err.println("XMenu: Unclosed tag: " + this.stateStack.peek());
        }
        this.debug = false;
    }

    public void startDocument() {
        try {
            this.labelSeparator = Jext.getBooleanProperty("labeled.separator");
            this.stateStack = new Stack();
            this.stateStack.push(null);
            this.mbar = new JextMenuBar();
        }
        catch (Exception var1_1) {
            // empty catch block
        }
    }

    public void endDocument() {
        this.parent.setJMenuBar(this.mbar);
        this.mbar = null;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.parent = null;
        this.mbar = null;
        this.stateStack.clear();
        this.stateStack = null;
        this.lastAttrValue = null;
        this.lastMenu = null;
        this.currentMenu = null;
        this.lastAttr = null;
        this.lastName = null;
        this.lastAction = null;
        this.lastPicture = null;
        this.lastID = null;
        this.lastLabel = null;
    }
}

