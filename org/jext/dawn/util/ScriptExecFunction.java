/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.DawnUtilities;
import org.jext.dawn.Function;

public class ScriptExecFunction
extends Function {
    public ScriptExecFunction() {
        super("run");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkEmpty(this);
        String string = dawnParser.popString();
        try {
            String string2;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(DawnUtilities.constructPath(string))));
            StringBuffer stringBuffer = new StringBuffer();
            while ((string2 = bufferedReader.readLine()) != null) {
                stringBuffer.append(string2).append('\n');
            }
            bufferedReader.close();
            DawnParser dawnParser2 = new DawnParser(new StringReader(stringBuffer.toString()));
            dawnParser2.exec();
            dawnParser.out.print("" + '\n' + dawnParser2.dump());
        }
        catch (Exception var3_4) {
            throw new DawnRuntimeException(this, dawnParser, "error occured attempting to execute script: " + string + "\n:" + var3_4.getMessage());
        }
    }
}

