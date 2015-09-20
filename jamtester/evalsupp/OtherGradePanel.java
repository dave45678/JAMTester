/*
 * Decompiled with CFR 0_102.
 */
package jamtester.evalsupp;

import jamtester.Results;
import jamtester.StudentResult;
import jamtester.evalsupp.FileViewerFrame;
import jamtester.evalsupp.GradeInputCollection;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

public class OtherGradePanel
extends JPanel {
    private Results res;
    private GradeInputCollection collection;
    private FileViewerFrame viewer;
    private JButton addButton;
    private JPanel codePanel;
    private JPanel gradePanel;
    private JScrollPane gradeScroller;
    private JList jList1;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JSplitPane jSplitPane1;
    private JSplitPane jSplitPane2;
    private JTextArea jTextArea1;

    public OtherGradePanel() {
        this.initComponents();
        this.init();
    }

    public OtherGradePanel(Results results) {
        this.initComponents();
        this.res = results;
        this.collection = new GradeInputCollection(results, this.gradeScroller);
        this.gradeScroller.setViewportView(this.collection);
        this.jList1.setListData(results.getStudents().toArray());
        this.init();
    }

    public void load(Results results) {
        this.removeAll();
        this.initComponents();
        this.res = results;
        this.collection = new GradeInputCollection(results, this.gradeScroller);
        this.gradeScroller.setViewportView(this.collection);
        this.jList1.setListData(results.getStudents().toArray());
        this.init();
    }

    public void addGrade() {
        String string = JOptionPane.showInputDialog(this, "What should the new category be called?", "Add a category", 3);
        if (string.length() > 0) {
            String string2 = JOptionPane.showInputDialog(this, "What should be the default value of \"" + string + "\"", "Default value", 3);
            this.res.addOtherGradeHeader(string, string2);
            this.collection.addInput(string);
        }
    }

    public void init() {
        this.jTextArea1.getDocument().addDocumentListener(new DocumentListener(){

            public void insertUpdate(DocumentEvent documentEvent) {
                ((StudentResult)OtherGradePanel.this.jList1.getSelectedValue()).setComments(OtherGradePanel.this.jTextArea1.getText());
            }

            public void changedUpdate(DocumentEvent documentEvent) {
                ((StudentResult)OtherGradePanel.this.jList1.getSelectedValue()).setComments(OtherGradePanel.this.jTextArea1.getText());
            }

            public void removeUpdate(DocumentEvent documentEvent) {
                ((StudentResult)OtherGradePanel.this.jList1.getSelectedValue()).setComments(OtherGradePanel.this.jTextArea1.getText());
            }
        });
        this.jList1.setSelectionMode(0);
        this.viewer = new FileViewerFrame();
        JScrollPane jScrollPane = new JScrollPane(this.viewer);
        this.codePanel.add(jScrollPane);
    }

    private void initComponents() {
        this.jSplitPane1 = new JSplitPane();
        this.jPanel1 = new JPanel();
        this.jScrollPane2 = new JScrollPane();
        this.jList1 = new JList();
        this.jPanel2 = new JPanel();
        this.jSplitPane2 = new JSplitPane();
        this.codePanel = new JPanel();
        this.jPanel4 = new JPanel();
        this.jPanel5 = new JPanel();
        this.jScrollPane1 = new JScrollPane();
        this.jTextArea1 = new JTextArea();
        this.gradePanel = new JPanel();
        this.addButton = new JButton();
        this.gradeScroller = new JScrollPane();
        this.setLayout(new GridLayout(1, 1));
        this.jSplitPane1.setDividerLocation(200);
        this.jPanel1.setLayout(new BorderLayout());
        this.jPanel1.setBorder(new TitledBorder(null, "Student", 0, 2));
        this.jPanel1.setPreferredSize(new Dimension(1000, 10));
        this.jPanel1.addKeyListener(new KeyAdapter(){

            public void keyPressed(KeyEvent keyEvent) {
                OtherGradePanel.this.jPanel1KeyPressed(keyEvent);
            }
        });
        this.jScrollPane2.addKeyListener(new KeyAdapter(){

            public void keyPressed(KeyEvent keyEvent) {
                OtherGradePanel.this.jScrollPane2KeyPressed(keyEvent);
            }
        });
        this.jList1.setBorder(new SoftBevelBorder(0));
        this.jList1.setSelectionMode(0);
        this.jList1.setDoubleBuffered(true);
        this.jList1.addKeyListener(new KeyAdapter(){

            public void keyPressed(KeyEvent keyEvent) {
                OtherGradePanel.this.jList1KeyPressed(keyEvent);
            }
        });
        this.jList1.addListSelectionListener(new ListSelectionListener(){

            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                OtherGradePanel.this.jList1ValueChanged(listSelectionEvent);
            }
        });
        this.jScrollPane2.setViewportView(this.jList1);
        this.jPanel1.add((Component)this.jScrollPane2, "Center");
        this.jSplitPane1.setLeftComponent(this.jPanel1);
        this.jPanel2.setLayout(new GridLayout(1, 1));
        this.jSplitPane2.setDividerLocation(350);
        this.codePanel.setLayout(new GridLayout(1, 1));
        this.codePanel.setBorder(new TitledBorder("Code"));
        this.jSplitPane2.setLeftComponent(this.codePanel);
        this.jPanel4.setLayout(new BorderLayout());
        this.jPanel4.setBorder(new TitledBorder(null, "Grades", 0, 2));
        this.jPanel4.setMinimumSize(new Dimension(150, 139));
        this.jPanel4.setPreferredSize(new Dimension(250, 189));
        this.jPanel5.setLayout(new GridLayout(1, 1));
        this.jPanel5.setBorder(new TitledBorder("Comments:"));
        this.jPanel5.setMinimumSize(new Dimension(36, 75));
        this.jPanel5.setPreferredSize(new Dimension(116, 125));
        this.jScrollPane1.setDoubleBuffered(true);
        this.jTextArea1.setLineWrap(true);
        this.jScrollPane1.setViewportView(this.jTextArea1);
        this.jPanel5.add(this.jScrollPane1);
        this.jPanel4.add((Component)this.jPanel5, "North");
        this.gradePanel.setLayout(new BorderLayout(0, 15));
        this.addButton.setText("Add a grade (+)");
        this.addButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                OtherGradePanel.this.addButtonActionPerformed(actionEvent);
            }
        });
        this.gradePanel.add((Component)this.addButton, "North");
        this.gradePanel.add((Component)this.gradeScroller, "Center");
        this.jPanel4.add((Component)this.gradePanel, "Center");
        this.jSplitPane2.setRightComponent(this.jPanel4);
        this.jPanel2.add(this.jSplitPane2);
        this.jSplitPane1.setRightComponent(this.jPanel2);
        this.add(this.jSplitPane1);
    }

    private void jPanel1KeyPressed(KeyEvent keyEvent) {
        this.jList1KeyPressed(keyEvent);
    }

    private void jScrollPane2KeyPressed(KeyEvent keyEvent) {
        this.jList1KeyPressed(keyEvent);
    }

    private void jList1KeyPressed(KeyEvent keyEvent) {
        int n;
        System.out.println(keyEvent.getKeyCode());
        System.out.println("DOWN: " + 40);
        System.out.println("UP: " + 38);
        if ((keyEvent.getKeyCode() & 40) == 40 && (n = this.jList1.getSelectedIndex()) < this.jList1.getMaxSelectionIndex()) {
            this.jList1.setSelectedIndex(n + 1);
        }
        if ((keyEvent.getKeyCode() & 38) == 38 && (n = this.jList1.getSelectedIndex()) > this.jList1.getMinSelectionIndex()) {
            this.jList1.setSelectedIndex(n - 1);
        }
    }

    private void jList1ValueChanged(ListSelectionEvent listSelectionEvent) {
        if (this.jList1.getSelectedValue() == null) {
            return;
        }
        try {
            this.viewer.load((StudentResult)this.jList1.getSelectedValue());
            this.viewer.revalidate();
            this.codePanel.repaint();
            this.codePanel.revalidate();
        }
        catch (Exception var2_2) {
            // empty catch block
        }
        try {
            this.collection.loadValues((StudentResult)this.jList1.getSelectedValue());
        }
        catch (Exception var2_3) {
            // empty catch block
        }
        try {
            this.jTextArea1.setText(((StudentResult)this.jList1.getSelectedValue()).getComments());
        }
        catch (Exception var2_4) {
            // empty catch block
        }
    }

    private void addButtonActionPerformed(ActionEvent actionEvent) {
        this.addGrade();
    }

}

