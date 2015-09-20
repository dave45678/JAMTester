/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  junit.runner.TestCaseClassLoader
 */
package jamtester;

import jamtester.NullOutputStream;
import jamtester.javaoverride.Runtime;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import junit.runner.TestCaseClassLoader;

public class GradingToolClassLoader
extends TestCaseClassLoader
implements Serializable {
    private PrintStream out;
    private PublicURLClassLoader urlcl;
    private Map nameMap;
    private ArrayList loadedClasses;

    public GradingToolClassLoader(String string, URL[] arruRL) {
        super(GradingToolClassLoader.appendURLSToClasspath(string, arruRL));
        this.urlcl = new PublicURLClassLoader(arruRL);
        this.loadedClasses = new ArrayList();
        this.nameMap = new HashMap();
        Class class_ = Runtime.class;
        this.nameMap.put("java.lang.Runtime", class_);
        try {
            this.out = NullOutputStream.PRINTSTREAM;
        }
        catch (Exception var3_3) {
            // empty catch block
        }
    }

    static String appendURLSToClasspath(String string, URL[] arruRL) {
        for (int i = 0; i < arruRL.length; ++i) {
            string = string + arruRL[i].getPath();
        }
        return string;
    }

    public Class loadClass(String string, boolean bl) throws ClassNotFoundException {
        Class class_ = null;
        class_ = this.urlcl.loadClass(string, false);
        this.resolveClass(class_);
        return class_;
    }

    private Class loadFromSuper(String string, boolean bl) throws ClassNotFoundException {
        this.out.println("Loading:  " + string);
        this.out.println("From threadgroup: " + Thread.currentThread().getThreadGroup().getName());
        this.out.println("From thread:  " + Thread.currentThread());
        if (this.nameMap.containsKey(string)) {
            this.out.println("About to retrieve " + string + " from the nameMap");
            this.out.println("Retrieved:  " + this.nameMap.get(string));
            return (Class)this.nameMap.get(string);
        }
        Class class_ = super.loadClass(string, false);
        if (bl) {
            this.resolveClass(class_);
        }
        this.loadedClasses.add(class_);
        this.nameMap.put(string, class_);
        return class_;
    }

    public ArrayList getLoadedClasses() {
        return this.loadedClasses;
    }

    private class PublicURLClassLoader
    extends URLClassLoader {
        public PublicURLClassLoader(URL[] arruRL) {
            super(arruRL, (ClassLoader)null);
        }

        public Class loadClass(String string, boolean bl) throws ClassNotFoundException {
            Class class_ = null;
            try {
                class_ = GradingToolClassLoader.this.loadFromSuper(string, bl);
            }
            catch (Exception var4_4) {
                // empty catch block
            }
            if (class_ != null) {
                return class_;
            }
            class_ = super.loadClass(string, false);
            if (bl) {
                GradingToolClassLoader.this.resolveClass(class_);
            }
            return class_;
        }
    }

}

