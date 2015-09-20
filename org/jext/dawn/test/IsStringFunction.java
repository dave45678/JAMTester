/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.test;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class IsStringFunction
extends Function {
    public IsStringFunction() {
        super("isString");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        double d = dawnParser.isTopString() ? 1.0 : 0.0;
        dawnParser.pop();
        dawnParser.pushNumber(d);
    }
}

