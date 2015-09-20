/*
 * Decompiled with CFR 0_102.
 */
package org.jext.actions;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.EditAction;
import org.jext.Jext;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public class BeginLine
extends MenuAction
implements EditAction {
    public BeginLine() {
        super("begin_lines_with");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String string = JOptionPane.showInputDialog(BeginLine.getJextParent(actionEvent), Jext.getProperty("add.line.label"), Jext.getProperty("begin.line.title"), 3);
        JextTextArea jextTextArea = BeginLine.getTextArea(actionEvent);
        if (string == null) {
            return;
        }
        jextTextArea.beginCompoundEdit();
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        try {
            Element element = syntaxDocument.getDefaultRootElement();
            int n = element.getElementIndex(jextTextArea.getSelectionStart());
            int n2 = element.getElementIndex(jextTextArea.getSelectionEnd());
            for (int i = n; i <= n2; ++i) {
                Element element2 = element.getElement(i);
                int n3 = element2.getStartOffset();
                int n4 = element2.getEndOffset() - 1;
                String string2 = string + jextTextArea.getText(n3, n4-=n3);
                syntaxDocument.remove(n3, n4);
                syntaxDocument.insertString(n3, string2, null);
            }
        }
        catch (BadLocationException var5_6) {
            // empty catch block
        }
        jextTextArea.endCompoundEdit();
    }
}

