/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.javaccess;

import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class FieldFunction
extends Function {
    public FieldFunction() {
        super("field");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 2);
        String string = dawnParser.popString();
        Object object = dawnParser.pop();
        if (!(object instanceof Class)) {
            throw new DawnRuntimeException(this, dawnParser, "" + object + " is not a class");
        }
        Field field = null;
        try {
            field = ((Class)object).getField(string);
        }
        catch (NoSuchFieldException var5_5) {
            throw new DawnRuntimeException(this, dawnParser, "field " + string + " can not be found");
        }
        catch (SecurityException var5_6) {
            throw new DawnRuntimeException(this, dawnParser, "security violation");
        }
        dawnParser.push(field);
    }
}

