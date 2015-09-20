/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.err;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.dawn.err.ErrManager;

public class ErrMsgFunction
extends Function {
    public ErrMsgFunction() {
        super("errMsg");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        DawnRuntimeException dawnRuntimeException = ErrManager.getErr(dawnParser);
        if (dawnRuntimeException != null) {
            dawnParser.pushString(dawnRuntimeException.getMessage());
        } else {
            dawnParser.pushString("no error has occured");
        }
    }
}

