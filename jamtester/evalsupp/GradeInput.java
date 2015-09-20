/*
 * Decompiled with CFR 0_102.
 */
package jamtester.evalsupp;

import jamtester.evalsupp.GradeInputCollection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class GradeInput
extends JPanel {
    private String title;
    private GradeInputCollection coll;
    private JTextField gradeTextBox;
    private JButton jButton2;
    private JPanel jPanel1;

    public GradeInput() {
        this.initComponents();
        this.init();
    }

    public GradeInput(String string, GradeInputCollection gradeInputCollection) {
        this.title = string;
        this.coll = gradeInputCollection;
        this.initComponents();
        this.init();
        ((TitledBorder)this.getBorder()).setTitle(string);
    }

    public void init() {
        this.gradeTextBox.getDocument().addDocumentListener(new DocumentListener(){

            public void insertUpdate(DocumentEvent documentEvent) {
                GradeInput.this.coll.save(GradeInput.this);
            }

            public void changedUpdate(DocumentEvent documentEvent) {
                GradeInput.this.coll.save(GradeInput.this);
            }

            public void removeUpdate(DocumentEvent documentEvent) {
                GradeInput.this.coll.save(GradeInput.this);
            }
        });
    }

    public void removeMe() {
        this.coll.remove(this);
    }

    public String getGrade() {
        return this.gradeTextBox.getText();
    }

    public void setGrade(String string) {
        this.gradeTextBox.setText(string);
    }

    public boolean equals(Object object) {
        return ((GradeInput)object).title.equals(this.title);
    }

    private void initComponents() {
        this.jButton2 = new JButton();
        this.jPanel1 = new JPanel();
        this.gradeTextBox = new JTextField();
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder(""));
        this.jButton2.setFont(new Font("Microsoft Sans Serif", 0, 24));
        this.jButton2.setForeground(new Color(255, 51, 51));
        this.jButton2.setText("-");
        this.jButton2.setToolTipText("Remove this category");
        this.jButton2.setDoubleBuffered(true);
        this.jButton2.setRolloverEnabled(true);
        this.jButton2.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GradeInput.this.remove(actionEvent);
            }
        });
        this.add((Component)this.jButton2, "East");
        this.jPanel1.setLayout(new BorderLayout());
        this.jPanel1.setBorder(new TitledBorder("Grade"));
        this.gradeTextBox.setText("0");
        this.jPanel1.add((Component)this.gradeTextBox, "Center");
        this.add((Component)this.jPanel1, "Center");
    }

    private void remove(ActionEvent actionEvent) {
        int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove category: " + this.title + "?", "Confirm category removal", 0, 3);
        if (n == 0) {
            this.removeMe();
        }
    }

}

