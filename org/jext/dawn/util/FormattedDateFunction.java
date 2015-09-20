/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class FormattedDateFunction
extends Function {
    public FormattedDateFunction() {
        super("fdate");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        dawnParser.pushString(new SimpleDateFormat(dawnParser.popString()).format(new Date()));
    }
}

