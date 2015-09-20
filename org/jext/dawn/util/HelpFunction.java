/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.util;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import org.jext.JextFrame;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.scripting.dawn.DawnLogWindow;

public class HelpFunction
extends Function {
    public HelpFunction() {
        super("help");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        Hashtable hashtable = DawnParser.getFunctions();
        Enumeration enumeration = hashtable.keys();
        Object[] arrobject = new String[hashtable.size()];
        int n = 0;
        while (enumeration.hasMoreElements()) {
            arrobject[n] = (String)enumeration.nextElement();
            ++n;
        }
        Arrays.sort(arrobject);
        StringBuffer stringBuffer = new StringBuffer(arrobject.length);
        for (int i = 0; i < arrobject.length; ++i) {
            stringBuffer.append((String)arrobject[i]).append('\n');
        }
        ((JextFrame)dawnParser.getProperty("JEXT.JEXT_FRAME")).getDawnLogWindow().logln(stringBuffer.toString());
    }
}

