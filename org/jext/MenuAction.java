/*
 * Decompiled with CFR 0_102.
 */
package org.jext;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.EventObject;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import org.jext.JextFrame;
import org.jext.JextTextArea;

public abstract class MenuAction
implements ActionListener {
    protected String name;

    public MenuAction(String string) {
        this.name = string;
    }

    public String getName() {
        return this.name;
    }

    public static JextTextArea getTextArea(EventObject eventObject) {
        if (eventObject.getSource() instanceof JextTextArea) {
            return (JextTextArea)eventObject.getSource();
        }
        return MenuAction.getJextParent(eventObject).getTextArea();
    }

    public static JextTextArea getNSTextArea(EventObject eventObject) {
        return MenuAction.getJextParent(eventObject).getNSTextArea();
    }

    public static JextFrame getJextParent(EventObject eventObject) {
        if (eventObject != null) {
            System.out.println(eventObject.getSource().getClass());
            Object object = eventObject.getSource();
            if (object instanceof Component) {
                Component component = (Component)object;
                do {
                    if (component instanceof JextTextArea) {
                        return ((JextTextArea)component).getJextParent();
                    }
                    if (component instanceof JextFrame) {
                        return (JextFrame)component;
                    }
                    if (component == null) break;
                    if (component instanceof JPopupMenu) {
                        component = ((JPopupMenu)component).getInvoker();
                        continue;
                    }
                    if (component instanceof JToolBar) {
                        return (JextFrame)((JComponent)component).getClientProperty("JEXT_INSTANCE");
                    }
                    component = component.getParent();
                } while (true);
            }
        }
        return null;
    }

    public static JextTextArea getTextArea(Component component) {
        return MenuAction.getJextParent(component).getTextArea();
    }

    public static JextTextArea getNSTextArea(Component component) {
        return MenuAction.getJextParent(component).getNSTextArea();
    }

    public static JextFrame getJextParent(Component component) {
        do {
            if (component instanceof JextFrame) {
                return (JextFrame)component;
            }
            if (component instanceof JPopupMenu) {
                component = ((JPopupMenu)component).getInvoker();
                continue;
            }
            if (component == null) break;
            component = component.getParent();
        } while (true);
        return null;
    }
}

