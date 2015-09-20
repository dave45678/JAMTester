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

public class RemoveWhitespace
extends MenuAction
implements EditAction {
    public RemoveWhitespace() {
        super("remove_end_whitespace");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = RemoveWhitespace.getTextArea(actionEvent);
        jextTextArea.beginCompoundEdit();
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        try {
            Element element = syntaxDocument.getDefaultRootElement();
            int n = element.getElementCount();
            for (int i = 0; i < n; ++i) {
                Element element2 = element.getElement(i);
                int n2 = element2.getStartOffset();
                int n3 = element2.getEndOffset() - 1;
                String string = this.doRemove(jextTextArea.getText(n2, n3-=n2));
                syntaxDocument.remove(n2, n3);
                if (string == null) continue;
                syntaxDocument.insertString(n2, string, null);
            }
            jextTextArea.endCompoundEdit();
        }
        catch (BadLocationException var4_5) {
            // empty catch block
        }
    }

    private String doRemove(String string) {
        int n = string.length();
        while (--n >= 0 && Character.isWhitespace(string.charAt(n))) {
        }
        return n < 0 ? null : string.substring(0, n + 1);
    }
}

