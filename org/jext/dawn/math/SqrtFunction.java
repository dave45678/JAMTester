/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.math;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class SqrtFunction
extends Function {
    public SqrtFunction() {
        super("sqrt");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        dawnParser.pushNumber(Math.sqrt(dawnParser.popNumber()));
    }
}

