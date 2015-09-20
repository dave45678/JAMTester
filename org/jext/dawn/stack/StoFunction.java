/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class StoFunction
extends Function {
    public StoFunction() {
        super("sto");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        String string = dawnParser.popString();
        dawnParser.checkVarName(this, string);
        DawnParser.setGlobalVariable(string, dawnParser.pop());
    }
}

