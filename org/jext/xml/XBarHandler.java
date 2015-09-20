/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  com.microstar.xml.HandlerBase
 */
package org.jext.xml;

import com.microstar.xml.HandlerBase;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.net.URL;
import java.util.Stack;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.gui.JextButton;
import org.jext.toolbar.JextToolBar;

public class XBarHandler
extends HandlerBase {
    private JextFrame parent;
    private JextToolBar tbar;
    private Stack stateStack;
    private boolean enabled = true;
    private String lastShortcut;
    private String lastAction;
    private String lastPicture;
    private String lastLabel;
    private String lastTip;

    public XBarHandler(JextFrame jextFrame) {
        this.parent = jextFrame;
    }

    public void attribute(String string, String string2, boolean bl) {
        if (string.equalsIgnoreCase("ACTION")) {
            this.lastAction = string2;
        } else if (string.equalsIgnoreCase("MNEMONIC")) {
            this.lastShortcut = string2;
        } else if (string.equalsIgnoreCase("LABEL")) {
            this.lastLabel = string2;
        } else if (string.equalsIgnoreCase("PICTURE")) {
            this.lastPicture = string2;
        } else if (string.equalsIgnoreCase("TIP")) {
            this.lastTip = string2;
        } else if (string.equalsIgnoreCase("ENABLED")) {
            this.enabled = string2.equalsIgnoreCase("YES");
        }
    }

    public void doctypeDecl(String string, String string2, String string3) throws Exception {
        if (!"XTOOLBAR".equalsIgnoreCase(string)) {
            throw new Exception("Not a valid XBar file !");
        }
    }

    public void startElement(String string) {
        this.stateStack.push(string);
    }

    public void endElement(String string) {
        if (string == null) {
            return;
        }
        String string2 = (String)this.stateStack.peek();
        if (string.equalsIgnoreCase(string2)) {
            AbstractButton abstractButton;
            if ("BUTTON".equalsIgnoreCase(string2)) {
                Object object;
                if (this.lastAction == null) {
                    return;
                }
                abstractButton = new JextButton();
                abstractButton.setFocusPainted(false);
                if (this.lastLabel != null) {
                    abstractButton.setText(this.lastLabel);
                }
                if (this.lastPicture != null) {
                    Class class_ = Jext.class;
                    object = new ImageIcon(class_.getResource(this.lastPicture.concat(Jext.getProperty("jext.look.icons")).concat(".gif")));
                    if (object != null) {
                        abstractButton.setIcon((Icon)object);
                    }
                }
                if (this.lastTip != null) {
                    abstractButton.setToolTipText(this.lastTip);
                }
                if (this.lastShortcut != null) {
                    abstractButton.setMnemonic(this.lastShortcut.charAt(0));
                }
                abstractButton.setActionCommand(this.lastAction);
                object = Jext.getAction(this.lastAction);
                if (object == null) {
                    abstractButton.setEnabled(false);
                } else {
                    abstractButton.addActionListener((ActionListener)object);
                    abstractButton.setEnabled(this.enabled);
                }
                this.tbar.addButton((JextButton)abstractButton);
                this.enabled = true;
                this.lastShortcut = null;
                this.lastTip = null;
                this.lastPicture = null;
                this.lastLabel = null;
                this.lastAction = null;
            } else if ("SEPARATOR".equalsIgnoreCase(string2)) {
                this.tbar.addButtonSeparator();
            }
            abstractButton = this.stateStack.pop();
        } else {
            System.err.println("XBar: Unclosed tag: " + this.stateStack.peek());
        }
    }

    public void startDocument() {
        try {
            this.stateStack = new Stack();
            this.stateStack.push(null);
            this.tbar = new JextToolBar(this.parent);
        }
        catch (Exception var1_1) {
            // empty catch block
        }
    }

    public void endDocument() {
        this.parent.setJextToolBar(this.tbar);
        this.tbar = null;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.parent = null;
        this.tbar = null;
        this.stateStack.clear();
        this.stateStack = null;
        this.lastShortcut = null;
        this.lastAction = null;
        this.lastPicture = null;
        this.lastLabel = null;
        this.lastTip = null;
    }
}

