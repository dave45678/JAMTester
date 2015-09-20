/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import java.util.Stack;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class PickFunction
extends Function {
    public PickFunction() {
        super("pick");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        int n = (int)dawnParser.popNumber();
        dawnParser.checkArgsNumber(this, n);
        Stack stack = dawnParser.getStack();
        stack.push(stack.elementAt(stack.size() - 1 - n));
    }
}

