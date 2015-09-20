/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.io;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.dawn.io.FileManager;

public class OpenOutputFunction
extends Function {
    public OpenOutputFunction() {
        super("openForOutput");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        FileManager.openFileForOutput(dawnParser.popString(), dawnParser.popString(), this, dawnParser);
    }
}

