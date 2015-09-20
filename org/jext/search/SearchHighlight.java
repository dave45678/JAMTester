/*
 * Decompiled with CFR 0_102.
 */
package org.jext.search;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.text.Element;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextAreaHighlight;
import org.gjt.sp.jedit.textarea.TextAreaPainter;
import org.jext.JextTextArea;
import org.jext.search.SearchResult;

public class SearchHighlight
implements TextAreaHighlight {
    private ArrayList matches;
    private JextTextArea textArea;
    private TextAreaHighlight next;
    private boolean enabled = false;

    public void disable() {
        this.enabled = false;
    }

    public void enable() {
        this.enabled = true;
    }

    public void trigger(boolean bl) {
        this.enabled = bl;
    }

    public void setMatches(ArrayList arrayList) {
        this.matches = arrayList;
    }

    public void init(JEditTextArea jEditTextArea, TextAreaHighlight textAreaHighlight) {
        this.textArea = (JextTextArea)jEditTextArea;
        this.next = textAreaHighlight;
    }

    public void paintHighlight(Graphics graphics, int n, int n2) {
        if (this.enabled && this.matches != null) {
            graphics.setColor(Color.blue);
            Element element = this.textArea.getDocument().getDefaultRootElement();
            FontMetrics fontMetrics = this.textArea.getPainter().getFontMetrics();
            int[] arrn = new int[2];
            int n3 = fontMetrics.charWidth('w');
            int n4 = n2 + fontMetrics.getHeight() + fontMetrics.getLeading() + fontMetrics.getMaxDescent() + 1;
            int n5 = this.textArea.getHorizontalOffset();
            int n6 = this.textArea.getWidth();
            for (int i = 0; i < this.matches.size(); ++i) {
                Element element2;
                int n7;
                arrn = ((SearchResult)this.matches.get(i)).getPos();
                int n8 = element.getElementIndex(arrn[0]);
                if (n != n8 || (n7 = this.textArea.offsetToX(n, arrn[0] - (element2 = element.getElement(n)).getStartOffset())) < n5 || n7 >= n5 + n6) continue;
                int n9 = (arrn[1] - arrn[0]) * n3 + n7;
                while (n7 < n9) {
                    graphics.drawLine(n7, n4, n7 + 2, n4 - 2);
                    graphics.drawLine(n7 + 2, n4 - 2, n7 + 4, n4);
                    n7+=4;
                }
            }
        }
        if (this.next != null) {
            this.next.paintHighlight(graphics, n, n2);
        }
    }

    public String getToolTipText(MouseEvent mouseEvent) {
        return null;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.matches = null;
        this.textArea = null;
        this.next = null;
    }
}

