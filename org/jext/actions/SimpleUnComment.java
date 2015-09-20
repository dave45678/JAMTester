/*
 * Decompiled with CFR 0_102.
 */
package org.jext.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.EditAction;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public class SimpleUnComment
extends MenuAction
implements EditAction {
    public SimpleUnComment() {
        super("simple_uncomment");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = SimpleUnComment.getTextArea(actionEvent);
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
            String string2 = syntaxDocument.getText(n, n2 - n);
            for (int i = n3; i <= n4; ++i) {
                int n5 = element.getElement(i).getStartOffset();
                int n6 = element.getElement(i).getEndOffset();
                if (n5 >= n2) continue;
                this.possiblyUncomentThisLine(syntaxDocument, jextTextArea, n5, n6 - n5);
            }
        }
        catch (BadLocationException var10_11) {
            // empty catch block
        }
        jextTextArea.setCaretPosition(jextTextArea.getCaretPosition());
        jextTextArea.endCompoundEdit();
    }

    private void possiblyUncomentThisLine(Document document, JextTextArea jextTextArea, int n, int n2) {
        String string = new String();
        try {
            string = document.getText(n, n2);
        }
        catch (BadLocationException var6_6) {
            return;
        }
        String string2 = jextTextArea.getProperty("blockComment");
        String string3 = string.trim();
        int n3 = string3.indexOf(string2);
        if (n3 == 0) {
            int n4 = string.indexOf(string2);
            try {
                if (n + string2.length() <= jextTextArea.getSelectionEnd()) {
                    document.remove(n + n4, string2.length());
                }
            }
            catch (BadLocationException var10_11) {
                return;
            }
        }
    }
}

