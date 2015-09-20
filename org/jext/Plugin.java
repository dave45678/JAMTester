/*
 * Decompiled with CFR 0_102.
 */
package org.jext;

import java.util.Vector;
import org.jext.JextFrame;
import org.jext.options.OptionsDialog;

public interface Plugin {
    public void createMenuItems(JextFrame var1, Vector var2, Vector var3) throws Throwable;

    public void createOptionPanes(OptionsDialog var1) throws Throwable;

    public void start() throws Throwable;

    public void stop() throws Throwable;
}

