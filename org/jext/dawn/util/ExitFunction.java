/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class ExitFunction
extends Function {
    public ExitFunction() {
        super("exit");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.stop();
    }
}

