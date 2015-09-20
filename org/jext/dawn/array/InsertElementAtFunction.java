/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.array;

import java.util.Vector;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class InsertElementAtFunction
extends Function {
    public InsertElementAtFunction() {
        super("insertElementAt");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 3);
        int n = (int)dawnParser.popNumber();
        Object object = dawnParser.pop();
        try {
            dawnParser.peekArray().insertElementAt(object, n);
        }
        catch (ArrayIndexOutOfBoundsException var4_4) {
            throw new DawnRuntimeException(this, dawnParser, "array index " + n + " out of bounds");
        }
    }
}

