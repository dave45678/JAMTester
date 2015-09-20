/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.io.File;
import org.jext.misc.FindProgressCallback;

interface FindFilter {
    public boolean accept(File var1, FindProgressCallback var2);
}

