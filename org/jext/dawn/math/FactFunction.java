/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.math;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class FactFunction
extends Function {
    public FactFunction() {
        super("!");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        int n = (int)dawnParser.popNumber();
        if (n >= 0) {
            int n2 = 1;
            for (int i = 1; i <= n; ++i) {
                n2*=i;
            }
            dawnParser.pushNumber(n2);
        }
    }
}

