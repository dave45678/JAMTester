/*
 * Decompiled with CFR 0_102.
 */
package org.jext;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.misc.Workspaces;
import org.jext.xml.XPopupReader;

public class JextTabbedPane
extends JTabbedPane
implements ChangeListener {
    private static JPopupMenu popupMenu;
    private JextFrame parent;
    private HashMap fileNames = new HashMap();
    private PopupMenu _mouseListener;
    private static final Icon CLEAN_ICON;
    private static final Icon DIRTY_ICON;

    public JextTabbedPane(JextFrame jextFrame) {
        this.parent = jextFrame;
        GUIUtilities.setScrollableTabbedPane(this);
        this._mouseListener = new PopupMenu();
        this.addMouseListener(this._mouseListener);
        this.addChangeListener(this);
    }

    public static JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void setCleanIcon(JextTextArea jextTextArea) {
        int n = this.indexOfComponent(jextTextArea);
        if (n == -1) {
            return;
        }
        this.setIconAt(n, CLEAN_ICON);
    }

    public void setDirtyIcon(JextTextArea jextTextArea) {
        int n = this.indexOfComponent(jextTextArea);
        if (n == -1) {
            return;
        }
        this.setIconAt(n, DIRTY_ICON);
    }

    public void addTab(String string, Component component) {
        this.setIndexedTitle(string);
        super.addTab(this.getIndexedTitle(string), component instanceof JextTextArea ? (((JextTextArea)component).isDirty() ? DIRTY_ICON : CLEAN_ICON) : null, component);
    }

    public void removeTabAt(int n) {
        if (n == -1) {
            return;
        }
        this.removeTitle(n, this.getComponentAt(n).getName());
        super.removeTabAt(n);
        this.stateChanged(new ChangeEvent(this));
    }

    public void setTitleAt(int n, String string) {
        if (n == -1) {
            return;
        }
        this.removeTitle(n, this.getComponentAt(n).getName());
        this.setIndexedTitle(string);
        super.setTitleAt(n, this.getIndexedTitle(string));
    }

    private void removeTitle(int n, String string) {
        boolean bl = false;
        for (int i = 0; i < this.getTabCount(); ++i) {
            String string2;
            if (i == n || (string2 = this.getComponentAt(i).getName()) == null || !string2.equals(string)) continue;
            bl = true;
            break;
        }
        if (!bl) {
            this.fileNames.remove(string);
        }
    }

    private void setIndexedTitle(String string) {
        Integer n;
        if (string == null) {
            string = Jext.getProperty("general.unknown");
        }
        if ((n = (Integer)this.fileNames.get(string)) == null) {
            this.fileNames.put(string, new Integer(0));
        } else {
            this.fileNames.put(string, new Integer(n + 1));
        }
    }

    private String getIndexedTitle(String string) {
        int n;
        if (string == null) {
            return Jext.getProperty("general.unknown");
        }
        Integer n2 = (Integer)this.fileNames.get(string);
        if (n2 != null && (n = n2.intValue()) != 0) {
            return string + " (" + n + ')';
        }
        return string;
    }

    public void nextTab() {
        int n = this.getSelectedIndex();
        if (++n == this.getTabCount()) {
            n = 0;
        }
        this.setSelectedIndex(n);
    }

    public void previousTab() {
        int n = this.getSelectedIndex();
        n = n == 0 ? this.getTabCount() - 1 : --n;
        this.setSelectedIndex(n);
    }

    public void removeAll() {
        this.fileNames.clear();
        super.removeAll();
    }

    public void stateChanged(ChangeEvent changeEvent) {
        Component component = this.getSelectedComponent();
        if (!(component instanceof JextTextArea)) {
            if (component != null) {
                this.parent.setTitle("Jext - " + this.getTitleAt(this.indexOfComponent(component)) + " [" + this.parent.getWorkspaces().getName() + ']');
                this.parent.disableSplittedTextArea();
            }
            return;
        }
        JextTextArea jextTextArea = (JextTextArea)component;
        jextTextArea.setParentTitle();
        this.parent.updateStatus(jextTextArea);
        this.parent.setStatus(jextTextArea);
        this.parent.updateSplittedTextArea(jextTextArea);
        this.parent.fireJextEvent(77);
        jextTextArea.grabFocus();
        jextTextArea.requestFocus();
    }

    protected void finalize() throws Throwable {
        this.removeChangeListener(this);
        this.removeMouseListener(this._mouseListener);
        super.finalize();
        this.parent = null;
        this.fileNames.clear();
        this.fileNames = null;
        this._mouseListener = null;
    }

    static {
        Class class_ = Jext.class;
        CLEAN_ICON = Utilities.getIcon("images/tab_clean.gif", class_);
        DIRTY_ICON = Utilities.getIcon("images/tab_dirty.gif", Jext.class);
    }

    class PopupMenu
    extends MouseAdapter
    implements Runnable {
        PopupMenu() {
            if (popupMenu == null) {
                Thread thread = new Thread(this);
                thread.start();
            }
        }

        public void run() {
            Class class_ = JextTabbedPane.class$org$jext$Jext == null ? (JextTabbedPane.class$org$jext$Jext = JextTabbedPane.class$("org.jext.Jext")) : JextTabbedPane.class$org$jext$Jext;
            popupMenu = XPopupReader.read(class_.getResourceAsStream("jext.tabbedpane.popup.xml"), "jext.tabbedpane.popup.xml");
            if (Jext.getFlatMenus()) {
                popupMenu.setBorder(LineBorder.createBlackLineBorder());
            }
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            this.showPopupIfNeeded(mouseEvent);
        }

        public void mousePressed(MouseEvent mouseEvent) {
            this.showPopupIfNeeded(mouseEvent);
        }

        private void showPopupIfNeeded(MouseEvent mouseEvent) {
            if (mouseEvent.isPopupTrigger() && popupMenu != null) {
                int n = mouseEvent.getX();
                Dimension dimension = JextTabbedPane.this.parent.getSize();
                Point point = JextTabbedPane.this.parent.getLocationOnScreen();
                Insets insets = JextTabbedPane.this.parent.getInsets();
                Point point2 = JextTabbedPane.this.getLocationOnScreen();
                Dimension dimension2 = popupMenu.getSize();
                if (point2.x + n + dimension2.width > point.x + dimension.width - insets.right) {
                    n-=dimension2.width;
                }
                popupMenu.show(JextTabbedPane.this, n, mouseEvent.getY());
            }
        }
    }

}

