/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class EvalFunction
extends Function {
    public EvalFunction() {
        super("eval");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = dawnParser.popString();
        try {
            Function function = dawnParser.createOnFlyFunction(string);
            function.invoke(dawnParser);
        }
        catch (DawnRuntimeException var3_4) {
            throw new DawnRuntimeException(this, dawnParser, "code snippet contains an error:" + var3_4.getMessage());
        }
    }
}

