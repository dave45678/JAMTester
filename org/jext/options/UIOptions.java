/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.Utilities;
import org.jext.gui.AbstractOptionPane;
import org.jext.gui.JextCheckBox;
import org.jext.gui.ModifiedCellRenderer;
import org.jext.gui.Skin;
import org.jext.gui.SkinManager;
import org.jext.options.OptionsDialog;

public class UIOptions
extends AbstractOptionPane {
    private JComboBox icons;
    private JComboBox skins;
    private JextCheckBox showToolbar;
    private JextCheckBox labeledSeparator;
    private JextCheckBox gray;
    private JextCheckBox flatMenus;
    private JextCheckBox buttonsHighlight;
    private JextCheckBox toolbarRollover;
    private JextCheckBox decoratedFrames;
    private static final String[] iconsInternNames = new String[]{"_16", "_s16", "_20", "_s24", "_24"};
    private static final String[] iconsNames = new String[]{"Tiny", "Tiny Swing", "20x20", "Swing", "Gnome"};
    private int currSkinIndex;
    private SkinItem[] skinsNames;

    public UIOptions() {
        super("ui");
        HashMap hashMap = SkinManager.getSkinList();
        Object[] arrobject = new String[hashMap.size()];
        this.skinsNames = new SkinItem[hashMap.size()];
        int n = 0;
        Object object = hashMap.keySet().iterator();
        while (object.hasNext()) {
            String string;
            String string2 = (String)object.next();
            Skin skin = (Skin)hashMap.get(string2);
            arrobject[n] = string = skin.getSkinName();
            this.skinsNames[n] = new SkinItem(string, string2);
            ++n;
        }
        Arrays.sort(this.skinsNames);
        Arrays.sort(arrobject);
        this.skins = new JComboBox<Object>(arrobject);
        this.skins.setRenderer(new ModifiedCellRenderer());
        this.addComponent(Jext.getProperty("options.skins.label", "Select skin:"), this.skins);
        this.icons = new JComboBox<String>(iconsNames);
        this.addComponent(Jext.getProperty("options.icons.label"), this.icons);
        this.decoratedFrames = new JextCheckBox(Jext.getProperty("options.decoratedframes.label"));
        this.addComponent(this.decoratedFrames);
        this.decoratedFrames.setEnabled(Utilities.JDK_VERSION.charAt(2) >= '4');
        this.flatMenus = new JextCheckBox(Jext.getProperty("options.flatmenus.label"));
        this.addComponent(this.flatMenus);
        this.toolbarRollover = new JextCheckBox(Jext.getProperty("options.toolbarrollover.label"));
        this.addComponent(this.toolbarRollover);
        this.buttonsHighlight = new JextCheckBox(Jext.getProperty("options.buttonshighlight.label"));
        this.addComponent(this.buttonsHighlight);
        this.labeledSeparator = new JextCheckBox(Jext.getProperty("options.separator.label"));
        this.addComponent(this.labeledSeparator);
        this.showToolbar = new JextCheckBox(Jext.getProperty("options.toolbar.label"));
        this.addComponent(this.showToolbar);
        this.gray = new JextCheckBox(Jext.getProperty("options.graytoolbar.label"));
        this.addComponent(this.gray);
        object = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                UIOptions.this.control(true);
            }
        };
        this.flatMenus.addActionListener((ActionListener)object);
        this.showToolbar.addActionListener((ActionListener)object);
        this.load();
    }

    public void load() {
        int n;
        String string = Jext.getProperty("jext.look.icons");
        for (n = 0; n < 5; ++n) {
            if (string.equals(iconsInternNames[n])) break;
        }
        this.icons.setSelectedIndex(n);
        String string2 = Jext.getProperty("current_skin");
        for (n = 0; n < this.skinsNames.length; ++n) {
            if (string2.equals(this.skinsNames[n].skinIntName)) break;
        }
        this.currSkinIndex = n;
        if (n >= this.skinsNames.length) {
            n = 0;
        }
        this.skins.setSelectedIndex(n);
        this.decoratedFrames.setSelected(Jext.getBooleanProperty("decoratedFrames"));
        this.flatMenus.setSelected(Jext.getBooleanProperty("flatMenus"));
        this.toolbarRollover.setSelected(Jext.getBooleanProperty("toolbarRollover"));
        this.buttonsHighlight.setSelected(Jext.getBooleanProperty("buttonsHighlight"));
        this.labeledSeparator.setSelected(Jext.getBooleanProperty("labeled.separator"));
        this.showToolbar.setSelected(Jext.getBooleanProperty("toolbar", "on"));
        this.gray.setSelected(Jext.getBooleanProperty("toolbar.gray"));
        this.control(false);
    }

    public Component getComponent() {
        JScrollPane jScrollPane = new JScrollPane(this);
        Dimension dimension = this.getPreferredSize();
        jScrollPane.setPreferredSize(new Dimension(dimension.width, 410));
        jScrollPane.setBorder(LineBorder.createBlackLineBorder());
        return jScrollPane;
    }

    private void control(boolean bl) {
        this.labeledSeparator.setEnabled(this.flatMenus.isSelected());
        this.gray.setEnabled(this.showToolbar.isSelected());
        this.toolbarRollover.setEnabled(this.showToolbar.isSelected());
    }

    public void save() {
        Jext.setProperty("decoratedFrames", this.decoratedFrames.isSelected() ? "on" : "off");
        Jext.setProperty("toolbar", this.showToolbar.isSelected() ? "on" : "off");
        Jext.setProperty("toolbar.gray", this.gray.isEnabled() && this.gray.isSelected() ? "on" : "off");
        Jext.setProperty("labeled.separator", this.labeledSeparator.isEnabled() && this.labeledSeparator.isSelected() ? "on" : "off");
        Jext.setProperty("flatMenus", this.flatMenus.isSelected() ? "on" : "off");
        Jext.setProperty("buttonsHighlight", this.buttonsHighlight.isEnabled() && this.buttonsHighlight.isSelected() ? "on" : "off");
        Jext.setProperty("toolbarRollover", this.toolbarRollover.isEnabled() && this.toolbarRollover.isSelected() ? "on" : "off");
        Jext.setProperty("jext.look.icons", iconsInternNames[this.icons.getSelectedIndex()]);
        int n = this.skins.getSelectedIndex();
        if (this.currSkinIndex != n) {
            this.currSkinIndex = n;
            Jext.setProperty("current_skin", this.skinsNames[this.currSkinIndex].skinIntName);
            SkinManager.applySelectedSkin();
            this.updateUIs();
        }
    }

    private void updateUIs() {
        SwingUtilities.updateComponentTreeUI(OptionsDialog.getInstance());
        OptionsDialog.getInstance().pack();
        ArrayList arrayList = Jext.getInstances();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            JextFrame jextFrame = (JextFrame)arrayList.get(i);
            SwingUtilities.updateComponentTreeUI(jextFrame);
            jextFrame.pack();
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.icons = null;
        this.skins = null;
        this.showToolbar = null;
        this.labeledSeparator = null;
        this.gray = null;
        this.flatMenus = null;
        this.buttonsHighlight = null;
        this.toolbarRollover = null;
        this.decoratedFrames = null;
    }

    class SkinItem
    implements Comparable {
        public String skinName;
        public String skinIntName;

        public SkinItem(String string, String string2) {
            this.skinName = string;
            this.skinIntName = string2;
        }

        public boolean equals(Object object) {
            return this.skinName.equals(object);
        }

        public int compareTo(Object object) {
            return this.skinName.compareTo(((SkinItem)object).skinName);
        }

        public String toString() {
            return this.skinName;
        }
    }

}

