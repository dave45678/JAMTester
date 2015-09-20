/*
 * Decompiled with CFR 0_102.
 */
package org.jext;

import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.FocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.LayoutFocusTraversalPolicy;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;

public class JavaSupport {
    public static void initJavaSupport() {
        JFrame.setDefaultLookAndFeelDecorated(Jext.getBooleanProperty("decoratedFrames"));
        JDialog.setDefaultLookAndFeelDecorated(Jext.getBooleanProperty("decoratedFrames"));
        KeyboardFocusManager.setCurrentKeyboardFocusManager(new JextKeyboardFocusManager());
    }

    public static void setMouseWheel(final JextTextArea jextTextArea) {
        jextTextArea.addMouseWheelListener(new MouseWheelListener(){

            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                if (mouseWheelEvent.getScrollType() == 0) {
                    jextTextArea.setFirstLine(jextTextArea.getFirstLine() + mouseWheelEvent.getUnitsToScroll());
                }
            }
        });
    }

    static class JextKeyboardFocusManager
    extends DefaultKeyboardFocusManager {
        JextKeyboardFocusManager() {
            this.setDefaultFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
        }

        public boolean postProcessKeyEvent(KeyEvent keyEvent) {
            if (!keyEvent.isConsumed()) {
                Component component = (Component)keyEvent.getSource();
                if (!component.isShowing()) {
                    return true;
                }
                do {
                    if (component instanceof JextFrame) {
                        ((JextFrame)component).processKeyEvent(keyEvent);
                        return true;
                    }
                    if (component == null || component instanceof Window) break;
                    if (component instanceof JextTextArea) break;
                    component = component.getParent();
                } while (true);
            }
            return super.postProcessKeyEvent(keyEvent);
        }
    }

}

