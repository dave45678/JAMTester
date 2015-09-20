/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.array;

import java.util.Vector;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class RemoveElementAtFunction
extends Function {
    public RemoveElementAtFunction() {
        super("removeElementAt");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        int n = (int)dawnParser.popNumber();
        try {
            dawnParser.peekArray().removeElementAt(n);
        }
        catch (ArrayIndexOutOfBoundsException var3_3) {
            throw new DawnRuntimeException(this, dawnParser, "array index " + n + " out of bounds");
        }
    }
}

