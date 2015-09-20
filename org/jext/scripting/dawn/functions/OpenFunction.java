/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class OpenFunction
extends Function {
    public OpenFunction() {
        super("open");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        JextFrame jextFrame = (JextFrame)dawnParser.getProperty("JEXT.JEXT_FRAME");
        jextFrame.open(Utilities.constructPath(dawnParser.popString()));
    }
}

