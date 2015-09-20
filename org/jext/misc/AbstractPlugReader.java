/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.io.Reader;
import org.jext.misc.PluginDesc;

public interface AbstractPlugReader {
    public boolean loadXml(Reader var1);

    public PluginDesc[] getPlugList();
}

