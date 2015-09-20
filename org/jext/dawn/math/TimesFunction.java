/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.math;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class TimesFunction
extends Function {
    public TimesFunction() {
        super("*");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        dawnParser.pushNumber(dawnParser.popNumber() * dawnParser.popNumber());
    }
}

