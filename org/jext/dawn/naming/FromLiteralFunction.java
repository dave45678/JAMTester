/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.naming;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class FromLiteralFunction
extends Function {
    public FromLiteralFunction() {
        super("lit->");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        if (!dawnParser.isTopLiteral()) {
            throw new DawnRuntimeException(this, dawnParser, "topmost stack element is not a literal");
        }
        dawnParser.pushString(dawnParser.popString());
    }
}

