/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.string;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class EndsWithFunction
extends Function {
    public EndsWithFunction() {
        super("endsWith");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        String string = dawnParser.popString();
        String string2 = dawnParser.popString();
        dawnParser.pushNumber(string2.endsWith(string) ? 1.0 : 0.0);
    }
}

