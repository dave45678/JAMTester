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
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import org.jext.JARClassLoader;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.Utilities;
import org.jext.gui.AbstractDisposer;
import org.jext.gui.JextCheckBox;
import org.jext.gui.JextHighlightButton;

public class AboutPlugins
extends JDialog
implements ActionListener {
    private JextHighlightButton ok;
    private InstalledPlugin[] plugs;

    public AboutPlugins(JextFrame jextFrame) {
        Component component;
        Object object;
        Object object2;
        Object object3;
        super(jextFrame, Jext.getProperty("about.plugins.title"), false);
        this.getContentPane().setLayout(new BorderLayout());
        Box box = Box.createVerticalBox();
        ArrayList arrayList = JARClassLoader.pluginsNames;
        ArrayList arrayList2 = JARClassLoader.pluginsRealNames;
        this.plugs = new InstalledPlugin[arrayList2.size()];
        if (this.plugs.length != 0) {
            object = new String[3];
            for (int i = 0; i < this.plugs.length; ++i) {
                JextCheckBox jextCheckBox;
                object3 = (String)arrayList2.get(i);
                object2 = (String)arrayList.get(i);
                int n = object2.indexOf(".class");
                boolean bl = JARClassLoader.isEnabled((String)object3);
                if (bl && n != -1) {
                    int n2 = object2.lastIndexOf(47);
                    object2 = object2.substring(n2 == -1 ? 0 : n2 + 1, n);
                    object[0] = Jext.getProperty("plugin." + (String)object2 + ".name");
                    object[1] = Jext.getProperty("plugin." + (String)object2 + ".version");
                    object[2] = Jext.getProperty("plugin." + (String)object2 + ".author");
                    jextCheckBox = new JextCheckBox(Jext.getProperty("about.plugins.sentence", (Object[])object));
                    jextCheckBox.setSelected(true);
                } else {
                    jextCheckBox = new JextCheckBox((String)object3);
                    jextCheckBox.setSelected(bl);
                }
                box.add(jextCheckBox);
                this.plugs[i] = new InstalledPlugin(jextCheckBox, (String)object3);
            }
            component = this.plugs[0].getCheckBox();
        } else {
            component = new JLabel("" + ' ' + Jext.getProperty("no.plugins"));
            component.setForeground(Color.black);
            box.add(component);
        }
        this.getContentPane().add((Component)new JLabel(Jext.getProperty("about.plugins.header")), "North");
        object2 = new JScrollPane(box);
        object3 = this.getFontMetrics(component.getFont());
        object2.getViewport().setPreferredSize(new Dimension(30 * object3.charWidth('m'), 8 * component.getPreferredSize().height));
        this.getContentPane().add((Component)object2, "Center");
        this.ok = new JextHighlightButton(Jext.getProperty("general.ok.button"));
        this.ok.addActionListener(this);
        this.getRootPane().setDefaultButton(this.ok);
        object = new JPanel();
        object.add(this.ok);
        this.getContentPane().add("South", (Component)object);
        this.setDefaultCloseOperation(2);
        this.addKeyListener(new AbstractDisposer(this));
        this.pack();
        Utilities.centerComponentChild(jextFrame, this);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.ok) {
            for (int i = 0; i < this.plugs.length; ++i) {
                this.plugs[i].save();
            }
            this.dispose();
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.ok = null;
        this.plugs = null;
    }

    class InstalledPlugin {
        private String name;
        private JCheckBox box;

        protected void finalize() throws Throwable {
            super.finalize();
            this.name = null;
            this.box = null;
        }

        InstalledPlugin(JCheckBox jCheckBox, String string) {
            this.box = jCheckBox;
            this.name = string;
        }

        public JCheckBox getCheckBox() {
            return this.box;
        }

        public void save() {
            JARClassLoader.setEnabled(this.name, this.box.isSelected());
        }
    }

}

