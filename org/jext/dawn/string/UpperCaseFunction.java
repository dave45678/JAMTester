/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.string;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class UpperCaseFunction
extends Function {
    public UpperCaseFunction() {
        super("upperCase");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        dawnParser.pushString(dawnParser.popString().toUpperCase());
    }
}

