/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.string;

import java.util.StringTokenizer;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class TokenizeFunction
extends Function {
    public TokenizeFunction() {
        super("tokenize");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        StringTokenizer stringTokenizer = new StringTokenizer(dawnParser.popString());
        int n = stringTokenizer.countTokens();
        while (stringTokenizer.hasMoreTokens()) {
            dawnParser.pushString(stringTokenizer.nextToken());
        }
        dawnParser.pushNumber(n);
    }
}

