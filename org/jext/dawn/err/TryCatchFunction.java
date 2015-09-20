/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.err;

import java.io.IOException;
import java.io.StreamTokenizer;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.DawnUtilities;
import org.jext.dawn.Function;
import org.jext.dawn.err.ErrManager;

public class TryCatchFunction
extends Function {
    public TryCatchFunction() {
        super("try");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        StreamTokenizer streamTokenizer = dawnParser.getStream();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            int n = 0;
            block20 : do {
                switch (streamTokenizer.nextToken()) {
                    case 10: {
                        ++dawnParser.lineno;
                        break;
                    }
                    case -1: {
                        throw new DawnRuntimeException(this, dawnParser, "try without catch");
                    }
                    case -3: {
                        if (streamTokenizer.sval.equals("try")) {
                            ++n;
                        } else if (streamTokenizer.sval.equals("err")) {
                            if (n > 0) {
                                --n;
                            }
                        } else if (streamTokenizer.sval.equals("catch") && n == 0) break block20;
                        stringBuffer.append("" + ' ' + streamTokenizer.sval);
                        break;
                    }
                    case 34: 
                    case 39: {
                        stringBuffer.append(" \"" + DawnUtilities.unescape(streamTokenizer.sval) + "\"");
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
            Function function = dawnParser.createOnFlyFunction(stringBuffer.toString());
            Function function2 = null;
            stringBuffer = new StringBuffer();
            block21 : do {
                switch (streamTokenizer.nextToken()) {
                    case 10: {
                        stringBuffer.append('\n');
                        break;
                    }
                    case -1: {
                        throw new DawnRuntimeException(this, dawnParser, "catch without err");
                    }
                    case -3: {
                        if (streamTokenizer.sval.equals("err")) break block21;
                        stringBuffer.append("" + ' ' + streamTokenizer.sval);
                        break;
                    }
                    case 34: 
                    case 39: {
                        stringBuffer.append(" \"" + DawnUtilities.unescape(streamTokenizer.sval) + "\"");
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
            function2 = dawnParser.createOnFlyFunction(stringBuffer.toString());
            try {
                function.invoke(dawnParser);
                return;
            }
            catch (DawnRuntimeException var7_8) {
                ErrManager.setErr(dawnParser, var7_8);
                function2.invoke(dawnParser);
                dawnParser.setStream(streamTokenizer);
                return;
            }
        }
        catch (IOException var4_5) {
            throw new DawnRuntimeException(this, dawnParser, "unexpected error occured during parsing");
        }
    }
}

