/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.math;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class PowerFunction
extends Function {
    public PowerFunction() {
        super("^");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        double d = dawnParser.popNumber();
        double d2 = dawnParser.popNumber();
        dawnParser.pushNumber(Math.pow(d2, d));
    }
}

