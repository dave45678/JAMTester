/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import java.util.Stack;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class ReverseFunction
extends Function {
    public ReverseFunction() {
        super("rev");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        int n;
        dawnParser.checkEmpty(this);
        Stack stack = dawnParser.getStack();
        Object[] arrobject = new Object[stack.size()];
        for (n = 0; n < arrobject.length; ++n) {
            arrobject[n] = stack.elementAt(arrobject.length - 1 - n);
        }
        stack.removeAllElements();
        for (n = 0; n < arrobject.length; ++n) {
            stack.addElement(arrobject[n]);
        }
    }
}

