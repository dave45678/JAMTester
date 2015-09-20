/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import org.gjt.sp.jedit.syntax.CTokenMarker;
import org.gjt.sp.jedit.syntax.KeywordMap;

public class PikeTokenMarker
extends CTokenMarker {
    private static KeywordMap pikeKeywords;

    public PikeTokenMarker() {
        super(true, false, PikeTokenMarker.getKeywords());
    }

    public static KeywordMap getKeywords() {
        if (pikeKeywords == null) {
            pikeKeywords = new KeywordMap(false);
            pikeKeywords.add("array", 8);
            pikeKeywords.add("break", 6);
            pikeKeywords.add("case", 6);
            pikeKeywords.add("catch", 6);
            pikeKeywords.add("continue", 6);
            pikeKeywords.add("default", 6);
            pikeKeywords.add("do", 6);
            pikeKeywords.add("else", 6);
            pikeKeywords.add("float", 8);
            pikeKeywords.add("for", 6);
            pikeKeywords.add("foreach", 6);
            pikeKeywords.add("function", 6);
            pikeKeywords.add("gauge", 6);
            pikeKeywords.add("if", 6);
            pikeKeywords.add("inherit", 6);
            pikeKeywords.add("inline", 6);
            pikeKeywords.add("int", 8);
            pikeKeywords.add("lambda", 6);
            pikeKeywords.add("mapping", 6);
            pikeKeywords.add("mixed", 8);
            pikeKeywords.add("multiset", 6);
            pikeKeywords.add("nomask", 6);
            pikeKeywords.add("object", 8);
            pikeKeywords.add("predef", 6);
            pikeKeywords.add("private", 6);
            pikeKeywords.add("program", 6);
            pikeKeywords.add("protected", 6);
            pikeKeywords.add("public", 6);
            pikeKeywords.add("return", 6);
            pikeKeywords.add("sscanf", 6);
            pikeKeywords.add("static", 6);
            pikeKeywords.add("string", 8);
            pikeKeywords.add("switch", 6);
            pikeKeywords.add("typeof", 6);
            pikeKeywords.add("varargs", 6);
            pikeKeywords.add("void", 8);
            pikeKeywords.add("while", 6);
        }
        return pikeKeywords;
    }
}

