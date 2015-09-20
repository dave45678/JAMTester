/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class GetTextFunction
extends Function {
    public GetTextFunction() {
        super("getText");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        JextFrame jextFrame = (JextFrame)dawnParser.getProperty("JEXT.JEXT_FRAME");
        String string = jextFrame.getTextArea().getText();
        dawnParser.pushString(string == null ? "" : string);
    }
}

