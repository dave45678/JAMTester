/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.test;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class NotFunction
extends Function {
    public NotFunction() {
        super("not");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        double d = dawnParser.popNumber();
        dawnParser.pushNumber(d >= 1.0 ? 0.0 : 1.0);
    }
}

