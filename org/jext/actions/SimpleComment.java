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
import org.jext.JextTextArea;
import org.jext.MenuAction;

public class SimpleComment
extends MenuAction
implements EditAction {
    public SimpleComment() {
        super("simple_comment");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = SimpleComment.getTextArea(actionEvent);
        String string = jextTextArea.getProperty("blockComment");
        if (string == null) {
            return;
        }
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        int n = jextTextArea.getSelectionStart();
        int n2 = jextTextArea.getSelectionEnd();
        Element element = syntaxDocument.getDefaultRootElement();
        int n3 = element.getElementIndex(n);
        int n4 = element.getElementIndex(n2);
        jextTextArea.beginCompoundEdit();
        try {
            int n5;
            StringBuffer stringBuffer = new StringBuffer(n2 - n + (string.length() + 1) * (n4 - n3));
            for (n5 = n3; n5 <= n4; ++n5) {
                int n6 = element.getElement(n5).getStartOffset();
                int n7 = element.getElement(n5).getEndOffset() - 1;
                stringBuffer.append(string).append(jextTextArea.getText(n6, n7-=n6));
                if (n5 == n4) continue;
                stringBuffer.append('\n');
            }
            n5 = element.getElement(n3).getStartOffset();
            syntaxDocument.remove(n5, element.getElement(n4).getEndOffset() - 1 - n5);
            syntaxDocument.insertString(n5, stringBuffer.toString(), null);
        }
        catch (BadLocationException var10_11) {
            // empty catch block
        }
        jextTextArea.setCaretPosition(jextTextArea.getCaretPosition());
        jextTextArea.endCompoundEdit();
    }
}

