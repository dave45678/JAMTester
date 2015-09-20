/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  gnu.regexp.RE
 */
package org.jext;

import gnu.regexp.RE;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.jext.Jext;
import org.jext.Mode;
import org.jext.Utilities;

public class ModeFileFilter
extends FileFilter {
    private RE regexp;
    private String modeName;
    private String description;

    public ModeFileFilter(Mode mode) {
        this.description = mode.getUserModeName();
        String string = Jext.getProperty("file.filters");
        if (!this.description.endsWith(string)) {
            this.description = this.description + string;
        }
        try {
            this.modeName = mode.getModeName();
            String string2 = Jext.getProperty("mode." + this.modeName + ".fileFilter");
            if (string2 != null) {
                this.regexp = new RE((Object)Utilities.globToRE(string2), 2);
            }
        }
        catch (Exception var3_4) {
            // empty catch block
        }
    }

    public String getModeName() {
        return this.modeName;
    }

    public boolean accept(File file) {
        if (file != null) {
            if (file.isDirectory() || this.regexp == null) {
                return true;
            }
            String string = new String();
            int n = file.getPath().lastIndexOf(File.separatorChar);
            if (n != -1) {
                string = file.getPath().substring(n + 1);
            }
            try {
                return this.regexp.isMatch((Object)string);
            }
            catch (Exception var4_4) {
                // empty catch block
            }
        }
        return false;
    }

    public String getDescription() {
        return this.description;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.regexp = null;
        this.modeName = null;
        this.description = null;
    }
}

