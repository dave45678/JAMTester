/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.math;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class TanFunction
extends Function {
    public TanFunction() {
        super("tan");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.pushNumber(Math.tan(dawnParser.popNumber()));
    }
}

