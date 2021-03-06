/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.string;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class TailFunction
extends Function {
    public TailFunction() {
        super("tail");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = dawnParser.popString();
        if (string.length() != 0) {
            dawnParser.pushString(string.substring(1, string.length()));
        }
    }
}

