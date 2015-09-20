/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class DupFunction
extends Function {
    public DupFunction() {
        super("dup");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        dawnParser.push(dawnParser.peek());
    }
}

