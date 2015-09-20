/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.io;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.dawn.io.FileManager;

public class ReadFunction
extends Function {
    public ReadFunction() {
        super("read");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = FileManager.read(dawnParser.popString(), this, dawnParser);
        if (string != null) {
            dawnParser.pushString(string);
        }
    }
}

