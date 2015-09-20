/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import java.util.Stack;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class DepthFunction
extends Function {
    public DepthFunction() {
        super("depth");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.pushNumber(dawnParser.getStack().size());
    }
}

