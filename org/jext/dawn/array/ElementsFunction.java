/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.array;

import java.util.Vector;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class ElementsFunction
extends Function {
    public ElementsFunction() {
        super("elements");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        int n = (int)dawnParser.popNumber();
        Vector vector = dawnParser.popArray();
        dawnParser.checkArgsNumber(this, n);
        for (int i = 0; i < n; ++i) {
            vector.addElement(dawnParser.pop());
        }
        dawnParser.pushArray(vector);
    }
}

