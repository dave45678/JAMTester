/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.string;

import java.util.StringTokenizer;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class TokenizeDelimFunction
extends Function {
    public TokenizeDelimFunction() {
        super("tokenized");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        String string = dawnParser.popString();
        StringTokenizer stringTokenizer = new StringTokenizer(dawnParser.popString(), string);
        int n = stringTokenizer.countTokens();
        while (stringTokenizer.hasMoreTokens()) {
            dawnParser.pushString(stringTokenizer.nextToken());
        }
        dawnParser.pushNumber(n);
    }
}

