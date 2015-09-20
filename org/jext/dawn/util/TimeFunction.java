/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class TimeFunction
extends Function {
    public TimeFunction() {
        super("time");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.pushNumber(System.currentTimeMillis());
    }
}

