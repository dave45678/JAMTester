/*
 * Decompiled with CFR 0_102.
 */
package org.jext.textarea;

import java.awt.event.ActionEvent;
import javax.swing.text.Element;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public final class NextLineIndent
extends MenuAction {
    public NextLineIndent() {
        super("next_line_indent");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        int n;
        JextTextArea jextTextArea = NextLineIndent.getTextArea(actionEvent);
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        Element element = syntaxDocument.getDefaultRootElement();
        int n2 = element.getElementIndex(jextTextArea.getCaretPosition());
        if (element.getElementCount() == n2 + 1) {
            jextTextArea.setCaretPosition(element.getElement(n2).getStartOffset());
            return;
        }
        Element element2 = element.getElement(n2 + 1);
        int n3 = element2.getStartOffset();
        int n4 = element2.getEndOffset() - 1 - n3;
        String string = jextTextArea.getText(n3, n4);
        block3 : for (n = 0; n < n4; ++n) {
            char c = string.charAt(n);
            switch (c) {
                case '\t': 
                case ' ': {
                    continue block3;
                }
                default: {
                    break block3;
                }
            }
        }
        jextTextArea.setCaretPosition(n3 + n);
    }
}

