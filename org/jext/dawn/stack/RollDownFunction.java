/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import java.util.Stack;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class RollDownFunction
extends Function {
    public RollDownFunction() {
        super("rolld");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        int n;
        dawnParser.checkEmpty(this);
        int n2 = (int)dawnParser.popNumber();
        if (n2 == 0) {
            return;
        }
        dawnParser.checkLevel(this, n2 - 1);
        Stack stack = dawnParser.getStack();
        Object[] arrobject = new Object[n2];
        arrobject[0] = stack.lastElement();
        for (n = 1; n < n2; ++n) {
            arrobject[n] = stack.elementAt(stack.size() - n2 + n - 1);
        }
        for (n = 0; n < n2; ++n) {
            stack.setElementAt(arrobject[n], stack.size() - n2 + n);
        }
    }
}

