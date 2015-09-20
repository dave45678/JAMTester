/*
 * Decompiled with CFR 0_102.
 */
package org.jext.textarea;

import java.awt.event.ActionEvent;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public final class ScrollPageDown
extends MenuAction {
    public ScrollPageDown() {
        super("scroll_page_down");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = ScrollPageDown.getTextArea(actionEvent);
        if (jextTextArea.getFirstLine() < jextTextArea.getLineCount() - jextTextArea.getVisibleLines()) {
            jextTextArea.setFirstLine(jextTextArea.getFirstLine() + jextTextArea.getVisibleLines());
        } else {
            jextTextArea.setFirstLine(jextTextArea.getLineCount() - jextTextArea.getVisibleLines());
        }
    }
}

