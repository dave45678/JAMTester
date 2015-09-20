/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.io;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.dawn.io.FileManager;

public class LineSeparatorFunction
extends Function {
    public LineSeparatorFunction() {
        super("lineSeparator");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.pushString(FileManager.NEW_LINE);
    }
}

