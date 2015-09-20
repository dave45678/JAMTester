/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.array;

import java.util.Vector;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class AddElementFunction
extends Function {
    public AddElementFunction() {
        super("addElement");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        Object object = dawnParser.pop();
        dawnParser.peekArray().addElement(object);
    }
}

