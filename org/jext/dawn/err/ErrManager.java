/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.err;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;

public class ErrManager {
    public static DawnRuntimeException getErr(DawnParser dawnParser) {
        return (DawnRuntimeException)dawnParser.getProperty("DAWN.ERR");
    }

    public static void setErr(DawnParser dawnParser, DawnRuntimeException dawnRuntimeException) {
        dawnParser.setProperty("DAWN.ERR", dawnRuntimeException);
    }
}

