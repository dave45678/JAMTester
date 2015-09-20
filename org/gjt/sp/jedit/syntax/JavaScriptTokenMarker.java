/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import org.gjt.sp.jedit.syntax.CTokenMarker;
import org.gjt.sp.jedit.syntax.KeywordMap;

public class JavaScriptTokenMarker
extends CTokenMarker {
    private static KeywordMap javaScriptKeywords;

    public JavaScriptTokenMarker() {
        super(false, false, JavaScriptTokenMarker.getKeywords());
    }

    public static KeywordMap getKeywords() {
        if (javaScriptKeywords == null) {
            javaScriptKeywords = new KeywordMap(false);
            javaScriptKeywords.add("function", 8);
            javaScriptKeywords.add("var", 8);
            javaScriptKeywords.add("else", 6);
            javaScriptKeywords.add("for", 6);
            javaScriptKeywords.add("if", 6);
            javaScriptKeywords.add("in", 6);
            javaScriptKeywords.add("new", 6);
            javaScriptKeywords.add("return", 6);
            javaScriptKeywords.add("while", 6);
            javaScriptKeywords.add("with", 6);
            javaScriptKeywords.add("break", 6);
            javaScriptKeywords.add("case", 6);
            javaScriptKeywords.add("continue", 6);
            javaScriptKeywords.add("default", 6);
            javaScriptKeywords.add("false", 5);
            javaScriptKeywords.add("this", 5);
            javaScriptKeywords.add("true", 5);
        }
        return javaScriptKeywords;
    }
}

