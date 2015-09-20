/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import java.io.OutputStream;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class ExecFunction
extends Function {
    public ExecFunction() {
        super("exec");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = dawnParser.popString();
        try {
            Process process = Runtime.getRuntime().exec(string);
            process.getOutputStream().close();
            dawnParser.pushNumber(process.waitFor());
        }
        catch (Exception var3_4) {
            throw new DawnRuntimeException(this, dawnParser, "error occured attempting to execute command: " + string + "\n:" + var3_4.getMessage());
        }
    }
}

