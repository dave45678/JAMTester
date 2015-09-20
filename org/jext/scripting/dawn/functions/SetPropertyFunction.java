/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import org.jext.Jext;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class SetPropertyFunction
extends Function {
    public SetPropertyFunction() {
        super("setProperty");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        String string = dawnParser.popString();
        Jext.setProperty(dawnParser.popString(), string);
    }
}

