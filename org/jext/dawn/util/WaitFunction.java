/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class WaitFunction
extends Function {
    public WaitFunction() {
        super("wait");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        try {
            Thread.sleep((int)dawnParser.popNumber() * 1000);
        }
        catch (InterruptedException var2_2) {
            // empty catch block
        }
    }
}

