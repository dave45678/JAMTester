/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.io;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.dawn.io.FileManager;

public class IsAvailableFunction
extends Function {
    public IsAvailableFunction() {
        super("isFileAvailable");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        dawnParser.pushNumber(FileManager.isFileAvailable(dawnParser.popString(), dawnParser) ? 1.0 : 0.0);
    }
}

