/*
 * Decompiled with CFR 0_102.
 */
package jamtester.studenttool;

import jamtester.studenttool.StudentToolTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import org.gjt.sp.jedit.textarea.TextAreaPainter;

public class StudentToolLineNumGutter
extends JComponent {
    private StudentToolTextArea tA;
    private Color back;
    private Color fore;
    private BorderLayout layout;

    public StudentToolLineNumGutter(StudentToolTextArea studentToolTextArea, BorderLayout borderLayout) {
        this.tA = studentToolTextArea;
        this.layout = borderLayout;
        this.back = studentToolTextArea.getPainter().getLineHighlightColor();
        this.fore = Color.black;
        studentToolTextArea.vertical().addAdjustmentListener(new ScrollListener(this));
        Dimension dimension = new Dimension(studentToolTextArea.getFontMetrics(studentToolTextArea.getPainter().getFont()).stringWidth("" + (studentToolTextArea.getFirstLine() + studentToolTextArea.getVisibleLines())), this.getHeight());
        this.setSize(dimension);
        this.setPreferredSize(dimension);
    }

    public void setSize(int n, int n2) {
        super.setSize(n, n2);
    }

    public void paint(Graphics graphics) {
        Dimension dimension = new Dimension(this.tA.getFontMetrics(this.tA.getPainter().getFont()).stringWidth("" + (this.tA.getFirstLine() + this.tA.getVisibleLines())), this.getHeight());
        Dimension dimension2 = this.getPreferredSize();
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        int n = this.tA.getFontMetrics(this.tA.getPainter().getFont()).getHeight();
        int n2 = this.tA.getPainter().getFont().getSize() - 2;
        graphics.setClip(0, 0, graphics.getClipBounds().width, this.tA.getPainter().getHeight());
        graphics.setColor(this.back);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        graphics.setColor(this.fore);
        graphics.setFont(this.tA.getPainter().getFont().deriveFont((float)n2));
        for (int i = 0; i < this.tA.getLineCount(); ++i) {
            int n3 = (graphics.getClipRect().width - this.tA.getFontMetrics(this.tA.getPainter().getFont()).stringWidth("" + (i + this.tA.getFirstLine()))) / 2;
            int n4 = (i + 1) * n - (n - n2) / 2;
            graphics.drawString("" + (i + 1 + this.tA.getFirstLine()), n3, n4);
        }
        this.paintBorder(graphics);
        if (!dimension2.equals(dimension)) {
            this.layout.layoutContainer(this.getParent());
        }
    }

    public void paintBorder(Graphics graphics) {
        graphics.setColor(this.fore);
        graphics.drawRect(0, 0, graphics.getClipBounds().width - 1, graphics.getClipBounds().height - 1);
    }

    private class ScrollListener
    implements AdjustmentListener {
        private JComponent j;

        public ScrollListener(JComponent jComponent) {
            this.j = jComponent;
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
            this.j.repaint();
        }
    }

}

