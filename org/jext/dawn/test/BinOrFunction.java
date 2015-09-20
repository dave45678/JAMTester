/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.test;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class BinOrFunction
extends Function {
    public BinOrFunction() {
        super("|");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        int n = (int)dawnParser.popNumber();
        int n2 = (int)dawnParser.popNumber();
        dawnParser.pushNumber(n2 | n);
    }
}

