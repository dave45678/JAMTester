/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.loop;

import java.io.IOException;
import java.io.StreamTokenizer;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.DawnUtilities;
import org.jext.dawn.Function;

public class DoLoopFunction
extends Function {
    public DoLoopFunction() {
        super("do");
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
            block18 : do {
                switch (streamTokenizer.nextToken()) {
                    case 10: {
                        stringBuffer.append('\n');
                        break;
                    }
                    case -1: {
                        throw new DawnRuntimeException(this, dawnParser, "do without loop");
                    }
                    case -3: {
                        if (streamTokenizer.sval.equals("do")) {
                            ++n;
                        } else if (streamTokenizer.sval.equals("until")) {
                            if (n > 0) {
                                --n;
                            }
                        } else if (streamTokenizer.sval.equals("loop") && n == 0) break block18;
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
            String string = stringBuffer.toString();
            Function function = dawnParser.createOnFlyFunction(string);
            Function function2 = null;
            int n2 = 0;
            do {
                if (function2 == null) {
                    stringBuffer = new StringBuffer();
                    block20 : do {
                        switch (streamTokenizer.nextToken()) {
                            case 10: {
                                stringBuffer.append('\n');
                                break;
                            }
                            case -1: {
                                throw new DawnRuntimeException(this, dawnParser, "loop without until");
                            }
                            case -3: {
                                if (streamTokenizer.sval.equals("until")) break block20;
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
                    string = stringBuffer.toString();
                    function2 = dawnParser.createOnFlyFunction(string);
                }
                function.invoke(dawnParser);
                function2.invoke(dawnParser);
            } while ((n2 = (int)dawnParser.popNumber()) == 0);
            return;
        }
        catch (IOException var4_5) {
            throw new DawnRuntimeException(this, dawnParser, "unexpected error occured during parsing");
        }
    }
}

