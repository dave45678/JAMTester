/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class SetTextFunction
extends Function {
    public SetTextFunction() {
        super("setText");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        JextFrame jextFrame = (JextFrame)dawnParser.getProperty("JEXT.JEXT_FRAME");
        jextFrame.getTextArea().setText(dawnParser.popString());
    }
}

