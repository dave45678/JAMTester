/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  junit.runner.TestCaseClassLoader
 */
package jamtester;

import junit.runner.TestCaseClassLoader;

public class JAMTCClassLoader
extends TestCaseClassLoader {
    private ClassLoader parent;

    public JAMTCClassLoader(String string) {
        this(string, ClassLoader.getSystemClassLoader());
    }

    public JAMTCClassLoader(String string, ClassLoader classLoader) {
        super(string);
        this.parent = classLoader;
    }

    public Class loadClass(String string, boolean bl) throws ClassNotFoundException {
        Class class_ = null;
        try {
            class_ = super.loadClass(string, bl);
        }
        catch (Exception var4_4) {
            this.parent.loadClass(string);
        }
        return class_;
    }
}

