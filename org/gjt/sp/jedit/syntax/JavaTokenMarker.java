/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import org.gjt.sp.jedit.syntax.CTokenMarker;
import org.gjt.sp.jedit.syntax.KeywordMap;

public class JavaTokenMarker
extends CTokenMarker {
    private static KeywordMap javaKeywords;

    public JavaTokenMarker() {
        super(false, true, JavaTokenMarker.getKeywords());
    }

    public static KeywordMap getKeywords() {
        if (javaKeywords == null) {
            javaKeywords = new KeywordMap(false);
            javaKeywords.add("package", 7);
            javaKeywords.add("import", 7);
            javaKeywords.add("byte", 8);
            javaKeywords.add("char", 8);
            javaKeywords.add("short", 8);
            javaKeywords.add("int", 8);
            javaKeywords.add("long", 8);
            javaKeywords.add("float", 8);
            javaKeywords.add("double", 8);
            javaKeywords.add("boolean", 8);
            javaKeywords.add("void", 8);
            javaKeywords.add("class", 8);
            javaKeywords.add("interface", 8);
            javaKeywords.add("abstract", 6);
            javaKeywords.add("assert", 6);
            javaKeywords.add("final", 6);
            javaKeywords.add("strictfp", 6);
            javaKeywords.add("private", 6);
            javaKeywords.add("protected", 6);
            javaKeywords.add("public", 6);
            javaKeywords.add("static", 6);
            javaKeywords.add("synchronized", 6);
            javaKeywords.add("native", 6);
            javaKeywords.add("volatile", 6);
            javaKeywords.add("transient", 6);
            javaKeywords.add("break", 6);
            javaKeywords.add("case", 6);
            javaKeywords.add("continue", 6);
            javaKeywords.add("default", 6);
            javaKeywords.add("do", 6);
            javaKeywords.add("else", 6);
            javaKeywords.add("for", 6);
            javaKeywords.add("if", 6);
            javaKeywords.add("instanceof", 6);
            javaKeywords.add("new", 6);
            javaKeywords.add("return", 6);
            javaKeywords.add("switch", 6);
            javaKeywords.add("while", 6);
            javaKeywords.add("throw", 6);
            javaKeywords.add("try", 6);
            javaKeywords.add("catch", 6);
            javaKeywords.add("extends", 6);
            javaKeywords.add("finally", 6);
            javaKeywords.add("implements", 6);
            javaKeywords.add("throws", 6);
            javaKeywords.add("this", 4);
            javaKeywords.add("null", 4);
            javaKeywords.add("super", 4);
            javaKeywords.add("true", 4);
            javaKeywords.add("false", 4);
        }
        return javaKeywords;
    }
}

