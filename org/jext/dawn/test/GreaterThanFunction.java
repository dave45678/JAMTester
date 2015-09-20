/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.test;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class GreaterThanFunction
extends Function {
    public GreaterThanFunction() {
        super(">");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        double d = dawnParser.popNumber();
        double d2 = dawnParser.popNumber();
        dawnParser.pushNumber(d2 > d ? 1.0 : 0.0);
    }
}

