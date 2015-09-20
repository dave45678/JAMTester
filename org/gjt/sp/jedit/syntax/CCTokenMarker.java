/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import org.gjt.sp.jedit.syntax.CTokenMarker;
import org.gjt.sp.jedit.syntax.KeywordMap;

public class CCTokenMarker
extends CTokenMarker {
    private static KeywordMap ccKeywords;

    public CCTokenMarker() {
        super(true, false, CCTokenMarker.getKeywords());
    }

    public static KeywordMap getKeywords() {
        if (ccKeywords == null) {
            ccKeywords = new KeywordMap(false);
            ccKeywords.add("and", 8);
            ccKeywords.add("and_eq", 8);
            ccKeywords.add("asm", 7);
            ccKeywords.add("auto", 6);
            ccKeywords.add("bitand", 8);
            ccKeywords.add("bitor", 8);
            ccKeywords.add("bool", 8);
            ccKeywords.add("break", 6);
            ccKeywords.add("case", 6);
            ccKeywords.add("catch", 6);
            ccKeywords.add("char", 8);
            ccKeywords.add("class", 8);
            ccKeywords.add("compl", 8);
            ccKeywords.add("const", 6);
            ccKeywords.add("const_cast", 8);
            ccKeywords.add("continue", 6);
            ccKeywords.add("default", 6);
            ccKeywords.add("delete", 6);
            ccKeywords.add("do", 6);
            ccKeywords.add("double", 8);
            ccKeywords.add("dynamic_cast", 8);
            ccKeywords.add("else", 6);
            ccKeywords.add("enum", 8);
            ccKeywords.add("explicit", 6);
            ccKeywords.add("export", 7);
            ccKeywords.add("extern", 7);
            ccKeywords.add("false", 4);
            ccKeywords.add("float", 8);
            ccKeywords.add("for", 6);
            ccKeywords.add("friend", 6);
            ccKeywords.add("goto", 6);
            ccKeywords.add("if", 6);
            ccKeywords.add("inline", 6);
            ccKeywords.add("int", 8);
            ccKeywords.add("long", 8);
            ccKeywords.add("mutable", 8);
            ccKeywords.add("namespace", 7);
            ccKeywords.add("new", 6);
            ccKeywords.add("not", 8);
            ccKeywords.add("not_eq", 8);
            ccKeywords.add("operator", 8);
            ccKeywords.add("or", 8);
            ccKeywords.add("or_eq", 8);
            ccKeywords.add("private", 6);
            ccKeywords.add("protected", 6);
            ccKeywords.add("public", 6);
            ccKeywords.add("register", 6);
            ccKeywords.add("reinterpret_cast", 8);
            ccKeywords.add("return", 6);
            ccKeywords.add("short", 8);
            ccKeywords.add("signed", 8);
            ccKeywords.add("sizeof", 6);
            ccKeywords.add("static", 6);
            ccKeywords.add("static_cast", 8);
            ccKeywords.add("struct", 8);
            ccKeywords.add("switch", 6);
            ccKeywords.add("template", 8);
            ccKeywords.add("this", 4);
            ccKeywords.add("throw", 6);
            ccKeywords.add("true", 4);
            ccKeywords.add("try", 6);
            ccKeywords.add("typedef", 8);
            ccKeywords.add("typeid", 8);
            ccKeywords.add("typename", 8);
            ccKeywords.add("union", 8);
            ccKeywords.add("unsigned", 8);
            ccKeywords.add("using", 7);
            ccKeywords.add("virtual", 6);
            ccKeywords.add("void", 6);
            ccKeywords.add("volatile", 6);
            ccKeywords.add("wchar_t", 8);
            ccKeywords.add("while", 6);
            ccKeywords.add("xor", 8);
            ccKeywords.add("xor_eq", 8);
            ccKeywords.add("NULL", 4);
        }
        return ccKeywords;
    }
}

