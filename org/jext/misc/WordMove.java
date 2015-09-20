/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import org.jext.Jext;

public class WordMove {
    private static final String ENABLED_PROPERTY = "editor.wordmove.go_over_space";

    public static boolean isEnabled() {
        return Jext.getBooleanProperty("editor.wordmove.go_over_space");
    }

    public static boolean isSpaceAfter() {
        return WordMove.isEnabled();
    }

    public static boolean isSpaceBefore() {
        return false;
    }

    private WordMove() {
    }
}

