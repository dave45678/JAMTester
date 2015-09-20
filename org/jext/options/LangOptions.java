/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import org.jext.Jext;
import org.jext.Utilities;
import org.jext.gui.AbstractOptionPane;
import org.jext.gui.ModifiedCellRenderer;

public class LangOptions
extends AbstractOptionPane {
    private JList langList;

    public LangOptions() {
        super("lang");
        this.setLayout(new GridLayout(1, 1));
        JPanel jPanel = new JPanel(new BorderLayout());
        DefaultListModel<String> defaultListModel = new DefaultListModel<String>();
        defaultListModel.addElement("English");
        String[] arrstring = Utilities.getWildCardMatches(Jext.JEXT_HOME + File.separator + "lang", "*_pack.jar", true);
        if (arrstring != null) {
            for (int i = 0; i < arrstring.length; ++i) {
                defaultListModel.addElement(arrstring[i].substring(0, arrstring[i].indexOf("_pack.jar")));
            }
        }
        this.langList = new JList(defaultListModel);
        this.langList.setCellRenderer(new ModifiedCellRenderer());
        jPanel.add("North", new JLabel(Jext.getProperty("options.languages.title")));
        jPanel.add("Center", new JScrollPane(this.langList));
        this.add(jPanel);
        this.load();
    }

    public void load() {
        this.langList.setSelectedValue(Jext.getLanguage(), true);
    }

    public void save() {
        if (Jext.getLanguage().equals(this.langList.getSelectedValue())) {
            return;
        }
        try {
            File file = new File(Jext.SETTINGS_DIRECTORY + ".lang");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            String string = this.langList.getSelectedValue().toString();
            bufferedWriter.write(string, 0, string.length());
            bufferedWriter.flush();
            bufferedWriter.close();
            Jext.setLanguage(string);
        }
        catch (Exception var1_2) {
            // empty catch block
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.langList = null;
    }
}

