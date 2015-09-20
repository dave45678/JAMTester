/*
 * Decompiled with CFR 0_102.
 */
package org.jext.dawn.math;

import org.jext.dawn.CodeSnippet;

public class InvertFunction
extends CodeSnippet {
    public String getName() {
        return "inv";
    }

    public String getCode() {
        return "1 swap /";
    }
}

