/*
 * Decompiled with CFR 0_102.
 */
package org.jext.textarea;

import java.awt.event.ActionEvent;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public final class ScrollUp
extends MenuAction {
    public ScrollUp() {
        super("scroll_up");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = ScrollUp.getTextArea(actionEvent);
        if (jextTextArea.getFirstLine() > 0) {
            jextTextArea.setFirstLine(jextTextArea.getFirstLine() - 1);
        }
    }
}

