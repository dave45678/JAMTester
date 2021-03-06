/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class NDupFunction
extends Function {
    public NDupFunction() {
        super("ndup");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        int n = (int)dawnParser.popNumber();
        for (int i = 0; i < n; ++i) {
            dawnParser.push(dawnParser.peek());
        }
    }
}

