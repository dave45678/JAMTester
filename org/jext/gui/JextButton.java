/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.EventListenerList;
import org.jext.EditAction;
import org.jext.Jext;
import org.jext.JextTextArea;
import org.jext.MenuAction;
import org.jext.OneClickAction;

public class JextButton
extends JButton {
    private MouseHandler _mouseListener;
    private ImageIcon grayedIcon;
    private ImageIcon coloredIcon;
    private Color nColor;
    private static Color commonHighlightColor = new Color(192, 192, 210);
    private static boolean rollover = true;
    private static boolean blockHighlightChange = false;
    static /* synthetic */ Class class$java$awt$event$ActionListener;

    public static void setRollover(boolean bl) {
        rollover = bl;
    }

    public static void setHighlightColor(Color color) {
        if (!blockHighlightChange) {
            commonHighlightColor = color;
        }
    }

    public static Color getHighlightColor() {
        return commonHighlightColor;
    }

    public static void blockHighlightChange() {
        blockHighlightChange = true;
    }

    public static void unBlockHighlightChange() {
        blockHighlightChange = false;
    }

    private void init() {
        this._mouseListener = new MouseHandler();
        if (rollover) {
            this.setBorderPainted(false);
            this.addMouseListener(this._mouseListener);
        } else if (Jext.getButtonsHighlight()) {
            this.nColor = this.getBackground();
            this.addMouseListener(this._mouseListener);
        }
    }

    public JextButton() {
        this.init();
    }

    public JextButton(Icon icon) {
        super(icon);
        this.init();
    }

    public JextButton(String string) {
        super(string);
        this.init();
    }

    public JextButton(String string, Icon icon) {
        super(string, icon);
        this.init();
    }

    public void setGrayed(boolean bl) {
        if (this.coloredIcon == null) {
            this.coloredIcon = (ImageIcon)this.getIcon();
        }
        if (bl && this.getRolloverIcon() == null) {
            GrayFilter grayFilter = new GrayFilter(true, 35);
            Image image = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(this.coloredIcon.getImage().getSource(), grayFilter));
            this.grayedIcon = new ImageIcon(image);
            this.setRolloverIcon(this.coloredIcon);
        }
        this.setIcon(bl ? this.grayedIcon : this.coloredIcon);
        this.setRolloverEnabled(bl);
    }

    protected void fireActionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = MenuAction.getTextArea(this);
        jextTextArea.setOneClick(null);
        jextTextArea.endCurrentEdit();
        Object[] arrobject = this.listenerList.getListenerList();
        ActionEvent actionEvent2 = null;
        for (int i = arrobject.length - 2; i >= 0; i-=2) {
            if (arrobject[i + 1] instanceof EditAction && !jextTextArea.isEditable() || arrobject[i] != (class$java$awt$event$ActionListener == null ? JextButton.class$("java.awt.event.ActionListener") : class$java$awt$event$ActionListener)) continue;
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
        this.removeMouseListener(this._mouseListener);
        super.finalize();
        this._mouseListener = null;
        this.grayedIcon = null;
        this.coloredIcon = null;
        this.nColor = null;
    }

    class MouseHandler
    extends MouseAdapter {
        MouseHandler() {
        }

        public void mouseEntered(MouseEvent mouseEvent) {
            if (JextButton.this.isEnabled()) {
                if (rollover) {
                    JextButton.this.setBorderPainted(true);
                } else {
                    JextButton.this.setBackground(commonHighlightColor);
                }
            }
        }

        public void mouseExited(MouseEvent mouseEvent) {
            if (JextButton.this.isEnabled()) {
                if (rollover) {
                    JextButton.this.setBorderPainted(false);
                } else {
                    JextButton.this.setBackground(JextButton.this.nColor);
                }
            }
        }
    }

}

