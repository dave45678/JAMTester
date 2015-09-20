/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.string;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class NumFunction
extends Function {
    public NumFunction() {
        super("num");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = dawnParser.popString();
        for (int i = 0; i < string.length(); ++i) {
            dawnParser.pushNumber(string.charAt(i));
        }
    }
}

