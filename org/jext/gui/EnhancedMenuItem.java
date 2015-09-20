/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonModel;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;
import org.jext.EditAction;
import org.jext.Jext;
import org.jext.JextTextArea;
import org.jext.MenuAction;
import org.jext.OneClickAction;

public class EnhancedMenuItem
extends JMenuItem {
    private String keyBinding;
    private Font acceleratorFont;
    private Color acceleratorForeground;
    private Color acceleratorSelectionForeground;
    static /* synthetic */ Class class$java$awt$event$ActionListener;

    public EnhancedMenuItem(String string) {
        this(string, null);
    }

    public EnhancedMenuItem(String string, String string2) {
        super(string);
        this.keyBinding = string2;
        if (Jext.getFlatMenus()) {
            this.setBorder(new EmptyBorder(2, 2, 2, 2));
        }
        this.acceleratorFont = UIManager.getFont("MenuItem.acceleratorFont");
        this.acceleratorForeground = UIManager.getColor("MenuItem.acceleratorForeground");
        this.acceleratorSelectionForeground = UIManager.getColor("MenuItem.acceleratorSelectionForeground");
    }

    public Dimension getPreferredSize() {
        Dimension dimension = super.getPreferredSize();
        if (this.keyBinding != null) {
            dimension.width+=this.getToolkit().getFontMetrics(this.acceleratorFont).stringWidth(this.keyBinding) + 30;
        }
        return dimension;
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (this.keyBinding != null) {
            graphics.setFont(this.acceleratorFont);
            graphics.setColor(this.getModel().isArmed() ? this.acceleratorSelectionForeground : this.acceleratorForeground);
            FontMetrics fontMetrics = graphics.getFontMetrics();
            Insets insets = this.getInsets();
            graphics.drawString(this.keyBinding, this.getWidth() - (fontMetrics.stringWidth(this.keyBinding) + insets.right + insets.left), this.getFont().getSize() + (insets.top - 1));
        }
    }

    public String getActionCommand() {
        return this.getModel().getActionCommand();
    }

    protected void fireActionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = MenuAction.getTextArea(this);
        jextTextArea.setOneClick(null);
        jextTextArea.endCurrentEdit();
        Object[] arrobject = this.listenerList.getListenerList();
        ActionEvent actionEvent2 = null;
        for (int i = arrobject.length - 2; i >= 0; i-=2) {
            if (arrobject[i + 1] instanceof EditAction && !jextTextArea.isEditable() || arrobject[i] != (class$java$awt$event$ActionListener == null ? EnhancedMenuItem.class$("java.awt.event.ActionListener") : class$java$awt$event$ActionListener)) continue;
            if (actionEvent2 == null) {
                String string = actionEvent.getActionCommand();
                if (string == null) {
                    string = this.getActionCommand();
                }
                actionEvent2 = new ActionEvent(this, 1001, string, actionEvent.getModifiers());
            }
            ((ActionListener)arrobject[i + 1]).actionPerformed(actionEvent2);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.keyBinding = null;
        this.acceleratorFont = null;
        this.acceleratorForeground = null;
        this.acceleratorSelectionForeground = null;
    }
}

