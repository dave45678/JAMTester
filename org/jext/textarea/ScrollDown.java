/*
 * Decompiled with CFR 0_102.
 */
package org.jext.textarea;

import java.awt.event.ActionEvent;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public final class ScrollDown
extends MenuAction {
    public ScrollDown() {
        super("scroll_down");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = ScrollDown.getTextArea(actionEvent);
        if (jextTextArea.getFirstLine() < jextTextArea.getLineCount() - jextTextArea.getVisibleLines()) {
            jextTextArea.setFirstLine(jextTextArea.getFirstLine() + 1);
        }
    }
}

