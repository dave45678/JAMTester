/*
 * Decompiled with CFR 0_102.
 */
package org.jext.textarea;

import java.awt.event.ActionEvent;
import org.jext.JextTextArea;
import org.jext.MenuAction;

public final class ScrollPageUp
extends MenuAction {
    public ScrollPageUp() {
        super("scroll_page_up");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextTextArea jextTextArea = ScrollPageUp.getTextArea(actionEvent);
        if (jextTextArea.getFirstLine() > jextTextArea.getVisibleLines()) {
            jextTextArea.setFirstLine(jextTextArea.getFirstLine() - jextTextArea.getVisibleLines());
        } else {
            jextTextArea.setFirstLine(0);
        }
    }
}

