/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import java.util.Stack;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class DupnFunction
extends Function {
    public DupnFunction() {
        super("dupn");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        int n = (int)dawnParser.popNumber();
        dawnParser.checkArgsNumber(this, n);
        Stack stack = dawnParser.getStack();
        for (int i = 0; i < n; ++i) {
            stack.push(stack.elementAt(stack.size() - n));
        }
    }
}

