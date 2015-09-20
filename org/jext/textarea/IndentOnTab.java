/*
 * Decompiled with CFR 0_102.
 */
package org.jext.textarea;

import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.EditAction;
import org.jext.Jext;
import org.jext.JextTextArea;
import org.jext.MenuAction;
import org.jext.Utilities;
import org.jext.misc.Indent;

public final class IndentOnTab
extends MenuAction
implements EditAction {
    public IndentOnTab() {
        super("indent_on_tab");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        int n;
        JextTextArea jextTextArea = IndentOnTab.getTextArea(actionEvent);
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        Element element = syntaxDocument.getDefaultRootElement();
        int n2 = element.getElementIndex(jextTextArea.getSelectionStart());
        int n3 = element.getElementIndex(jextTextArea.getSelectionEnd());
        if (n3 - n2 != 0) {
            Jext.getAction("right_indent").actionPerformed(actionEvent);
            return;
        }
        jextTextArea.beginCompoundEdit();
        int n4 = jextTextArea.getTabSize();
        int n5 = jextTextArea.getCaretLine();
        if (Jext.getBooleanProperty("editor.tabStop")) {
            try {
                Element element2 = element.getElement(n5);
                int n6 = Utilities.getRealLength(syntaxDocument.getText(element2.getStartOffset(), jextTextArea.getCaretPosition() - element2.getStartOffset()), n4);
                n = n4 - n6 % n4;
            }
            catch (BadLocationException var10_10) {
                n = n4;
            }
        } else {
            n = n4;
        }
        if (JextTextArea.getTabIndent()) {
            if (!Indent.indent(jextTextArea, n5, true, false)) {
                jextTextArea.setSelectedText(Utilities.createWhiteSpace(n, JextTextArea.getSoftTab() ? 0 : n4));
            }
        } else {
            jextTextArea.setSelectedText(Utilities.createWhiteSpace(n, JextTextArea.getSoftTab() ? 0 : n4));
        }
        jextTextArea.endCompoundEdit();
    }
}

