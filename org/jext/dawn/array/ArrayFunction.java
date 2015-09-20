/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.array;

import java.util.Vector;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class ArrayFunction
extends Function {
    public ArrayFunction() {
        super("array");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.pushArray(new Vector());
    }
}

