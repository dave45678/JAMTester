/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import org.jext.Jext;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class GetPropertyFunction
extends Function {
    public GetPropertyFunction() {
        super("getProperty");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        dawnParser.pushString(Jext.getProperty(dawnParser.popString()));
    }
}

