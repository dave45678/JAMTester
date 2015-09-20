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

public class WingComment
extends MenuAction
implements EditAction {
    public WingComment() {
        super("wing_comment");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = WingComment.getTextArea(actionEvent);
        jextTextArea.beginCompoundEdit();
        String string = jextTextArea.getProperty("commentStart");
        String string2 = jextTextArea.getProperty("commentEnd");
        if (string == null || string2 == null) {
            return;
        }
        string = string + ' ';
        string2 = "" + ' ' + string2;
        try {
            jextTextArea.getDocument().insertString(jextTextArea.getSelectionStart(), string, null);
            jextTextArea.getDocument().insertString(jextTextArea.getSelectionEnd(), string2, null);
        }
        catch (BadLocationException var5_5) {
            // empty catch block
        }
        jextTextArea.setCaretPosition(jextTextArea.getCaretPosition());
        jextTextArea.endCompoundEdit();
    }
}

