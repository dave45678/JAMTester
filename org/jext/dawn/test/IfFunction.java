/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.test;

import java.io.IOException;
import java.io.StreamTokenizer;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.DawnUtilities;
import org.jext.dawn.Function;

public class IfFunction
extends Function {
    public IfFunction() {
        super("if");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        StreamTokenizer streamTokenizer = dawnParser.getStream();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            block18 : do {
                switch (streamTokenizer.nextToken()) {
                    case 10: {
                        stringBuffer.append('\n');
                        break;
                    }
                    case -1: {
                        throw new DawnRuntimeException(this, dawnParser, "if without then");
                    }
                    case -3: {
                        if (streamTokenizer.sval.equals("then")) break block18;
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
            int n = 0;
            boolean bl = false;
            StringBuffer stringBuffer2 = new StringBuffer();
            StringBuffer stringBuffer3 = new StringBuffer();
            block19 : do {
                switch (streamTokenizer.nextToken()) {
                    case 10: {
                        (bl ? stringBuffer3 : stringBuffer2).append('\n');
                        break;
                    }
                    case -1: {
                        throw new DawnRuntimeException(this, dawnParser, "if without else or end");
                    }
                    case -3: {
                        if (streamTokenizer.sval.equals("if")) {
                            ++n;
                        } else if (streamTokenizer.sval.equals("else")) {
                            if (n == 0) {
                                bl = true;
                                break;
                            }
                        } else if (streamTokenizer.sval.equals("end")) {
                            if (n <= 0) break block19;
                            --n;
                        }
                        (bl ? stringBuffer3 : stringBuffer2).append("" + ' ' + streamTokenizer.sval);
                        break;
                    }
                    case 34: 
                    case 39: {
                        (bl ? stringBuffer3 : stringBuffer2).append(" \"" + DawnUtilities.unescape(streamTokenizer.sval) + "\"");
                        break;
                    }
                    case 45: {
                        (bl ? stringBuffer3 : stringBuffer2).append(" -");
                        break;
                    }
                    case -2: {
                        (bl ? stringBuffer3 : stringBuffer2).append(" " + streamTokenizer.nval);
                    }
                }
            } while (true);
            Function function = dawnParser.createOnFlyFunction(stringBuffer.toString());
            function.invoke(dawnParser);
            int n2 = (int)dawnParser.popNumber();
            if (n2 >= 1) {
                if (stringBuffer2.length() == 0) return;
                function = dawnParser.createOnFlyFunction(stringBuffer2.toString());
                function.invoke(dawnParser);
                return;
            }
            if (stringBuffer3.length() == 0) return;
            function = dawnParser.createOnFlyFunction(stringBuffer3.toString());
            function.invoke(dawnParser);
            return;
        }
        catch (IOException var4_5) {
            throw new DawnRuntimeException(this, dawnParser, "unexpected error occured during parsing");
        }
    }
}

