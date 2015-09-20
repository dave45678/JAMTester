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

public class WhileFunction
extends Function {
    public WhileFunction() {
        super("while");
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
                        throw new DawnRuntimeException(this, dawnParser, "while without repeat");
                    }
                    case -3: {
                        if (streamTokenizer.sval.equals("repeat")) break block18;
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
            function.invoke(dawnParser);
            Function function2 = null;
            int n = 0;
            int n2 = (int)dawnParser.popNumber();
            while (n2 >= 1) {
                if (function2 == null) {
                    stringBuffer = new StringBuffer();
                    block20 : do {
                        switch (streamTokenizer.nextToken()) {
                            case 10: {
                                stringBuffer.append('\n');
                                break;
                            }
                            case -1: {
                                throw new DawnRuntimeException(this, dawnParser, "while without wend");
                            }
                            case -3: {
                                if (streamTokenizer.sval.equals("while")) {
                                    ++n;
                                } else if (streamTokenizer.sval.equals("wend")) {
                                    if (n <= 0) break block20;
                                    --n;
                                }
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
                }
                function2.invoke(dawnParser);
                function.invoke(dawnParser);
                n2 = (int)dawnParser.popNumber();
            }
            return;
        }
        catch (IOException var4_5) {
            throw new DawnRuntimeException(this, dawnParser, "unexpected error occured during parsing");
        }
    }
}

