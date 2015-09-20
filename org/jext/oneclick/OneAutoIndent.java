/*
 * Decompiled with CFR 0_102.
 */
package org.jext.oneclick;

import java.awt.event.ActionEvent;
import org.jext.JextTextArea;
import org.jext.OneClickAction;
import org.jext.misc.Indent;

public class OneAutoIndent
extends OneClickAction {
    public OneAutoIndent() {
        super("one_auto_indent");
    }

    public void oneClickActionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = OneAutoIndent.getTextArea(actionEvent);
        Indent.indent(jextTextArea, jextTextArea.getCaretLine(), true, false);
    }
}

