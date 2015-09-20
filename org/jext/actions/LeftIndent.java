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

public class LeftIndent
extends MenuAction
implements EditAction {
    public LeftIndent() {
        super("left_indent");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = LeftIndent.getTextArea(actionEvent);
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        jextTextArea.beginCompoundEdit();
        try {
            int n = jextTextArea.getTabSize();
            boolean bl = JextTextArea.getSoftTab();
            Element element = jextTextArea.getDocument().getDefaultRootElement();
            int n2 = element.getElementIndex(jextTextArea.getSelectionStart());
            int n3 = element.getElementIndex(jextTextArea.getSelectionEnd());
            for (int i = n2; i <= n3; ++i) {
                Element element2 = element.getElement(i);
                int n4 = element2.getStartOffset();
                String string = syntaxDocument.getText(n4, element2.getEndOffset() - n4 - 1);
                int n5 = Utilities.getLeadingWhiteSpace(string);
                if (n5 == 0) continue;
                int n6 = Math.max(0, Utilities.getLeadingWhiteSpaceWidth(string, n) - n);
                syntaxDocument.remove(n4, n5);
                syntaxDocument.insertString(n4, Utilities.createWhiteSpace(n6, bl ? 0 : n), null);
            }
        }
        catch (BadLocationException var4_5) {
            var4_5.printStackTrace();
        }
        jextTextArea.endCompoundEdit();
    }
}

