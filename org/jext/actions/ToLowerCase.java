/*
 * Decompiled with CFR 0_102.
 */
package org.jext.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.EditAction;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public class ToLowerCase
extends MenuAction
implements EditAction {
    public ToLowerCase() {
        super("to_lower_case");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = ToLowerCase.getTextArea(actionEvent);
        jextTextArea.beginCompoundEdit();
        String string = jextTextArea.getSelectedText();
        if (string != null) {
            jextTextArea.setSelectedText(string.toLowerCase());
        } else {
            SyntaxDocument syntaxDocument = jextTextArea.getDocument();
            try {
                int n = jextTextArea.getCaretPosition();
                int n2 = jextTextArea.getLineOfOffset(n);
                int n3 = jextTextArea.getLineStartOffset(n2);
                int n4 = jextTextArea.getLineEndOffset(n2) - 1;
                if (n == n4) {
                    jextTextArea.endCompoundEdit();
                    return;
                }
                char c = jextTextArea.getText(n3, n4-=n3).charAt(n - n3);
                syntaxDocument.remove(n, 1);
                syntaxDocument.insertString(n, new StringBuffer(1).append(c).toString().toLowerCase(), null);
                jextTextArea.setCaretPosition(n + 1);
            }
            catch (BadLocationException var5_6) {
                // empty catch block
            }
        }
        jextTextArea.endCompoundEdit();
    }
}

