/*
 * Decompiled with CFR 0_102.
 */
package org.jext.actions;

import java.awt.event.ActionEvent;
import java.util.StringTokenizer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.EditAction;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public class JoinAllLines
extends MenuAction
implements EditAction {
    public JoinAllLines() {
        super("join_all_lines");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = JoinAllLines.getTextArea(actionEvent);
        jextTextArea.beginCompoundEdit();
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        StringTokenizer stringTokenizer = new StringTokenizer(jextTextArea.getText(), "\n");
        try {
            syntaxDocument.remove(0, jextTextArea.getLength());
            while (stringTokenizer.hasMoreTokens()) {
                syntaxDocument.insertString(jextTextArea.getLength(), stringTokenizer.nextToken().trim() + ' ', null);
            }
        }
        catch (BadLocationException var5_5) {
            // empty catch block
        }
        jextTextArea.endCompoundEdit();
        jextTextArea.getJextParent().updateStatus(jextTextArea);
    }
}

