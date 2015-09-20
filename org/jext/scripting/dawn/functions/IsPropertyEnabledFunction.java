/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import org.jext.Jext;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class IsPropertyEnabledFunction
extends Function {
    public IsPropertyEnabledFunction() {
        super("isPropertyEnabled");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        dawnParser.pushNumber(Jext.getBooleanProperty(dawnParser.popString()) ? 1.0 : 0.0);
    }
}

