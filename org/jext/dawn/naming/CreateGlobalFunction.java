/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.naming;

import java.io.IOException;
import java.io.StreamTokenizer;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;

public class CreateGlobalFunction
extends Function {
    public CreateGlobalFunction() {
        super("global");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = dawnParser.popString();
        dawnParser.checkVarName(this, string);
        StreamTokenizer streamTokenizer = dawnParser.getStream();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            int n = 0;
            block10 : do {
                switch (streamTokenizer.nextToken()) {
                    case 10: {
                        stringBuffer.append('\n');
                        break;
                    }
                    case -1: {
                        throw new DawnRuntimeException(this, dawnParser, "global without endGlobal");
                    }
                    case -3: {
                        if (streamTokenizer.sval.equals("global")) {
                            ++n;
                        } else if (streamTokenizer.sval.equals("endGlobal")) {
                            if (n <= 0) break block10;
                            --n;
                        }
                        stringBuffer.append("" + ' ' + streamTokenizer.sval);
                        break;
                    }
                    case 34: 
                    case 39: {
                        stringBuffer.append(" \"" + streamTokenizer.sval + "\"");
                        break;
                    }
                    case 45: {
                        stringBuffer.append(" -");
                        break;
                    }
                    case -2: {
                        stringBuffer.append(" " + streamTokenizer.nval);
                    }
                }
            } while (true);
            DawnParser.createGlobalFunction(string, stringBuffer.toString());
            return;
        }
        catch (IOException var5_6) {
            throw new DawnRuntimeException(this, dawnParser, "unexpected error occured during parsing");
        }
    }
}

