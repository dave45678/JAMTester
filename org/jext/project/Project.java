/*
 * Decompiled with CFR 0_102.
 */
package org.jext.project;

import java.io.File;

public interface Project {
    public String getName();

    public File[] getFiles();

    public void openFile(File var1);

    public void closeFile(File var1);

    public void selectFile(File var1);

    public File getSelectedFile();

    public Object getAttribute(String var1);

    public Object getAttribute(String var1, Object var2);

    public String getAttributeAsString(String var1);

    public void setAttribute(String var1, Object var2);
}

