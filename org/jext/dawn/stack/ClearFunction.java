/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import java.util.Stack;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class ClearFunction
extends Function {
    public ClearFunction() {
        super("clear");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.getStack().removeAllElements();
    }
}

