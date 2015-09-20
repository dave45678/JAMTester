/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.string;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class FromStringFunction
extends Function {
    public FromStringFunction() {
        super("str->");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        try {
            dawnParser.pushNumber(new Double(dawnParser.popString()));
        }
        catch (Exception var2_2) {
            throw new DawnRuntimeException(this, dawnParser, "string argument does not contain numeric value");
        }
    }
}

