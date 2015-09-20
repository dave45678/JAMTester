/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.stack;

import java.util.Vector;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class RclFunction
extends Function {
    public RclFunction() {
        super("rcl");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = dawnParser.popString();
        dawnParser.checkVarName(this, string);
        Object object = dawnParser.getVariable(string);
        if (object == null) {
            throw new DawnRuntimeException(this, dawnParser, "unknown variable:" + string);
        }
        if (object instanceof Double) {
            dawnParser.pushNumber((Double)object);
        } else if (object instanceof Vector) {
            dawnParser.pushArray((Vector)object);
        } else if (object instanceof String) {
            String string2 = object.toString();
            if (string2.length() == 0) {
                string2 = "";
            } else if (string2.startsWith("\"") && string2.endsWith("\"")) {
                string2 = string2.substring(1, string2.length() - 1);
            }
            dawnParser.pushString(string2);
        } else {
            dawnParser.pushString(object.toString());
        }
    }
}

