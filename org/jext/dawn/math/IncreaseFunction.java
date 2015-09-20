/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.math;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class IncreaseFunction
extends Function {
    public IncreaseFunction() {
        super("++");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        if (dawnParser.isTopNumeric()) {
            dawnParser.pushNumber(dawnParser.popNumber() + 1.0);
        } else {
            String string = dawnParser.popString();
            dawnParser.checkVarName(this, string);
            Object object = dawnParser.getVariable(string);
            if (object instanceof Double) {
                double d = (Double)object;
                dawnParser.setVariable(string, new Double(d + 1.0));
            } else {
                throw new DawnRuntimeException(this, dawnParser, "variable " + string + " does not contains a numeric value");
            }
        }
    }
}

