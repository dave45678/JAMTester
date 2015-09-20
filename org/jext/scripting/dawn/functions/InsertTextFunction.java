/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class InsertTextFunction
extends Function {
    public InsertTextFunction() {
        super("insertText");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        JextTextArea jextTextArea = ((JextFrame)dawnParser.getProperty("JEXT.JEXT_FRAME")).getTextArea();
        jextTextArea.setSelectedText(dawnParser.popString());
    }
}

