/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.textarea;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import org.gjt.sp.jedit.textarea.JEditTextArea;

public interface TextAreaHighlight {
    public void init(JEditTextArea var1, TextAreaHighlight var2);

    public void paintHighlight(Graphics var1, int var2, int var3);

    public String getToolTipText(MouseEvent var1);
}

