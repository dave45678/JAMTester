/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.string;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class SubFunction
extends Function {
    public SubFunction() {
        super("sub");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 3);
        int n = (int)dawnParser.popNumber();
        int n2 = (int)dawnParser.popNumber();
        String string = dawnParser.popString();
        if (n2 < 0 || n2 > string.length()) {
            throw new DawnRuntimeException(this, dawnParser, "start index [" + n2 + "] out of bounds");
        }
        if (n < 0 || n > string.length()) {
            throw new DawnRuntimeException(this, dawnParser, "end index [" + n + "] out of bounds");
        }
        if (n < n2) {
            throw new DawnRuntimeException(this, dawnParser, "end index must be greater than/equals to start index");
        }
        dawnParser.pushString(string.substring(n2, n));
    }
}

