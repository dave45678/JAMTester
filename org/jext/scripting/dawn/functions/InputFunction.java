/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import javax.swing.JOptionPane;
import org.jext.JextFrame;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class InputFunction
extends Function {
    public InputFunction() {
        super("input");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        JextFrame jextFrame = (JextFrame)dawnParser.getProperty("JEXT.JEXT_FRAME");
        String string = JOptionPane.showInputDialog(jextFrame, dawnParser.popString(), "Dawn", 3);
        if (string == null) {
            string = "";
        }
        dawnParser.pushString(string);
    }
}

