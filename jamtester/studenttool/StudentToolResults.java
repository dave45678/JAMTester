/*
 * Decompiled with CFR 0_102.
 */
package jamtester.studenttool;

import jamtester.GradingToolAboutDialog;
import jamtester.Results;
import jamtester.ResultsActionListener;
import jamtester.ResultsMouseAdapter;
import jamtester.studenttool.StudentFrame;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class StudentToolResults
extends Results {
    private JSplitPane pane;

    public JSplitPane getSP() {
        return this.pane;
    }

    public String getColumnName(int n) {
        String string = super.getColumnName(n);
        if (string.startsWith("Field")) {
            return "Test Class Name";
        }
        return string;
    }

    public Object getValueAt(int n, int n2) {
        if (n < 0 || n2 < 0) {
            return null;
        }
        if (n == 0 && n2 == 0) {
            return this.testFile.getName();
        }
        return super.getValueAt(n, n2);
    }

    public JPanel generatePanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 1));
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("Help");
        jMenu.setMnemonic('H');
        JMenuItem jMenuItem = new JMenuItem("About");
        jMenuItem.setMnemonic('A');
        jMenuItem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GradingToolAboutDialog gradingToolAboutDialog = new GradingToolAboutDialog(null);
                gradingToolAboutDialog.show();
            }
        });
        jMenu.add(jMenuItem);
        jMenuBar.add(jMenu);
        JMenu jMenu2 = new JMenu("File");
        jMenu2.setMnemonic('F');
        JMenuItem jMenuItem2 = new JMenuItem("Save as JAM");
        jMenuItem2.setMnemonic('S');
        jMenuItem2.addActionListener(new ResultsActionListener(this, false));
        JMenuItem jMenuItem3 = new JMenuItem("Save as CSV");
        jMenuItem3.addActionListener(new ResultsActionListener(this, true));
        jMenuItem3.setMnemonic('C');
        jMenu2.add(jMenuItem2);
        jMenu2.add(jMenuItem3);
        jMenuBar.add((Component)jMenu2, 0);
        JScrollPane jScrollPane = null;
        JTable jTable = this.generateTable();
        JScrollPane jScrollPane2 = jScrollPane = JTable.createScrollPaneForTable(jTable);
        jTable.setPreferredSize(new Dimension(100, 200));
        jScrollPane2.setPreferredSize(new Dimension(100, 200));
        jScrollPane.setVerticalScrollBarPolicy(21);
        JTextArea jTextArea = new JTextArea();
        this.areas.add(jTextArea);
        jTextArea.setEditable(false);
        jScrollPane = new JScrollPane(jTextArea);
        jScrollPane.setWheelScrollingEnabled(true);
        jScrollPane.setHorizontalScrollBarPolicy(32);
        jScrollPane.setVerticalScrollBarPolicy(22);
        JSplitPane jSplitPane = new JSplitPane(0, jScrollPane2, jScrollPane);
        if (!ma) {
            jTable.addMouseListener(new ResultsMouseAdapter(jTable, jTextArea, this));
        } else {
            jTable.addMouseListener(new StudentFrame.StudentToolResultsMouseAdapter(jTable, jTextArea, this));
        }
        jPanel.add(jSplitPane);
        jSplitPane.setPreferredSize(new Dimension(100, 200));
        this.pane = jSplitPane;
        jPanel.setPreferredSize(new Dimension(100, 200));
        jSplitPane.setDividerLocation(0.5);
        jSplitPane.resetToPreferredSizes();
        jSplitPane.setResizeWeight(0.15);
        return jPanel;
    }

}

