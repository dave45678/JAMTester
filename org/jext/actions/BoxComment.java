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
import org.jext.Utilities;

public class BoxComment
extends MenuAction
implements EditAction {
    public BoxComment() {
        super("box_comment");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = BoxComment.getTextArea(actionEvent);
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        String string = jextTextArea.getProperty("commentStart");
        String string2 = jextTextArea.getProperty("commentEnd");
        String string3 = jextTextArea.getProperty("boxComment");
        if (string == null || string2 == null || string3 == null) {
            return;
        }
        string = string + ' ';
        string2 = "" + ' ' + string2;
        string3 = string3 + ' ';
        int n = jextTextArea.getSelectionStart();
        int n2 = jextTextArea.getSelectionEnd();
        Element element = syntaxDocument.getDefaultRootElement();
        int n3 = element.getElementIndex(n);
        int n4 = element.getElementIndex(n2);
        jextTextArea.beginCompoundEdit();
        try {
            Element element2 = element.getElement(n3);
            int n5 = element2.getStartOffset();
            int n6 = Utilities.getLeadingWhiteSpace(syntaxDocument.getText(n5, element2.getEndOffset() - n5));
            syntaxDocument.insertString(Math.max(n5 + n6, n), string, null);
            for (int i = n3 + 1; i < n4; ++i) {
                element2 = element.getElement(i);
                n5 = element2.getStartOffset();
                n6 = Utilities.getLeadingWhiteSpace(syntaxDocument.getText(n5, element2.getEndOffset() - n5));
                syntaxDocument.insertString(n5 + n6, string3, null);
            }
            element2 = element.getElement(n4);
            n5 = element2.getStartOffset();
            n6 = Utilities.getLeadingWhiteSpace(syntaxDocument.getText(n5, element2.getEndOffset() - n5));
            if (n3 < n4) {
                syntaxDocument.insertString(n5 + n6, string3, null);
            }
            syntaxDocument.insertString(Math.max(n5 + n6, jextTextArea.getSelectionEnd()), string2, null);
            jextTextArea.setCaretPosition(jextTextArea.getCaretPosition());
        }
        catch (BadLocationException var12_13) {
            // empty catch block
        }
        jextTextArea.endCompoundEdit();
    }
}

