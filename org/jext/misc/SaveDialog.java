/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.gui.AbstractDisposer;
import org.jext.gui.JextCheckBox;
import org.jext.gui.JextHighlightButton;
import org.jext.misc.Workspaces;

public class SaveDialog
extends JDialog
implements ActionListener {
    public static final int CLOSE_WINDOW = 0;
    public static final int CLOSE_TEXT_AREAS_ONLY = 1;
    public static final int DO_NOTHING = 2;
    private int mode;
    private int dirty;
    private DirtyArea[] areas;
    private JextFrame parent;
    private JextHighlightButton all;
    private JextHighlightButton none;
    private JextHighlightButton cancel;
    private JextHighlightButton ok;

    public SaveDialog(JextFrame jextFrame, int n) {
        Component component;
        Container container;
        super(jextFrame, Jext.getProperty("save.dialog.title"), true);
        this.parent = jextFrame;
        this.mode = n;
        this.getContentPane().setLayout(new BorderLayout());
        Box box = Box.createVerticalBox();
        Object[] arrobject = n == 0 ? this.createTextAreasArray() : jextFrame.getTextAreas();
        Vector<DirtyArea> vector = new Vector<DirtyArea>(arrobject.length);
        boolean bl = false;
        boolean bl2 = false;
        for (int i = 0; i < arrobject.length; ++i) {
            if (arrobject[i] instanceof JextTextArea) {
                component = (JextTextArea)arrobject[i];
                if (!component.isDirty() || component.isEmpty()) continue;
                container = new JextCheckBox(component.getName());
                container.setSelected(true);
                box.add(container);
                vector.addElement(new DirtyArea((JCheckBox)container, (JextTextArea)component));
                bl = true;
                bl2 = true;
                ++this.dirty;
                continue;
            }
            if (i != 0) {
                if (!bl2) {
                    box.remove(box.getComponentCount() - 1);
                } else {
                    component = new JLabel(" ");
                    box.add(component);
                }
            }
            component = new WorkspaceLabel(arrobject[i].toString());
            component.setFont(component.getFont().deriveFont(2));
            box.add(component);
            bl2 = false;
        }
        if (!bl) {
            this.exit();
            return;
        }
        if (!(bl2 || n != 0)) {
            box.remove(box.getComponentCount() - 1);
            if (box.getComponentCount() > 0) {
                box.remove(box.getComponentCount() - 1);
            }
        }
        this.getContentPane().add((Component)new JLabel(Jext.getProperty("save.dialog.label")), "North");
        this.areas = new DirtyArea[arrobject.length];
        vector.copyInto(this.areas);
        vector = null;
        JCheckBox jCheckBox = this.areas[0].getCheckBox();
        component = new JScrollPane(box);
        component.getViewport().setPreferredSize(new Dimension(component.getViewport().getPreferredSize().width, 6 * jCheckBox.getPreferredSize().height));
        this.getContentPane().add(component, "Center");
        container = new JPanel();
        this.all = new JextHighlightButton(Jext.getProperty("save.dialog.all.button"));
        container.add(this.all);
        this.all.setMnemonic(Jext.getProperty("save.dialog.all.mnemonic").charAt(0));
        this.none = new JextHighlightButton(Jext.getProperty("save.dialog.none.button"));
        container.add(this.none);
        this.none.setMnemonic(Jext.getProperty("save.dialog.none.mnemonic").charAt(0));
        this.ok = new JextHighlightButton(Jext.getProperty("general.ok.button"));
        container.add(this.ok);
        this.ok.setMnemonic(Jext.getProperty("general.ok.mnemonic").charAt(0));
        this.cancel = new JextHighlightButton(Jext.getProperty("general.cancel.button"));
        container.add(this.cancel);
        this.cancel.setMnemonic(Jext.getProperty("general.cancel.mnemonic").charAt(0));
        this.getContentPane().add((Component)container, "South");
        this.all.addActionListener(this);
        this.none.addActionListener(this);
        this.ok.addActionListener(this);
        this.cancel.addActionListener(this);
        this.addKeyListener(new AbstractDisposer(this));
        this.getRootPane().setDefaultButton(this.ok);
        this.setDefaultCloseOperation(2);
        this.pack();
        this.setResizable(false);
        Utilities.centerComponent(this);
        this.setVisible(true);
    }

    private Object[] createTextAreasArray() {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        DefaultListModel defaultListModel = this.parent.getWorkspaces().getList();
        for (int i = 0; i < defaultListModel.size(); ++i) {
            arrayList.add(((Workspaces.WorkspaceElement)defaultListModel.get(i)).getName());
            ArrayList arrayList2 = ((Workspaces.WorkspaceElement)defaultListModel.get((int)i)).contents;
            for (int j = 0; j < arrayList2.size(); ++j) {
                if (!(arrayList2.get(j) instanceof JextTextArea)) continue;
                arrayList.add(arrayList2.get(j));
            }
        }
        return arrayList.toArray();
    }

    private void save() {
        this.parent.setBatchMode(true);
        for (int i = 0; i < this.dirty; ++i) {
            DirtyArea dirtyArea = this.areas[i];
            JextTextArea jextTextArea = dirtyArea.getTextArea();
            if (dirtyArea.isSelected()) {
                jextTextArea.saveContent();
            }
            if (this.mode != 1) continue;
            this.parent.close(jextTextArea, false);
        }
        this.parent.setBatchMode(false);
        this.exit();
    }

    private void exit() {
        if (this.mode == 0) {
            this.parent.getWorkspaces().save();
            Jext.closeWindow(this.parent);
        } else if (this.mode == 1) {
            this.parent.setBatchMode(true);
            JextTextArea[] arrjextTextArea = this.parent.getTextAreas();
            for (int i = 0; i < arrjextTextArea.length; ++i) {
                this.parent.close(arrjextTextArea[i], false);
            }
            this.parent.setBatchMode(false);
        }
        this.dispose();
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.cancel) {
            this.dispose();
        } else if (object == this.ok) {
            this.save();
        } else {
            for (int i = 0; i < this.dirty; ++i) {
                this.areas[i].setSelected(object == this.all);
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.areas = null;
        this.parent = null;
        this.all = null;
        this.none = null;
        this.cancel = null;
        this.ok = null;
    }

    class DirtyArea {
        private JCheckBox box;
        private JextTextArea area;

        DirtyArea(JCheckBox jCheckBox, JextTextArea jextTextArea) {
            this.box = jCheckBox;
            this.area = jextTextArea;
        }

        public JCheckBox getCheckBox() {
            return this.box;
        }

        public boolean isSelected() {
            return this.box.isSelected();
        }

        public void setSelected(boolean bl) {
            this.box.setSelected(bl);
        }

        public JextTextArea getTextArea() {
            return this.area;
        }
    }

    class WorkspaceLabel
    extends JLabel {
        WorkspaceLabel(String string) {
            super(string);
        }

        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            graphics.setColor(Color.black);
            graphics.drawLine(0, this.getHeight() - 2, this.getWidth() - 2, this.getHeight() - 2);
        }
    }

}

