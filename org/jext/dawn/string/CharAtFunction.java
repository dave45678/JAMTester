/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.string;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class CharAtFunction
extends Function {
    public CharAtFunction() {
        super("charAt");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        int n = (int)dawnParser.popNumber();
        String string = dawnParser.popString();
        if (n < 0 || n > string.length()) {
            throw new DawnRuntimeException(this, dawnParser, "index out of bounds");
        }
        dawnParser.pushString("" + string.charAt(n));
    }
}

