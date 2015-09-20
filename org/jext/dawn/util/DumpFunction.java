/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import org.jext.JextFrame;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.scripting.dawn.DawnLogWindow;

public class DumpFunction
extends Function {
    public DumpFunction() {
        super("dump");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        ((JextFrame)dawnParser.getProperty("JEXT.JEXT_FRAME")).getDawnLogWindow().logln(dawnParser.dump());
    }
}

