/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.test;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class GetTypeFunction
extends Function {
    public GetTypeFunction() {
        super("type");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        double d = dawnParser.getTopType();
        dawnParser.pop();
        dawnParser.pushNumber(d);
    }
}

