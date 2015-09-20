/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class DropnFunction
extends Function {
    public DropnFunction() {
        super("dropn");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        int n = (int)dawnParser.popNumber();
        dawnParser.checkArgsNumber(this, n);
        for (int i = 0; i < n; ++i) {
            dawnParser.pop();
        }
    }
}

