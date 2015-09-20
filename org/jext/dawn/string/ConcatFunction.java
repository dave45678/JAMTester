/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.string;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class ConcatFunction
extends Function {
    public ConcatFunction() {
        super("concat");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        String string = dawnParser.popString();
        dawnParser.pushString(dawnParser.popString().concat(string));
    }
}

