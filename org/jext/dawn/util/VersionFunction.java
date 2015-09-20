/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class VersionFunction
extends Function {
    public VersionFunction() {
        super("version");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.pushString("Dawn v1.1.1 final [$12:12:55 07/08/00]");
    }
}

