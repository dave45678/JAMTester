/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import javax.swing.JOptionPane;
import org.jext.JextFrame;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class YesNoCancelFunction
extends Function {
    public YesNoCancelFunction() {
        super("yesNoCancel");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        JextFrame jextFrame = (JextFrame)dawnParser.getProperty("JEXT.JEXT_FRAME");
        int n = JOptionPane.showConfirmDialog(jextFrame, dawnParser.popString(), "Dawn", 1, 3);
        dawnParser.pushNumber(n);
    }
}

