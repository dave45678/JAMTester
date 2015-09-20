/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class SwapFunction
extends Function {
    public SwapFunction() {
        super("swap");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        Object object = dawnParser.pop();
        Object object2 = dawnParser.pop();
        dawnParser.push(object);
        dawnParser.push(object2);
    }
}

