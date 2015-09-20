/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.io;

import java.io.PrintStream;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class PrintlnFunction
extends Function {
    public PrintlnFunction() {
        super("println");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        dawnParser.out.println(dawnParser.popString());
    }
}

