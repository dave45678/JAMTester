/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import java.util.Date;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class DateFunction
extends Function {
    public DateFunction() {
        super("date");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.pushString(new Date().toString());
    }
}

