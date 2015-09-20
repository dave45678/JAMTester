/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import org.jext.JextFrame;
import org.jext.console.Console;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class ConsoleFunction
extends Function {
    public ConsoleFunction() {
        super("console");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        JextFrame jextFrame = (JextFrame)dawnParser.getProperty("JEXT.JEXT_FRAME");
        String string = dawnParser.popString();
        Console console = jextFrame.getConsole();
        console.addHistory(string);
        console.setText(string);
        console.execute(string);
    }
}

