/*
 * Decompiled with CFR 0_102.
 */
package org.jext.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.EditAction;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public class JoinLines
extends MenuAction
implements EditAction {
    public JoinLines() {
        super("join_lines");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = JoinLines.getTextArea(actionEvent);
        jextTextArea.beginCompoundEdit();
        StringBuffer stringBuffer = new StringBuffer();
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        Element element = syntaxDocument.getDefaultRootElement();
        try {
            int n = element.getElementIndex(jextTextArea.getCaretPosition());
            if (n == element.getElementCount() - 1) {
                jextTextArea.endCompoundEdit();
                return;
            }
            Element element2 = element.getElement(n + 1);
            int n2 = element2.getStartOffset();
            int n3 = element2.getEndOffset() - 1;
            stringBuffer.append(' ').append(jextTextArea.getText(n2, n3-=n2).trim());
            syntaxDocument.remove(n2, n == element.getElementCount() - 2 ? n3 : n3 + 1);
            syntaxDocument.insertString(element.getElement(n).getEndOffset() - 1, stringBuffer.toString(), null);
        }
        catch (BadLocationException var6_7) {
            // empty catch block
        }
        jextTextArea.endCompoundEdit();
        jextTextArea.getJextParent().updateStatus(jextTextArea);
    }
}

