/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.io;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.dawn.io.FileManager;

public class WriteFunction
extends Function {
    public WriteFunction() {
        super("write");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        FileManager.write(dawnParser.popString(), dawnParser.popString(), this, dawnParser);
    }
}

