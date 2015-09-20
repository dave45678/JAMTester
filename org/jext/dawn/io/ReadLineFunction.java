/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.io;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.dawn.io.FileManager;

public class ReadLineFunction
extends Function {
    public ReadLineFunction() {
        super("readLine");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = FileManager.readLine(dawnParser.popString(), this, dawnParser);
        if (string != null) {
            dawnParser.pushString(string);
        }
    }
}

