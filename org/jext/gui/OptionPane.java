/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Component;

public interface OptionPane {
    public String getName();

    public Component getComponent();

    public void save();

    public boolean isCacheable();

    public void load();
}

