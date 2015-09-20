/*
 * Decompiled with CFR 0_102.
 */
package org.jext.search;

import javax.swing.text.Element;
import javax.swing.text.Position;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.JextTextArea;
import org.jext.Utilities;

public class SearchResult {
    private String str;
    private Position end;
    private Position start;
    private JextTextArea textArea;

    public SearchResult(JextTextArea jextTextArea, Position position, Position position2) {
        this.start = position;
        this.end = position2;
        this.textArea = jextTextArea;
        Element element = jextTextArea.getDocument().getDefaultRootElement();
        int n = element.getElementIndex(position.getOffset());
        this.str = "" + (n + 1) + ":" + this.getLine(element.getElement(n));
    }

    public int[] getPos() {
        int[] arrn = new int[]{this.start.getOffset(), this.end.getOffset()};
        return arrn;
    }

    private String getLine(Element element) {
        if (element == null) {
            return "";
        }
        String string = this.textArea.getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset() - 1);
        if ((string = string.substring(Utilities.getLeadingWhiteSpace(string))).length() > 70) {
            string = string.substring(0, 70) + "...";
        }
        return string;
    }

    public JextTextArea getTextArea() {
        return this.textArea;
    }

    public String toString() {
        return this.str;
    }
}

