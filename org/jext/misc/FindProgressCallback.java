/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.io.File;
import org.jext.misc.FindFilter;

interface FindProgressCallback {
    public boolean reportProgress(FindFilter var1, File var2, long var3, long var5);
}

