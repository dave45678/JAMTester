/*
 * Decompiled with CFR 0_102.
 */
package jamtester.evalsupp;

import jamtester.Results;
import jamtester.StudentResult;
import jamtester.evalsupp.GradeInput;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class GradeInputCollection
extends JPanel
implements Scrollable {
    private ArrayList inputs;
    private Results spawner;
    private int vgap = 5;
    private JScrollPane scroller;
    private StudentResult student;

    public GradeInputCollection() {
        this.initComponents();
    }

    public GradeInputCollection(Results results) {
        this.inputs = new ArrayList();
        this.spawner = results;
        ArrayList arrayList = this.spawner.getOtherGradeHeaders();
        for (int i = 0; i < arrayList.size(); ++i) {
            this.addInput(arrayList.get(i).toString());
        }
    }

    public GradeInputCollection(Results results, JScrollPane jScrollPane) {
        this(results);
        this.scroller = jScrollPane;
    }

    public void setScrollPane(JScrollPane jScrollPane) {
        this.scroller = jScrollPane;
    }

    public void remove(GradeInput gradeInput) {
        int n = this.indexOf(gradeInput);
        this.inputs.remove(n);
        this.remove(n);
        this.spawner.removeOtherGradeAt(n);
        this.refreshHeight();
    }

    public int indexOf(GradeInput gradeInput) {
        for (int i = 0; i < this.inputs.size(); ++i) {
            if (gradeInput != this.inputs.get(i)) continue;
            return i;
        }
        return -1;
    }

    public void loadValues(StudentResult studentResult) {
        this.student = studentResult;
        for (int i = 0; i < this.inputs.size(); ++i) {
            ((GradeInput)this.inputs.get(i)).setGrade(this.student.getOtherGrades().get(i).toString());
        }
    }

    public void save(GradeInput gradeInput) {
        if (this.student == null) {
            return;
        }
        int n = this.indexOf(gradeInput);
        this.student.setOtherGrade(n, gradeInput.getGrade());
        System.out.println("SAVED: " + this.student.toCsv());
    }

    public void addInput(String string) {
        GradeInput gradeInput = new GradeInput(string, this);
        this.inputs.add(gradeInput);
        this.refreshHeight();
        this.add(gradeInput);
        this.doLayout();
        this.invalidate();
        this.repaint();
        this.validate();
        for (int i = 0; i < this.inputs.size(); ++i) {
            ((Component)this.inputs.get(i)).validate();
        }
        if (this.scroller != null) {
            SwingUtilities.invokeLater(new Runnable(){

                public void run() {
                    JScrollBar jScrollBar = GradeInputCollection.this.scroller.getVerticalScrollBar();
                    jScrollBar.setValue(jScrollBar.getMaximum() + jScrollBar.getVisibleAmount());
                }
            });
        }
        if (this.student != null) {
            this.loadValues(this.student);
        }
    }

    private void refreshHeight() {
        this.setLayout(new GridLayout(this.inputs.size(), 1, 0, this.vgap));
        if (this.inputs.size() != 0) {
            this.setSize(this.getWidth(), this.inputs.size() * (this.vgap + ((JComponent)this.inputs.get(0)).getHeight()));
        } else {
            this.setSize(this.getWidth(), 0);
        }
    }

    public Dimension getPreferredScrollableViewportSize() {
        return null;
    }

    public int getScrollableBlockIncrement(Rectangle rectangle, int n, int n2) {
        return 90;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public int getScrollableUnitIncrement(Rectangle rectangle, int n, int n2) {
        return 30;
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.setBorder(new MatteBorder(null));
    }

}

