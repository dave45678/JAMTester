/*
 * Decompiled with CFR 0_102.
 */
package org.jext.textarea;

import java.awt.event.ActionEvent;
import org.jext.EditAction;
import org.jext.JextTextArea;
import org.jext.MenuAction;
import org.jext.misc.Indent;

public final class IndentOnEnter
extends MenuAction
implements EditAction {
    public IndentOnEnter() {
        super("indent_on_enter");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = IndentOnEnter.getTextArea(actionEvent);
        jextTextArea.beginCompoundEdit();
        jextTextArea.setSelectedText("\n");
        if (JextTextArea.getEnterIndent()) {
            Indent.indent(jextTextArea, jextTextArea.getCaretLine(), true, false);
        }
        jextTextArea.endCompoundEdit();
    }
}

