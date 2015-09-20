/*
 * Decompiled with CFR 0_102.
 */
package jamtester.studenttool;

import jamtester.studenttool.StudentToolEditPanel;
import jamtester.studenttool.StudentToolPanel;
import jamtester.studenttool.Tab;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JTabbedPane;

public class StudentTabbedPane
extends JTabbedPane {
    private ArrayList tabs = new ArrayList();

    public StudentTabbedPane() {
        super(1, 1);
    }

    public void setTabs(ArrayList arrayList) {
        this.removeAll();
        for (int i = 0; i < arrayList.size(); ++i) {
            Tab tab = (Tab)arrayList.get(i);
            if (!new File(tab.getStartFile()).exists()) continue;
            if (!new File(tab.getTestFile()).exists()) continue;
            this.addTab(tab.getName(), new StudentToolPanel(new File(tab.getStartFile()), tab.getName(), new File(tab.getTestFile())));
        }
    }

    public ArrayList getTabs() {
        return this.tabs;
    }

    public void addTab(String string, Component component) {
        Tab tab = new Tab();
        StudentToolPanel studentToolPanel = (StudentToolPanel)component;
        File file = studentToolPanel.getStartFile();
        File file2 = new File(file.getParentFile(), studentToolPanel.getEditor().saveTemp().getName());
        tab.setTestFile(file2.getAbsolutePath());
        tab.setStartFile(file.getAbsolutePath());
        tab.setName(string);
        this.tabs.add(tab);
        studentToolPanel.setAssociatedTab(tab);
        super.addTab(string, component);
    }

    public void removeCurrent() {
        this.tabs.remove(super.getSelectedIndex());
        super.remove(super.getSelectedIndex());
    }

    public void removeAll() {
        this.tabs.clear();
        super.removeAll();
    }
}

