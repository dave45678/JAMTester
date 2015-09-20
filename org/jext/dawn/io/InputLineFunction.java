/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class InputLineFunction
extends Function {
    public InputLineFunction() {
        super("inputLine");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        String string;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dawnParser.in));
        try {
            string = bufferedReader.readLine();
        }
        catch (Exception var4_4) {
            throw new DawnRuntimeException(this, dawnParser, "unexpected error occured");
        }
        dawnParser.pushString(string);
    }
}

