/*
 * Decompiled with CFR 0_102.
 */
package org.jext;

import java.util.ArrayList;
import org.gjt.sp.jedit.syntax.TokenMarker;
import org.jext.Jext;
import org.jext.Plugin;

public class Mode {
    protected String modeName;
    protected String userModeName;
    protected String className;
    private ArrayList plugins = new ArrayList();

    public Mode(String string) {
        this.modeName = string;
        this.userModeName = Jext.getProperty("mode." + string + ".name");
        this.className = Jext.getProperty("mode." + string + ".tokenMarker");
    }

    public String getModeName() {
        return this.modeName;
    }

    public String getUserModeName() {
        return this.userModeName;
    }

    public TokenMarker getTokenMarker() {
        if (this.className != null) {
            try {
                ClassLoader classLoader = this.getClass().getClassLoader();
                Class class_ = classLoader == null ? Class.forName(this.className) : classLoader.loadClass(this.className);
                return (TokenMarker)class_.newInstance();
            }
            catch (Exception var1_3) {
                // empty catch block
            }
        }
        return null;
    }

    public void addPlugin(Plugin plugin) {
        this.plugins.add(plugin);
    }

    public ArrayList getPlugins() {
        return this.plugins;
    }

    public void setPlugins(ArrayList arrayList) {
        this.plugins = arrayList;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.modeName = null;
        this.userModeName = null;
        this.className = null;
        this.plugins = null;
    }
}

