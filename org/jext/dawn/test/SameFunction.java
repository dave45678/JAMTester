/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.test;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class SameFunction
extends Function {
    public SameFunction() {
        super("same");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        Object object = dawnParser.pop();
        Object object2 = dawnParser.pop();
        if (object2 == null) {
            throw new DawnRuntimeException(this, dawnParser, "null object");
        }
        dawnParser.pushNumber(object2.equals(object) ? 1.0 : 0.0);
    }
}

