/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import org.gjt.sp.jedit.syntax.CTokenMarker;
import org.gjt.sp.jedit.syntax.KeywordMap;

public class BeanShellTokenMarker
extends CTokenMarker {
    private static KeywordMap bshKeywords;

    public BeanShellTokenMarker() {
        super(false, false, BeanShellTokenMarker.getKeywords());
    }

    public static KeywordMap getKeywords() {
        if (bshKeywords == null) {
            bshKeywords = new KeywordMap(false);
            bshKeywords.add("import", 7);
            bshKeywords.add("byte", 8);
            bshKeywords.add("char", 8);
            bshKeywords.add("short", 8);
            bshKeywords.add("int", 8);
            bshKeywords.add("long", 8);
            bshKeywords.add("float", 8);
            bshKeywords.add("double", 8);
            bshKeywords.add("boolean", 8);
            bshKeywords.add("void", 8);
            bshKeywords.add("break", 6);
            bshKeywords.add("case", 6);
            bshKeywords.add("continue", 6);
            bshKeywords.add("default", 6);
            bshKeywords.add("do", 6);
            bshKeywords.add("else", 6);
            bshKeywords.add("for", 6);
            bshKeywords.add("if", 6);
            bshKeywords.add("instanceof", 6);
            bshKeywords.add("new", 6);
            bshKeywords.add("return", 6);
            bshKeywords.add("switch", 6);
            bshKeywords.add("while", 6);
            bshKeywords.add("throw", 6);
            bshKeywords.add("try", 6);
            bshKeywords.add("catch", 6);
            bshKeywords.add("finally", 6);
            bshKeywords.add("this", 4);
            bshKeywords.add("null", 4);
            bshKeywords.add("true", 4);
            bshKeywords.add("false", 4);
        }
        return bshKeywords;
    }
}

