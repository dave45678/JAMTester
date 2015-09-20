/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

public abstract class Skin {
    public abstract void apply() throws Throwable;

    public abstract String getSkinName();

    public abstract String getSkinInternName();

    public boolean isAvailable() {
        return true;
    }

    public void unapply() throws Throwable {
    }
}

