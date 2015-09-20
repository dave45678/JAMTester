/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.naming;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class ToLiteralFunction
extends Function {
    public ToLiteralFunction() {
        super("->lit");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        if (!dawnParser.isTopString()) {
            throw new DawnRuntimeException(this, dawnParser, "topmost stack element is not a string");
        }
        dawnParser.push(dawnParser.popString());
    }
}

