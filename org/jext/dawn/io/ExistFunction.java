/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.io;

import java.io.File;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.DawnUtilities;
import org.jext.dawn.Function;

public class ExistFunction
extends Function {
    public ExistFunction() {
        super("exists");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        dawnParser.pushNumber(new File(DawnUtilities.constructPath(dawnParser.popString())).exists() ? 1.0 : 0.0);
    }
}

