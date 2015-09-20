/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import org.gjt.sp.jedit.syntax.CTokenMarker;
import org.gjt.sp.jedit.syntax.KeywordMap;

public class IDLTokenMarker
extends CTokenMarker {
    private static KeywordMap idlKeywords;

    public IDLTokenMarker() {
        super(true, false, IDLTokenMarker.getKeywords());
    }

    public static KeywordMap getKeywords() {
        if (idlKeywords == null) {
            idlKeywords = new KeywordMap(false);
            idlKeywords.add("any", 8);
            idlKeywords.add("attribute", 6);
            idlKeywords.add("boolean", 8);
            idlKeywords.add("case", 6);
            idlKeywords.add("char", 8);
            idlKeywords.add("const", 6);
            idlKeywords.add("context", 6);
            idlKeywords.add("default", 6);
            idlKeywords.add("double", 8);
            idlKeywords.add("enum", 8);
            idlKeywords.add("exception", 6);
            idlKeywords.add("FALSE", 4);
            idlKeywords.add("fixed", 6);
            idlKeywords.add("float", 8);
            idlKeywords.add("in", 6);
            idlKeywords.add("inout", 6);
            idlKeywords.add("interface", 6);
            idlKeywords.add("long", 8);
            idlKeywords.add("module", 6);
            idlKeywords.add("Object", 8);
            idlKeywords.add("octet", 8);
            idlKeywords.add("oneway", 6);
            idlKeywords.add("out", 6);
            idlKeywords.add("raises", 6);
            idlKeywords.add("readonly", 6);
            idlKeywords.add("sequence", 8);
            idlKeywords.add("short", 8);
            idlKeywords.add("string", 8);
            idlKeywords.add("struct", 8);
            idlKeywords.add("switch", 6);
            idlKeywords.add("TRUE", 4);
            idlKeywords.add("typedef", 8);
            idlKeywords.add("unsigned", 8);
            idlKeywords.add("union", 8);
            idlKeywords.add("void", 8);
            idlKeywords.add("wchar", 8);
            idlKeywords.add("wstring", 8);
        }
        return idlKeywords;
    }
}

