/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.DawnUtilities;
import org.jext.dawn.Function;

public class ConstructPathFunction
extends Function {
    public ConstructPathFunction() {
        super("constructPath");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = dawnParser.popString();
        String string2 = DawnUtilities.constructPath(string);
        dawnParser.pushString(string2 != null ? string2 : string);
    }
}

