/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn;

import org.jext.dawn.DawnParser;
import org.jext.dawn.Function;

public class DawnRuntimeException
extends Exception {
    public DawnRuntimeException(DawnParser dawnParser, String string) {
        super("Error at line:" + dawnParser.lineno() + ':' + string);
    }

    public DawnRuntimeException(Function function, DawnParser dawnParser, String string) {
        super("Error at line:" + dawnParser.lineno() + (function == null ? ":" : new StringBuffer().append(':').append(function.getName()).append(':').toString()) + string);
    }
}

