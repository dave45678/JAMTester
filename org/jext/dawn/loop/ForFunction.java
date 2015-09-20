/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.loop;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Hashtable;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.DawnUtilities;
import org.jext.dawn.Function;

public class ForFunction
extends Function {
    public ForFunction() {
        super("for");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        int n;
        dawnParser.checkArgsNumber(this, 3);
        String string = dawnParser.popString();
        if (string.equals("needs") || string.equals("needsGlobal")) {
            throw new DawnRuntimeException(this, dawnParser, "you cannot use reserved keyword'needs' or 'needsGlobal'");
        }
        boolean bl = false;
        for (n = 0; n < string.length(); ++n) {
            if (Character.isDigit(string.charAt(n)) && !bl) {
                throw new DawnRuntimeException(this, dawnParser, "bad for-loop counter identifier:" + string);
            }
            bl = true;
        }
        if (dawnParser.getVariables().get(string) != null) {
            throw new DawnRuntimeException(this, dawnParser, "for-loop counter identifier already exists");
        }
        n = (int)dawnParser.popNumber();
        int n2 = (int)dawnParser.popNumber();
        int n3 = 0;
        StreamTokenizer streamTokenizer = dawnParser.getStream();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            block11 : do {
                switch (streamTokenizer.nextToken()) {
                    case 10: {
                        stringBuffer.append('\n');
                        break;
                    }
                    case -1: {
                        throw new DawnRuntimeException(this, dawnParser, "for without next");
                    }
                    case -3: {
                        if (streamTokenizer.sval.equals("for")) {
                            ++n3;
                        } else if (streamTokenizer.sval.equals("next")) {
                            if (n3 <= 0) break block11;
                            --n3;
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
            String string2 = stringBuffer.toString();
            Function function = dawnParser.createOnFlyFunction(string2);
            if (n2 <= n) {
                for (int i = n2; i < n; ++i) {
                    dawnParser.getVariables().put(string, new Double(i));
                    function.invoke(dawnParser);
                    dawnParser.getVariables().remove(string);
                }
                return;
            } else {
                for (int i = n2 - 1; i >= n; --i) {
                    dawnParser.getVariables().put(string, new Double(i));
                    function.invoke(dawnParser);
                    dawnParser.getVariables().remove(string);
                }
            }
            return;
        }
        catch (IOException var9_10) {
            throw new DawnRuntimeException(this, dawnParser, "unexpected error occured during parsing");
        }
    }
}

