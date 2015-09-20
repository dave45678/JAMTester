/*
 * Decompiled with CFR 0_102.
 */
package jamtester.coverage;

import jamtester.coverage.CoverageInfo;
import jamtester.studenttool.StudentToolTextArea;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintStream;
import javax.swing.JTable;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextAreaHighlight;
import org.gjt.sp.jedit.textarea.TextAreaPainter;

public class CoverageViewer
extends StudentToolTextArea {
    private JTable theTable;
    private CoverageInfo info;
    private File source;
    private String sourceString;

    public CoverageViewer(CoverageInfo coverageInfo, File file) {
        super(file, false);
        System.err.println("Coverage Viewing: " + file);
        this.info = coverageInfo;
        this.source = file;
        TextAreaPainter textAreaPainter = this.getPainter();
        textAreaPainter.addCustomHighlight(new Highlight());
        super.getPainter().setAntiAliasingEnabled(true);
        this.getPainter().setLineHighlightEnabled(false);
    }

    private class Highlight
    implements TextAreaHighlight {
        JEditTextArea tA;
        TextAreaHighlight next;
        TextAreaPainter painter;

        private Highlight() {
        }

        public void init(JEditTextArea jEditTextArea, TextAreaHighlight textAreaHighlight) {
            this.tA = jEditTextArea;
            this.next = textAreaHighlight;
            this.painter = this.tA.getPainter();
        }

        public void paintHighlight(Graphics graphics, int n, int n2) {
            if (graphics.getClipRect().width < this.tA.getPainter().getWidth()) {
                this.tA.getPainter().invalidate();
                this.tA.getPainter().repaint();
                return;
            }
            n2 = this.tA.lineToY(n) + this.painter.getFontMetrics().getDescent();
            int n3 = this.painter.getFontMetrics().getHeight();
            if (CoverageViewer.this.info.timesRun(n + 1) == -1) {
                return;
            }
            if (CoverageViewer.this.info.timesRun(n + 1) == 0) {
                graphics.setColor(Color.red.brighter());
            } else {
                graphics.setColor(Color.green.brighter());
            }
            graphics.fillRect(0, n2, graphics.getClipRect().width, n3);
            graphics.setColor(Color.black);
            graphics.drawRect(0, n2, graphics.getClipRect().width, n3);
            if (this.next != null) {
                this.next.paintHighlight(graphics, n, n2);
            }
        }

        public String getToolTipText(MouseEvent mouseEvent) {
            int n = this.getLineForPoint(mouseEvent.getPoint());
            TextAreaPainter textAreaPainter = CoverageViewer.this.getPainter();
            if (CoverageViewer.this.info.timesRun(n) == -1) {
                return "Line not checked";
            }
            if (CoverageViewer.this.info.timesRun(n) == 0) {
                return "Line not covered";
            }
            return "Times covered: " + CoverageViewer.this.info.timesRun(n);
        }

        private int getLineForPoint(Point point) {
            return this.tA.yToLine(point.y) + 1;
        }
    }

}

